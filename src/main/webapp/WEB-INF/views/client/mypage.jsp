<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>마이페이지</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypage.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/client.css">
</head>
<body>
	<jsp:include page="/WEB-INF/views/includes/header.jsp" flush="true"/>
	<!-- ✅메시지 알림 -->
	<c:if
		test="${not empty message or not empty success or not empty error}">
		<script>
			window
					.addEventListener(
							'DOMContentLoaded',
							function() {
								<c:if test="${not empty message}">alert("<c:out value='${message}'/>");
								</c:if>
								<c:if test="${not empty success}">alert("<c:out value='${success}'/>");
								</c:if>
								<c:if test="${not empty error}">alert("<c:out value='${error}'/>");
								</c:if>
							});
		</script>
	</c:if>

	<!-- ✅ 마이페이지 wrapper 안에 타이틀 포함 -->
	<div class="mypage-wrapper">

		<!-- ✅ 사이드바 -->
		<div class="mypage-sidebar">
			<h1 class="mypage-title">
				<a href="${pageContext.request.contextPath}/client/mypage"
					style="text-decoration: none; color: inherit;">마이페이지</a>
			</h1>
			<div class="mypage-divider"></div>

			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/client/mypageProfile'">프로필</div>
			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/client/mypageUpdate'">정보수정</div>
			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/client/mypagePassword'">비밀번호
				변경</div>
			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/client/mypageDelete'">회원탈퇴</div>
		</div>

		<!-- ✅ 콘텐츠 영역 -->
		<div class="mypage-content">

			<div class="mypage-welcome-box">
				<h2 class="welcome-title">${client.name}님 환영합니다!</h2>
				<p class="welcome-date">
					가입일 :
					<fmt:formatDate value="${client.regDate}" pattern="yyyy.MM.dd" />
				</p>
				<p class="welcome-dday">
					N:EAR와 함께한 지 <strong>D+${dDay}</strong>일 😊
				</p>
			</div>

		</div>
	</div>
	<jsp:include page="/WEB-INF/views/includes/footer.jsp" flush="true"/>
	
</body>
</html>
