let timerInterval;

// 인증코드 전송
$('#sendCodeBtn').click(async function() {
	const phone = $('#phone').val().trim();

	if (!/^010\d{7,8}$/.test(phone)) {
		alert('올바른 휴대폰 번호를 입력하세요.');
		return;
	}

	try {
		// ✅ 1. 중복 체크 요청
		const checkRes = await fetch(`/client/check-phone?phone=${encodeURIComponent(phone)}`);
		const checkText = await checkRes.text();

		if (checkText === 'duplicate') {
			$('#phoneCheckResult').text('❌ 이미 사용 중인 휴대폰 번호입니다.').css('color', 'red');
			alert('이미 사용 중인 휴대폰 번호입니다.');
			return; // 중복이면 중단
		}

		// ✅ 2. 인증번호 전송 요청
		const sendRes = await fetch('/sms/send/join', {
			method: 'POST',
			headers: { 'Content-Type': 'application/json' },
			body: JSON.stringify({ phone: phone })
		});

		const data = await sendRes.text();
		alert(data);
		$('#verificationCode').prop('disabled', false).val('');
		$('#verifyCodeBtn').prop('disabled', false);
		$('#verifyResult').text('').css('color', '');
		$('#verificationSection').show();
		
		startTimer(15, document.getElementById('timerDisplay'));

	} catch (err) {
		console.error(err);
		alert('인증번호 전송에 실패했습니다.');
	} finally {
		setTimeout(() => $('#sendCodeBtn').prop('disabled', false), 10000);
	}
});



// 인증번호 검증 버튼
const verifyBtn = document.getElementById("verifyCodeBtn");
if (verifyBtn) {
	verifyBtn.addEventListener("click", async function(e) {
		e.preventDefault();
		const code = document.getElementById("verificationCode").value.trim();
		const phone = document.getElementById("phone").value.trim();
		const resultEl = document.getElementById("verifyResult");

		if (!code) {
			alert("인증번호를 입력해주세요.");
			return;
		}

		try {
			const res = await fetch("/sms/verify", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({ phone, code, type: "MEMBER_JOIN" })
			});
			const result = await res.json();

			// 🔽 인증번호 만료 처리
			if (res.status === 410) {
				alert("⏰ 인증번호가 만료되었습니다. 다시 요청해주세요.");
				resultEl.innerText = "⏰ 만료된 인증번호입니다.";
				resultEl.style.color = "orange";
				return;
			}


			if (!result.success) {
				alert("❌ 인증 실패: 인증번호가 일치하지 않습니다.");
				document.getElementById("verificationCode").focus();
				return;
			}

			// 인증 성공 시
			alert("✅ 인증에 성공했습니다!");
			sessionStorage.setItem("phoneVerified", "true");
			clearInterval(timerInterval);
			document.getElementById("timerDisplay").textContent = "✅ 인증 완료";
			document.getElementById("verifyResult").innerText = "";
			document.getElementById("phoneVerifiedField").value = "Y";
			document.getElementById("registerBtn").disabled = false;

		} catch (err) {
			console.error(err);
			alert("⚠️ 서버 오류로 인증에 실패했습니다. 잠시 후 다시 시도해주세요.");
		}
	});
}

// 아이디 찾기용 인증번호 전송
function sendIdCode() {
	const phone = document.getElementById("findIdPhone").value;
	const msgEl = document.getElementById("idMessage");
	const timerEl = document.getElementById("idTimerDisplay");

	if (!/^010\d{7,8}$/.test(phone)) {
		msgEl.innerText = "📱 올바른 휴대폰 번호를 입력하세요.";
		timerEl.textContent = "";
		return;
	}

	fetch("/sms/send/find-id", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ phone })
	})
		.then(res => res.text())
		.then(data => {
			if (data === "success") {
				msgEl.innerText = "✅ 인증번호가 전송되었습니다.";
				startTimer(15, timerEl);
			} else {
				msgEl.innerText = "❌ 전송 실패. 다시 시도해주세요.";
				timerEl.textContent = "";
			}
		})
		.catch((err) => {
			console.error(err);
			msgEl.innerText = "⚠️ 서버 오류가 발생했습니다.";
			timerEl.textContent = "";
		});
}

// 아이디 찾기 인증번호 확인
function checkIdCode() {
	const phone = document.getElementById("findIdPhone").value;
	const code = document.getElementById("findIdCode").value;

	fetch("/sms/verify", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ phone, code, type: "FIND_ID" })
	})
		.then(res => res.json())
		.then(result => {
			const resultEl = document.getElementById("idResult");
			const idTextEl = document.getElementById("foundIdText");
			const idTimerDisplay = document.getElementById("idTimerDisplay");
			if (result.success) {
				clearInterval(timerInterval);
				idTimerDisplay.style.display = "none";
				resultEl.innerText = "✅ 인증 성공!";
				resultEl.style.color = "green";

				idTextEl.style.display = "block";
				idTextEl.innerHTML = `📌회원님의 아이디는 <strong>${result.clientId}</strong> 입니다.<br><br>
				<a href="/client/login" class="btn-confirm-small">로그인 하러 가기</a>`;
			} else {
				resultEl.innerText = "❌ 인증 실패: " + result.message;
				resultEl.style.color = "red";
				idTextEl.style.display = "none";
			}
		})
		.catch(() => {
			const resultEl = document.getElementById("idResult");
			resultEl.innerText = "⚠️ 서버 오류가 발생했습니다.";
			resultEl.style.color = "red";
		});
}

function sendPwCode() {
	const clientId = document.getElementById("findPwId").value;
	const phone = document.getElementById("findPwPhone").value;
	const msgEl = document.getElementById("pwMessage");
	const timerEl = document.getElementById("pwTimerDisplay");


	if (!clientId || !phone) {
		msgEl.innerText = "⚠️ 아이디와 휴대폰 번호를 모두 입력해주세요.";
		msgEl.style.color = "red";
		return;
	}

	if (!/^010\d{7,8}$/.test(phone)) {
		msgEl.innerText = "📱 올바른 휴대폰 번호를 입력하세요.";
		msgEl.style.color = "red";
		return;
	}

	fetch("/sms/send/find-pw", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ clientId, phone })
	})
		.then(res => res.text())
		.then(data => {
			if (data === "success") {
				msgEl.innerText = "✅ 인증번호가 전송되었습니다.";
				msgEl.style.color = "green";
				startTimer(180, timerEl);

				document.getElementById("pwCodeGroup").style.display = "block";
				document.getElementById("pwVerifyBtn").style.display = "block";
			} else {
				msgEl.innerText = "❌ 전송 실패. 다시 시도해주세요.";
				msgEl.style.color = "red";
			}
		})
		.catch(() => {
			msgEl.innerText = "⚠️ 서버 오류가 발생했습니다.";
			msgEl.style.color = "red";
		});
}

function checkPwCode() {
	const phone = document.getElementById("findPwPhone")?.value;
	const code = document.getElementById("findPwCode")?.value;
	const resultEl = document.getElementById("pwResult");
	const timerEl = document.getElementById("pwTimerDisplay");

	if (!phone || !code) {
		resultEl.innerText = "⚠️ 모든 값을 입력해주세요.";
		resultEl.style.color = "orange";
		return;
	}

	fetch("/sms/verify", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({
			phone: phone,
			code: code,
			type: "FIND_PASSWORD"
		})
	})
		.then(res => {
			if (!res.ok) {
				throw new Error("서버 응답 실패");
			}
			return res.json();
		})
		.then(result => {
			if (result.success) {
				clearInterval(timerInterval);
				resultEl.innerText = "✅ 인증 성공!";
				resultEl.style.color = "green";

				document.getElementById("clientIdHidden").value = document.getElementById("findPwId").value;
				document.getElementById("resetPwSection").style.display = "block";
			} else {
				resultEl.innerText = "❌ 인증 실패: " + result.message;
				resultEl.style.color = "red";
			}
		})
		.catch(() => {
			resultEl.innerText = "⚠️ 서버 오류가 발생했습니다.";
			resultEl.style.color = "red";
		});
}


// jQuery 방식 예외 처리용 (예: 다른 인증 방식용)
$('#verifyCodeBtn').click(function() {
	const phone = $('#phoneInput').val();
	const code = $('#verificationCode').val();

	$.ajax({
		type: 'POST',
		url: '/sms/verify',
		contentType: 'application/json',
		data: JSON.stringify({
			phone: phone,
			code: code,
			type: currentType // 예: 'FIND_PASSWORD'
		}),
		success: function(response) {
			$('#verifyResult').text(response.message).css('color', 'green');
			clearInterval(timerInterval); // ⬅️ 타이머 종료
			$('#timerDisplay').text("✅ 인증 완료");
		},
		error: function(xhr) {
			const res = xhr.responseJSON;
			$('#verifyResult').text(res.message || '인증 실패').css('color', 'red');
		}
	});
});

// 타이머 시작 함수
function startTimer(duration, display) {
	clearInterval(timerInterval);
	let timer = duration;

	timerInterval = setInterval(() => {
		let minutes = Math.floor(timer / 60);
		let seconds = timer % 60;

		display.textContent = `${minutes < 10 ? '0' + minutes : minutes}:${seconds < 10 ? '0' + seconds : seconds}`;

		if (--timer < 0) {
			clearInterval(timerInterval);
			display.textContent = "⏰ 만료됨";

			// ✅ 인증번호 입력 필드 및 버튼 비활성화
			const codeInput = document.getElementById("verificationCode");
			const verifyBtn = document.getElementById("verifyCodeBtn");

			if (codeInput) codeInput.disabled = false;
			if (verifyBtn) verifyBtn.disabled = false;

			// ✅ 사용자 안내
			const resultEl = document.getElementById("verifyResult");
			if (resultEl) {
				resultEl.innerText = "⏰ 인증 시간이 만료되었습니다. 다시 요청해주세요.";
				resultEl.style.color = "orange";
			}

			// ✅ 인증 상태 초기화
			const hiddenField = document.getElementById("phoneVerifiedField");
			if (hiddenField) hiddenField.value = "N";

			// ✅ 버튼 다시 보이도록 (선택사항)
			$('#sendCodeBtn').prop('disabled', false);
		}
	}, 1000);
}
