<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypage.css">
</head>
<body>
	<jsp:include page="/WEB-INF/views/includes/header.jsp" flush="true" />

	<div class="mypage-wrapper">
		<div class="mypage-sidebar">
			<h1 class="mypage-title">
				<a href="${pageContext.request.contextPath}/mypage/"
					style="text-decoration: none; color: inherit;">마이페이지</a>
			</h1>
			<div class="mypage-divider"></div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypageReservation'">예약확인</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypageReport'">검사기록</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypagePharmFav'">즐겨찾기</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypageProfile'">프로필</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypageUpdate'">정보수정</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypagePassword'">비밀번호 변경</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypageDelete'">회원탈퇴</div>
		</div>

		<div class="mypage-content">
			<!-- ✅ 기본 정보 -->
			<h2>${client.name}님 환영합니다!</h2>

			<!-- ✅ 구분선 -->
			<div class="mypage-divider" style="margin-top: 40px; margin-bottom: 30px;"></div>
			<!-- ✅ 추천 설문 -->
			<h3>관심사 기반 추천 설문</h3>
			<c:if test="${empty recommendedSurveys}">
				<p>추천 설문이 없습니다.</p>
			</c:if>

			<!-- ✅ 슬라이더 전체 컨테이너 -->
			<div class="slider-wrapper-container">
				<div class="slider-wrapper">
					<button class="slider-button prev" onclick="moveSlide(-1)">&#8249;</button>

					<div class="slider-track" id="sliderTrack">
						<c:forEach var="survey" items="${recommendedSurveys}">
							<div class="slider-card">
								<h4>${survey.surveyName}</h4>
								<p>${survey.desc}</p>
								<a
									href="${pageContext.request.contextPath}/survey/start?surveyId=${survey.surveyId}">설문
									시작</a>
							</div>
						</c:forEach>
					</div>

					<button class="slider-button next" onclick="moveSlide(1)">&#8250;</button>
				</div>
			</div>
			
			<c:if test="${not empty recommendExplanation}">
			    <div class="recommend-explanation-box">
			        <h3>추천 이유</h3>
			        <p>${recommendExplanation}</p>
			    </div>
			</c:if>
		</div>
	</div>

	<jsp:include page="/WEB-INF/views/includes/footer.jsp" flush="true" />
	<script src="${pageContext.request.contextPath}/js/mypage.js" defer></script>
</body>
</html>
