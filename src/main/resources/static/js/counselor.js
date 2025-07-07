
// 비동기 fetch 섹션 나누기 
function loadSection(section) {
  fetch(`/counselor/section/${section}`)
    .then(response => {
      if (!response.ok) throw new Error("불러오기 실패");
      return response.text();
    })
    .then(html => {
      document.getElementById('contentArea').innerHTML = html;
    })
    .catch(error => {
      document.getElementById('contentArea').innerHTML = "<p>오류가 발생했습니다.</p>";
      console.error(error);
    });
}

// 예약 가능시간 
//document.addEventListener('DOMContentLoaded', () => {
//  const selectedTimes = new Set();
//
//  window.generateTimeButtons = function () {
//    const dateInput = document.getElementById("date");
//    const timeContainer = document.getElementById("time-section");
//    const hiddenInputArea = document.getElementById("hidden-inputs");
//
//    const date = dateInput.value;
//    timeContainer.innerHTML = "";
//    hiddenInputArea.innerHTML = "";
//    selectedTimes.clear();
//
//    if (!date) {
//      alert("날짜를 선택해주세요.");
//      return;
//    }
//
//    for (let hour = 9; hour <= 17; hour++) {
//      const timeStr = (hour < 10 ? "0" + hour : hour) + ":00";
//      const dateTime = date + " " + timeStr;
//
//      const btn = document.createElement("button");
//      btn.type = "button";
//      btn.innerText = timeStr;
//      btn.className = "time-btn";
//      btn.dataset.time = dateTime;
//
//      btn.addEventListener("click", () => toggleTime(dateTime, btn, hiddenInputArea));
//      timeContainer.appendChild(btn);
//    }
//  };
//
//  function toggleTime(dateTime, button, hiddenInputArea) {
//    if (selectedTimes.has(dateTime)) {
//      selectedTimes.delete(dateTime);
//      button.classList.remove("selected");
//      hiddenInputArea.querySelector(`input[value="${dateTime}"]`)?.remove();
//    } else {
//      selectedTimes.add(dateTime);
//      button.classList.add("selected");
//      const input = document.createElement("input");
//      input.type = "hidden";
//      input.name = "selectedTimes";
//      input.value = dateTime;
//      hiddenInputArea.appendChild(input);
//    }
//  }
//
//  window.submitAvailableTimes = function () {
//    const counselorId = document.querySelector("input[name='counselorId']").value;
//    const selected = Array.from(document.querySelectorAll("input[name='selectedTimes']"))
//                          .map(input => input.value);
//
//    if (selected.length === 0) {
//      alert("선택된 시간이 없습니다.");
//      return;
//    }
//
//    fetch("/counselor/time/save", {
//      method: "POST",
//      headers: { "Content-Type": "application/json" },
//      body: JSON.stringify({ counselorId, selectedTimes: selected })
//    })
//    .then(res => res.ok ? res.text() : Promise.reject("저장 실패"))
//    .then(() => {
//      alert("✅ 예약 가능 시간이 저장되었습니다.");
//      selectedTimes.clear();
//      document.getElementById("time-section").innerHTML = "";
//      document.getElementById("hidden-inputs").innerHTML = "";
//      document.getElementById("date").value = "";
//    })
//    .catch(err => {
//      console.error(err);
//      alert("❌ 저장 중 오류가 발생했습니다.");
//    });
//  };
//});
//
//function initCalendar(savedDates) {
//  flatpickr("#date", {
//    dateFormat: "Y-m-d",
//    minDate: "today",
//    maxDate: new Date().fp_incr(30),
//    onDayCreate: function(dObj, dStr, fp, dayElem) {
//      const date = dayElem.dateObj.toISOString().slice(0, 10);
//      if (savedDates.includes(date)) {
//        dayElem.classList.add("booked-date");
//      }
//    },
//    onChange: function(selectedDates, dateStr, instance) {
//      document.getElementById("date").value = dateStr;
//      generateTimeButtons();
//    }
//  });
//}

