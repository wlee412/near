// ✅ FullCalendar: 예약 가능 시간 설정용
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
		  start: new Date(Date.now() + 86400000).toISOString().split('T')[0], // 내일부터
		  end: new Date(new Date().setMonth(new Date().getMonth() + 1)).toISOString().split('T')[0]
		},
		dateClick: (info) => {
			selectedDate = info.dateStr;
			$('#selected-date').text(selectedDate);

			if (selectedCell) selectedCell.classList.remove('selected-date');
			selectedCell = info.dayEl;
			selectedCell.classList.add('selected-date');

			selectedTimes.clear();

			// 🔄 기존 시간 불러오기
			$.ajax({
				url: "/counselor/existing",
				method: "GET",
				success: function(data) {
					// 해당 날짜에 속한 시간만 선택 상태로 만듦
					data.forEach(t => {
						if (t.startsWith(selectedDate)) {
							selectedTimes.add(t);
						}
					});
					renderTimeButtons(selectedDate, selectedTimes);
				}
			});
		}
	});

	calendar.render();

	// ✅ 저장 버튼
	$(document).off('click', '#save-available-times').on('click', '#save-available-times', function() {
		if (!selectedDate) {
			alert('날짜와 시간을 선택해주세요.');
			return;
		}

		//    const times = Array.from(selectedTimes);
		const times = Array.from(selectedTimes).map(t => t.split(' ')[1]); // "HH:mm"만 추출

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
}

// ✅ 시간 버튼 렌더링 (선택된 시간만 표시)
function renderTimeButtons(date, selectedTimes) {
	const timeContainer = $("#time-buttons");
	timeContainer.empty();

	// ✅ 한국 시간 기준 오늘 날짜
	const today = new Date(Date.now() + 9 * 60 * 60 * 1000);
	const todayStr = today.toISOString().split('T')[0];

	if (date === todayStr) {
		timeContainer.html('<p style="color:gray;">오늘은 예약 가능 시간을 설정할 수 없습니다.</p>');
		// ✅ 저장 버튼도 비활성화
		$("#save-button").prop("disabled", true);
		return;
	} else {
		$("#save-button").prop("disabled", false);
	}

	for (let hour = 9; hour <= 17; hour++) {
		const hourStr = hour.toString().padStart(2, '0');
		const timeStr = `${date} ${hourStr}:00`;

		const button = $("<button>")
			.text(`${hourStr}:00`)
			.addClass("time-btn")
			.toggleClass("selected", selectedTimes.has(timeStr))
			.on("click", function () {
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


// ✅ 페이지 로드 시 캘린더 초기화
$(document).ready(function() {
	initCalendar();
});
