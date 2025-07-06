/*회원가입 아이디 유효성 + 중복 검사 */

// 1. 주소 찾기 (공통)
function execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function (data) {
      const roadAddr = data.roadAddress;
      let extraAddr = '';

      if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
        extraAddr += data.bname;
      }
      if (data.buildingName !== '' && data.apartment === 'Y') {
        extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
      }
      if (extraAddr !== '') {
        extraAddr = ' (' + extraAddr + ')';
      }

      document.getElementById('postcode').value = data.zonecode;
      document.getElementById('roadAddress').value = roadAddr + extraAddr;
      document.getElementById('detailAddress').focus();
    }
  }).open();
}
  
  // 2. 전체 유효성 검사
  async function validateForm() {
      // 각 유효성 검사의 결과를 저장할 변수들
      let isValid = true; // 최종 유효성 여부
      let firstInvalidElement = null; // 유효하지 않은 첫 번째 요소 (포커스용)

      // 1. 아이디 유효성 검사
      const isIdValid = await checkId();
      if (!isIdValid) {
          isValid = false;
          if (!firstInvalidElement) firstInvalidElement = document.getElementById("id");
      }

      // 2. 비밀번호 유효성 검사
      const isPwValid = checkPassword(); // checkPassword 함수는 이미 alert 없이 메시지만 표시
      if (!isPwValid) {
          isValid = false;
          if (!firstInvalidElement) firstInvalidElement = document.getElementById("pw");
      }

      // 3. 휴대폰 번호 유효성 검사
      const isPhoneValid = checkPhone(); // checkPhone 함수는 이미 alert 없이 메시지만 표시
      if (!isPhoneValid) {
          isValid = false;
          if (!firstInvalidElement) firstInvalidElement = document.getElementById("phone");
      }

      // 4. 이름 유효성 검사
      if (!validateName()) { // validateName 함수는 이제 alert 없이 메시지만 표시
          isValid = false;
          if (!firstInvalidElement) firstInvalidElement = document.getElementById("name");
      }

      // 5. 생년월일 유효성 검사
      const birth = document.getElementById("birth")?.value.trim();
      if (!validateBirth(birth)) { // validateBirth 함수는 alert를 포함
          isValid = false;
          if (!firstInvalidElement) firstInvalidElement = document.getElementById("birth");
      }

      // 6. 주소 유효성 검사
      const postcode = document.getElementById("postcode")?.value.trim();
      const roadAddress = document.getElementById("roadAddress")?.value.trim();
      const detailAddress = document.getElementById("detailAddress")?.value.trim();
      if (!validateAddress(postcode, roadAddress, detailAddress)) { // validateAddress 함수는 alert를 포함
          isValid = false;
          if (!firstInvalidElement) firstInvalidElement = document.getElementById("postcode");
      }

      // 7. 성별 유효성 검사
      const isGenderSelected = document.querySelector("input[name='gender']:checked") !== null;
      if (!validateGender(isGenderSelected)) { // validateGender 함수는 alert를 포함
          isValid = false;
      }

      // 8. 관심사 유효성 검사 (!!!! 이 부분이 핵심 !!!!)
      const interests = document.querySelectorAll("input[name='interestList']:checked");
      if (!validateInterests(interests)) { // validateInterests 함수는 alert를 포함
          isValid = false;
      }

      // 9. 비밀번호 확인 일치 여부
      const pw = document.getElementById("pw")?.value;
      const pwConfirm = document.getElementById("pwConfirm")?.value;
      if (!validatePasswordMatch(pw, pwConfirm)) { // validatePasswordMatch 함수는 alert를 포함
          isValid = false;
          if (!firstInvalidElement) firstInvalidElement = document.getElementById("pwConfirm");
      }
      
      // 10. 이메일 유효성 검사
      const emailId = document.getElementById("emailId")?.value.trim();
      const domainSelect = document.getElementById("emailDomainSelect")?.value;
      const customDomain = document.getElementById("customEmailDomain")?.value.trim();
      const emailDomain = domainSelect === "custom" ? customDomain : domainSelect;
      const fullEmail = `${emailId}@${emailDomain}`;
      const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

      // 이메일 입력 자체의 유효성 검사
      if (!emailId || !emailDomain) {
        alert("이메일을 정확히 입력해주세요.");
        isValid = false;
        if (!firstInvalidElement) firstInvalidElement = document.getElementById("emailId");
      } else if (!emailRegex.test(fullEmail)) {
        alert("유효한 이메일 형식이 아닙니다.");
        isValid = false;
        if (!firstInvalidElement) firstInvalidElement = document.getElementById("emailId");
      } else {
          const isEmailDuplicateValid = await (async () => {
              const emailResultBox = document.getElementById("emailCheckResult");
              try {
                  const res = await fetch("/client/checkEmailDuplicate", {
                      method: "POST",
                      headers: { "Content-Type": "application/json" },
                      body: JSON.stringify({ email: fullEmail })
                  });
                  const isDuplicate = await res.json();
                  if (isDuplicate) {
                      emailResultBox.innerText = "❌ 이미 사용 중인 이메일입니다.";
                      emailResultBox.style.color = "red";
                      return false;
                  } else {
                      emailResultBox.innerText = "✅ 사용 가능한 이메일입니다.";
                      emailResultBox.style.color = "green";
                      return true;
                  }
              } catch (error) {
                  console.error("이메일 중복 검사 오류:", error);
                  emailResultBox.innerText = "서버 오류가 발생했습니다.";
                  emailResultBox.style.color = "red";
                  return false;
              }
          })();

          if (!isEmailDuplicateValid) {
              alert("이메일 유효성 또는 중복 검사에 실패했습니다."); // 최종 alert
              isValid = false;
              if (!firstInvalidElement) firstInvalidElement = document.getElementById("emailId");
          }
      }


      // 모든 검사 후 유효하지 않은 경우 첫 번째 필드로 포커스 이동
      if (!isValid) {
          if (firstInvalidElement) {
              firstInvalidElement.focus();
          }
          return false;
      }

      return true; // 모든 검사 통과
  }
  
  function validateGender(isGenderValid) {
    if (!isGenderValid) {
      alert("성별을 선택해주세요.");
      return false;
    }
    return true;
  }

  function validateBirth(birth) {
    if (!birth || birth.trim() === "") {
      alert("생년월일을 입력해주세요.");
      document.getElementById("birth").focus(); // 입력 필드로 포커스 이동
      return false;
    }
    // 추가적인 날짜 형식 또는 나이 검사가 필요하면 여기에 로직 추가
    return true;
  }
  
  
  
  function validateInterests(interests) {
    if (interests.length === 0) {
      alert("관심사를 하나 이상 선택해주세요.");
      return false;
    }
    return true;
  }
  
  // 주소 유효성 검사
  function validateAddress(postcode, roadAddress, detailAddress) {
    if (!postcode || !roadAddress || !detailAddress || postcode.trim() === "" || roadAddress.trim() === "" || detailAddress.trim() === "") {
      alert("주소를 모두 입력해주세요.");
      document.getElementById("postcode").focus(); // 입력 필드로 포커스 이동
      return false;
    }
    return true;
  }

  // 3. 아이디 중복 + 유효성
  async function checkId() {
    const id = document.getElementById("id").value.trim();
    const resultBox = document.getElementById("idCheckResult");
    const regex = /^(?=.*[a-zA-Z])(?=.*\d)[a-zA-Z0-9]{4,}$/;

    if (!regex.test(id)) {
      resultBox.textContent = "아이디는 영문과 숫자를 포함해 4자 이상이어야 합니다.";
      resultBox.style.color = "gray";
      return false;
    }

    const res = await fetch("/client/check-id?id=" + encodeURIComponent(id));
    const data = await res.text();

    if (data === "duplicate") {
      resultBox.textContent = "❌ 이미 사용 중인 아이디입니다.";
      resultBox.style.color = "red";
      return false;
    } else {
      resultBox.textContent = "✅ 사용 가능한 아이디입니다.";
      resultBox.style.color = "green";
      return true;
    }
  }

  // 4. 비밀번호 유효성 + 확인
  function checkPasswordMatch() {
      const pwInput = document.getElementById("pw");
      const pwConfirmInput = document.getElementById("pwConfirm");
      const matchResult = document.getElementById("pwMatchResult");

      const pw = pwInput?.value; // 옵셔널 체이닝으로 null/undefined 방지
      const pwConfirm = pwConfirmInput?.value; // 옵셔널 체이닝으로 null/undefined 방지

      // 두 필드 모두 비어있을 때는 메시지를 표시하지 않습니다.
      if (!pw && !pwConfirm) {
          matchResult.textContent = ""; 
          return false; 
      }


      if (!pwConfirm) {
          matchResult.textContent = "";
          return false;
      }

      if (pw === pwConfirm) {
          matchResult.textContent = "✅ 비밀번호가 일치합니다.";
          matchResult.style.color = "green";
          return true; // 일치함
      } else {
          matchResult.textContent = "❌ 비밀번호가 일치하지 않습니다.";
          matchResult.style.color = "red";
          return false; // 불일치
      }
  }
  
  // 5. 핸드폰 검사
  function checkPhone() {
  	const phoneInput = document.getElementById("phone");
  	const resultBox = document.getElementById("phoneCheckResult");

  	// 숫자 외 문자 제거
  	phoneInput.value = phoneInput.value.replace(/[^0-9]/g, '');
  	const phone = phoneInput.value;

  	// 010으로 시작하지 않으면 경고
  	if (phone && !phone.startsWith("010")) {
  		resultBox.textContent = "❌ 010으로 시작하는 번호만 입력 가능합니다.";
  		resultBox.style.color = "red";
  		return false;
  	}

  	// 11자리 입력 전 안내 문구
  	if (phone.length < 11) {
  		resultBox.textContent = "휴대폰 번호는 11자리여야 합니다.";
  		resultBox.style.color = "gray";
  		return false;
  	}

  	// 최종 정규식 검사
  	const regex = /^010\d{8}$/;
  	if (!regex.test(phone)) {
  		resultBox.textContent = "❌ 올바른 휴대폰번호를 입력해주세요.";
  		resultBox.style.color = "red";
  		return false;
  	} else {
  		resultBox.textContent = "✅ 올바른 휴대폰 번호입니다.";
  		resultBox.style.color = "green";
  		return true;
  	}
  }
  

// 이름:한글 + 영문만 입력

function validateName() {
    const nameInput = document.getElementById("name").value.trim();
    const nameResult = document.getElementById("nameCheckResult");
    const nameRegex = /^[가-힣a-zA-Z]+$/;

    // 1. 입력값이 비어 있을 경우
    if (nameInput === "") {
        nameResult.innerText = ""; // 메시지 비우기
                                    // 폼 제출 시 validateForm()에서 최종 검사
        return false; // 빈 값도 유효하지 않다고 반환
    }

    // 2. 정규식 검사에 실패할 경우 (한글 또는 영문 외 문자 포함)
    if (!nameRegex.test(nameInput)) {
        nameResult.innerText = "❌ 이름은 한글 또는 영문만 입력 가능합니다.";
        nameResult.style.color = "red";
        return false;
    } else {
        // 3. 유효한 입력일 경우
        nameResult.innerText = ""; // 메시지 비우기
        return true;
    }
}

// 비밀번호 유효성 + 비번 확인

function checkPassword() {
	const pw = document.getElementById("pw").value;
	const resultBox = document.getElementById("pwCheckResult");

	const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()\-_=+\\|[\]{};:'",.<>/?`~])[A-Za-z\d!@#$%^&*()\-_=+\\|[\]{};:'",.<>/?`~]{8,}$/

	if (!regex.test(pw)) {
		resultBox.textContent = "비밀번호는 8자 이상, 영문과 숫자, 특수문자를 포함해야 합니다.";
		resultBox.style.color = "gray";
		return false;
	} else {
		resultBox.textContent = "✅ 사용 가능한 비밀번호입니다.";
		resultBox.style.color = "green";
		return true;
	}
}

function validatePasswordMatch(pw, pwConfirm) {
  if (!pwConfirm || pw !== pwConfirm) {
    alert("비밀번호가 일치하지 않습니다.");
    return false;
  }
  return true;
}


$(function () {
  $("#registerForm").on("submit", async function (e) {
    e.preventDefault();
    const isValid = await validateForm();
    if (!isValid) return;

    $("#registerBtn").prop("disabled", true);
    $("#registerForm").hide();
    $(".container").hide();
    $("#registerOverlay").show();
    this.submit();
  });
});


document.addEventListener("DOMContentLoaded", () => {
  const idInput = document.getElementById("id");
  idInput?.addEventListener("input", checkId);

  const pwInput = document.getElementById("pw");
  const pwConfirmInput = document.getElementById("pwConfirm");
  pwInput?.addEventListener("input", () => {
    checkPassword();
    checkPasswordMatch();
  });
  pwConfirmInput?.addEventListener("input", checkPasswordMatch);

  const phoneInput = document.getElementById("phone");
  phoneInput?.addEventListener("input", checkPhone);

  const birthInput = document.getElementById("birth");
  birthInput?.addEventListener("blur", () => {
    if (!birthInput.value) {
      alert("생년월일을 반드시 선택해야 합니다!");
      birthInput.focus();
    }
  });

  document.getElementById("tabIdBtn")?.addEventListener("click", () => showTab("id"));
  document.getElementById("tabPwBtn")?.addEventListener("click", () => showTab("pw"));

  document.getElementById("emailId")?.addEventListener("input", checkEmailDuplicateLive);
  document.getElementById("emailId")?.addEventListener("blur", function () {
    validateEmailId(this);
  });
  document.getElementById("emailDomainSelect")?.addEventListener("change", handleDomainChange);
  document.getElementById("customEmailDomain")?.addEventListener("input", checkEmailDuplicateLive);
  document.getElementById("customEmailDomain")?.addEventListener("blur", checkEmailDuplicate);

  const confirmBtn = document.querySelector("button[onclick='checkIdCode()']");
  confirmBtn?.addEventListener("click", checkIdCode);
});

// 이메일 도메인 선택 핸들링 (직접입력일 때 input창 보여주고 name 제어)
function handleDomainChange() {
	const select = document.getElementById("emailDomainSelect");
	const customInput = document.getElementById("customEmailDomain");

	if (select.value === "custom") {
		// 직접입력 선택 시
		select.removeAttribute("name"); // select는 서버 전송 제외
		customInput.setAttribute("name", "emailDomain"); // input은 서버 전송 대상
		customInput.style.display = "inline-block";
		customInput.disabled = false;
		customInput.value = "";
		customInput.focus();
	} else {
		// 일반 도메인 선택 시
		select.setAttribute("name", "emailDomain");
		customInput.removeAttribute("name");
		customInput.style.display = "none";
		customInput.disabled = true;
		customInput.value = "";
	}

	// 도메인 바뀌면 중복검사 다시 실행
	checkEmailDuplicate();
}

// 이메일 아이디 유효성 검사 (영문자+숫자만 허용)
function validateEmailId(input) {
	const value = input.value;
	const msg = document.getElementById("emailIdMessage");
	const regex = /^[a-zA-Z0-9._-]+$/;

	if (!regex.test(value)) {
		msg.textContent = "❌ 올바른 이메일아이디 형식이 아닙니다. (영문자+숫자만 허용)";
		msg.style.color = "red";
		input.value = value.replace(/[^a-zA-Z0-9]/g, "");
	} else {
		msg.textContent = "";
	}

	checkEmailDuplicate(); // 유효성 통과 후 중복검사 실행
}

// ✅ 이메일 중복검사 함수 (실제 fetch 요청 실행)
function checkEmailDuplicate() {
	const emailId = document.getElementById("emailId").value.trim();
	let domain = document.getElementById("emailDomainSelect").value;
	const customInput = document.getElementById("customEmailDomain");

	if (domain === "custom") {
		domain = customInput.value.trim();
	}

	const result = document.getElementById("emailCheckResult");

	// 기본 입력값 없으면 메시지 초기화
	if (!emailId || !domain) {
		result.innerText = "";
		result.style.color = "";
		return;
	}

	const fullEmail = `${emailId}@${domain}`;
	const emailRegex = /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;

	// 이메일 형식이 유효하지 않으면 메시지 숨김
	if (!emailRegex.test(fullEmail)) {
		result.innerText = "";
		result.style.color = "";
		return;
	}

	// ✅ 유효한 이메일일 경우 중복검사 fetch 요청
	fetch("/client/checkEmailDuplicate", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ email: fullEmail })
	})
		.then(res => res.json())
		.then(isDuplicate => {
			if (isDuplicate) {
				result.innerText = "❌ 이미 사용 중인 이메일입니다.";
				result.style.color = "red";
			} else {
				result.innerText = "✅ 사용 가능한 이메일입니다.";
				result.style.color = "green";
			}
		})
		.catch(() => {
			result.innerText = "서버 오류가 발생했습니다.";
			result.style.color = "red";
		});
}

// ✅ 입력 중 실시간 중복검사 함수 (입력 도중에도 문구 반영되게)
function checkEmailDuplicateLive() {
	// 실시간 검사용으로 checkEmailDuplicate() 그대로 사용
	checkEmailDuplicate();
}




//성별 
function getGender() {
	let gender = document.querySelector('input[name="gender"]:checked');
	if (gender) {
		return gender.value;
	} else {
		alert("성별을 선택해주세요!");
		return null;
	}
}

// 성별 버튼
const registerBtn = document.getElementById("registerBtn");
if (registerBtn) {
	registerBtn.addEventListener("click", function () {
		let gender = getGender();
	});
}


// ** 비밀번호 변경

// ✅ 현재 비밀번호 확인
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
	const newPw = document.getElementById("newPw").value;
	const newPwMsg = document.getElementById("newPwMsg");

	const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()\-_=+\\|[\]{};:'",.<>/?`~]).{8,}$/;

	if (!newPw) {
		newPwMsg.textContent = "";
		return;
	}

	if (!regex.test(newPw)) {
		newPwMsg.textContent = "비밀번호는 8자 이상, 영문+숫자+특수문자를 포함해야 합니다.";
		newPwMsg.style.color = "gray";
	} else {
		newPwMsg.textContent = "✅ 사용 가능한 비밀번호입니다.";
		newPwMsg.style.color = "green";
	}
}

// ✅ 비밀번호 일치 여부만 검사
function checkPwMatch() {
	const newPw = document.getElementById("newPw").value;
	const confirmPw = document.getElementById("confirmPw").value;
	const matchMsg = document.getElementById("pwMatchMsg");

	if (!confirmPw) {
		matchMsg.textContent = "";
		return;
	}

	if (newPw === confirmPw) {
		matchMsg.textContent = "✅ 비밀번호가 일치합니다.";
		matchMsg.style.color = "green";
	} else {
		matchMsg.textContent = "❌ 비밀번호가 일치하지 않습니다.";
		matchMsg.style.color = "red";
	}
}


// 회원탈퇴
function confirmDelete() {
	const pw = document.getElementsByName("pw")[0].value;
	const pwConfirm = document.getElementsByName("pwConfirm")[0].value;

	const msg = document.getElementById("pwMatchMsg");
	msg.innerText = ""; // 초기화

	// 빈칸 검사
	if (!pw || !pwConfirm) {
		msg.innerText = "비밀번호를 정확히 입력해주세요.";
		msg.style.color = "red";
		return false;
	}

	// 비밀번호 불일치
	if (pw !== pwConfirm) {
		msg.innerText = "비밀번호가 일치하지 않습니다.";
		msg.style.color = "red";
		return false;
	}

	// 정말 탈퇴 확인
	const confirmResult = confirm("정말 탈퇴하시겠습니까?");
	if (!confirmResult) {
		return false;
	}

	// 성공이면 통과
	return true;
}


// 아이디/ 비번찾기
function sendIdCode() {
	const email = document.getElementById("findIdEmail").value.trim();
	const message = document.getElementById("idMessage");

	if (!email) {
		message.innerText = "⚠ 이메일을 입력하세요.";
		message.style.color = "red";
		return;
	}

	message.innerText = "⏳ 인증번호를 전송 중입니다...";
	message.style.color = "gray";
	
	fetch("/verify/sendFindIdCode", {
		method: "POST",
		headers: { "Content-Type": "application/x-www-form-urlencoded" },
		body: new URLSearchParams({ email })
	})
		.then(res => res.json())
		.then(data => {


			if (data.success) {
				idAuthCode = data.code;
				idFound = data.clientId;
				document.getElementById("idMessage").innerText = "✅ 인증번호가 전송되었습니다.";
			} else {
				document.getElementById("idMessage").innerText = "❌ " + data.message;
			}
		})
		.catch(err => {
			console.error("전송 오류:", err);
			document.getElementById("idMessage").innerText = "❌ 서버 오류 발생";
		});
}


//아이디 찾기
function checkIdCode() {
	const code = document.getElementById("findIdCode").value.trim();
	const email = document.getElementById("findIdEmail").value.trim();
	const result = document.getElementById("idResult");

	fetch("/verify/verifyCodeForId", {
		method: "POST",
		headers: { "Content-Type": "application/x-www-form-urlencoded" },
		body: new URLSearchParams({ email, code })
	})
		.then(res => res.json())
		.then(data => {
			if (data.success) {
				result.innerHTML = `✅ 회원님의 아이디는 <strong>${data.clientId}</strong> 입니다.<br><br>
               <a href="/client/login" style="color: black; text-decoration: none;">로그인 페이지로 이동하기</a>`;
				result.style.color = "green";
			} else {
				result.innerText = "❌ 인증번호가 일치하지 않습니다.";
				result.style.color = "red";
			}
		})
		.catch(() => {
			result.innerText = "❌ 서버 오류가 발생했습니다.";
			result.style.color = "red";
		});
}
// 재원 - 아이디 찾기 코드 유효성 검사 (끝).

// 비번찾기
function sendPwCode() {
	const id = document.getElementById("findPwId").value.trim();
	const email = document.getElementById("findPwEmail").value.trim();
	const message = document.getElementById("pwMessage");


	if (!id || !email) {
		message.innerText = "⚠ 아이디와 이메일을 모두 입력하세요.";
		message.style.color = "red";
		return;
	}


	message.innerText = "⏳ 인증번호를 전송 중입니다...";
	message.style.color = "gray";


	fetch("/verify/sendFindPwCode", {
		method: "POST",
		headers: { "Content-Type": "application/x-www-form-urlencoded" },
		body: new URLSearchParams({ email })
	})
		.then(res => res.json())
		.then(data => {
			if (data.success) {
				message.innerText = "✅ 인증번호가 이메일로 전송되었습니다.";
				message.style.color = "green";
				pwAuthCode = data.code;

				document.getElementById("pwCodeGroup").style.display = "block";
				document.getElementById("pwVerifyBtn").style.display = "block";

			} else {
				message.innerText = "❌ " + data.message;
				message.style.color = "red";
			}
		})
		.catch(err => {
			console.error("비밀번호 인증번호 전송 오류:", err);
			message.innerText = "❌ 서버 오류가 발생했습니다.";
			message.style.color = "red";
		});
}

//재원 - 인증코드 유효성 검사 (시작) 
function verifyCode() {
	const email = document.getElementById("findPwEmail").value.trim();
	const code = document.getElementById("findPwCode").value.trim();
	const result = document.getElementById("pwResult");

	fetch("/verify/verifyCodeForPw", { //DB ACCOUNT_VERIFICATION - VERIFED, USED_AT 업데이트 위해 수정
		method: "POST",
		headers: { "Content-Type": "application/x-www-form-urlencoded" },
		body: new URLSearchParams({ email, code })
	})
		.then(res => res.json())
		.then(data => {
			if (data.success) {
				document.getElementById("clientIdHidden").value = data.clientId; 
				result.innerText = "✅ 인증 성공! 비밀번호를 재설정하세요.";
				result.style.color = "green";
				document.getElementById("resetPwSection").style.display = "block";

				currentUserId = document.getElementById("findPwId").value.trim();
			} else {
				result.innerText = "❌ 인증번호가 일치하지 않습니다.";
				result.style.color = "red";
			}
		})
		.catch(() => {
			result.innerText = "❌ 서버 오류가 발생했습니다.";
			result.style.color = "red";
		});
}
//재원 - 인증코드 유효성 검사 (끝) 

function resetPassword() {
	const userId = document.getElementById("findPwId").value.trim();
	const newPw = document.getElementById("newPw").value.trim();
	const confirmPw = document.getElementById("confirmPw").value.trim();
	const resetResult = document.getElementById("resetResult");
	const clientId = document.getElementById("clientIdHidden").value.trim(); 



	const regex = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[!@#$%^&*()\-_=+\\|[\]{};:'",.<>/?`~]).{8,}$/;

	if (!newPw || !confirmPw) {
		resetResult.innerText = "비밀번호를 모두 입력해주세요.";
		resetResult.style.color = "red";
		return;
	}

	if (!regex.test(newPw)) {
		resetResult.innerText = "비밀번호는 8자 이상, 영문+숫자+특수문자를 포함해야 합니다.";
		resetResult.style.color = "gray";
		return;
	}

	if (newPw !== confirmPw) {
		resetResult.innerText = "비밀번호가 일치하지 않습니다.";
		resetResult.style.color = "red";
		return;
	}

	fetch("/client/resetPassword", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ clientId: clientId, newPassword: newPw })
	})
		.then(res => res.json())
		.then(data => {
			if (data.success) {
				resetResult.innerText = "✅ 비밀번호가 변경되었습니다. 로그인 페이지로 이동합니다.";
				resetResult.style.color = "green";
				setTimeout(() => location.href = "/client/login", 2500);
			} else {
				resetResult.innerText = "❌ 비밀번호 변경에 실패했습니다.";
				resetResult.style.color = "red";
			}
		})
		.catch(err => {
			console.error("비밀번호 변경 오류:", err);
			resetResult.innerText = "❌ 서버 오류가 발생했습니다.";
			resetResult.style.color = "red";
		});
}




////회원정보수정 적용 안 돼서 추가함..More actions
//
//function validateUpdateForm() {
//	const name = document.getElementById("name")?.value.trim();
//	const nickname = document.getElementById("nickname")?.value.trim();
//	const phone = document.getElementById("phone")?.value.trim();
//	const emailId = document.getElementById("emailId")?.value.trim();
//	const emailDomain = document.getElementById("emailDomainSelect")?.value === "custom"
//		? document.getElementById("customEmailDomain")?.value.trim()
//		: document.getElementById("emailDomainSelect")?.value;
//	const birth = document.getElementById("birth")?.value.trim();
//	const agree = document.querySelector("input[name='agreeUpdate']");
//
//	if (!name || !nickname || !phone || !emailId || !emailDomain || !birth) {
//		alert("모든 항목을 빠짐없이 입력해주세요.");
//		return false;
//	}
//
//	// ✅ 이름 검사 (한글 또는 영문만)
//	const nameRegex = /^[가-힣a-zA-Z]+$/;
//	if (!nameRegex.test(name)) {
//		alert("이름은 한글 또는 영문만 입력 가능합니다.");
//		return false;
//	}
//
//	// ✅ 닉네임 검사 (2~10자)
//	if (nickname.length < 2 || nickname.length > 10) {
//		alert("닉네임은 2자 이상 10자 이하로 입력해주세요.");
//		return false;
//	}
//
//	// ✅ 필수 항목 누락 확인
//	if (!phone || !emailId || !emailDomain || !birth) {
//		alert("모든 항목을 빠짐없이 입력해주세요.");
//		return false;
//	}
//
//	// ✅ 전화번호 형식 확인
//	if (!/^010\d{8}$/.test(phone)) {
//		alert("휴대폰 번호는 010으로 시작하는 11자리 숫자여야 합니다.");
//		return false;
//	}
//
//	// ✅ 동의 여부
//	if (!agree?.checked) {
//		alert("정보 수정 동의는 필수입니다.");
//		return false;
//	}
//
//	return true; // 모든 조건 통과 시 제출 허용
//}