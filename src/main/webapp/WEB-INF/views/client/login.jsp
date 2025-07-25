<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>로그인</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">

</head>
<%@ include file="../includes/header.jsp"%>
<body class="login-page">


	<div class="wrapper">
		<div class="login-box">
			<div class="logo-global">
				<div class="logo">
					<a href="/">n:ear</a>
				</div>
			</div>

			<form id="loginForm" action="/client/login" method="post">
			    <input type="text" id="idInput" name="clientId" placeholder="아이디" required>
			    <input type="password" id="pwInput" name="password" placeholder="비밀번호" required>
			    
				<div class="login-options">
					<label><input type="checkbox" name="rememberMe">로그인 상태 유지</label> 
					<a href="/client/find" class="find-link" id="findLink">아이디/비밀번호 찾기</a>

					<!-- 토글 버튼 -->
					<div class="role-toggle">
						<span id="roleLabel">내담자</span> <label class="toggle-switch">
							<input type="checkbox" onchange="toggleRole(this)"> <span
							class="slider"></span>
						</label>
					</div>
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
					<a href="/client/register" class="signup-btn">회원가입하기</a>
				</div>
			</div>
		</div>
	</div>
	
	<%@ include file="../includes/footer.jsp"%>
	<script src="${pageContext.request.contextPath}/js/login.js"></script>
</body>
</html>
