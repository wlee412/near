<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>정신건강 대시보드</title>
    <link rel="stylesheet" href="/css/mental/mentalDashboard.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>
<body>
<jsp:include page="/includes/header.jsp"/>
	<div class="wrapper">
<div class="dashboard-container">
    <h2></h2>
    <div class="card-container">
        <div class="card" onclick="openModal('young')">
            <span>혼자가 아니에요</span>
        </div>
        <div class="card" onclick="openModal('old')">
            <span>오늘의 행운카드</span>
        </div>
        <div class="card" onclick="openModal('dummy')">
            <span>기분전환 할까요</span>
        </div>
    </div>
</div>
</div>
<!-- 모달 -->
<div id="chartModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal()">&times;</span>
        <h3>2010-2023 연령대별 정신건강 이슈</h3>
        <canvas id="mentalChart" width="800" height="400"></canvas>
    </div>
</div>

<!-- 차트 관련 JS -->
<script src="/js/chart.js"></script>

<!-- 모달 제어 JS -->
<script>
    function openModal(type) {
        document.getElementById("chartModal").style.display = "block";
        loadChartData(type); // 인자 전달 (young, old 등)
    }

    function closeModal() {
        document.getElementById("chartModal").style.display = "none";
    }

    window.onclick = function(event) {
        if (event.target === document.getElementById("chartModal")) {
            closeModal();
        }
    }
</script>

<jsp:include page="/includes/footer.jsp"/>
</body>
</html>
