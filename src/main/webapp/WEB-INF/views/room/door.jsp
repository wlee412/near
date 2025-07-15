<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>코드 입력</title>
<link rel="stylesheet" href="/css/common.css" type="text/css" />
<link rel="stylesheet" href="/css/roomdoor.css" type="text/css" />
</head>
<body>
	<div class="wrapper">
		<jsp:include page="/WEB-INF/views/includes/header.jsp" flush="true" />
			<div class="container">
				<img src="https://i.imgur.com/H5grzMN.png" alt="logo-img">
				<form action="/chat/token" method="get">
					<div id="token-input">
						<input type="text" name="t" id="token" required>
						<button type="submit" class="svg-btn">
						입장
						<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-log-in-icon lucide-log-in"><path d="m10 17 5-5-5-5"/><path d="M15 12H3"/><path d="M15 3h4a2 2 0 0 1 2 2v14a2 2 0 0 1-2 2h-4"/></svg>
						</button>
					</div>
					<h1>상담실 입장 코드를 입력하세요</h1>
				</form>
			</div>
		<jsp:include page="/WEB-INF/views/includes/footer.jsp" flush="true" />
	</div>
</body>
</html>