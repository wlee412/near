let selectedDate = '';
let selectedTimes = new Set();
let originalTimes = new Set();
let selectedCell = null;

// ✅ FullCalendar 초기화
function initCalendar() {
  const calendarEl = document.getElementById('calendar');
  if (!calendarEl) return;

  const calendar = new FullCalendar.Calendar(calendarEl, {
    initialView: 'dayGridMonth',
    locale: 'ko',
    themeSystem: 'none',
    selectable: true,

    headerToolbar: {
      left: 'prev,title,next',
      center: '',
      right: ''
    },
    buttonText: {
      prev: '〈',
      next: '〉'
    },

    dateClick: function (info) {
//    const clickedDate = new Date(info.dateStr);
      const today = new Date();
      today.setHours(0, 0, 0, 0);
	  
	   const maxDate = new Date();
	   maxDate.setMonth(maxDate.getMonth() + 1);
	   maxDate.setDate(today.getDate());
	   maxDate.setHours(23, 59, 59, 999);


      selectedDate = info.dateStr;
      $('#selected-date').text(selectedDate);

      if (selectedCell) selectedCell.classList.remove('selected-date');
      selectedCell = info.dayEl;
      selectedCell.classList.add('selected-date');

      selectedTimes.clear();
      originalTimes.clear();

      // 예약 가능 시간 조회
      $.ajax({
        url: "/counselor/existing/selectedDate/" + encodeURIComponent(selectedDate),
        method: "GET",
        success: function (available) {
          available.forEach(t => {
            selectedTimes.add(t);
            originalTimes.add(t); // ← 기존 시간 저장
          });
          renderTimeButtons(selectedDate);
        }
      });
    },

    dayCellDidMount: function (info) {
      const cellDate = new Date(info.dateStr);
      const today = new Date();
      today.setHours(0, 0, 0, 0);
	  
      const maxDate = new Date();
      maxDate.setMonth(maxDate.getMonth() + 1);
	  maxDate.setDate(today.getDate()); 
      maxDate.setHours(23, 59, 59, 999);

//      if (cellDate < today || cellDate > maxDate) {
//      info.el.classList.add('out-of-range');
//		info.el.style.pointerEvents = 'none';
//		info.el.style.opacity = '0.3';
//		
//		// 툴팁 추가
//		    info.el.setAttribute('title', '한 달 이내 날짜만 선택할 수 있습니다.');
//		    info.el.style.cursor = 'not-allowed';  // 마우스 커서 변경
//		
			// 수정 후: 오늘 이전만 막고, 한 달 이후는 허용
			if (cellDate < today) {
			  info.el.classList.add('out-of-range');
			  info.el.style.color = '#ccc';
			  info.el.style.fontStyle = 'italic';
			  info.el.style.cursor = 'not-allowed';
			}
      
    }
  });

  calendar.render();
}

// ✅ 시간 버튼 렌더링
function renderTimeButtons(date) {
  const counselorId = $("#counselor-id").val(); 
  const timeContainer = $("#time-buttons");
  timeContainer.empty();

  const today = new Date(Date.now() + 9 * 60 * 60 * 1000);
  const todayStr = today.toISOString().split('T')[0];

  if (date === todayStr) {
    timeContainer.html('<p style="color:gray;">오늘은 예약 가능 시간을 설정할 수 없습니다.</p>');
    $("#save-available-times").prop("disabled", true);
    return;
  } else {
    $("#save-available-times").prop("disabled", false);
  }

  // ✅ 예약된 시간 조회 먼저 AJAX
  $.ajax({
    url: "/counselor/reserved/selectedDate/" + encodeURIComponent(date),
    method: "GET",
    dataType: "json",
    success: function (reservedTimes) {
      // 👉 예: reservedTimes = ["2025-07-31 09:00:00", ...]
      // → "초(:ss)" 제거
      const trimmedReserved = reservedTimes.map(t => t.slice(0, 16)); // "2025-07-31 09:00"

      for (let hour = 9; hour <= 17; hour++) {
        if (hour === 12) continue;

        const hourStr = hour.toString().padStart(2, '0');
        const timeStr = `${date} ${hourStr}:00`;

        const isReserved = trimmedReserved.includes(timeStr);
        const isSelected = selectedTimes.has(timeStr);

        const button = $("<button>")
          .text(`${hourStr}:00`)
          .addClass("time-btn")
          .toggleClass("selected", isSelected)
          .prop("disabled", isReserved)               // ❗ 예약된 시간은 클릭 못하게 막기
          .toggleClass("reserved", isReserved)        // (선택) 스타일 추가용 클래스
          .on("click", function () {
            if ($(this).prop("disabled")) return;
            $(this).toggleClass("selected");
            if (selectedTimes.has(timeStr)) {
              selectedTimes.delete(timeStr);
            } else {
              selectedTimes.add(timeStr);
            }
            console.log("선택된 시간들:", selectedTimes);
          });

        timeContainer.append(button);
      }
    },
    error: function () {
      alert("예약된 시간 조회에 실패했습니다. 버튼은 제한 없이 표시됩니다.");
    }
  });
}

// ✅ 저장 버튼 클릭 이벤트
$(document).off('click', '#save-available-times').on('click', '#save-available-times', function () {
  if (!selectedDate) {
    alert('날짜와 시간을 선택해주세요.');
    return;
  }

  const times = Array.from(selectedTimes);
  const timesToDelete = Array.from([...originalTimes].filter(t => !selectedTimes.has(t)));

  // 둘 다 비었을 때만 경고
  if (times.length === 0 && timesToDelete.length === 0) {
    alert('선택된 예약 가능한 시간이 없습니다.');
    return;
  }

  $.ajax({
    url: '/counselor/save',
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify({
      selectedDate: selectedDate,
      selectedTimes: times,
      timesToDelete: timesToDelete
    }),
    success: function () {
      alert('예약 가능 시간이 저장되었습니다!');
      selectedTimes.clear();
      originalTimes.clear();
      $(`#calendar`).find('.selected-date').click(); // 다시 로드
    },
    error: function (xhr, status, error) {
      console.error('오류 발생:', error);
      alert('예약 가능 시간 저장에 실패했습니다. 다시 시도해 주세요.');
    }
  });
});

// ✅ 페이지 로드 시 캘린더 실행
$(document).ready(function () {
  initCalendar();
});