<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>아이디/비밀번호 찾기</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/member.css">
</head>
<body>
	<div class="containerbox">
		<h2 class="title">아이디 / 비밀번호 찾기</h2>
		<div class="divider"></div>
		<div class="tab-menu">
			<button id="tabIdBtn" class="tab-button active">아이디 찾기</button>
			<button id="tabPwBtn" class="tab-button">비밀번호 찾기</button>
		</div>

		<div id="tabId" class="tab-content active">
			<div class="input-inline">
				<input type="email" id="findIdEmail" placeholder="example@gmail.com">
				<button class="btn-small" onclick="sendIdCode()">인증번호 전송</button>
			</div>
			<p id="idMessage" class="result-text"></p>

			<div class="input-inline">
				<input type="text" id="findIdCode" placeholder="인증번호 입력">
				<button class="btn-small" onclick="checkIdCode()">확인</button>
			</div>
			<p id="idResult" class="result-text"></p>
		</div>

		<div id="tabPw" class="tab-content">
			<div class="form-group">
				<label for="findPwId">회원 ID</label> <input type="text" id="findPwId"
					placeholder="아이디 입력">
			</div>

			<div class="form-group">
				<label for="findPwEmail">이메일 주소</label> <input type="email"
					id="findPwEmail" placeholder="example@gmail.com">
			</div>

			<div class="form-group">
				<button class="btn-slim" onclick="sendPwCode()">인증번호 전송</button>
			</div>
			<p id="pwMessage" class="result-text"></p>

			<div class="form-group" id="pwCodeGroup" style="display: none;">
				<label for="findPwCode">인증번호</label> <input type="text"
					id="findPwCode" placeholder="인증번호 입력">
			</div>

			<div class="form-group" id="pwVerifyBtn" style="display: none;">
				<button class="btn-slim" onclick="verifyCode()">확인</button>
			</div>
			<p id="pwResult" class="result-text"></p>
		</div>

		<div id="resetPwSection" class="tab-content"
			style="display: none; margin-top: 20px;">
			<div class="form-group">
				<label for="newPw">새 비밀번호</label> <input type="password" id="newPw"
					placeholder="새 비밀번호 입력">
			</div>
			<div class="form-group">
				<label for="confirmPw">새 비밀번호 확인</label> <input type="password"
					id="confirmPw" placeholder="비밀번호 확인">
			</div>

			<div class="form-group button-row">
				<button class="btn-slim" onclick="resetPassword()">비밀번호
					변경하기</button>
			</div>
			<p id="resetResult" class="result-text"></p>
		</div>
	</div>
	<!--  비번 탭 안넘어가서 .. js 같이 씀  -->
	<script>
let idAuthCode = null;
let idFound = null;
let pwAuthCode = null;

function showTab(tab) {
  const idTab = document.getElementById("tabId");
  const pwTab = document.getElementById("tabPw");
  const idBtn = document.getElementById("tabIdBtn");
  const pwBtn = document.getElementById("tabPwBtn");

  idTab.classList.remove("active");
  pwTab.classList.remove("active");
  idBtn.classList.remove("active");
  pwBtn.classList.remove("active");

  if (tab === "id") {
    idTab.classList.add("active");
    idBtn.classList.add("active");
    idTab.querySelector("input")?.focus();
  } else {
    pwTab.classList.add("active");
    pwBtn.classList.add("active");
    pwTab.querySelector("input")?.focus();
  }
 }

document.addEventListener("DOMContentLoaded", () => {
  document.getElementById("tabIdBtn")?.addEventListener("click", () => showTab("id"));
  document.getElementById("tabPwBtn")?.addEventListener("click", () => showTab("pw"));
});

 </script>

	<script src="${pageContext.request.contextPath}/js/member.js" defer></script>
</body>
</html>