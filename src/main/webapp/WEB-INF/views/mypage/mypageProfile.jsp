<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내 프로필</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypage.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypageProfile.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypageLayout.css">

</head>
<body>
	<!-- 헤더 영역 -->
	<c:if test="${not empty message}">
		<script>
			alert("${message}");
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
						class="sidebar-button active">프로필</a> <a
						href="${pageContext.request.contextPath}/mypage/mypageUpdate"
						class="sidebar-button">정보수정</a> <a
						href="${pageContext.request.contextPath}/mypage/mypagePassword"
						class="sidebar-button">비밀번호 변경</a> <a
						href="${pageContext.request.contextPath}/mypage/mypageDelete"
						class="sidebar-button">회원탈퇴</a>
				</aside>
				<div class="mypage-content-wrapper">
					<section class="main-section" id="contentArea">
						<h3>정보수정</h3>
						<div class="divider"></div>
						<!-- 아이디 -->
						<div class="form-group">
							<label>아이디</label> <input type="text" value="${client.clientId}"
								readonly>
						</div>

						<!-- 이름 -->
						<div class="form-group">
							<label>이름</label> <input type="text" value="${client.name}"
								readonly>
						</div>


						<!-- 전화번호 -->
						<div class="form-group">
							<label>휴대폰번호</label> <input type="text" value="${client.phone}"
								readonly>
						</div>

						<!-- 이메일 -->
						<div class="form-group">
							<label>이메일</label> <input type="text"
								value="${client.emailId}@${client.emailDomain}" readonly>
						</div>

						<!-- 생년월일 -->
						<div class="form-group">
							<label>생년월일</label> <input type="text" value="${client.birth}"
								readonly>
						</div>
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
	<script src="${pageContext.request.contextPath}/js/loading.js" defer></script>
</body>
</html>
