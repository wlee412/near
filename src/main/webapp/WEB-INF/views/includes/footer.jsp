<%@ page contentType="text/html; charset=UTF-8" %>
<!-- 공통 푸터 -->
<footer>
  <div class="footer-container">
    <p>© 2025 n:ear. All rights reserved.</p>
    <p>
      <a href="/terms">이용약관</a> |
      <a href="/privacy">개인정보처리방침</a>
    </p>
  </div>
</footer>

	<!-- Dialogflow 챗봇 UI -->
<style>
  df-messenger {
    position: fixed !important;
    bottom: 80px !important;
    right: 20px !important;
    z-index: 9999 !important;

    transform: scale(0.8); /* 챗봇창 전체를 70%로 축소 */
    transform-origin: bottom right; /* 우하단 기준으로 줄이기 */

    --df-messenger-button-titlebar-color: #5daec5;
    --df-messenger-chat-background-color: #ffffff;
    --df-messenger-font-color: #333333;
    --df-messenger-user-message: #d0f0c0;
    --df-messenger-bot-message: #e3f2fd;
  }
</style>

<df-messenger
  intent="WELCOME"
  chat-title="n:ear챗봇상담"
  agent-id="9f1618e2-0167-4e27-bb92-db0c60be0f6d"
  language-code="ko"
  chat-icon="https://example.com/my-icon.png">
</df-messenger>

<script src="https://www.gstatic.com/dialogflow-console/fast/messenger/bootstrap.js?v=1"></script>



