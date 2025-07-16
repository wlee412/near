function toggleRole(checkbox) {
    const isCounselor = checkbox.checked;

    const snsTitle = document.querySelector('.sns-title');
    const snsButtons = document.querySelector('.sns-buttons');
    const register = document.querySelector('.register');
    const toggleLabel = document.getElementById('roleLabel');
    const loginForm = document.getElementById('loginForm');
    const idInput = document.getElementById("idInput");

    const findLink = document.getElementById("findLink"); 

    if (isCounselor) {
        snsTitle.style.display = 'none';
        snsButtons.style.display = 'none';
        register.style.display = 'none';
        toggleLabel.innerText = '상담자';
        loginForm.action = "/counselor/login";
        idInput.name = "counselorId";

        findLink.style.display = "none"; 
    } else {
        snsTitle.style.display = 'block';
        snsButtons.style.display = 'flex';
        register.style.display = 'block';
        toggleLabel.innerText = '내담자';
        loginForm.action = "/client/login";
        idInput.name = "clientId";

        findLink.style.display = "inline"; 
    }
}

window.addEventListener("DOMContentLoaded", () => {
	const role = "${role != null ? role : ''}";
    const checkbox = document.querySelector('.toggle-switch input');

    if (role === "counselor") {
        checkbox.checked = true;
        toggleRole(checkbox);  // 상태 반영
    }
});

