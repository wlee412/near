<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>상담사 프로필</title>
  <link rel="stylesheet" href="/css/common.css" />
  <link rel="stylesheet" href="/css/counselor.css" />
</head>
<body>
  <div class="wrapper">
    <%@ include file="../includes/header.jsp" %>

    <div class="counselor-container">
      <!-- 타이틀 -->
      <div class="mypage-title">
			<a href="/counselor/mypage">
				<h2>상담사 마이페이지</h2>
				</a>
			</div>

      <div class="mypage-body">
        <!-- 좌측 사이드바 -->
        <aside class="mypage-sidebar">
          <a href="/counselor/profile" class="sidebar-button active">프로필</a>
          <a href="/counselor/time" class="sidebar-button">상담 가능시간 설정</a>
          <a href="/counselor/reservation" class="sidebar-button">상담 예약현황</a>
        </aside>

        <!-- 우측 콘텐츠 -->
        <section class="main-section">
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
        </section>
      </div>
    </div>
  </div>

  <%@ include file="../includes/footer.jsp" %>
  <script>
  window.addEventListener('DOMContentLoaded', () => {
    document.body.classList.add('loaded');
  });
</script>
</body>
</html>
