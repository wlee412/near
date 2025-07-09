<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>예약 확인</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypage.css">
<style>
/* 예약 카드 UI */
.reservation-card {
  border: 1px solid #d2e3ea;
  padding: 20px;
  border-radius: 12px;
  background-color: #ffffff;
  margin-bottom: 24px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
  transition: transform 0.2s ease;
}

.reservation-card:hover {
  transform: translateY(-3px);
  background-color: #f0fbff;
}

.reservation-card h3 {
  margin-bottom: 10px;
  font-size: 18px;
  color: #0077b6;
}

.reservation-card p {
  font-size: 14px;
  margin: 5px 0;
}

</style>
</head>
<body>
	<jsp:include page="/WEB-INF/views/includes/header.jsp" flush="true" />

	<div class="mypage-wrapper">
		<div class="mypage-sidebar">
			<h1 class="mypage-title">
				<a href="${pageContext.request.contextPath}/mypage/"
					style="text-decoration: none; color: inherit;">마이페이지</a>
			</h1>
			<div class="mypage-divider"></div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypageReservation'">예약확인</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypageReport'">검사기록</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypagePharmFav'">즐겨찾기</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypageProfile'">프로필</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypageUpdate'">정보수정</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypagePassword'">비밀번호 변경</div>
			<div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/mypage/mypageDelete'">회원탈퇴</div>
		</div>

		<div class="mypage-content">
			<h2>예약 확인</h2>
			<div class="mypage-divider" style="margin: 20px 0;"></div>

			<c:if test="${empty reservationList}">
				<p>예약 내역이 없습니다.</p>
			</c:if>

			<c:forEach var="res" items="${reservationList}">
				<div class="reservation-card">
					<h3>예약번호: ${res.reservationNo}</h3>
					<p>
						<strong>상담사:</strong> ${res.counselorName}
					</p>
					<p>
						<strong>내담자 ID:</strong> ${res.clientId}
					</p>
					<p>
						<strong>상담 시작:</strong>
						<fmt:formatDate value="${res.startTime}"
							pattern="yyyy-MM-dd HH:mm" />
					</p>
					<p>
						<strong>예약 상태:</strong> ${res.state}
					</p>
					<p>
						<strong>등록일:</strong>
						<fmt:formatDate value="${res.regDate}" pattern="yyyy-MM-dd HH:mm" />
					</p>
				</div>
			</c:forEach>
		</div>
	</div>

	<jsp:include page="/WEB-INF/views/includes/footer.jsp" flush="true" />
</body>
</html>