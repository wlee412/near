<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common.css">

<!-- 공통 헤더 -->
<header>
	<div class="header-container">
		<div class="logo">
			<a href="/">n:ear</a>
		</div>

		<nav class="center-menu">
			<c:if test="${not empty loginCounselor or not empty loginClient}">
				<div class="spacer"></div>
			</c:if> <!-- 로그인한 경우에만 표시 -->

			<a href="/introduce">소개</a> <a href="/reservation">상담예약</a> <a
				href="/room/door">상담하기</a> <a href="/survey/selfSurveyList">심리검사</a>
			<a href="/hospitalMap">병원찾기</a> <a href="/mental/mentalDashboard">멘탈케어</a>
		</nav>

		<div class="login">
			<c:choose>
				<c:when test="${not empty sessionScope.loginClient}">
					<a href="/client/logout">로그아웃</a>
					<a href="/mypage">마이페이지</a>
				</c:when>
				<c:when test="${not empty sessionScope.loginCounselor}">
					<a href="/client/logout">로그아웃</a>
					<a href="/counselor/mypage">상담사페이지</a>
				</c:when>
				<c:otherwise>
					<a href="/client/login">로그인</a>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</header>
