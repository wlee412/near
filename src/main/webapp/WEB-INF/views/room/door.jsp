<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form action="/chat/room" method="get">
  <label>상담실 비밀번호 (UUID)</label>
  <input type="text" name="token" required>
  <button type="submit">입장</button>
</form>
</body>
</html>