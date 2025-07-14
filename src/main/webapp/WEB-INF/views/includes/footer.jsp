<%@ page contentType="text/html; charset=UTF-8" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/chat.css">

<!-- 공통 푸터 -->
<footer>
  <div class="footer-container">
    <p>© 2025 n:ear. All rights reserved.</p>
    <p>
      <a href="/terms">이용약관</a>
      <span style="color:#555;">&nbsp;&nbsp;&nbsp;|&nbsp;</span>
      <a href="/privacy">개인정보처리방침</a>
    </p>
  </div>

</footer>
<%@ include file="../chatbot/chat.jsp" %>
