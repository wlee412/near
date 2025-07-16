function checkCurrentPassword() {
	const currentPw = document.getElementById("currentPassword").value;
	const msg = document.getElementById("currentPwResult");

	// 입력 없을 경우 메시지 제거
	if (!currentPw) {
		msg.textContent = "";
		document.getElementById("newPw").disabled = true;
		document.getElementById("confirmPw").disabled = true;
		return;
	}

	fetch("/client/checkPassword", {
		method: "POST",
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify({ currentPassword: currentPw })
	})
		.then(response => response.json())
		.then(data => {
			if (data.match) {
				msg.textContent = "✅ 현재 비밀번호와 일치합니다.";
				msg.style.color = "green";

				document.getElementById("newPw").disabled = false;
				document.getElementById("confirmPw").disabled = false;
			} else {
				msg.textContent = "❌ 비밀번호가 일치하지 않습니다.";
				msg.style.color = "red";

				document.getElementById("newPw").disabled = true;
				document.getElementById("confirmPw").disabled = true;
			}
		});
	// 현재비번 노 일치시 새비밀번호 입력 막아져있음..이거 푸는 법!
	if (data.match) {
		msg.textContent = "✅ 현재 비밀번호와 일치합니다.";
		msg.style.color = "green";

		// ✅ 입력 허용
		document.getElementById("newPw").disabled = false;
		document.getElementById("confirmPw").disabled = false;
	} else {
		msg.textContent = "❌ 비밀번호가 일치하지 않습니다.";
		msg.style.color = "red";

		// ✅ 다시 입력 막기
		document.getElementById("newPw").disabled = true;
		document.getElementById("confirmPw").disabled = true;

		// ✅ 기존 값도 초기화하면 깔끔
		document.getElementById("newPw").value = "";
		document.getElementById("confirmPw").value = "";
		document.getElementById("newPwMsg").textContent = "";
		document.getElementById("pwMatchMsg").textContent = "";
	}

}

// ✅ 새 비밀번호 유효성만 검사
function checkNewPwValid() {
    const currentPwInput = document.getElementById("currentPassword"); // 현재 비밀번호 input 가져오기
    const newPwInput = document.getElementById("newPw");
    const newPwMsg = document.getElementById("newPwMsg");

    const currentPw = currentPwInput?.value; // 현재 비밀번호 값
    const newPw = newPwInput?.value; // 새 비밀번호 값

    const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()\-_=+\\|[\]{};:'",.<>/?`~]).{8,}$/;

    if (!newPw) {
        newPwMsg.textContent = "";
        return;
    }

    // 1. 새 비밀번호가 현재 비밀번호와 일치하는지 확인
    if (newPw === currentPw) {
        newPwMsg.textContent = "❌ 현재 비밀번호와 동일합니다. 다른 비밀번호를 사용해주세요.";
        newPwMsg.style.color = "red";
        return; // 일치하면 다른 유효성 검사 중단
    }

    // 2. 새 비밀번호가 유효성 정규식에 맞는지 확인
    if (!regex.test(newPw)) {
        newPwMsg.textContent = "비밀번호는 8자 이상, 영문+숫자+특수문자를 포함해야 합니다.";
        newPwMsg.style.color = "gray";
    } else {
        newPwMsg.textContent = "✅ 사용 가능한 비밀번호입니다.";
        newPwMsg.style.color = "green";
    }
}
