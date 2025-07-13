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
						<h3 class="section-title"> 상담 예약 현황</h3>

						<!-- 전체선택 체크박스 -->
						<table class="reservation-table">
							<thead>
								<tr>
									<th><input type="checkbox" id="select-all"></th>
									<!-- 전체 선택 -->
									<th class="sortable" data-sort="state">상태 <c:if
											test="${sortColumn == 'state'}">
											<span id="state-sort-arrow">${sortOrder == 'asc' ? '↑' : '↓'}</span>
										</c:if>
									</th>

									<th class="sortable" data-sort="start">상담일시 <c:if
											test="${sortColumn == 'start'}">
											<span id="start-sort-arrow">${sortOrder == 'asc' ? '&#9650;' : '&#9660;'}</span>
										</c:if>
									</th>


									<th>이름</th>
									<th>생년월일</th>
									<th>전화번호</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach var="r" items="${reservationList}">
									<tr class="reservation-row" data-id="${r.reservationNo}">
										<td><input type="checkbox" class="row-check"
											data-id="${r.reservationNo}"></td>
										<td data-column="state">${r.state}</td>
										<td data-column="start"><fmt:formatDate
												value="${r.start}" pattern="yyyy-MM-dd HH:mm" /></td>

										<td><a
											href="/counselor/reservation/detail/${r.reservationNo}">${r.name}</a></td>
										<td>${fn:substring(r.birth, 2, 4)}${fn:substring(r.birth, 5, 7)}${fn:substring(r.birth, 8, 10)}
											/ ${r.gender}</td>
										<td>${r.phone}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>

						<!-- 로딩 애니메이션은 테이블 바깥에 배치 -->
						<div id="loading-overlay" style="display: none;">
							<div class="spinner"></div>
						</div>

						<!-- 예약 취소 버튼 -->
						<button class="cancelSelectedBtn">예약 취소하기</button>


						<!-- 실제 페이지 내용 -->
						<div id="content" style="display: none;">
							<!-- 페이지의 실제 내용 -->
						</div>
						<!-- 페이지네이션 -->
						<div class="pagination">
							<c:if test="${currentPage > 1}">
								<a href="?page=1" class="page-btn first"><<</a>
							</c:if>

							<c:if test="${currentPage > 1}">
								<a href="?page=${currentPage - 1}" class="page-btn prev"><</a>
							</c:if>

							<c:forEach begin="1" end="${totalPages}" var="i">
								<c:choose>
									<c:when test="${i == currentPage}">
										<span class="current-page">${i}</span>
									</c:when>
									<c:otherwise>
										<a href="?page=${i}" class="page-btn">${i}</a>
									</c:otherwise>
								</c:choose>
							</c:forEach>

							<c:if test="${currentPage < totalPages}">
								<a href="?page=${currentPage + 1}" class="page-btn next">></a>
							</c:if>

							<c:if test="${currentPage < totalPages}">
								<a href="?page=${totalPages}" class="page-btn last">>></a>
							</c:if>
						</div>


					</div>
				</section>
			</div>
		</div>
	</div>

	<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
	<script>
	$(document).ready(function () {
	  const urlParams = new URLSearchParams(window.location.search);
	  const currentSortColumn = urlParams.get("sortColumn") || "start";
	  const currentSortOrder = urlParams.get("sortOrder") || "desc";

	  //  정렬 버튼 클릭
	  $("[data-sort='start']").on("click", function () {
	    const newOrder = currentSortColumn === "start" && currentSortOrder === "asc" ? "desc" : "asc";
	    window.location.href = `/counselor/reservation?page=1&sortColumn=start&sortOrder=${newOrder}`;
	  });

	  $("[data-sort='state']").on("click", function () {
	    const newOrder = currentSortColumn === "state" && currentSortOrder === "asc" ? "desc" : "asc";
	    window.location.href = `/counselor/reservation?page=1&sortColumn=state&sortOrder=${newOrder}`;
	  });

	  //  전체 선택 체크박스
	  $("#select-all").on("change", function () {
	    const isChecked = $(this).prop("checked");
	    $(".row-check").prop("checked", isChecked);
	  });

	  //  예약 취소 버튼
	  $(".cancelSelectedBtn").on("click", function () {
	    const selectedReservations = [];
	    $(".row-check:checked").each(function () {
	      selectedReservations.push($(this).data("id"));
	    });

	    if (selectedReservations.length > 0 && confirm("선택된 예약을 취소하시겠습니까?")) {
	      $.post("/counselor/reservation/cancel", { reservationNos: selectedReservations }, function () {
	        alert("예약이 취소되었습니다.");
	        location.reload();
	      }).fail(function (_, __, error) {
	        console.error("Error:", error);
	        alert("예약 취소 중 오류가 발생했습니다.");
	      });
	    }
	  });

	  // ⏳ 로딩 애니메이션
	  $("a").on("click", function (e) {
	    const target = $(this).attr("href");
	    if (target && target !== "#") {
	      $("#loading-overlay").show();
	    }
	  });
	});
	</script>

	<%@ include file="../includes/footer.jsp"%>
</body>
</html>
