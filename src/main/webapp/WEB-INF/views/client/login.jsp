<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>로그인</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/login.css">
<style>
    .tab-container {
        position: relative;
        width: 280px;
        height: 40px;
        background-color: #64a0af; /* 로그인 버튼과 같은 파란색 */
        border-radius: 20px;
        display: flex;
        align-items: center;
        padding: 5px;
        margin: 0 auto 30px;
    }

    .tab-switch {
        position: absolute;
        top: 5px;
        left: 5px;
        width: 130px;
        height: 30px;
        background-color: white;
        border-radius: 15px;
        transition: transform 0.3s ease;
        z-index: 1;
    }

    .tab-labels {
        display: flex;
        justify-content: space-between;
        width: 100%;
        z-index: 2;
        font-size: 14px;
        font-weight: bold;
    }

    .tab-labels div {
        flex: 1;
        text-align: center;
        cursor: pointer;
        line-height: 30px;
        color: white;
    }

    .tab-labels .active {
        color: #333;
    }

    .login-box {
        width: 350px;
        background: #fff;
        padding: 30px;
        border-radius: 10px;
        box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        margin: 0 auto;
    }

    .login-form {
        display: none;
    }

    .login-form.active {
        display: block;
    }

    .login-btn {
        width: 100%;
        padding: 10px;
        background-color: #64a0af;
        border: none;
        color: white;
        font-size: 16px;
        border-radius: 5px;
        margin-top: 10px;
        cursor: pointer;
    }

    .sns-buttons img {
        width: 30px;
        height: 30px;
    }

    .bottom-links {
        text-align: center;
        margin-top: 30px;
    }

    .sns-buttons {
        display: flex;
        justify-content: center;
        gap: 20px;
        margin: 10px 0;
    }

    .signup-btn {
        display: inline-block;
        padding: 10px 20px;
        border: 1px solid black;
        border-radius: 5px;
        text-decoration: none;
        color: black;
        margin-top: 10px;
    }
</style>

</head>
<%@ include file="../includes/header.jsp"%>
<body>
	<div class="content-wrapper">
		<c:if test="${not empty message}">
			<script>alert("${message}");</script>
		</c:if>
		<c:if test="${not empty error}">
			<script>alert("${error}");</script>
		</c:if>

		<div class="tab-container">
    <div id="tab-indicator" class="tab-switch"></div>
    <div class="tab-labels">
        <div id="client-tab" class="active" onclick="switchTab('client')">내담자</div>
        <div id="counselor-tab" onclick="switchTab('counselor')">상담자</div>
    </div>
</div>

<div class="login-box">
    <div class="logo-global" style="text-align: center; margin-bottom: 20px;">
        <a href="/"><div class="logo" style="font-size: 28px; font-weight: bold; color: #64a0af;">n:ear</div></a>
    </div>

    <!-- 내담자 로그인 폼 -->
    <form id="client-form" class="login-form active" action="/client/login" method="post">
        <input type="text" name="clientId" placeholder="아이디" required style="width: 100%; padding: 10px; margin-bottom: 10px;">
        <input type="password" name="password" placeholder="비밀번호" required style="width: 100%; padding: 10px; margin-bottom: 10px;">
        <div class="login-options" style="display: flex; justify-content: space-between; font-size: 13px;">
            <label><input type="checkbox" name="rememberMe"> 로그인 상태 유지</label>
            <a href="/client/find">아이디/비밀번호 찾기</a>
        </div>
        <input type="submit" value="로그인" class="login-btn">
    </form>

    <!-- 상담자 로그인 폼 -->
    <form id="counselor-form" class="login-form" action="/counselor/login" method="post">
        <input type="text" name="counselorId" placeholder="상담자 아이디" required style="width: 100%; padding: 10px; margin-bottom: 10px;">
        <input type="password" name="password" placeholder="비밀번호" required style="width: 100%; padding: 10px; margin-bottom: 10px;">
        <input type="submit" value="로그인" class="login-btn">
    </form>

    <!-- SNS 및 회원가입 영역 -->
    <div id="sns-section" class="bottom-links">
        <p class="sns-title">SNS 계정으로 간편 로그인하기</p>
        <div class="sns-buttons">
            <a href="/oauth2/authorization/google"><img src="${pageContext.request.contextPath}/images/icons/google.svg" alt="Google"></a>
            <a href="/oauth2/authorization/naver"><img src="${pageContext.request.contextPath}/images/icons/naver.svg" alt="Naver"></a>
            <a href="/oauth2/authorization/kakao"><img src="${pageContext.request.contextPath}/images/icons/kakao.png" alt="Kakao"></a>
        </div>
        <div class="register">
            <a href="/client/register" class="signup-btn">회원가입하기</a>
        </div>
    </div>
</div>
	</div>
	<%@ include file="../includes/footer.jsp"%>
	
	<script>
    function switchTab(role) {
        const indicator = document.getElementById("tab-indicator");
        const clientTab = document.getElementById("client-tab");
        const counselorTab = document.getElementById("counselor-tab");
        const clientForm = document.getElementById("client-form");
        const counselorForm = document.getElementById("counselor-form");
        const snsSection = document.getElementById("sns-section");

        if (role === "client") {
            indicator.style.transform = "translateX(0%)";
            clientTab.classList.add("active");
            counselorTab.classList.remove("active");
            clientForm.classList.add("active");
            counselorForm.classList.remove("active");
            snsSection.style.display = "block";
        } else {
            indicator.style.transform = "translateX(100%)";
            counselorTab.classList.add("active");
            clientTab.classList.remove("active");
            counselorForm.classList.add("active");
            clientForm.classList.remove("active");
            snsSection.style.display = "none";
        }
    }

    // 기본 탭 설정
    window.onload = () => switchTab('client');
</script>
</body>
</html>
