<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>n:ear</title>
<link rel="stylesheet" href="/css/common.css" />
</head>
<body>
	<div class="wrapper">
		<%@ include file="includes/header.jsp"%>

		<section class="main-banner">
			<h1>가까이 귀 기울이는 누군가, n:ear</h1>
			<p>당신의 마음을 편안히 들어주는 온라인 상담 플랫폼</p>
			<button>상담 예약하기</button>
		</section>
	</div>
	<!-- Dialogflow 최신 챗봇 UI -->
	<script
		src="https://www.gstatic.com/dialogflow-console/fast/messenger/bootstrap.js?v=1"></script>
	<df-messenger
 		intent="WELCOME"
  		chat-title="n:ear 챗봇상담"
 		agent-id="your-agent-id"
  		language-code="ko"
  		chat-icon="/img/chat-icon.png"
  		style="
   			 position: fixed;
   			 bottom: 60px;
   			 right: 24px;
   			 transform: translateY(-50%);
   			 z-index: 9999;
  ">
</df-messenger>

	<%@ include file="includes/footer.jsp"%>
</body>
</html>
