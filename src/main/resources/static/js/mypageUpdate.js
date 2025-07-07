function handleDomainChange() {
  const select = document.getElementById("emailDomainSelect");
  const input = document.getElementById("customEmailDomain");
  const selected = select.value;

  if (selected === "custom") {
    input.style.display = "inline-block";
    input.removeAttribute("disabled");
    input.setAttribute("name", "emailDomain");

    select.removeAttribute("name");
    select.disabled = true;
  } else {
    input.style.display = "none";
    input.setAttribute("disabled", true);
    input.removeAttribute("name");

    select.setAttribute("name", "emailDomain");
    select.disabled = false;
  }
}

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

  function validateUpdateForm() {
    const name = document.getElementById("name")?.value.trim();
    const phone = document.getElementById("phone")?.value.trim();
    const emailId = document.getElementById("emailId")?.value.trim();
    const emailDomainSelect = document.getElementById("emailDomainSelect");
    const customEmailInput = document.getElementById("customEmailDomain");
    const birth = document.getElementById("birth")?.value.trim();
    const agree = document.querySelector("input[name='agreeUpdate']");
    const postcode = document.getElementById("postcode")?.value.trim();
    const roadAddress = document.getElementById("roadAddress")?.value.trim();
    const detailAddress = document.getElementById("detailAddress")?.value.trim();
    const gender = document.querySelector('input[name="gender"]:checked');
    const interests = document.querySelectorAll('input[name="interestList"]:checked');

    const emailDomain = emailDomainSelect.value === "custom"
      ? customEmailInput?.value.trim()
      : emailDomainSelect?.value;

    // ✅ 이름 유효성
    const nameRegex = /^[가-힣a-zA-Z]+$/;
    if (!name || !nameRegex.test(name)) {
      alert("이름은 한글 또는 영문만 입력 가능합니다.");
      return false;
    }

    // ✅ 휴대폰 번호 유효성
    const phoneRegex = /^010\d{8}$/;
    if (!phone || !phoneRegex.test(phone)) {
      alert("휴대폰 번호는 010으로 시작하는 11자리 숫자여야 합니다.");
      return false;
    }

    // ✅ 이메일 ID 유효성
    const emailIdRegex = /^[a-zA-Z0-9._-]+$/;
    if (!emailId || !emailIdRegex.test(emailId)) {
      alert("이메일 아이디는 영문자, 숫자, 일부 특수문자(._-)만 사용할 수 있습니다.");
      return false;
    }

    // ✅ 이메일 도메인 유효성
    const emailRegex = /^[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/;
    if (!emailDomain || !emailRegex.test(emailDomain)) {
      alert("올바른 이메일 도메인을 입력해주세요.");
      return false;
    }

    // ✅ 주소 유효성
    if (!postcode || !roadAddress || !detailAddress) {
      alert("주소를 모두 입력해주세요. (주소 찾기를 통해 입력 가능)");
      return false;
    }

    // ✅ 생년월일 검사
    if (!birth || birth === "1900-01-01") {
      alert("생년월일을 정확히 입력해주세요.");
      return false;
    }

    // ✅ 성별 선택 검사
    if (!gender) {
      alert("성별을 선택해주세요.");
      return false;
    }

    // ✅ 관심사 최소 1개 이상 선택
    if (interests.length === 0) {
      alert("관심사를 하나 이상 선택해주세요.");
      return false;
    }

    // ✅ 정보수정 동의
    if (!agree?.checked) {
      alert("정보 수정에 동의하셔야 합니다.");
      return false;
    }

    return true; // 모든 조건 통과 시 제출 허용
  }
