<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- FullCalendar CSS (비동기 로딩용, 중복되면 상위 JSP에서 제거) -->
<link href="https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.css" rel="stylesheet" />
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.js"></script>

<div class="time-box">
  <h3 class="section-title">📅 예약 가능 시간 선택</h3>

  <!-- 캘린더 영역 -->
  <div id="calendar" class="calendar-container"></div>

  <!-- 날짜 출력 -->
  <div id="selected-date-box" style="margin-top: 20px;">
    <p><strong>선택한 날짜:</strong> <span id="selected-date"></span></p>
  </div>

  <!-- 시간 버튼 영역 -->
  <div id="time-buttons" class="time-buttons-container">
    <%-- JS로 시간 버튼이 들어옵니다 --%>
  </div>

  <!-- 저장 버튼 -->
  <div class="button-wrapper" style="margin-top: 20px;">
    <button type="button" id="save-available-times" class="save-button">
      예약 가능 시간 저장
    </button>
  </div>
</div>
