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
<style>

.reservation-table {
  width: 100%;
  border-collapse: collapse;    /* 셀 간 경계를 겹치지 않도록 */
  table-layout: fixed;          /* 컬럼 너비 균일 배분 */
  font-size: 0.95rem;
}

.reservation-table th,
.reservation-table td {
  border: 1px solid #ddd;       /* 연한 회색 실선으로 경계 */
  padding: 0.75em 0.5em;
  text-align: center;
  vertical-align: middle;
}

.reservation-table th {
  background-color: #f7f7f7;    /* 헤더 배경 약간 다른 색 */
  font-weight: 600;
}

.reservation-table tr:nth-child(even) {
  background-color: #fafafa;    /* 짝수 행에 은은한 배경색으로 가독성 향상 */
}

.reservation-table input[type="button"] {
  padding: 0.3em 0.6em;
  font-size: 0.9rem;
  cursor: pointer;
}
</style>
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
			<section class="main-section">
				<div class="reservation-box">
					<h3 class="section-title">📅 상담 예약 현황</h3>
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
							    <td>
							      <fmt:formatDate value="${r.startTime}" pattern="yy.MM.dd (E) HH:mm" />
							    </td>
							    <td>${r.name}</td>
							    <td>${r.counselorPhone}</td>
							    <td>${r.symptom}</td>
							    <td>
							    	<input type="button" class="cancel-btn" name="cancel-btn" id="cancel-btn" data-no="${r.reservationNo}" value="예약취소">
							    </td>
							  </tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</section>
			<div id="pagination" style="text-align: center; margin-top: 20px;"></div>
		</div>
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
</body>
</html>