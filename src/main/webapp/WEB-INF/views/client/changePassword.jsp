<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title> 비밀번호 변경하기 </title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/member.css">
</head>
<body>
	<div class="container">
		<h2>비밀번호 변경</h2>
		<div class= "divider"></div>

		<c:if test="${not empty error}">
			<p class="result-text" style="color: red;">${error}</p>
		</c:if>

		<c:if test="${not empty success}">
			<p class="result-text" style="color: green;">${success}</p>
		</c:if>
		
		
		<form
			action="${pageContext.request.contextPath}/member/changePassword"
			method="post">
			<!-- 회원ID -->
			<div class="form-group">
				<label>회원 ID</label> <input type="text" name="id"
					value="${loginMember.id}" readonly>
			</div>

			<!--현재 비밀번호 -->
			<div class="form-group">
				<label>현재 비밀번호</label> <input type="password" id="currentPassword"
					name="currentPassword" required oninput="checkCurrentPassword()">
				<p id="currentPwResult" class="result-text"></p>
			</div>

			<!-- 새 비밀번호 -->
			<div class="form-group">
				<label>새 비밀번호</label> <input type="password" id="newPw"
					name="newPassword" required disabled oninput="checkNewPwValid()">
				<p id="newPwMsg" class="result-text"></p>
				<!-- ✅ 새 비밀번호 유효성 메시지 -->
			</div>

			<!-- 새 비밀번호 확인 -->
			<div class="form-group">
				<label>새 비밀번호 확인</label> <input type="password" id="confirmPw"
					name="confirmPassword" required disabled oninput="checkPwMatch()">
				<p id="pwMatchMsg" class="result-text"></p>
				<!-- ✅ 비밀번호 일치 여부 메시지 -->
			</div>

			<!-- 버튼 -->
			<div class="form-group button-row">
				<button type="submit" class="btn btn-half">변경하기</button>
				<button type="button" class="btn btn-half"
					onclick="location.href='/member/mypage'">취소</button>
			</div>
		</form>
	</div>

	<!-- js연결! -->
	<script src="${pageContext.request.contextPath}/js/member.js"></script>
</body>
</html>
