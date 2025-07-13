<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
   	<!-- Dialogflow 챗봇 UI -->
<style>
  df-messenger {
    position: fixed !important;
    bottom: 80px !important;
    right: 20px !important;
    z-index: 9999 !important;

    transform: scale(0.8); /* 챗봇창 전체를 70%로 축소함 */
    transform-origin: bottom right; /* 우하단 기준으로 줄이기 */

    --df-messenger-button-titlebar-color: #7bb8cc;
    --df-messenger-chat-background-color: #ffffff;
    --df-messenger-font-color: #333333;
    --df-messenger-user-message: #f1f1f1;
    --df-messenger-bot-message: #e3f2fd;
    
  }
</style>

<df-messenger 
    agent-id="$9f1618e2-0167-4e27-bb92-db0c60be0f6d" 
    intent="WELCOME" 
    chat-title="n:ear챗봇상담" 
    language-code="ko"
    chat-icon="/images/chatbot.png">
</df-messenger>

<script src="https://www.gstatic.com/dialogflow-console/fast/messenger/bootstrap.js?v=1"></script>


   
</body>
</html>
