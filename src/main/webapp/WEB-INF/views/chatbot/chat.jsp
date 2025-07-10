<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>n:ear 챗봇</title>
    <script>
        function sendMessage() {
            const msg = document.getElementById("msg").value;
            const clientId = "guest123"; // 로그인시 아이디, 비회원이면 임시 ID

            fetch("/chatbot/save", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    clientId: clientId,
                    sender: "user",
                    message: msg
                })
            }).then(() => {
                loadChat();
                document.getElementById("msg").value = "";
            });
        }

        function loadChat() {
            const clientId = "guest123";
            fetch("/chatbot/history/" + clientId)
                .then(response => response.json())
                .then(data => {
                    const area = document.getElementById("chatArea");
                    area.innerHTML = "";
                    data.forEach(chat => {
                        area.innerHTML += `<div><b>${chat.sender}</b>: ${chat.message}</div>`;
                    });
                });
        }

        window.onload = loadChat;
    </script>
</head>
<body>
    <h2>n:ear 챗봇</h2>
    <div id="chatArea" style="border:1px solid #ccc; padding:10px; height:300px; overflow-y:scroll;"></div>
    <input type="text" id="msg" placeholder="메시지 입력" />
    <button onclick="sendMessage()">전송</button>
</body>
</html>
