<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>즐겨찾는 약국 목록</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypage.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/mypagePharmFav.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/common.css" />
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

		<div class="report-content">
			<h2>즐겨찾는 약국 목록</h2>
			<table id="reportTable" class="report-table">
				<thead>
					<tr>
						<th>약국명</th>
						<th>주소</th>
						<th>연락처</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="fav" items="${favList}">
						<tr>
							<td>${fav.pharmName}</td>
							<td>${fav.pharmAddress}</td>
							<td>${fav.pharmTel}</td>
							<td>
								 
						      
					   	   <button type="button"                        
							        class="delete-btn"
							        data-client-id="${fav.clientId}"
							        data-pharm-id="${fav.pharmId}"
							        onclick="deleteFav(this)">
							        삭제	</button>
							</td>
						</tr>
					</c:forEach>
					<c:if test="${empty favList}">
						<tr>
							<td colspan="3">즐겨찾기된 약국이 없습니다.</td>
						</tr>
					</c:if>
				</tbody>
			</table>
			<div id="pagination" style="text-align: center; margin-top: 20px;"></div>
		</div>
	</div>


	<jsp:include page="/WEB-INF/views/includes/footer.jsp" flush="true" />
	
	<script>
	  function deleteFav(btn) {
	    const clientId = btn.dataset.clientId;
	    const pharmId  = btn.dataset.pharmId;

	    fetch(`${window.location.origin}${pageContext.request.contextPath}/mypage/pharmDelete`, {
	      method: "POST",
	      headers: { "Content-Type": "application/json" },
	      body: JSON.stringify({ clientId, pharmId })
	    })
	    .then(res => {
	      if (!res.ok) throw new Error(res.statusText);
	      return res.json();   // 컨트롤러가 int를 리턴 → JSON 숫자로
	    })
	    .then(deletedCount => {
	      if (deletedCount > 0) {
	        // 성공 시 해당 행 제거
	        btn.closest("tr").remove();
	        alert("삭제 성공!");
	      } else {
	        alert("삭제된 항목이 없습니다.");
	      }
	    })
	    .catch(err => {
	      console.error(err);
	      alert("삭제 중 오류가 발생했습니다.");
	    });
	  }
	
	document.addEventListener('DOMContentLoaded', function() {
		  const rowsPerPage = 10;
		  const table       = document.getElementById("reportTable");
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
