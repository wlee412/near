<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상담사 마이페이지</title>
<link rel="stylesheet" href="/css/common.css" />
<link rel="stylesheet" href="/css/counselor.css" />
</head>
<body>

	<div class="wrapper">
		<%-- 공통 헤더 --%>
		<%@ include file="../includes/header.jsp"%>

		<div class="counselor-container">

			<!-- 타이틀 한 줄 -->
			<div class="mypage-title">
				<h2>상담사 마이페이지</h2>
			</div>

			<!-- 좌우 영역 -->
			<div class="mypage-body">
				<!-- 좌측 사이드바 -->
				<aside class="mypage-sidebar">
					<ul>
						<li onclick="loadSection('profile')">프로필</li>
						<li onclick="loadSection('time')">상담 가능시간 설정</li>
						<li onclick="loadSection('reservation')">상담 예약현황</li>
						<li onclick="loadSection('room')">상담 방 개설하기</li>
					</ul>
				</aside>

				<!-- 오른쪽 콘텐츠 -->
				<section class="main-section" id="contentArea">
					<p>환영합니다, ${sessionScope.loginCounselor.name} 상담사님!</p>
				</section>
			</div>
		</div>
	</div>
		<%-- 공통 푸터 --%>
		<jsp:include page="../includes/footer.jsp" />
</body>
<script defer src="/js/counselor.js"></script>
</html>
