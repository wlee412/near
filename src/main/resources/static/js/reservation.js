document.addEventListener("DOMContentLoaded", () => {
  const dateInputs = document.querySelectorAll(".date-input");
  const today = new Date();
  today.setHours(0, 0, 0, 0); // 오늘 00:00 기준

  // 날짜 변경 시 시간 조회
  dateInputs.forEach(input => {
    input.addEventListener("change", async (e) => {
      const counselorId = e.target.dataset.counselorId;
      const dateStr = e.target.value;

      if (!dateStr) {
        alert("날짜를 선택해 주세요.");
        return;
      }

      const container = document.querySelector(`.time-slots[data-counselor-id="${counselorId}"]`);
      const selectedDate = new Date(dateStr);

      let available = [];
      let unavailable = [];

      try {
        const resAvailable = await fetch(`/api/reservation/available-times?counselorId=${counselorId}&date=${dateStr}`);
        available = await resAvailable.json();

        const resUnavailable = await fetch(`/api/reservation/unavailable?counselorId=${counselorId}&date=${dateStr}`);
        unavailable = await resUnavailable.json();
      } catch (err) {
        console.error("⛔ 시간 조회 실패:", err);
      }

      // 시간 렌더링
      container.querySelectorAll(".time-slot").forEach(slot => {
        const time = slot.dataset.time;
        const hour = parseInt(time.split(":")[0]);

        const isPast =
          selectedDate < today ||
          (selectedDate.toDateString() === today.toDateString() && hour <= new Date().getHours());

        const isNotAvailable = !available.includes(time);
        const isBooked = unavailable.includes(time);

        if (isPast || isNotAvailable || isBooked) {
          slot.classList.add("disabled");
        } else {
          slot.classList.remove("disabled");
        }

        slot.classList.remove("selected");
      });
    });
  });

  // 예약 버튼 클릭 이벤트
  const reserveButtons = document.querySelectorAll(".reserve-btn");
  reserveButtons.forEach(btn => {
    btn.addEventListener("click", () => {
      const clientId = window.clientId;
      if (!clientId) {
        alert("로그인이 필요합니다.");
        return;
      }

      const counselorId = btn.dataset.counselorId;
      const card = document.querySelector(`.counselor-card[data-counselor-id="${counselorId}"]`);
      const date = card.querySelector(".date-input").value;
      const timeSlot = card.querySelector(".time-slot.selected");
      const symptoms = Array.from(card.querySelectorAll("input[name='symptom']:checked"))
        .map(el => el.value);

      // 유효성 검사
      if (!date || !timeSlot) {
        alert("날짜와 시간을 선택해주세요.");
        return;
      }

      if (symptoms.length === 0) {
        alert("증상을 1개 이상 선택해주세요.");
        return;
      } else if (symptoms.length > 3) {
        alert("최대 3개까지 선택할 수 있습니다.");
        return;
      }

      const selectedTime = timeSlot.dataset.time;

      // 예약 요청
      fetch("/api/reservation/save", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          clientId,
          counselorId,
          date,
          time: selectedTime,
          symptoms
        })
      })
        .then(res => {
          if (res.ok) {
            alert("✅ 예약 완료되었습니다!");
            location.reload();
          } else {
            res.text().then(msg => alert("❌ 예약 실패: " + msg));
          }
        })
        .catch(err => {
          console.error("예약 요청 오류:", err);
          alert("서버 오류 발생!");
        });
    });
  });

  // 시간 클릭 선택 처리
  document.querySelectorAll(".time-slot").forEach(slot => {
    slot.addEventListener("click", () => {
      if (slot.classList.contains("disabled")) return;

      const container = slot.parentElement;
      container.querySelectorAll(".time-slot").forEach(s => s.classList.remove("selected"));
      slot.classList.add("selected");
    });
  });
});