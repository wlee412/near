z<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage.css">
</head>
<body>

	<div class="wrapper">
	<jsp:include page="/WEB-INF/views/includes/header.jsp" flush="true" />
		<div class="client-container">
		<div class="mypage-title">
			<a href="${pageContext.request.contextPath}/mypage/"><h2>마이페이지</h2></a>
	    </div>
		<div class="mypage-body">
		<aside class="mypage-sidebar">
			<a href="${pageContext.request.contextPath}/mypage/mypageClientReservation" class="sidebar-button">예약확인</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageReport" class="sidebar-button">검사기록</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageFavorite" class="sidebar-button">즐겨찾기</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageProfile" class="sidebar-button">프로필</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageUpdate" class="sidebar-button">정보수정</a>
			<a href="${pageContext.request.contextPath}/mypage/mypagePassword" class="sidebar-button">비밀번호 변경</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageDelete" class="sidebar-button">회원탈퇴</a>
		</aside>
			
			<section class="main-section" id="contentArea">
				<div class="welcome-box">
					<h2>${client.name}님 환영합니다!</h2>
				</div>
							<h3>관심사 기반 추천 설문</h3>
			<c:if test="${empty recommendedSurveys}">
				<p>추천 설문이 없습니다.</p>
			</c:if>

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
				
			</section>
		</div>
		</div>
	</div>

	<jsp:include page="/WEB-INF/views/includes/footer.jsp" flush="true" />
	<script src="${pageContext.request.contextPath}/js/mypage.js" defer></script>
</body>
</html>
