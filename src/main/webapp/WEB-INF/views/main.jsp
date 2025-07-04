<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>n:ear</title>
<link rel="stylesheet" href="/css/common.css" />
</head>
<body>
<meta name="viewport" content="width=device-width, initial-scale=1">


	<div class="wrapper">
		<%@ include file="includes/header.jsp"%>

		<section class="main-banner">
			<h1>가까이 귀 기울이는 누군가, n:ear</h1>
			<p>당신의 마음을 편안히 들어주는 온라인 상담 플랫폼</p>
			<button>상담 예약하기</button>
		</section>
	</div>
	<!-- Dialogflow 챗봇 UI -->


<style>
  df-messenger {
    position: fixed !important;
    bottom: 80px !important;
    right: 20px !important;
    z-index: 9999 !important;

    transform: scale(0.8); /* 챗봇창 전체를 70%로 축소 */
    transform-origin: bottom right; /* 우하단 기준으로 줄이기 */

    --df-messenger-button-titlebar-color: #5daec5;
    --df-messenger-chat-background-color: #ffffff;
    --df-messenger-font-color: #333333;
    --df-messenger-user-message: #d0f0c0;
    --df-messenger-bot-message: #e3f2fd;
  }
</style>

<df-messenger
  intent="WELCOME"
  chat-title="n:ear챗봇상담"
  chat-icon="https://favicon.io/emoji-favicons/robot/robot.png"
  agent-id="9f1618e2-0167-4e27-bb92-db0c60be0f6d"
  language-code="ko"
  chat-icon="https://example.com/my-icon.png">
</df-messenger>

<!--  반드시 가장 마지막에 이 스크립트  -->
<script src="https://www.gstatic.com/dialogflow-console/fast/messenger/bootstrap.js?v=1"></script>


	<%@ include file="includes/footer.jsp"%>
</body>
</html>
