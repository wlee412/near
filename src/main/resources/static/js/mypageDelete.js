function validateDeleteForm() {
	const pw = document.getElementsByName("pw")[0].value;
	const pwConfirm = document.getElementsByName("pwConfirm")[0].value;

	const msg = document.getElementById("pwMatchResult");
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