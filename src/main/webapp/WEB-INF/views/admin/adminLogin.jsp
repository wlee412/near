<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>

<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>관리자 로그인</title>
    <link rel="stylesheet" href="<c:url value='/css/adminLogin.css' />">
</head>
<body>

<div class="login-container">
    <h2>관리자 로그인</h2>

    <form action="<c:url value='/admin/login' />" method="post">
        <input type="text" name="id" placeholder="아이디" required>
        <input type="password" name="password" placeholder="비밀번호" required>
        <button type="submit" class="button-main">로그인</button>
    </form>

    <c:if test="${not empty loginError}">
        <p class="login-error">${loginError}</p>
    </c:if>
</div>

</body>
</html>