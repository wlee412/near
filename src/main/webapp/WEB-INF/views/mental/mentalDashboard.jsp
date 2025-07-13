<%@ page contentType="text/html;charset=UTF-8" language="java" %> 
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>정신건강 대시보드</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mentalDashboard.css">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <!-- Lottie 애니메이션 추가 -->
    <script src="https://unpkg.com/@lottiefiles/lottie-player@latest/dist/lottie-player.js"></script>
</head>

<jsp:include page="/includes/header.jsp"/>   	    		<!-- 2개로 동작 -->   
<%-- <%@ include file="/includes/header.jsp" %>  --%>       <!-- 1개로 동작 --> 

<body>

<div class="wrapper">
    <div class="dashboard-container">
        <h2></h2>
       <div class="card-container">
            <div class="card" onclick="openModal('young')">
                <span>연령별 정신건강 통계</span>
                <lottie-player
                    src="${pageContext.request.contextPath}/lottie/chartWebble.json"
                    background="transparent"
                    speed="1"
                    style="width: 180px; height: 180px;"
                    loop autoplay>
                </lottie-player>
            </div>
            <div class="card" onclick="openCardModal()">
                <span>오늘의 행운카드</span>
                <lottie-player
                    src="${pageContext.request.contextPath}/lottie/wheelOfFate.json"
                    background="transparent"
                    speed="1"
                    style="width: 180px; height: 180px;"
                    loop autoplay>
                </lottie-player>
            </div>
            <div class="card" onclick="openYoutubeModal()">
                <span>맞춤 추천 영상</span>
                <lottie-player
                    src="${pageContext.request.contextPath}/lottie/video.json"
                    background="transparent"
                    speed="1"
                    style="width: 180px; height: 180px;"
                    loop autoplay>
                </lottie-player>
            </div>
            <div class="card" onclick="openGameModal()">
                <span>미니 게임</span>
                <lottie-player
                    src="https://assets2.lottiefiles.com/packages/lf20_x62chJ.json"
                    background="transparent" speed="1"
                    style="width: 280px; height: 260px;"
                    loop autoplay>
                </lottie-player>
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

<!-- 유튜브 영상 모달 -->
<jsp:include page="youtubeView.jsp"/>

<!-- 🎮 미니게임 모달 -->
<div id="gameModal" class="modal" style="display: none;">
  <div class="modal-content">
    <span class="close" onclick="closeGameModal()">&times;</span>
    <h3>기분전환 미니게임 🎲</h3>

    <div class="game-tabs">
      <button onclick="showGame('typing')">감정 타자 게임</button>
      <button onclick="showGame('matching')">감정 짝 맞추기</button>
      <button onclick="showGame('balloon')">스트레스 풍선 게임</button>
    </div>

    <div id="gameContainer">
      <iframe id="gameFrame" src="" width="100%" height="400px" frameborder="0"></iframe>
    </div>
  </div>
</div>

<!-- 차트 JS -->
<script src="/js/chart.js"></script>

<!-- 유튜브 기능 JS 분리 -->
<script src="${pageContext.request.contextPath}/js/youtube.js"></script>

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
</script>

<script>
    function openGameModal() {
        document.getElementById("gameModal").style.display = "block";
        showGame('typing'); // 기본 게임
    }

    function closeGameModal() {
        document.getElementById("gameModal").style.display = "none";
        document.getElementById("gameFrame").src = "";
    }

    function showGame(type) {
        let url = "";
        if (type === 'typing') url = "/relax/typing";
        else if (type === 'matching') url = "/relax/matching";
        else if (type === 'balloon') url = "/relax/balloon";

        document.getElementById("gameFrame").src = url;
    }

    // 모달 바깥 클릭 시 닫기
    window.onclick = function(event) {
        if (event.target === document.getElementById("chartModal")) closeModal();
        else if (event.target === document.getElementById("cardModal")) closeCardModal();
        else if (event.target === document.getElementById("gameModal")) closeGameModal(); // 추가
    }
</script>

<jsp:include page="/includes/footer.jsp"/> 
<%-- <%@ include file="/includes/footer.jsp" %> --%>
</body>
</html>
