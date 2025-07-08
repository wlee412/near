<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
  <title>관리자 통계 페이지</title>
  <link rel="stylesheet" href="/css/surveyStats.css" />
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <script src="/js/surveyStats.js"></script>
</head>
<body>

<div class="content-wrapper">
    <div class="menu-container">
    <div class="menu-box">
      <a href="/admin/adminHome">홈</a>
    </div>
    <div class="menu-box">
      <a href="/admin/adminMember">회원관리</a>
    </div>
    <div class="menu-box">
      <a href="/admin/adminReservation">예약내역</a>
    </div>
  </div>
  <!--  탭 메뉴 -->
  <div class="tab-menu">
    <button class="tab-button active" data-tab="risk">🧠 고위험군 통계</button>
    <button class="tab-button" data-tab="symptom">📊 증상별 평균</button>
  </div>

  <!--  탭 내용: 고위험군 -->
  <div class="tab-content" id="tab-risk">
    <canvas id="highRiskChart"></canvas>
  </div>

  <!-- ✅ 탭 내용: 증상 선택자 평균 -->
  <div class="tab-content" id="tab-symptom" style="display: none;">
    <canvas id="symptomAvgChart"></canvas>
  </div>

</div>

<script>
  // ✅ JSP에서 전달된 데이터를 JS로 변환 (마지막 쉼표 주의!)
  const highRiskStats = [
    <c:forEach var="item" items="${highRiskStats}" varStatus="loop">
      { name: "${item.surveyName}", rate: ${item.highRiskRate} }<c:if test="${!loop.last}">,</c:if>
    </c:forEach>
  ];

  const symptomAvgStats = [
    <c:forEach var="item" items="${symptomAvgStats}" varStatus="loop">
      { name: "${item.symptomName}", avg: ${item.avgScore} }<c:if test="${!loop.last}">,</c:if>
    </c:forEach>
  ];
</script>

</body>
</html>
