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
<script src="http://code.jquery.com/jquery-latest.js"></script>
<%-- jQuery CDN --%>
</head>
<body>
	<div class="wrapper">
		<%@ include file="../includes/header.jsp"%>

		<div class="counselor-container">
			<!-- 타이틀 -->
			<div class="mypage-title">
				<h2>상담사 마이페이지</h2>
			</div>

			<!-- 본문: 좌우 영역 -->
			<div class="mypage-body">
				<!-- 좌측 사이드바 -->
				<aside class="mypage-sidebar">
					<a href="/counselor/profile" class="sidebar-button">프로필</a> <a
						href="/counselor/time" class="sidebar-button">상담 가능시간 설정</a> <a
						href="/counselor/reservation" class="sidebar-button">상담 예약현황</a> <a
						href="/counselor/room" class="sidebar-button">상담 방 개설하기</a>

				</aside>

				<!-- 우측 콘텐츠 -->
				<section class="main-section" id="contentArea">
					<div class="welcome-box">
						<h2>환영합니다, ${sessionScope.loginCounselor.name} 상담사님!</h2>
						<p class="today-count">
							오늘 접수된 신규 상담 건수: <strong>${todayCount == null ? 0 : todayCount}</strong>
							건
						</p>
					</div>
				</section>
			</div>
		</div>
	</div>

	<%@ include file="../includes/footer.jsp"%>

	<script src="/js/counselor.js"></script>
	
</body>
</html>
