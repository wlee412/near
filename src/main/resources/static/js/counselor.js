let selectedTimes = new Set(); // 시간들을 저장할 Set
let selectedCell = null;
  
  
// ✅ FullCalendar: 예약 가능 시간 설정용
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

    dateClick: function(info) {
      const clickedDate = new Date(info.dateStr);
      const today = new Date();
      const maxDate = new Date();
      today.setHours(0, 0, 0, 0);
      maxDate.setMonth(maxDate.getMonth() + 1);
      maxDate.setHours(23, 59, 59, 999);

      if (
        clickedDate.getTime() < today.getTime() ||
        clickedDate.getTime() > maxDate.getTime()
      ) {
        return;
      }

      selectedDate = info.dateStr;
      $('#selected-date').text(selectedDate);

      if (selectedCell) selectedCell.classList.remove('selected-date');
      selectedCell = info.dayEl;
      selectedCell.classList.add('selected-date');

      selectedTimes.clear();  // 이전 선택된 시간 초기화

      $.ajax({
        url: "/counselor/existing?selectedDate=" + selectedDate,
        method: "GET",
        success: function(data) {
          data.forEach(t => {
            selectedTimes.add(t);  // API에서 받은 시간 데이터를 selectedTimes에 추가
          });

          renderTimeButtons(selectedDate, selectedTimes); // 시간 버튼 렌더링
        }
      });
    },

    dayCellDidMount: function(info) {
      const cellDate = new Date(info.dateStr);
      const today = new Date();
      const maxDate = new Date();
      today.setHours(0, 0, 0, 0);
      maxDate.setMonth(maxDate.getMonth() + 1);
      maxDate.setHours(23, 59, 59, 999);

      // ✅ 흐림 처리: 지난 날짜 & 한 달 초과 날짜 모두
      if (cellDate.getTime() < today.getTime()) {
        info.el.classList.add('out-of-range');
      }

      if (cellDate.getTime() > maxDate.getTime()) {
        info.el.classList.add('out-of-range');
      }
    }
  });

  calendar.render();
}

// ✅ 시간 버튼 렌더링 (선택된 시간만 표시)
function renderTimeButtons(date, selectedTimes) {
  const timeContainer = $("#time-buttons");
  timeContainer.empty();  // 기존 버튼 초기화

  const today = new Date(Date.now() + 9 * 60 * 60 * 1000);  // 한국시간
  const todayStr = today.toISOString().split('T')[0]; // 오늘 날짜(YYYY-MM-DD)

  // 오늘 날짜는 예약 가능 시간 설정 불가
  if (date === todayStr) {
    timeContainer.html('<p style="color:gray;">오늘은 예약 가능 시간을 설정할 수 없습니다.</p>');
    $("#save-available-times").prop("disabled", true); // 저장 버튼 비활성화
    return;
  } else {
    $("#save-available-times").prop("disabled", false); // 저장 버튼 활성화
  }

  // 09:00~17:00 사이로 시간 버튼을 생성합니다.
  for (let hour = 9; hour <= 17; hour++) {
    if (hour === 12 || hour === 13) continue;  // 12:00~13:00 제외

    const hourStr = hour.toString().padStart(2, '0');  // 09, 10, ... 형식
    const timeStr = `${date} ${hourStr}:00`; // HH:mm:ss형식

    // 시간 버튼 생성
    const button = $("<button>")
      .text(`${hourStr}:00`) // "14:00" 형식의 시간 표시
      .addClass("time-btn")
      .toggleClass("selected", selectedTimes.has(timeStr))  // selectedTimes에서 timeStr을 사용하여 선택 상태 적용
      .on("click", function () {
        $(this).toggleClass("selected");
        if (selectedTimes.has(timeStr)) {
          selectedTimes.delete(timeStr);  // 선택 해제
        } else {
          selectedTimes.add(timeStr);  // 선택
        }

        console.log("선택된 데이터:", selectedTimes);
      });

    // 버튼을 time-container에 추가
    timeContainer.append(button);
  }
}

// ✅ 저장 버튼 클릭 시 동작
$(document).off('click', '#save-available-times').on('click', '#save-available-times', function() {
  if (!selectedDate) {
    alert('날짜와 시간을 선택해주세요.');
    return;
  }

  const times = Array.from(selectedTimes);  // selectedTimes에서 시간 배열로 변환

  $.ajax({
    url: '/counselor/save',
    type: 'POST',
    contentType: 'application/json',
    data: JSON.stringify({
      selectedDate: selectedDate,
      selectedTimes: times
    }),
    success: () => alert('예약 가능 시간이 저장되었습니다!'),
    error: () => alert('오류가 발생했습니다.')
  });
});

// ✅ 페이지 로드 시 캘린더 초기화
$(document).ready(function() {
  initCalendar();
});
