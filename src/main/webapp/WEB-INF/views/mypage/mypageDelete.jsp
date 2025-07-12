<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>회원탈퇴</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypage.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/client.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common.css">
</head>
<body>
	<!-- 헤더 영역 -->
	<jsp:include page="/WEB-INF/views/includes/header.jsp" flush="true" />

	<!-- 회원탈퇴 페이지의 맨 위나 body 안에 삽입 -->
	<c:if test="${not empty error}">
		<script>
			alert("${error}");
		</script>
	</c:if>
	

	<!-- ✅ 마이페이지 wrapper 안에 타이틀 포함 -->
	<div class="mypage-wrapper">

		<!-- ✅ 사이드바 -->
		<%
		String uri = request.getRequestURI();
		%>
		<div class="mypage-sidebar">
			<h1 class="mypage-title">
				<a href="${pageContext.request.contextPath}/mypage/"
					style="text-decoration: none; color: inherit;">마이페이지</a>
			</h1>

			<div class="mypage-divider"></div>

			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/mypage/mypageClientReservation'">예약확인</div>
			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/mypage/mypageReport'">검사기록</div>
			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/mypage/mypageFavorite'">즐겨찾기</div>
			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/mypage/mypageProfile'">프로필</div>
			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/mypage/mypageUpdate'">정보수정</div>
			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/mypage/mypagePassword'">비밀번호
				변경</div>
			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/mypage/mypageDelete'">회원탈퇴</div>
		</div>
		
			

		<!-- ✅ 콘텐츠 영역 -->
		<div class="mypage-content">
			<h2>회원탈퇴</h2>
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
		</div>
	</div>

	</div>

	<div style="height: 300px"></div>
	<!-- js연결! -->
	<script src="https://code.jquery.com/jquery-latest.js"></script>
	<script src="${pageContext.request.contextPath}/js/mypageDelete.js"></script>

	<!-- 프로필 아이콘 드롭박스 -->
	<!-- 푸터 영역 -->
	<jsp:include page="/WEB-INF/views/includes/footer.jsp" flush="true" />
</body>
</html>
