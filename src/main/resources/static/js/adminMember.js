document.addEventListener("DOMContentLoaded", function () {
    const forms = document.querySelectorAll("form[action$='/admin/updateState']");

    forms.forEach((form) => {
        form.addEventListener("submit", function (e) {
            e.preventDefault(); // 기본 전송 막고

            const confirmChange = confirm("정말 상태를 변경하시겠습니까?");
            if (!confirmChange) return;

            const formData = new FormData(form);

            fetch(form.action, {
                method: "POST",
                body: formData
            })
            .then(response => response.text())
            .then(result => {
                if (result.trim() === "success") {
                    alert("✅ 변경되었습니다.");
                    location.reload(); // 페이지 새로고침
                } else {
                    alert("❌ 변경 실패! 다시 시도해주세요.");
                }
            })
            .catch(error => {
                console.error("Error:", error);
                alert("⚠️ 오류 발생. 다시 시도해주세요.");
            });
        });
    });
});
