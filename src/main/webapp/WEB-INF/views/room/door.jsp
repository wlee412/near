<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>코드 입력</title>
<link rel="stylesheet" href="/css/common.css" type="text/css" />
<link rel="stylesheet" href="/css/roomdoor.css" type="text/css" />
</head>
<body>
	<img src="https://i.imgur.com/H5grzMN.png" alt="logo-img">
	<form action="/chat/token" method="get">
		<div id="token-input">
			<input type="text" name="t" id="token" required>
			<button type="submit">입장</button>
		</div>
		<h1>상담실 입장 코드를 입력하세요</h1>
	</form>
</body>
</html>