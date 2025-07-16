
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%-- <%@ include file="/includes/header.jsp" %> --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/luckycard.css"> <!-- CSS 연결 -->

<h2 style="text-align: center; margin-top: 30px;">오늘의 행운카드를 선택해보세요</h2>

<div class="card-choice-container">
    <div class="flip-card" onclick="selectCard(this, 0)">
        <div class="flip-card-inner">
            <div class="flip-card-front">🌸 행운 카드 </div>
            <div class="flip-card-back" id="back-0"></div>
        </div>
    </div>
    <div class="flip-card" onclick="selectCard(this, 1)">
        <div class="flip-card-inner">
            <div class="flip-card-front">🌸 행운 카드 </div>
            <div class="flip-card-back" id="back-1"></div>
        </div>
    </div>
    <div class="flip-card" onclick="selectCard(this, 2)">
        <div class="flip-card-inner">
            <div class="flip-card-front">🌸 행운 카드 </div>
            <div class="flip-card-back" id="back-2"></div>
        </div>
    </div>
</div>

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
        "✨ 믿는 만큼 좋은 일이 생겨요.",
        "🌟 당신의 하루는 분명히 의미 있는 하루가 될 거예요.",
        "🫶 당신의 작은 친절이 누군가에게 큰 힘이 될 거예요.",
        "🌷 지금 이 순간, 당신은 충분히 잘하고 있어요.",
        "🕊️ 힘들면 잠시 쉬어가도 괜찮아요.",
        "💫 오늘은 어제보다 더 나은 날이 될 거예요.",
        "🎈 당신은 혼자가 아니에요. 누군가는 항상 당신을 생각하고 있어요.",
        "🍎 당신이 가진 따뜻함은 누군가에겐 선물이에요.",
        "🌟 조급해하지 마세요. 당신의 속도도 괜찮아요.",
        "🌸 오늘은 당신에게 특별한 선물이 찾아올지도 몰라요.",
        "🌿 스스로를 믿는 그 마음이 가장 큰 힘이에요.",
        "💖 오늘도 당신은 누군가에게 소중한 사람이에요.",
        "🌞 걱정보다는 기대가 더 많은 하루가 되길 바라요.",
        "🦋 지금의 당신도 충분히 아름답습니다.",
        "📖 오늘은 당신 이야기에 특별한 한 페이지가 더해질 거예요.",
        "🌼 당신의 미소는 세상을 조금 더 따뜻하게 만듭니다.",
        "✨ 아무 일 없어도 괜찮아요. 그런 날도 꼭 필요하니까요.",
        "🎵 작은 기쁨이 오늘 하루를 가득 채워줄 거예요.",
        "☕ 따뜻한 마음 하나면, 어떤 날도 견딜 수 있어요.",
        "🌈 당신은 생각보다 많은 걸 이겨내온 사람이에요."


    ];

    const randomMsg = messages[Math.floor(Math.random() * messages.length)];
    const backDiv = document.getElementById("back-" + index);
    backDiv.innerText = randomMsg;
}
</script>

<%-- <%@ include file="/includes/footer.jsp" %> --%>
