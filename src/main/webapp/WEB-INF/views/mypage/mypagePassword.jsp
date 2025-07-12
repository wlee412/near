<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>비밀번호 변경</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/client.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
</head>
<body>
<!-- 헤더 영역 -->
<jsp:include page="/WEB-INF/views/includes/header.jsp" flush="true"/>

	<div class="mypage-wrapper">
		<!-- ✅ 사이드바 -->
		<div class="mypage-sidebar">
			<h1 class="mypage-title">
				<a href="${pageContext.request.contextPath}/mypage/" style="text-decoration: none; color: inherit;">마이페이지</a>
			</h1>

			<div class="mypage-divider"></div>

			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypageClientReservation'">예약확인</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypageReport'">검사기록</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypageFavorite'">즐겨찾기</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypageProfile'">프로필</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypageUpdate'">정보수정</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypagePassword'">비밀번호 변경</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypageDelete'">회원탈퇴</div>
			
		</div>
		<div class="mypage-content">
			<h2>비밀번호 변경</h2>
			<div class="divider"></div>

			<c:if test="${not empty success}">
				<p class="result-text" style="color: green;">${success}</p>
			</c:if>

			<!-- ✅ 소셜 로그인 여부에 따른 조건 분기 -->
			<c:choose>
				<c:when test="${empty loginClient.socialPlatform}">
					<form id="passwordForm" action="${pageContext.request.contextPath}/client/changePassword" method="post" onsubmit="return validatePasswordForm()">

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
								name="newPassword" required disabled oninput="checkNewPwValid()">
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
		</div>
	</div>
		
		<!-- js연결! -->
		<script src="${pageContext.request.contextPath}/js/mypagePassword.js"></script>
		
		<!-- 프로필 아이콘 드롭박스 -->
	<script src="<c:url value='/js/proFile.js'/>"></script>
<!-- 푸터 영역 -->
<jsp:include page="/WEB-INF/views/includes/footer.jsp" flush="true"/>
</body>
</html>