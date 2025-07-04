<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원가입</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/client.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common.css">
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
  function execDaumPostcode() {
    new daum.Postcode({
      oncomplete: function(data) {
        // 주소 변수
        var roadAddr = data.roadAddress; // 도로명 주소
        var extraAddr = ''; // 참고 항목

        // 참고 항목 조합
        if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
          extraAddr += data.bname;
        }
        if (data.buildingName !== '' && data.apartment === 'Y') {
          extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
        }
        if (extraAddr !== '') {
          extraAddr = ' (' + extraAddr + ')';
        }

        // 입력 필드에 값 설정
        document.getElementById('postcode').value = data.zonecode;
        document.getElementById("roadAddress").value = roadAddr + extraAddr;
        document.getElementById("detailAddress").focus(); // 상세주소 입력에 포커스
      }
    }).open();
  }
</script>
</head>
<body>
	<%@ include file="../includes/header.jsp"%>
	<div class="content-wrapper">

		<!-- 가입 중 스피너 -->
		<div id="registerOverlay"
			style="display: none; text-align: center; padding: 20px;">
			<img src="" class="loading-spinner" alt="로딩 중">
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
			<form id="registerForm"
				action="${pageContext.request.contextPath}/client/register"
				method="post">
				<input type="hidden" name="verify_type" value="MEMBER_JOIN">

				<!-- ID -->
				<div class="form-group">
					<label for="id">ID</label> <input type="text" name="clientId"
						id="id" placeholder="영문+숫자 4자 이상 입력" required>
					<div id="idCheckResult" class="result-text"
						style="font-size: 14px; margin-top: 4px;"></div>
				</div>

				<!-- 이름 -->
				<div class="form-group">
					<label for="name">이름</label> <input type="text" name="name"
						id="name" oninput="validateName()" placeholder="이름을 입력하세요"
						required>
					<div id="nameCheckResult" class="result-text"
						style="font-size: 14px; margin-top: 4px;"></div>
				</div>
				<!-- 닉네임 -->
				<!-- 			<div class="form-group"> -->
				<!-- 				<label for="nickname">닉네임</label> <input type="text" name="nickname" -->
				<!-- 					id="nickname" placeholder="한글 2자 이상 입력" required> -->
				<!-- 				<div id="nicknameCheckResult" class="result-text" -->
				<!-- 					style="font-size: 14px; margin-top: 4px;"></div> -->
				<!-- 			</div> -->

				<!-- 비밀번호 -->
				<div class="form-group">
					<label for="pw">비밀번호</label> <input type="password" name="password"
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

				<!-- 주소 -->
					<label>주소</label>
					<div class="input-row">
						<input type="text" name="zipcode" id="postcode" placeholder="우편번호" readonly style="width: 150px;">
						<button type="button" onclick="execDaumPostcode()" class="btn">주소 찾기</button>
					</div>
					<input type="text" id="roadAddress" placeholder="도로명 주소" name="addrBase" readonly required style="margin-top: 8px;"> 
					<input type="text" id="detailAddress" placeholder="상세 주소 입력" name="addrDetail" required style="margin-top: 8px;">

				<!-- 생년월일 -->
				<div class="form-group">
					<label for="birth">생년월일</label> <input type="date" name="birth" id="birth" required>
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


				<!-- 관심사 -->
				<div class="form-group">
					<label>관심사 (복수 선택 가능)</label>
					<div class="checkbox-group">
						<label><input type="checkbox" name="interestList"
							value="우울감">우울감</label> <label><input type="checkbox"
							name="interestList" value="불안">불안</label> <label><input
							type="checkbox" name="interestList" value="자존감">자존감</label> <label><input
							type="checkbox" name="interestList" value="수면 문제">수면 문제</label> <label><input
							type="checkbox" name="interestList" value="스트레스">스트레스</label> <label><input
							type="checkbox" name="interestList" value="공황/불안발작">공황/불안발작</label>
						<label><input type="checkbox" name="interestList"
							value="대인관계">대인관계</label> <label><input type="checkbox"
							name="interestList" value="섭식 문제">섭식 문제</label> <label><input
							type="checkbox" name="interestList" value="ADHD">ADHD</label> <label><input
							type="checkbox" name="interestList" value="자기통제">자기통제</label>
					</div>
				</div>

				<!-- 이메일 약관 동의 -->

				<!-- 			<div class="form-group" style="align-items: center;"> -->
				<!-- 				<label><input type="checkbox" name="emailAd" value="Y"> -->
				<!-- 					이메일 광고 수신 동의</label> -->
				<!-- 			</div> -->


				<!-- 가입 버튼 -->
				<button type="submit" class="btn btn-full" id="registerBtn">가입하기</button>
			</form>
		</div>
	</div>
	<%@ include file="../includes/footer.jsp"%>

	<script src="https://code.jquery.com/jquery-latest.js" defer></script>
	<script src="${pageContext.request.contextPath}/js/client.js" defer></script>
</body>
</html>

