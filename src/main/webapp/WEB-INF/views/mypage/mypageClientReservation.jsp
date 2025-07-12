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
			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/mypage/mypageClientReservation'">예약확인</div>
			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/mypage/mypageReport'">검사기록</div>
			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/mypage/mypagePharmFav'">즐겨찾기</div>
			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/mypage/mypageProfile'">프로필</div>
			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/mypage/mypageUpdate'">정보수정</div>
			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/mypage/mypagePassword'">비밀번호
				변경</div>
			<div class="menu-item"
				onclick="location.href='${pageContext.request.contextPath}/mypage/mypageDelete'">회원탈퇴</div>
		</div>

		<div class="mypage-content">
			<h2>예약 확인</h2>
			<div class="mypage-divider" style="margin: 20px 0;"></div>

			<c:if test="${empty reservationList}">
				<p>예약 내역이 없습니다.</p>
			</c:if>
			<c:forEach var="r" items="${reservationList}">
			  <!-- DTO인 경우 -->
			  <div>
			    reservationNo = ${r.reservationNo},
			    state         = ${r.state},
			    startTime     = ${r.startTime},
			    counselorName = ${r.name},
			    counselorPhone= ${r.counselorPhone},
			    symptom       = ${r.symptom}
			  </div>
				</c:forEach>
<!-- 			<section class="main-section"> -->
<!-- 				<div class="reservation-box"> -->
<!-- 					<h3 class="section-title">📅 상담 예약 현황</h3> -->
<!-- 					<table class="reservation-table"> -->
<!-- 						<thead> -->
<!-- 							<tr> -->
<!-- 								<th>예약번호</th> -->
<!-- 								<th>예약상태</th> -->
<!-- 								<th>예약일시</th> -->
<!-- 								<th>상담사명</th> -->
<!-- 								<th>전화번호</th> -->
<!-- 								<th>상담목적</th> -->
<!-- 							</tr> -->
<!-- 						</thead> -->
<!-- 						<tbody> -->
<%-- 							<c:forEach var="r" items="${reservationList}"> --%>
<!-- 								<tr> -->
<%-- 									<td>${r.reservationNo}</td> --%>
<%-- 									<td>${r.state}</td> --%>
<%-- 									<td><fmt:formatDate value="${r.startTime}" --%>
<%-- 											pattern="yy.MM.dd (E)" /></td> --%>
<%-- 									<td>${r.counselorName}</td> --%>
<%-- 									<td>${r.counselorPhone}</td> --%>
<%-- 									<td>${r.symptom}</td> --%>
<!-- 								</tr> -->
<%-- 							</c:forEach> --%>
<!-- 						</tbody> -->
<!-- 					</table> -->
<!-- 				</div> -->
<!-- 			</section> -->
		</div>
	</div>

	<jsp:include page="/WEB-INF/views/includes/footer.jsp" flush="true" />
</body>
</html>