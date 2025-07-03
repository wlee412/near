<%@page import="java.time.LocalDate"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>상담 가능 시간 설정</title>
  <style>
    .time-option {
      display: inline-block;
      padding: 8px 12px;
      margin: 5px;
      border: 1px solid #aaa;
      border-radius: 6px;
      cursor: pointer;
    }
    .time-option.selected {
      background-color: #4ecdc4;
      color: white;
    }
  </style>
</head>
<body>
  <h2>상담 가능 시간 설정</h2>

  <label for="date">날짜 선택:</label>
  <input type="date" id="date" name="date" min="<%= LocalDate.now() %>">

  <div id="timeSlots" style="margin-top: 20px;"></div>

  <button onclick="submitTimes()">저장</button>

  <script>
    const validHours = [9, 10, 11, 13, 14, 15, 16]; // 12시 제외
    const timeSlotsDiv = document.getElementById('timeSlots');

    document.getElementById('date').addEventListener('change', () => {
      const selectedDate = document.getElementById('date').value;
      timeSlotsDiv.innerHTML = '';
      if (!selectedDate) return;

      validHours.forEach(hour => {
        const timeStr = `${hour.toString().padStart(2, '0')}:00`;
        const div = document.createElement('div');
        div.classList.add('time-option');
        div.textContent = timeStr;
        div.dataset.time = timeStr;
        div.addEventListener('click', () => {
          div.classList.toggle('selected');
        });
        timeSlotsDiv.appendChild(div);
      });
    });

    function submitTimes() {
      const selectedDate = document.getElementById('date').value;
      const selectedTimes = Array.from(document.querySelectorAll('.time-option.selected'))
        .map(el => el.dataset.time);

      const datetimes = selectedTimes.map(time => `${selectedDate}T${time}:00`);
      console.log("예약 가능 시간 전송:", datetimes);

      // fetch로 백엔드 전송 가능
      // fetch('/counselor/available-times', { method: 'POST', body: JSON.stringify(...) })
    }
  </script>
</body>
</html>
