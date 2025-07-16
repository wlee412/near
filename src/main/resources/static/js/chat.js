// 챗봇 모달 열기/닫기
function toggleChatModal() {
  const modal = document.querySelector(".chat-modal");
  modal.style.display = modal.style.display === "none" ? "flex" : "none";
}

// 메시지 전송처리
function sendMessage() {
  const input = document.querySelector(".chat-input-field");
  const message = input.value.trim();
  if (!message) return;

  const log = document.querySelector(".chat-log");
  const userDiv = document.createElement("div");
  userDiv.className = "user-msg";
  userDiv.innerText = message;
  log.appendChild(userDiv);

  fetch("/webhook", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ message })
  })
  .then(res => res.json())
  .then(data => {
    const botDiv = document.createElement("div");
    botDiv.className = "bot-msg";
    botDiv.innerText = data.reply || data.fulfillmentText || "응답을 받지 못했습니다.";
    log.appendChild(botDiv);
    log.scrollTop = log.scrollHeight;
  });

  input.value = "";
}
