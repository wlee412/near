<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>정신건강 대시보드</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mentalDashboard.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
</head>

<jsp:include page="/includes/header.jsp"/>   	    		<!-- 2개로 동작 -->   
<%-- <%@ include file="/includes/header.jsp" %>  --%>       <!-- 1개로 동작 --> 

<body>

<div class="wrapper">
    <div class="dashboard-container">
        <h2></h2>
        <div class="card-container">
            <div class="card" onclick="openModal('young')">
                <span>혼자가 아니에요</span>
            </div>
            <div class="card" onclick="openCardModal()">
                <span>오늘의 행운카드</span>
            </div>
            <div class="card" onclick="openModal('dummy')">
                <span>기분전환 할까요</span>
            </div>
        </div>
    </div>
</div>

<!-- 차트 모달 -->
<div id="chartModal" class="modal">
    <div class="modal-content">
        <span class="close" onclick="closeModal()">&times;</span>
        <h3>2010-2023 연령대별 정신건강 이슈</h3>
        <canvas id="mentalChart" width="800" height="400"></canvas>
    </div>
</div>

<!-- 행운카드 모달 -->
<div id="cardModal" class="modal" style="display: none;">
    <div class="modal-content">
        <span class="close" onclick="closeCardModal()">&times;</span>
<%--         <%@ include file="luckyCard.jsp" %>   --%>
<jsp:include page="luckyCard.jsp"/>  
    </div>
</div>


<!-- 차트 JS -->
<script src="/js/chart.js"></script>

<!-- 모달 제어 -->
<script>
    function openModal(type) {
        document.getElementById("chartModal").style.display = "block";
        loadChartData(type);
    }

    function closeModal() {
        document.getElementById("chartModal").style.display = "none";
    }

    function openCardModal() {
        document.getElementById("cardModal").style.display = "block";
    }

    function closeCardModal() {
        document.getElementById("cardModal").style.display = "none";
    }

    window.onclick = function(event) {
        if (event.target === document.getElementById("chartModal")) {
            closeModal();
        } else if (event.target === document.getElementById("cardModal")) {
            closeCardModal();
        }
    }
</script>

<!-- 행운카드 선택 로직 (JS 포함이 핵심!) -->
<script>
    let cardSelected = false;

    function selectCard(cardElement, index) {
        if (cardSelected) return;
        cardSelected = true;

        cardElement.classList.add('flipped');

        const messages = [
            "🌟 당신은 생각보다 강합니다.",
            "💖 오늘 누군가의 위로가 되어줄 거예요.",
            "🍀 좋은 소식이 곧 찾아올 거예요.",
            "🌈 당신의 존재만으로도 충분해요.",
            "🪄 오늘은 기분 좋은 하루가 될 거예요.",
            "🌼 실수해도 괜찮아요, 누구나 그래요.",
            "🌻 오늘 당신을 위한 축복이 기다리고 있어요.",
            "☀️ 지금 이 순간도 당신은 빛나고 있어요.",
            "🌙 고요한 밤이 당신을 편안하게 해줄 거예요.",
            "✨ 믿는 만큼 좋은 일이 생겨요."
        ];

        const randomMsg = messages[Math.floor(Math.random() * messages.length)];
        const backDiv = document.getElementById("back-" + index);
        backDiv.innerText = randomMsg;
    }
</script>

<jsp:include page="/includes/footer.jsp"/> 
<%-- <%@ include file="/includes/footer.jsp" %> --%>
</body>
</html>
