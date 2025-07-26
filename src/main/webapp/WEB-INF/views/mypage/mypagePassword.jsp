<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>비밀번호 변경</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypage.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypagePassword.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypageLayout.css">
</head>
<body>
	
	<c:if test="${not empty fail}">
		<script>
			alert('<c:out value="${fail}" />');
		</script>
	</c:if>
	<c:if test="${not empty success}">
		<script>
			alert('<c:out value="${success}" />');
		</script>
	</c:if>

	<div class="wrapper">
		<jsp:include page="/WEB-INF/views/includes/header.jsp" flush="true" />
		<div class="client-container">
			<div class="mypage-title">
				<a href="${pageContext.request.contextPath}/mypage/"><h2>마이페이지</h2></a>
			</div>
			<div class="mypage-body">
				<aside class="mypage-sidebar">
					<a
						href="${pageContext.request.contextPath}/mypage/mypageClientReservation"
						class="sidebar-button">예약확인</a> <a
						href="${pageContext.request.contextPath}/mypage/mypageReport"
						class="sidebar-button">검사기록</a> <a
						href="${pageContext.request.contextPath}/mypage/mypageFavorite"
						class="sidebar-button">즐겨찾기</a> <a
						href="${pageContext.request.contextPath}/mypage/mypageProfile"
						class="sidebar-button">프로필</a> <a
						href="${pageContext.request.contextPath}/mypage/mypageUpdate"
						class="sidebar-button">정보수정</a> <a
						href="${pageContext.request.contextPath}/mypage/mypagePassword"
						class="sidebar-button active">비밀번호 변경</a> <a
						href="${pageContext.request.contextPath}/mypage/mypageDelete"
						class="sidebar-button">회원탈퇴</a>
				</aside>
				<div class="mypage-content-wrapper">
					<section class="main-section" id="contentArea">
						<h3>비밀번호 변경</h3>
						<div class="divider"></div>

						<c:if test="${not empty success}">
							<p class="result-text" style="color: green;">${success}</p>
						</c:if>

						<!-- ✅ 소셜 로그인 여부에 따른 조건 분기 -->
						<c:choose>
							<c:when test="${empty loginClient.socialPlatform}">
								<form id="passwordForm"
									action="${pageContext.request.contextPath}/client/changePassword"
									method="post" onsubmit="return validatePasswordForm()">

									<div class="form-group">
										<label>회원 ID</label> <input type="text" name="id"
											value="${loginClient.clientId}" readonly>
									</div>

									<div class="form-group">
										<label>현재 비밀번호</label> <input type="password"
											id="currentPassword" name="currentPassword" required
											oninput="checkCurrentPassword()">
										<p id="currentPwResult" class="result-text"></p>
									</div>

									<div class="form-group">
										<label>새 비밀번호</label> <input type="password" id="newPw"
											name="newPassword" required disabled
											oninput="checkNewPwValid()">
										<p id="newPwMsg" class="result-text"></p>
									</div>

									<div class="form-group">
										<label>새 비밀번호 확인</label> <input type="password" id="confirmPw"
											name="confirmPassword" required disabled
											oninput="checkPwMatch()">
										<p id="pwMatchMsg" class="result-text"></p>

										<c:if test="${not empty error}">
											<p class="result-text" style="color: red;">
												<c:out value="${error}" />
											</p>
										</c:if>
									</div>

									<div class="form-group button-row">
										<button type="submit" class="btn btn-half">변경하기</button>
										<button type="button" class="btn btn-half"
											onclick="location.href='/mypage/'">취소</button>
									</div>
								</form>
							</c:when>

							<c:otherwise>
								<!-- ❌ 소셜 로그인 안내 -->
								<p style="font-size: 18px; color: #555; margin-top: 30px;">⚠️
									소셜 로그인으로 가입하신 회원은 비밀번호 변경이 불가능합니다.</p>
								<div class="form-group button-row" style="margin-top: 30px;">
									<button type="button" class="btn btn-half"
										onclick="location.href='/mypage/'">돌아가기</button>
								</div>
							</c:otherwise>
						</c:choose>
					</section>
				</div>
			</div>
		</div>
		<jsp:include page="/WEB-INF/views/includes/footer.jsp" flush="true" />
	</div>
	<div id="loadingOverlay" class="loading-overlay" style="display: none;">
		<div class="spinner"></div>
		<div class="loading-text">Loading...</div>
	</div>
	<script src="${pageContext.request.contextPath}/js/mypagePassword.js"></script>
	<script src="${pageContext.request.contextPath}/js/loading.js" defer></script>
</body>
</html>