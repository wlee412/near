<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="profile-box">
  <h3>상담사 프로필</h3>
  <ul>
    <li><strong>아이디:</strong> ${counselor.id}</li>
    <li><strong>이름:</strong> ${counselor.name}</li>
    <li><strong>전화번호:</strong> ${counselor.phone}</li>
  </ul>
</div>
