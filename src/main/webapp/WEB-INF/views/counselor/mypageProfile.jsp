<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="profile-box">
  <div class="profile-content">
    <!-- 왼쪽 이미지 -->
    <img class="profile-img" src="/images/${counselor.counselorId}.png" alt="상담사 이미지">

    <!-- 오른쪽 텍스트 -->
    <div class="profile-info">
      <h3 class="section-title">상담사 프로필</h3>
      <ul>
        <li><strong>아이디:</strong> ${counselor.counselorId}</li>
        <li><strong>이름:</strong> ${counselor.name}</li>
        <li><strong>전화번호:</strong> ${counselor.phone}</li>
      </ul>
    </div>
  </div>
</div>

