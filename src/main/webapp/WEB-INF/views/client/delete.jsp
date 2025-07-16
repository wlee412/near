<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<title>회원탈퇴</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/member.css">
</head>
<body>

	<!--✅ 마이페이지 헤더 추가 -->
	<div class="mypage-header">
		<h1 class="mypage-title">마이페이지</h1>
		<div class="divider"></div>
	</div>


	<div class="container">
		<h2 class="title">회원 탈퇴</h2>
		<div class="divider"></div>

		<form action="/member/delete" method="post"
			onsubmit="return confirmDelete();">
			<div class="form-group">
				<label>ID</label> <input type="text" name="id"
					value="${loginMember.id}" readonly>
			</div>

			<div class="form-group">
				<label>비밀번호</label> <input type="password" name="pw" required>
			</div>

			<div class="form-group">
				<label>비밀번호 확인</label> <input type="password" name="pwConfirm"
					required oninput="checkPwMatch()">

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

	<!-- js연결! -->
	<script src="${pageContext.request.contextPath}/js/member.js"></script>
</body>
</html>