<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/chatbot.css" />

<!-- 챗봇 아이콘: 우측 하단에 고정 -->
<div class="chatbot-icon" onclick="toggleChatModal()">
<img src="${pageContext.request.contextPath}/images/chatbot.png" alt="챗봇 아이콘">
</div>

<!-- 챗봇 모달 (처음엔 숨겨짐) -->
<div class="chat-modal" style="display: none;">
	<div class="chat-header">
		<span>n:ear 챗봇상담</span>
		<button onclick="toggleChatModal()">-</button>
	</div>
	<div class="chat-log">
		<c:choose>
			<c:when test="${not empty chatList}">
				<c:forEach var="chat" items="${chatList}">
					<div class="chat-bubble ${chat.sender eq 'user' ? 'user' : 'bot'}">
						<div class="message">${chat.message}</div>
					</div>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<div class="chat-bubble bot">
					<div class="message">
						안녕하세요! n:ear입니다.<br> 어떤 도움이 필요하신가요? 😊<br> 심리상담이 필요하거나
						다른 것에 대해 이야기 나누고 싶으시면 말씀해주세요.
					</div>
				</div>
			</c:otherwise>
		</c:choose>
	</div>

	<div class="chat-input">
		<input type="text" class="chat-input-field" placeholder="메시지를 입력하세요"
			onkeydown="if(event.key==='Enter') sendMessage()" />
		<button onclick="sendMessage()">전송</button>
	</div>
</div>

<!-- JS 파일 -->
<script src="${pageContext.request.contextPath}/js/chatbot.js"></script>
