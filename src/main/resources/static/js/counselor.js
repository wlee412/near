// ✅ 전역 변수
let disabledSlots = []; // DB에 저장된 시간들: ["2025-07-10 09:00", ...]

// 비동기
$(document).ready(function() {
	// 사이드바 클릭 시
	$('.sidebar-button').click(function(e) {
		e.preventDefault();
		const section = $(this).data('section');
		if (section && section.trim() !== '') {
			loadSection(section);
			$('.sidebar-button').removeClass('active');
			$(this).addClass('active');
		}
	});

	handleSidebarNavigation(); // URL 해시 처리
});

// ✅ URL 해시로 섹션 로드
function handleSidebarNavigation() {
	const currentHash = window.location.hash?.substring(1);
	$('.sidebar-button').removeClass('active');
	$(`.sidebar-button[data-section="${currentHash}"]`).addClass('active');
	loadSection(currentHash);
}

// ✅ 섹션 로드
function loadSection(section) {
	$('#contentArea').load(`/counselor/section/${section}`, function(response, status, xhr) {
		if (status === "error") {
			console.error(`❌ '${section}' 섹션 로드 실패:`, xhr.status, xhr.statusText);
		}
		if (section === 'time') {
			setTimeout(() => initCalendar(), 50);
		}
	});
}

// ✅ FullCalendar 초기화
function initCalendar() {
	const calendarEl = document.getElementById('calendar');
	if (!calendarEl) return;

	let selectedDate = null;
	let selectedTimes = new Set();
	let selectedCell = null;

	const calendar = new FullCalendar.Calendar(calendarEl, {
		initialView: 'dayGridMonth',
		locale: 'ko',
		selectable: true,
		validRange: {
			start: new Date().toISOString().split('T')[0],
			end: new Date(new Date().setMonth(new Date().getMonth() + 1)).toISOString().split('T')[0]
		},
		dateClick: (info) => {
			selectedDate = info.dateStr;
			$('#selected-date').text(selectedDate);

			if (selectedCell) selectedCell.classList.remove('selected-date');
			selectedCell = info.dayEl;
			selectedCell.classList.add('selected-date');

			selectedTimes.clear();

			// 🔄 AJAX로 최신 예약된 시간 불러오고 버튼 렌더링
			$.ajax({
				url: "/counselor/time/existing",
				method: "GET",
				success: function(data) {
					disabledSlots = data;
					renderTimeButtons(selectedDate, selectedTimes);
				}
			});
		},
		dayCellDidMount: function(info) {
			const dateStr = info.date.toISOString().split('T')[0];
			const storedDates = new Set(disabledSlots.map(dt => dt.split(' ')[0]));
			if (storedDates.has(dateStr)) {
				info.el.style.backgroundColor = '#e0f7fa';
			}
		}
	});

	calendar.render();

	// 저장 버튼 클릭
	$(document).off('click', '#save-available-times').on('click', '#save-available-times', function() {
		//    if (!selectedDate || selectedTimes.size === 0) {
		if (!selectedDate) {
			alert('날짜와 시간을 선택해주세요.');
			return;
		}

		const times = Array.from(selectedTimes);
		$.ajax({
			url: '/counselor/time/save',
			type: 'POST',
			contentType: 'application/json',
			data: JSON.stringify({ date: selectedDate, times: times }),
			success: () => alert('예약 가능 시간이 저장되었습니다!'),
			error: () => alert('오류가 발생했습니다.')
		});
	});
}

// ✅ 시간 버튼 렌더링
function renderTimeButtons(date, selectedTimes) {
	const timeContainer = $("#time-buttons");
	timeContainer.empty();

	const todayStr = new Date().toISOString().split('T')[0];
	if (date === todayStr) {
		timeContainer.html('<p style="color:gray;">오늘은 예약 가능 시간을 설정할 수 없습니다.</p>');
		return;
	}

	for (let hour = 9; hour <= 17; hour++) {
		const hourStr = hour.toString().padStart(2, '0');
		const timeStr = `${date} ${hourStr}:00`;

		const isStored = disabledSlots.includes(timeStr);
		if (isStored) selectedTimes.add(timeStr);

		const button = $("<button>")
			.text(`${hourStr}:00`)
			.addClass("time-btn")
			.toggleClass("selected", selectedTimes.has(timeStr));

		button.on("click", function() {
			$(this).toggleClass("selected");
			if (selectedTimes.has(timeStr)) {
				selectedTimes.delete(timeStr);
			} else {
				selectedTimes.add(timeStr);
			}
		});

		timeContainer.append(button);
	}
}

// 예약 현황용 캘린더
function initReservationCalendar() {
  const calendarEl = document.getElementById('calendar-reservation');
  if (!calendarEl) return;

  $.ajax({
    url: '/counselor/reservations/calendar',
    method: 'GET',
    dataType: 'json',
    success: function (data) {
      const events = data.map(item => ({
        title: `${item.count}건`,
        start: item.date,
        allDay: true,
        backgroundColor: '#5daec5',
        borderColor: '#5daec5',
        textColor: '#fff'
      }));

      const calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        locale: 'ko',
        headerToolbar: {
          left: 'prev,next today',
          center: 'title',
          right: ''
        },
        events: events,
        eventClick: function (info) {
          alert(`선택한 날짜: ${info.event.startStr}\n예약: ${info.event.title}`);
        }
      });

      calendar.render();
    },
    error: function (xhr, status, error) {
      console.error('📛 캘린더 데이터 불러오기 실패:', status, error);
    }
  });
}
