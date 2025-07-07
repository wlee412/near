<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>FullCalendar 테스트 (로컬 JS 사용)</title>

  <!-- ✅ 로컬 CSS 연결 -->
  <link rel="stylesheet" href="/css/fullcalendar.min.css">

  <!-- ✅ 로컬 JS 연결 -->
  <script src="/js/fullcalendar.min.js"></script>

  <style>
    body {
      font-family: 'Noto Sans KR', sans-serif;
      padding: 30px;
      background: #f2f9ff;
    }

    #calendar {
      max-width: 900px;
      margin: 0 auto;
      background: white;
      padding: 20px;
      border-radius: 10px;
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    }

    h2 {
      text-align: center;
      margin-bottom: 20px;
    }
  </style>
</head>
<body>

<h2>📅 상담 예약 달력</h2>
<div id="calendar"></div>

<script>
  document.addEventListener('DOMContentLoaded', function () {
    const calendarEl = document.getElementById('calendar');

    const calendar = new FullCalendar.Calendar(calendarEl, {
      initialView: 'dayGridMonth',
      locale: 'ko',
      dateClick: function(info) {
        alert('선택한 날짜: ' + info.dateStr);
      },
      events: [
        {
          title: '🧠 예약됨',
          start: new Date().toISOString().split('T')[0]
        }
      ]
    });

    calendar.render();
  });
</script>

</body>
</html>
