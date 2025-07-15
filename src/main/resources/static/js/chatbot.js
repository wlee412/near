// 챗봇 모달 열기/닫기
function toggleChatModal() {
  const modal = document.querySelector(".chat-modal");
  modal.style.display = modal.style.display === "none" ? "flex" : "none";
}

// 메시지 전송처리
function sendMessage() {
  const inputField = document.querySelector('.chat-input-field');
  const message = inputField.value.trim();
  if (!message) return;

  appendMessage('user', message);
  inputField.value = '';
  inputField.disabled = true; // 입력 잠금

  fetch('/webhook', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ message: message })
  })
  .then(response => response.json())
  .then(data => {
    if (data.type === 'richContent' && Array.isArray(data.content)) {
      appendMessage('bot', data.content); // → richContent 배열 전달
    } else {
      const reply = data.reply || '죄송해요, 다시 말씀해 주세요.';
      appendMessage('bot', reply); // 일반 텍스트
    }
    inputField.disabled = false;
    inputField.focus();
  })

  .catch(err => {
    appendMessage('bot', '오류가 발생했어요. 다시 시도해 주세요.');
    inputField.disabled = false;
  });
}

function appendMessage(sender, textOrRich) {
  const chatLog = document.querySelector('.chat-log');
  const bubble = document.createElement('div');
  bubble.className = 'chat-bubble ' + sender;

  const message = document.createElement('div');
  message.className = 'message';

  // RichContent 처리
  if (typeof textOrRich === 'object' && Array.isArray(textOrRich)) {
    textOrRich.forEach(row => {
      row.forEach(item => {
        if (item.type === 'info') {
          const title = document.createElement('strong');
          title.textContent = item.title;
          message.appendChild(title);

          const subtitle = document.createElement('p');
          subtitle.textContent = item.subtitle;
          message.appendChild(subtitle);
        }
        if (item.type === 'button') {
          const button = document.createElement('a');
          button.textContent = item.text;
          button.href = item.link;
          button.target = '_blank';
          button.className = 'chat-button'; // CSS로 스타일링
          message.appendChild(button);
        }
      });
    });
  } else {
    // 일반 텍스트
    message.innerHTML = textOrRich.replace(/\n/g, '<br>');
  }

  bubble.appendChild(message);
  chatLog.appendChild(bubble);
  chatLog.scrollTop = chatLog.scrollHeight;
}
