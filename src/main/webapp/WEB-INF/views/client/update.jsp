<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html>
<head>
<title>회원정보 수정</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/member.css">


</head>
<body>
	<div class="container">
		<h2 class="title">회원정보 수정</h2>
		<div class="divider"></div>

		<!-- 수정 성공/ 실패 메시지 alert -->
		<c:if test="${not empty message}">
			<script>
				alert("${message}");
			</script>
		</c:if>


		<form action="/member/update" method="post"
			onsubmit="return validateForm();">

			<!-- 아아디 -->
			<div class="form-group">
				<label>아이디</label> <input type="text" name="id" value="${member.id}"
					readonly>
			</div>
			<!-- 이름 -->
			<div class="form-group">
				<label for="name">이름</label> <input type="text" name="name"
					id="name" value="${member.name}">
				<div id="nameCheckResult" class="result-text"
					style="font-size: 14px; margin-top: 4px;"></div>
			</div>

			<div class="form-group">
				<label>닉네임</label> <input type="text" id="nickname" name="nickname"
					value="${member.nickname}">
				<div id="nicknameCheckResult" class="result-text"
					style="font-size: 14px; margin-top: 4px;"></div>
			</div>
			<div class="form-group">
				<label>휴대폰 번호</label> <input type="tel" id="phone" name="phone"
					maxlength="11" value="${member.phone}">
				<div id="phoneCheckResult" class="result-text"
					style="font-size: 14px; margin-top: 4px;"></div>
			</div>

			<!-- 📧 이메일 입력 -->
			<div class="form-group">
				<label>이메일</label>
				<div class="input-row">
					<!-- 이메일 아이디 -->
					<input type="text" id="emailId" name="emailId"
						placeholder="이메일 아이디" value="${member.emailId}" required>
					<span>@</span>

					<!-- 직접입력 -->
					<input type="text" id="customEmailDomain" name="emailDomain"
						placeholder="직접입력" style="display: none;"
						<%-- 직접입력을 사용했던 경우만 보여지게 처리 (JS가 제어) --%>
						value="${member.emailDomain}"
						${member.emailDomain != 'gmail.com' && member.emailDomain != 'naver.com' && member.emailDomain != 'daum.net' ? '' : 'disabled'}>

					<!-- 도메인 선택 -->
					<select id="emailDomainSelect" name="emailDomain"
						onchange="handleDomainChange()" required>
						<option value="">도메인 선택</option>
						<option value="gmail.com"
							${member.emailDomain == 'gmail.com' ? 'selected' : ''}>gmail.com</option>
						<option value="naver.com"
							${member.emailDomain == 'naver.com' ? 'selected' : ''}>naver.com</option>
						<option value="daum.net"
							${member.emailDomain == 'daum.net' ? 'selected' : ''}>daum.net</option>
						<option value="custom"
							${member.emailDomain != 'gmail.com' && member.emailDomain != 'naver.com' && member.emailDomain != 'daum.net' ? 'selected' : ''}>직접입력</option>
					</select>
				</div>
			</div>

			<div class="form-group">
				<label>생년월일</label> <input type="date" name="birthdate" id="birth"
					value="${member.birthDate}">
			</div>

			<div class="form-group">
				<label>성별</label>
				<div class="radio-group">
					<label><input type="radio" name="gender" value="M"
						${member.gender == 'M' ? 'checked' : ''}> 남자</label> <label><input
						type="radio" name="gender" value="F"
						${member.gender == 'F' ? 'checked' : ''}> 여자</label>
				</div>
			</div>


			<div class="form-group checkbox-group">
				<label><input type="checkbox" name="agreeUpdate" required>
					정보 수정 동의 (필수)</label>
			</div>

			<div class="form-group button-row">
				<button type="submit" class="btn btn-half">수정하기</button>
				<button type="button" class="btn btn-half"
					onclick="location.href='/member/mypage'">취소</button>
			</div>
		</form>
	</div>
	<script src="${pageContext.request.contextPath}/js/member.js" defer></script>
</body>
</html>