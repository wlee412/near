<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>내 프로필</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/member.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypage.css">
<link rel="stylesheet" href="<c:url value='/css/header.css' />" />
<link rel="stylesheet" href="<c:url value='/css/footer.css' />" />
</head>
<body>
<!-- 헤더 영역 -->
<jsp:include page="/WEB-INF/views/common/header.jsp" flush="true"/>
	<c:if test="${not empty message}">
		<script>
			alert("${message}");
		</script>
	</c:if>

	<!-- ✅ 마이페이지 wrapper 안에 타이틀 포함 -->
	<div class="mypage-wrapper">

		<!-- ✅사이드바 -->
		<%
		String uri = request.getRequestURI();
		%>
		<div class="mypage-sidebar">
			<h1 class="mypage-title">
				<a href="${pageContext.request.contextPath}/member/mypage"
					style="text-decoration: none; color: inherit;">마이페이지</a>
			</h1>

			<div class="mypage-divider"></div>

			<div
				class="menu-item <%= uri.contains("mypageProfile") ? "active" : "" %>"
				onclick="location.href='${pageContext.request.contextPath}/member/mypageProfile'">프로필</div>

			<div
				class="menu-item <%= uri.contains("mypageUpdate") ? "active" : "" %>"
				onclick="location.href='${pageContext.request.contextPath}/member/mypageUpdate'">정보수정</div>

			<div
				class="menu-item <%= uri.contains("mypagePassword") ? "active" : "" %>"
				onclick="location.href='${pageContext.request.contextPath}/member/mypagePassword'">비밀번호
				변경</div>

			<div
				class="menu-item <%= uri.contains("mypageDelete") ? "active" : "" %>"
				onclick="location.href='${pageContext.request.contextPath}/member/mypageDelete'">회원탈퇴</div>
		</div>

		<!-- ✅ 콘텐츠 영역 -->
		<div class="mypage-content">

			<h2 class="title">내 프로필</h2>
			<div class="divider"></div>

			<!-- 아이디 -->
			<div class="form-group">
				<label>아이디</label> <input type="text" value="${member.id}" readonly>
			</div>

			<!-- 이름 -->
			<div class="form-group">
				<label>이름</label> <input type="text" value="${member.name}" readonly>
			</div>

			<!-- 닉네임 -->
			<div class="form-group">
				<label>닉네임</label> <input type="text" value="${member.nickname}"
					readonly>
			</div>

			<!-- 전화번호 -->
			<div class="form-group">
				<label>휴대폰번호</label> <input type="text" value="${member.phone}"
					readonly>
			</div>

			<!-- 이메일 -->
			<div class="form-group">
				<label>이메일</label> <input type="text"
					value="${member.emailId}@${member.emailDomain}" readonly>
			</div>

			<!-- 생년월일 -->
			<div class="form-group">
				<label>생년월일</label> <input type="text" value="${member.birthDate}"
					readonly>
			</div>

		</div>

	</div>
	<!-- 프로필 아이콘 드롭박스 -->
	<script src="<c:url value='/js/proFile.js'/>"></script>
<!-- 푸터 영역 -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" flush="true"/>
</body>
</html>
