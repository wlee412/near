<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<title>회원탈퇴</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypage.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/member.css">
<link rel="stylesheet" href="<c:url value='/css/header.css' />" />
<link rel="stylesheet" href="<c:url value='/css/footer.css' />" />
</head>
<body>
<!-- 헤더 영역 -->
<jsp:include page="/WEB-INF/views/common/header.jsp" flush="true"/>

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
			<h2 class="title">회원 탈퇴</h2>
			<div class="divider"></div>

			<form action="/member/delete" method="post"
				onsubmit="return validateDeleteForm();">

				<div class="form-group">
					<label>ID</label> <input type="text" name="id"
						value="${loginMember.id}" readonly>
				</div>

				<div class="form-group">
					<label>비밀번호</label> <input type="password" name="pw" required>
				</div>

				<div class="form-group">
					<label>비밀번호 확인</label> <input type="password" name="pwConfirm"
						oninput="checkDeletePwMatch()">

				</div>
				<!-- 자바스크립트 결과 메시지 표시 -->
				<p id="pwMatchMsg" class="result-text"></p>

				<div class="form-group button-row">
					<button type="submit" class="btn btn-half">탈퇴하기</button>
					<button type="button" class="btn btn-half"
						onclick="location.href='/member/mypage'">취소</button>
				</div>

			</form>
		</div>
		
		</div>

		<div style="height:300px"></div>
		<!-- js연결! -->
		<script src="${pageContext.request.contextPath}/js/member.js"></script>
		
		<!-- 프로필 아이콘 드롭박스 -->
	<script src="<c:url value='/js/proFile.js'/>"></script>
<!-- 푸터 영역 -->
<jsp:include page="/WEB-INF/views/common/footer.jsp" flush="true"/>
</body>
</html>
