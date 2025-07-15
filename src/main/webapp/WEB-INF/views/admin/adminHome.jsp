<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
  <title>관리자 홈</title>
  <link rel="stylesheet" href="<c:url value='/css/adminHome.css' />">
</head>
<body>

  <h1>관리자 홈 화면</h1>

  <div class="menu-container">
    <div class="menu-box">
      <a href="/admin/adminMember">회원관리</a>
    </div>
    <div class="menu-box">
      <a href="/admin/surveyStats">통계</a>
    </div>
    <div class="menu-box">
      <a href="/admin/adminReservation">예약내역</a>
    </div>
     <div class="menu-box">
    <a href="/admin/logout">로그아웃</a>
    </div>
  </div>

</body>
</html>