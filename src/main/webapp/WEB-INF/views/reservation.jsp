<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>상담 예약</title>

  <!-- ✅ static 경로 기준 CSS -->
  <link rel="stylesheet" href="<c:url value='/css/reservation.css' />">
</head>
<body>

<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="content-wrapper">

<h2 style="text-align: center;">상담 예약</h2>

<!-- ✅ 시간대 리스트 JSTL 변수로 선언 -->
<c:set var="timeSlots" value="${fn:split('09:00,10:00,11:00,13:00,14:00,15:00,16:00,17:00', ',')}" />

<!-- ✅ 상담사 카드 하드코딩 -->
<div class="counselor-wrapper">

  <!-- 상담사 1 -->
  <div class="counselor-card" data-counselor-id="counselor01">
    <img src="/images/counselor01.png" alt="상담사1" width="100" /><br>
    <strong>상담사 이병철</strong><br><br>

    <input type="date" class="date-input" data-counselor-id="counselor01"><br><br>

    <div class="time-slots" data-counselor-id="counselor01">
      <c:forEach var="time" items="${timeSlots}">
        <div class="time-slot" data-time="${time}">${time}</div>
      </c:forEach>
    </div>

	<div class="symptom-title">상담 사유</div>
     <div class="symptom-list">
  <label><input type="checkbox" name="symptom" value="1"> 우울</label>
  <label><input type="checkbox" name="symptom" value="2"> 불안</label>
  <label><input type="checkbox" name="symptom" value="3"> 자존감</label>
  <label><input type="checkbox" name="symptom" value="4"> 수면문제</label>
  <label><input type="checkbox" name="symptom" value="5"> 스트레스</label>
  <label><input type="checkbox" name="symptom" value="6"> 공황/불안발작</label>
  <label><input type="checkbox" name="symptom" value="7"> 대인관계</label>
  <label><input type="checkbox" name="symptom" value="8"> 섭식문제</label>
  <label><input type="checkbox" name="symptom" value="9"> ADHD</label>
  <label><input type="checkbox" name="symptom" value="10"> 자기통제</label>
</div>


    <button class="reserve-btn" data-counselor-id="counselor01">예약하기</button>
  </div>

  <!-- 상담사 2 -->
  <div class="counselor-card" data-counselor-id="counselor02">
    <img src="/images/counselor02.png" alt="상담사2" width="100" /><br>
    <strong>상담사 신하령</strong><br><br>

    <input type="date" class="date-input" data-counselor-id="counselor02"><br><br>

    <div class="time-slots" data-counselor-id="counselor02">
      <c:forEach var="time" items="${timeSlots}">
        <div class="time-slot" data-time="${time}">${time}</div>
      </c:forEach>
    </div>

	<div class="symptom-title">상담 사유</div>
    <div class="symptom-list">
  <label><input type="checkbox" name="symptom" value="1"> 우울</label>
  <label><input type="checkbox" name="symptom" value="2"> 불안</label>
  <label><input type="checkbox" name="symptom" value="3"> 자존감</label>
  <label><input type="checkbox" name="symptom" value="4"> 수면문제</label>
  <label><input type="checkbox" name="symptom" value="5"> 스트레스</label>
  <label><input type="checkbox" name="symptom" value="6"> 공황/불안발작</label>
  <label><input type="checkbox" name="symptom" value="7"> 대인관계</label>
  <label><input type="checkbox" name="symptom" value="8"> 섭식문제</label>
  <label><input type="checkbox" name="symptom" value="9"> ADHD</label>
  <label><input type="checkbox" name="symptom" value="10"> 자기통제</label>
</div>


    <button class="reserve-btn" data-counselor-id="counselor02">예약하기</button>
  </div>

  <!-- 상담사 3 -->
  <div class="counselor-card" data-counselor-id="counselor03">
    <img src="/images/counselor03.png" alt="상담사3" width="100" /><br>
    <strong>상담사 전미희</strong><br><br>

    <input type="date" class="date-input" data-counselor-id="counselor03"><br><br>

    <div class="time-slots" data-counselor-id="counselor03">
      <c:forEach var="time" items="${timeSlots}">
        <div class="time-slot" data-time="${time}">${time}</div>
      </c:forEach>
    </div>

	<div class="symptom-title">상담 사유</div>
    <div class="symptom-list">
  <label><input type="checkbox" name="symptom" value="1"> 우울</label>
  <label><input type="checkbox" name="symptom" value="2"> 불안</label>
  <label><input type="checkbox" name="symptom" value="3"> 자존감</label>
  <label><input type="checkbox" name="symptom" value="4"> 수면문제</label>
  <label><input type="checkbox" name="symptom" value="5"> 스트레스</label>
  <label><input type="checkbox" name="symptom" value="6"> 공황/불안발작</label>
  <label><input type="checkbox" name="symptom" value="7"> 대인관계</label>
  <label><input type="checkbox" name="symptom" value="8"> 섭식문제</label>
  <label><input type="checkbox" name="symptom" value="9"> ADHD</label>
  <label><input type="checkbox" name="symptom" value="10"> 자기통제</label>
</div>

    <button class="reserve-btn" data-counselor-id="counselor03">예약하기</button>
  </div>
</div>

<!-- ✅ 로그인된 사용자 ID 전역 전달 -->
<script>
  window.clientId = "${sessionScope.clientId}";
</script>

<!-- ✅ JS 파일 경로 static 기준 -->
<script src="<c:url value='/js/reservation.js' />"></script>

</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />

</body>
</html>