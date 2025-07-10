<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>상담 예약 현황</title>
<link rel="stylesheet" href="/css/common.css" />
<link rel="stylesheet" href="/css/counselor.css" />
</head>
<body>
	<div class="wrapper">
		<%@ include file="../includes/header.jsp"%>

		<div class="counselor-container">
			<div class="mypage-title">
				<h2>상담사 마이페이지</h2>
			</div>

			<div class="mypage-body">
				<aside class="mypage-sidebar">
					<a href="/counselor/profile" class="sidebar-button">프로필</a> <a
						href="/counselor/time" class="sidebar-button">상담 가능시간 설정</a> <a
						href="/counselor/reservation" class="sidebar-button active">상담
						예약현황</a> <a href="/counselor/room" class="sidebar-button">상담 방
						개설하기</a>
				</aside>

				<section class="main-section">
					<div class="reservation-box">
						<h3 class="section-title">📅 상담 예약 현황</h3>
						<table class="reservation-table">
							<thead>
								<tr>
									<th>상태</th>
									<th>상담일시</th>
									<th>이름</th>
									<th>생년월일</th>
									<th>성별</th>
									<th>전화번호</th>
									<th>상세</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="r" items="${reservationList}">
									<tr>
										<td>${r.state}</td>
										<td><fmt:formatDate value="${r.start}"
												pattern="yy.MM.dd (E)" /></td>


										<td>${r.name}</td>
										<td>${fn:substring(r.birth, 2, 4)}${fn:substring(r.birth, 5, 7)}${fn:substring(r.birth, 8, 10)}
										</td>
										<td>${r.gender}</td>
										<td>${r.phone}</td>
										<td>
											<button class="detail-btn" data-id="${r.reservationNo}">보기</button>
										</td>
									</tr>
								</c:forEach>

							</tbody>
						</table>
					</div>
				</section>
			</div>
		</div>
	</div>

	<!-- 상담 상세 모달 -->
	<div id="reservationModal" class="modal">
		<div class="modal-content">
			<span class="close-btn">&times;</span>
			<h3>상담 상세 정보</h3>

			<p>
				<strong>인적사항:</strong> <span id="modal-name"></span> / <span
					id="modal-birth"></span> / <span id="modal-gender"></span>
			</p>

			<p>
				<strong>전화번호:</strong> <span id="modal-phone"></span>
			</p>
			<p>
				<strong>주소:</strong> <span id="modal-address"></span>
			</p>

			<p>
				<strong>참고사항 분석:</strong> <span id="modal-gpt"></span>
			</p>
		</div>
	</div>

	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script>
		$(document).ready(function() {
			$('.detail-btn').on('click', function() {
				const reservationNo = $(this).data('id');
				$.getJSON('/counselor/reservation/detail', {
					no : reservationNo
				}, function(data) {


					$('#modal-state').text(data.state);
					$('#modal-id').text(data.clientId);
					$('#modal-name').text(data.name);
					$('#modal-birth').text(data.birth);
					$('#modal-gender').text(data.gender);
					$('#modal-phone').text(data.phone);
					$('#modal-address').text(data.address);
					$('#modal-gpt').text(data.gptSummary);
					$('#reservationModal').show();
				});
			});

			$('.close-btn').on('click', function() {
				$('#reservationModal').hide();
			});
		});
	</script>

	<%@ include file="../includes/footer.jsp"%>
</body>
</html>
