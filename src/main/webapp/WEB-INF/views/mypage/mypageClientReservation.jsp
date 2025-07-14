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
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypageLayout.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypageClientReservation.css">
</head>
<body>
	<div class="wrapper">
		<jsp:include page="/WEB-INF/views/includes/header.jsp" flush="true" />
		<div class="client-container">
			<div class="mypage-title">
				<a href="${pageContext.request.contextPath}/mypage/"><h2>마이페이지</h2></a>
			</div>
			<div class="mypage-body">
				<aside class="mypage-sidebar">
					<a
						href="${pageContext.request.contextPath}/mypage/mypageClientReservation"
						class="sidebar-button">예약확인</a> <a
						href="${pageContext.request.contextPath}/mypage/mypageReport"
						class="sidebar-button">검사기록</a> <a
						href="${pageContext.request.contextPath}/mypage/mypageFavorite"
						class="sidebar-button">즐겨찾기</a> <a
						href="${pageContext.request.contextPath}/mypage/mypageProfile"
						class="sidebar-button">프로필</a> <a
						href="${pageContext.request.contextPath}/mypage/mypageUpdate"
						class="sidebar-button">정보수정</a> <a
						href="${pageContext.request.contextPath}/mypage/mypagePassword"
						class="sidebar-button">비밀번호 변경</a> <a
						href="${pageContext.request.contextPath}/mypage/mypageDelete"
						class="sidebar-button">회원탈퇴</a>
				</aside>
				<div class="mypage-content-wrapper">
					<section class="main-section" id="contentArea">
						<h3>예약확인</h3>
						<div class="divider"></div>
						<c:if test="${empty reservationList}">
							<p>예약 내역이 없습니다.</p>
						</c:if>
						<div class="reservation-box">
							<table class="reservation-table" id="reservation-table">
								<thead>
									<tr>
										<th>예약번호</th>
										<th>예약상태</th>
										<th>예약일시</th>
										<th>상담사명</th>
										<th>전화번호</th>
										<th>상담사유</th>
										<th>예약취소</th>
									</tr>
								</thead>
								<tbody>
									<c:forEach var="r" items="${reservationList}">
										<tr>
											<td>${r.reservationNo}</td>
											<td>${r.state}</td>
											<td><fmt:formatDate value="${r.startTime}"
													pattern="yy.MM.dd (E) HH:mm" /></td>
											<td>${r.name}</td>
											<td>${r.counselorPhone}</td>
											<td>${r.symptom}</td>
											<td>
											<c:choose>
										        <c:when test="${r.state eq '예약'}">
										          <!-- 예약 상태일 때만 활성화 -->
										          <input type="button"
										                 class="cancel-btn"
										                 data-no="${r.reservationNo}"
										                 value="예약취소" />
										        </c:when>
										        <c:otherwise>
										          <!-- 그 외 상태일 땐 비활성화 -->
										          <input type="button"
										                 class="cancel-btn"
										                 value="예약취소"
										                 disabled
										                 style="opacity:0.5; cursor:not-allowed;" />
										        </c:otherwise>
										      </c:choose>
											</td>
										</tr>
									</c:forEach>
								</tbody>
							</table>
						</div>
						<div id="pagination" style="text-align: center; margin-top: 20px;"></div>
					</section>
				</div>
			</div>
		</div>
	</div>
		<div id="loadingOverlay" class="loading-overlay" style="display: none;">
		<div class="spinner"></div>
		<div class="loading-text">Loading...</div>
	</div>
	<jsp:include page="/WEB-INF/views/includes/footer.jsp" flush="true" />
	<script>
	document.addEventListener('DOMContentLoaded', function() {
	    // 모든 cancel-btn 에 리스너 부착
	    document.querySelectorAll('.cancel-btn').forEach(function(btn) {
	      btn.addEventListener('click', function() {
	        var reservationNo = this.dataset.no;
	        if (!confirm('정말 이 예약을 취소하시겠습니까?')) {
	          return;
	        }
	        // 동적 폼 생성
	        var form = document.createElement('form');
	        form.method = 'post';
	        form.action = '${pageContext.request.contextPath}/mypage/mypageCancelReservation';
			
	        
	     	// CSRF 토큰
	        var csrfParam = '${_csrf.parameterName}';
	        var csrfToken = '${_csrf.token}';
	        var t = document.createElement('input');
	        t.type = 'hidden';
	        t.name = csrfParam;
	        t.value = csrfToken;
	        form.appendChild(t);
	        
	        var input = document.createElement('input');
	        input.type = 'hidden';
	        input.name = 'reservationNo';
	        input.value = reservationNo;
	        form.appendChild(input);

	        document.body.appendChild(form);
	        form.submit();
	      });
	    });
	  });
	
	document.addEventListener('DOMContentLoaded', function() {
		  const rowsPerPage = 10;
		  const table       = document.getElementById("reservation-table");
		  const tbody       = table.querySelector("tbody");
		  const allRows     = Array.from(tbody.querySelectorAll("tr"));
		  const pagination  = document.getElementById("pagination");

		  // ① detailRow 로우가 없으니 그냥 1:1 매핑
		  const rows = allRows;

		  const totalPages = Math.ceil(rows.length / rowsPerPage);

		  // ② 페이지 표시 함수
		  function showPage(page) {
		    rows.forEach((tr, idx) => {
		      tr.style.display = (idx >= (page-1)*rowsPerPage && idx < page*rowsPerPage)
		                          ? "" : "none";
		    });
		    // ▶ 버튼 활성화 토글
		    Array.from(pagination.children).forEach((btn, idx) => {
		      btn.classList.toggle("active-page", idx === page-1);
		    });
		  } // ← 여기서 showPage 함수 닫기

		  // ③ 버튼 생성 (총 페이지가 1보다 클 때만)
		  if (totalPages > 1) {
		    for (let i = 1; i <= totalPages; i++) {
		      const btn = document.createElement("button");
		      btn.innerText = i;
		      btn.style.margin = "0 4px";
		      btn.addEventListener("click", () => showPage(i));
		      pagination.appendChild(btn);
		    }
		  }

		  // ④ 초기 1페이지만 보여주기
		  showPage(1);
		}); // ← 여기서 DOMContentLoaded 핸들러 닫기
	</script>
	<script src="${pageContext.request.contextPath}/js/loading.js" defer></script>
</body>
</html>