const txtBox = document.getElementById('chat-input');

function sendChatMessage() {
	if (txtBox.value.trim() == "")
		return false;
	sendTextMsg('chat', txtBox.value);
	txtBox.value = "";
}

function sendTextMsg(type, payload) {
	const msg = { type, roomId, sender: iAm, payload };
	stompClient.send(`/app/textmsg/${roomId}`, {}, JSON.stringify(msg));
}

window.textmsgSubscribe = function() {
	stompClient.subscribe(`/topic/textmsg/${roomId}`, frame => {
		try {
			const msg = JSON.parse(frame.body);
			const payload = msg.payload;

			switch (msg.type.trim()) {
				case "sys":
				case "chat":
					const time = new Date().toLocaleTimeString();
					const name = msg.sender || "익명";
					const text = payload;

					const chatLine = document.createElement("div");
					chatLine.className = "chat-line";

					const bubble = document.createElement("div");
					bubble.className = "chat-bubble";
					bubble.innerHTML = `<b class="nickname">[${name}]</b><div>${text}</div>`;

					const timestamp = document.createElement("div");
					timestamp.className = "chat-time";
					timestamp.textContent = `[${time}]`;

					switch (msg.sender) {
						case iAm:
							chatLine.classList.add("my-msg");
							break;
						case "시스템":
							chatLine.className = "sys-msg";
							break;
						default:
							chatLine.classList.add("other-msg");
					}

					chatLine.appendChild(bubble);
					chatLine.appendChild(timestamp);
					document.getElementById("chat-window").appendChild(chatLine);

					// 스크롤 아래로
					const chatWindow = document.getElementById("chat-window");
					chatWindow.scrollTop = chatWindow.scrollHeight;
					break;;
			}
		} catch (e) {
			console.error("텍스트 메시지 오류:", e);
		}

	});
}

// Enter 키로 전송
document.getElementById("chat-input").addEventListener("keypress", function(e) {
	if (e.key === "Enter" && !e.shiftKey) {
		e.preventDefault(); // 줄바꿈 방지
		sendChatMessage();
	}
});

window.expireSub = function() {
	stompClient.subscribe(`/topic/expired/${roomId}`, msg => {
		console.log("만료 메시지 수신:", msg.body);
		const data = JSON.parse(msg.body);
		if (data.type === "expired") {
			if (sfutest) {
				sfutest.send({
					message: { request: "leave" },
					success: () => console.log("Leave request sent."),
					error: err => console.error("Leave error", err)
				});
			}
			alert("상담 시간이 종료되었습니다. 퇴장합니다.");
			window.location.href = "/";
		}
	});
};

sfutest.onmessage = (msg, jsep) => {
	const event = msg["videoroom"];
	console.log("Janus message", msg);
	if (event === "event") {
		if (msg["leaving"]) {
			console.log("Participant leaving:", msg["leaving"]);
			janus.destroy();
		}
	}
};

