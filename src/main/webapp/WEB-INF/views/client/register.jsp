<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/member.css">
	
</head>

<body>

<!-- 가입 중 스피너 -->
<div id="registerOverlay" style="display: none; text-align: center; padding: 20px;">
    <img src="/images/spinner.png" class="loading-spinner" alt="로딩 중">
    <p>가입 중입니다... 잠시만 기다려주세요</p>
</div>

<c:if test="${not empty error}">
	<script>
		alert("${error}");
	</script>
</c:if>

	<div class="container">
		<h2 class="title">회원가입</h2>
		<div class="divider"></div>

		<!-- 		<form action="/member/register" method="post"> -->
		<form id="registerForm" action="${pageContext.request.contextPath}/member/register" method="post">
			<input type="hidden" name="verify_type" value="MEMBER_JOIN"> 

			<!-- ID -->
			<div class="form-group">
				<label for="id">ID</label> <input type="text" name="id" id="id"
					placeholder="영문+숫자 4자 이상 입력" required>
				<div id="idCheckResult" class="result-text"
					style="font-size: 14px; margin-top: 4px;"></div>
			</div>

			<!-- 이름 -->
			<div class="form-group">
				<label for="name">이름</label> <input type="text" name="name"
					id="name" oninput="validateName()" placeholder="이름을 입력하세요" required>
				<div id="nameCheckResult" class="result-text"
					style="font-size: 14px; margin-top: 4px;"></div>
			</div>
			<!-- 닉네임 -->
			<div class="form-group">
				<label for="nickname">닉네임</label> <input type="text" name="nickname"
					id="nickname" placeholder="한글 2자 이상 입력" required>
				<div id="nicknameCheckResult" class="result-text"
					style="font-size: 14px; margin-top: 4px;"></div>
			</div>

			<!-- 비밀번호 -->
			<div class="form-group">
				<label for="pw">비밀번호</label> <input type="password" name="pw"
					id="pw" placeholder="영문+숫자+특수문자 포함 8자 이상" required>
				<div id="pwCheckResult" class="result-text"
					style="font-size: 14px; margin-top: 4px;"></div>
			</div>
			<!-- 비밀번호 확인 -->
			<div class="form-group">
				<label for="pwConfirm">비밀번호 확인</label> <input type="password"
					name="pwConfirm" id="pwConfirm" placeholder="비밀번호 재입력" required>
				<div id="pwMatchResult" class="result-text"
					style="font-size: 14px; margin-top: 4px;"></div>
			</div>

			<!-- 휴대폰 -->
			<div class="form-group">
				<label for="phone">휴대폰 번호</label> <input type="tel" name="phone"
					id="phone" required placeholder="숫자만 입력 (예: 01012345678)"
					maxlength="11">
				<div id="phoneCheckResult" class="result-text"
					style="font-size: 14px; margin-top: 4px;"></div>
			</div>

			<!-- 📧 이메일 입력 -->
			<div class="form-group">
				<label>이메일</label>
				<div class="input-row">
					<input type="text" id="emailId" name="emailId"
						placeholder="이메일 아이디" required> <span>@</span>

					<!-- 직접입력 input (초기에는 숨김) -->
					<input type="text" id="customEmailDomain" placeholder="직접입력"
						style="display: none;" disabled>
					<!-- 기본 도메인 선택 -->
					<select id="emailDomainSelect" name="emailDomain"
						onchange="handleDomainChange()" required>
						<option value="">도메인 선택</option>
						<option value="gmail.com">gmail.com</option>
						<option value="naver.com">naver.com</option>
						<option value="daum.net">daum.net</option>
						<option value="custom">직접입력</option>
					</select>

				</div>
				<div id="emailCheckResult" class="result-text"
					style="font-size: 14px; margin-top: 4px;"></div>
			</div>


			<!-- 생년월일 -->
			<div class="form-group">
				<label for="birth">생년월일</label> <input type="date" name="birthDate"
					id="birth" required>
			</div>


			<!-- 성별 -->
			<div class="form-group">
				<label>성별</label>
				<div class="radio-group">
					<label><input type="radio" name="gender" value="M" checked>남자</label>
					<label><input type="radio" name="gender" value="F">
						여자</label>
				</div>
			</div>

 
			<!-- 선호 장르 -->
			<div class="form-group">
				<label>선호 장르 (복수 선택가능)</label>
				<div class="checkbox-group">
					<label><input type="checkbox" name="genre" checked
						value="rpg">RPG</label> <label><input type="checkbox"
						name="genre" value="fps">FPS</label> <label><input
						type="checkbox" name="genre" value="sports">스포츠</label> <label><input
						type="checkbox" name="genre" value="strategy">전략</label> <label><input
						type="checkbox" name="genre" value="adventure">어드벤처</label>
				</div>
			</div>

			<!-- 이메일 약관 동의 -->

			<div class="form-group" style="align-items: center;">
				<label><input type="checkbox" name="emailAd" value="Y">
					이메일 광고 수신 동의</label>
			</div>


			<!-- 가입 버튼 -->
			<button type="submit" class="btn btn-full" id="registerBtn">가입하기</button>
		</form>
	</div>
	
	<script src="https://code.jquery.com/jquery-latest.js" defer></script>
	<script src="${pageContext.request.contextPath}/js/member.js" defer></script>
</body>
</html>

