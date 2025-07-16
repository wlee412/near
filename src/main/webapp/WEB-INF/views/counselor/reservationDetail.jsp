<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>

<meta charset="UTF-8">
<title>상담 상세 보기</title>
<link rel="stylesheet" href="/css/common.css" />
<link rel="stylesheet" href="/css/counselor.css" />
</head>
<body>
	<div class="wrapper">
		<%@ include file="../includes/header.jsp"%>

		<div class="counselor-container">
			<!-- 타이틀 -->
			<div class="mypage-title">

				<a href="/counselor/mypage">
					<h2>상담사 마이페이지</h2>

				</a>
			</div>

			<div class="mypage-body">
				<aside class="mypage-sidebar">
					<a href="/counselor/profile" class="sidebar-button">프로필</a> <a
						href="/counselor/time" class="sidebar-button">상담 가능시간 설정</a> <a
						href="/counselor/reservation" class="sidebar-button active">상담
						예약현황</a>
				</aside>

				<section class="main-section">
					<div class="reservation-box">
						<h3 class="section-title">상담 전 상세정보 분석</h3>

						<!-- ✅ 예약 정보 테이블 -->
						<table class="detail-table">
							<tr>
								<th>상담일시</th>
								<th>이름</th>
								<th>생년월일</th>
								<th>전화번호</th>
							</tr>
							<tr>
								<td><fmt:formatDate value="${reservation.start}"
										pattern="yyyy-MM-dd HH:mm" /></td>
								<td>${reservation.name}</td>
                				  <td>
 									 ${fn:substring(reservation.birth, 2, 4)}
 									 ${fn:substring(reservation.birth, 5, 7)}
								     ${fn:substring(reservation.birth, 8, 10)} / ${reservation.gender}
									</td>
								<td>${reservation.phone}</td>
							</tr>

							<c:if test="${not empty gptSummary}">
								<tr>
									<td colspan="4" class="gpt-summary-cell">${gptSummary}</td>
								</tr>
							</c:if>
						</table>

						<!-- ✅ 돌아가기 버튼 -->
						<div class="btn-box">
							<a href="/counselor/reservation" class="btn-back">이전으로 이동하기</a>
						</div>
					</div>
				</section>
			</div>
		</div>
	</div>

	<%@ include file="../includes/footer.jsp"%>
</body>
</html>

