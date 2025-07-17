<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>회원탈퇴</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypageLayout.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypageDelete.css">
</head>
<body>

	<!-- 회원탈퇴 페이지의 맨 위나 body 안에 삽입 -->
	<c:if test="${not empty error}">
		<script>
			alert("${error}");
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
			<a href="${pageContext.request.contextPath}/mypage/mypageClientReservation" class="sidebar-button">예약확인</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageReport" class="sidebar-button">검사기록</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageFavorite" class="sidebar-button">즐겨찾기</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageProfile" class="sidebar-button">프로필</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageUpdate" class="sidebar-button">정보수정</a>
			<a href="${pageContext.request.contextPath}/mypage/mypagePassword" class="sidebar-button">비밀번호 변경</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageDelete" class="sidebar-button active">회원탈퇴</a>
		</aside>
		<div class="mypage-content-wrapper">
		<section class="main-section" id="contentArea">
			<h3>회원탈퇴</h3>
			<div class="divider"></div>
			<c:choose>
				<%-- 일반 회원 --%>
				<c:when test="${empty loginClient.socialPlatform}">
					<form action="/client/delete" method="post"
						onsubmit="return validateDeleteForm();">
						<div class="form-group">
							<label>ID</label> <input type="text" name="clientId"
								value="${loginClient.clientId}" readonly> <input
								type="hidden" name="clientId" value="${loginClient.clientId}" />
						</div>

						<div class="form-group">
							<label>비밀번호</label> <input type="password" name="pw" required>
						</div>

						<div class="form-group">
							<label>비밀번호 확인</label> <input type="password" name="pwConfirm"
								oninput="checkDeletePwMatch()">
						</div>

						<p id="pwMatchMsg" class="result-text"></p>

						<div class="form-group button-row">
							<button type="submit" class="btn btn-half">탈퇴하기</button>
							<button type="button" class="btn btn-half"
								onclick="location.href='/mypage/'">취소</button>
						</div>
					</form>
				</c:when>

				<%-- 소셜 로그인 회원 --%>
				<c:otherwise>
					<form action="${pageContext.request.contextPath}/client/delete"
						method="post">
						<input type="hidden" name="social" value="true" /> <input
							type="hidden" name="clientId" value="${loginClient.clientId}" />
						<div class="form-group button-row">
							<button type="submit" class="btn btn-half">소셜로그인 연동 해제</button>
							<button type="button" class="btn btn-half"
								onclick="location.href='${pageContext.request.contextPath}/mypage/'">
								취소</button>
						</div>
					</form>
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

	<!-- js연결! -->
	<script src="https://code.jquery.com/jquery-latest.js"></script>
	<script src="${pageContext.request.contextPath}/js/mypageDelete.js"></script>
	<script src="${pageContext.request.contextPath}/js/loading.js" defer></script>
	<!-- 프로필 아이콘 드롭박스 -->
	<!-- 푸터 영역 -->
</body>
</html>
