<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>로그인</title>
<style>
.container {
	max-width: 1300px;
	margin: 0 auto;
	padding: 0 20px;
}

.logo-global {
	text-align: center;
	margin: 30px auto 40px;
}

.logo-global-img {
	justify-content: center;
	width: 250px;
	height: auto;
}

.divider {
	height: 1px;
	background-color: #ddd;
	margin: 20px 0;
}

.login-box {
	max-width: 350px;
	padding: 24px;
	margin: 100px auto 0 auto;
	background: white;
	border-radius: 12px;
	box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
}

.login-title {
	text-align: center;
	font-size: 24px;
	font-weight: bold;
	margin-bottom: 20px;
	color: #222;
}

.login-box input[type="text"], .login-box input[type="password"] {
	width: 100%;
	box-sizing: border-box;
	border: 1px solid #ccc;
	padding: 12px 14px;
	border-radius: 6px;
	font-size: 13px;
	background-color: #f9f9f9;
	transition: border 0.2s ease;
	margin-bottom: 15px;
}

.login-box input[type="submit"] {
	width: 100%;
	display: inline-flex;
	justify-content: center;
	align-items: center;
	padding: 10px 0;
	font-weight: bold;
	font-size: 15px;
	color: white;
	border: none;
	border-radius: 8px;
	cursor: pointer;
	margin-top: 30px;
	text-align: center;
	text-decoration: none;
	transition: background-color 0.2s ease;
	background-color: #222;
}

.login-btn:hover {
	background-color: red;
}

.login-options {
	display: flex;
	justify-content: space-between;
	align-items: center;
	font-size: 13px;
	margin: 10px 0;
}

.find-link {
	color: #333;
	text-decoration: none;
}

.find-link:hover {
	text-decoration: underline;
}

.sns-title {
	font-weight: bold;
	text-align: center;
	font-size: 15px;
	margin-bottom: 12px;
	color: #444;
}

.sns-buttons {
	display: flex;
	justify-content: center;
	gap: 12px;
	margin: 20px 0;
}

.sns-icon {
	width: 40px;
	height: 40px;
	border-radius: 50%;
	overflow: hidden;
	/* 	border: 1px solid #ccc; */
	/* 	background-color: white; */
	display: flex;
	justify-content: center;
	align-items: center;
}

.sns-icon img {
	width: 35px;
	height: 35px;
}

.sns-signup {
	margin-top: 20px;
	display: flex;
	justify-content: center;
}

.sns-signup a {
	padding: 10px 24px;
	font-size: 14px;
	border: 1px solid #aaa;
	border-radius: 24px;
	text-decoration: none;
	color: #222;
}

.bottom-links {
	text-align: center;
	font-size: 14px;
	margin-top: 20px;
}

.bottom-links a {
	color: #000;
	text-decoration: none;
	margin: 0 6px;
}

.bottom-links a:hover {
	text-decoration: underline;
}

.footer-copy {
	margin-top: 20px;
	text-align: center;
	font-size: 12px;
	color: #999;
	margin-bottom: 100px;
}

.signup-btn {
	display: inline-block;
	width: 100%;
	padding: 10px 0;
	font-weight: bold;
	font-size: 15px;
	color: #000;
	background-color: #fff;
	border: 1px solid #000;
	border-radius: 8px;
	text-align: center;
	margin-top: 10px;
	cursor: pointer;
	text-decoration: none;
}

.signup-btn:hover {
	background-color: #fff;
	color: #000;
	text-decoration: none !important;
}
</style>
</head>
<body>
	<c:if test="${not empty message}">
		<script>
			alert("${message}");
		</script>
	</c:if>
	
	<c:if test="${not empty error}">
    <script>
        alert("${error}");
    </script>
</c:if>

	<div class="container">


		<div class="login-box">
			<!-- 			<h2 class="login-title">겜세모 로그인</h2> -->
			<div class="logo-global">
			<a href="<c:url value='/main' />">
				<img src="https://i.imgur.com/VKxL314.png" alt="로고"
					class="logo-global-img">
			</a>
			</div>
			<!-- 			<div class="divider"></div> -->
			<form action="/member/login" method="post">
				<input type="text" name="id" placeholder="아이디" required> <input
					type="password" name="pw" placeholder="비밀번호" required>
				<div class="login-options">
					<label> <input type="checkbox" name="rememberMe">
						로그인 상태 유지
					</label> <a href="/member/find" class="find-link">아이디/비밀번호 찾기</a>
				</div>
				<input type="submit" name="login" value="로그인" class="login-btn">
			</form>

			<div class="bottom-links">

				<p class="sns-title">SNS 계정으로 간편 로그인하기</p>
				<div class="sns-buttons">
					<a href="/oauth2/authorization/google" class="sns-icon"> <img
						src="${pageContext.request.contextPath}/images/icons/google.svg"
						alt="Google">
					</a> <a href="/oauth2/authorization/naver" class="sns-icon"> <img
						src="${pageContext.request.contextPath}/images/icons/naver.svg"
						alt="Naver">
					</a> <a href="/oauth2/authorization/kakao" class="sns-icon"> <img
						src="${pageContext.request.contextPath}/images/icons/kakao.png"
						alt="Kakao">
					</a>

				</div>

				<div class="register">
					<a href="/member/register" class="signup-btn">회원가입하기</a>
				</div>
			</div>
		</div>
	</div>
	<div class="footer-copy">© 2025 겜세일모아. All rights reserved.</div>
</body>
</html>

