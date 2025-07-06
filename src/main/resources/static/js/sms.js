let timerInterval;

// 인증코드 전송
$('#sendCodeBtn').click(function () {
    const phone = $('#phone').val().trim();

    if (!/^010\d{7,8}$/.test(phone)) {
        alert('올바른 휴대폰 번호를 입력하세요.');
        return;
    }

    $('#sendCodeBtn').prop('disabled', true);

    fetch('/sms/send/join', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ phone: phone })
    })
    .then(res => res.text())
    .then(data => {
        alert(data);
        $('#verificationSection').show();
        startTimer(180, document.getElementById('timerDisplay')); // ⬅️ 수정된 부분
    })
    .catch(() => {
        alert('인증번호 전송에 실패했습니다.');
    })
    .finally(() => {
        setTimeout(() => $('#sendCodeBtn').prop('disabled', false), 10000);
    });
});


// 인증번호 검증 버튼
const verifyBtn = document.getElementById("verifyCodeBtn");
if (verifyBtn) {
    verifyBtn.addEventListener("click", function () {
        const code = document.getElementById("verificationCode").value;
        const phoneNumber = document.getElementById("phone").value;
        const clientId = document.getElementById("id")?.value;
        const type = "MEMBER_JOIN";

        fetch("/sms/verify", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ phone: phoneNumber, code: code, type: type })
        })
        .then(res => res.json())
        .then(result => {
            const resultEl = document.getElementById("verifyResult");

            if (result.success) {
                resultEl.innerText = "✅ 인증 성공!";
                resultEl.style.color = "green";
                clearInterval(timerInterval);
                document.getElementById("timerDisplay").textContent = "✅ 인증 완료";
            } else {
                resultEl.innerText = "❌ 인증 실패. 다시 시도하세요.";
                resultEl.style.color = "red";
            }
        })
        .catch(err => {
            console.error(err);
            document.getElementById("verifyResult").innerText = "⚠️ 오류 발생. 다시 시도해주세요.";
        });
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
            startTimer(180, timerEl); 
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
            idTextEl.innerHTML = `📌 회원님의 아이디는 <strong>${result.clientId}</strong> 입니다.<br>
            <a href="/client/login" style="text-decoration:none; color:blue;">로그인 하러 가기</a>`;
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
$('#verifyCodeBtn').click(function () {
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
        success: function (response) {
            $('#verifyResult').text(response.message).css('color', 'green');
            clearInterval(timerInterval); // ⬅️ 타이머 종료
            $('#timerDisplay').text("✅ 인증 완료");
        },
        error: function (xhr) {
            const res = xhr.responseJSON;
            $('#verifyResult').text(res.message || '인증 실패').css('color', 'red');
        }
    });
});

// 타이머 시작 함수
function startTimer(duration, display) {
    clearInterval(timerInterval); // 기존 타이머 종료
    let timer = duration;

    timerInterval = setInterval(() => {
        let minutes = Math.floor(timer / 60);
        let seconds = timer % 60;

        display.textContent = `${minutes < 10 ? '0' + minutes : minutes}:${seconds < 10 ? '0' + seconds : seconds}`;

        if (--timer < 0) {
            clearInterval(timerInterval);
            display.textContent = "⏰ 만료됨";
        }
    }, 1000);
}
