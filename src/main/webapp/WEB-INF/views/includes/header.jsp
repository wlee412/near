<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- 공통 헤더 -->
<header>
	<div class="header-container">
		<div class="logo">
			<a href="/">n:ear</a>
		</div>

		<nav class="center-menu">
			<a href="/introduce">소개</a> <a href="/reservation">상담예약</a> <a
				href="/chat">상담하기</a> <a href="/survey/list">심리검사</a> <a href="/hospital">병원찾기</a>
		</nav>

		<div class="login">
			<c:choose>
				<c:when test="${not empty sessionScope.loginClient}">
					<a href="/client/logout">로그아웃</a>
					<a href="/client/mypage">마이페이지</a>
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
