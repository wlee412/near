<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상담 가능 시간 설정</title>
<link rel="stylesheet" href="/css/common.css" />
<link rel="stylesheet" href="/css/counselor.css" />

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<!-- FullCalendar CSS & JS -->
<!-- <link -->
<!-- 	href="https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.css" -->
<!-- 	rel="stylesheet" /> -->
<script
	src="https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.js"></script>

<!-- 캘린더 기능용 JS -->
<script src="/js/counselor.js"></script>
</head>
<body>
	<div class="wrapper">
		<%@ include file="../includes/header.jsp"%>

		<div class="counselor-container">
			<!-- 타이틀 -->
			<div class="mypage-title">
			<a href="/counselor/mypage">
				<h2>상담사 마이페이지</h2>
				</a>
			</div>

			<div class="mypage-body">
				<!-- 좌측 사이드바 -->
				<aside class="mypage-sidebar">
					<a href="/counselor/profile" class="sidebar-button">프로필</a> <a
						href="/counselor/time" class="sidebar-button active">상담 가능시간
						설정</a> <a href="/counselor/reservation" class="sidebar-button">상담
						예약현황</a>
				</aside>

				<!-- 우측 콘텐츠 -->
				<section class="main-section">
					<div class="time-box">
						<h3 class="section-title">예약가능한 날짜와 시간을 선택해주세요</h3>
						
						<div id="calendar-wrapper">
						<!-- 캘린더 영역 -->
						<div id="calendar" class="calendar-container"></div>
									</div>
						<!-- 날짜출력 -->
						<div id="selected-date-box" style="margin-top: 20px;">
							<p>
								<span id="selected-date"></span>
							</p>
						</div>

						<!-- 시간 버튼 영역 -->
						
  						<div id="time-buttons-wrapper">
						<div id="time-buttons" class="time-buttons-container">
						</div></div>

						<!-- 저장 버튼 -->
						<div class="button-wrapper" style="margin-top: 20px;">
							<button type="button" id="save-available-times"
								class="save-button">예약 가능 시간 저장</button>
						</div>
					</div>
				</section>
			</div>
		</div>
	</div>

	<%@ include file="../includes/footer.jsp"%>
	<script>
		document.addEventListener('DOMContentLoaded', function() {
			initCalendar();
		});
	</script>
</body>
</html>
