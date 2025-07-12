<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>즐겨찾는 목록</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypage.css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypageFavorite.css" />
</head>

<style>

</style>
<body>
	<div class="wrapper">
	<jsp:include page="/WEB-INF/views/includes/header.jsp" flush="true" />
		<div class="client-container">
				<div class="mypage-title">
			<a href="${pageContext.request.contextPath}/mypage/"><h2>마이페이지</h2></a>
	    </div>
	    <div class="mypage-body">
		<aside class="mypage-sidebar">
			<a href="${pageContext.request.contextPath}/mypage/mypageClientReservation" class="sidebar-button">예약확인</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageReport" class="sidebar-button">검사기록</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageFavorite" class="sidebar-button">즐겨찾기</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageProfile" class="sidebar-button">프로필</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageUpdate" class="sidebar-button">정보수정</a>
			<a href="${pageContext.request.contextPath}/mypage/mypagePassword" class="sidebar-button">비밀번호 변경</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageDelete" class="sidebar-button">회원탈퇴</a>
		</aside>
		<section class="main-section" id="contentArea">
		<h3>즐겨찾기</h3>
			<div class="divider"></div>
		<div class="report-content">
			<div class="fav-tabs">
				<button id="tab-pharm" class="fav-tab active">약국 즐겨찾기</button>
				<button id="tab-hosp" class="fav-tab">병원 즐겨찾기</button>
			</div>
			<!-- 약국 섹션 -->
			<div id="pharmSection" class="fav-section active">
				<table id="pharmTable" class="report-table">
					<thead>
						<tr>
							<th>약국명</th>
							<th>주소</th>
							<th>연락처</th>
							<th>삭제</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="fav" items="${pharmList}">
							<tr>
								<td>${fav.pharmName}</td>
								<td>${fav.pharmAddress}</td>
								<td>${fav.pharmTel}</td>
								<td>
									<button type="button" class="delete-btn" data-type="pharm"
										data-client-id="${fav.clientId}" data-id="${fav.pharmId}"
										onclick="deleteFav(this)">삭제</button>
								</td>
							</tr>
						</c:forEach>
						<c:if test="${empty pharmList}">
							<tr>
								<td colspan="4">즐겨찾기된 약국이 없습니다.</td>
							</tr>
						</c:if>
					</tbody>
				</table>
				<!-- 여기 ID 가 setupPaging 에 넘기는 키 -->
				<div id="pharmPagination" class="pagination"
					style="text-align: center; margin-top: 20px;"></div>
			</div>

			<!-- 병원 섹션 -->
			<div id="hospSection" class="fav-section">
				<table id="hospTable" class="report-table">
					<thead>
						<tr>
							<th>병원명</th>
							<th>주소</th>
							<th>연락처</th>
							<th>삭제</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="fav" items="${hospList}">
							<tr>
								<td>${fav.hospName}</td>
								<td>${fav.hospAddress}</td>
								<td>${fav.hospTel}</td>
								<td>
									<button type="button" class="delete-btn" data-type="hosp"
										data-client-id="${fav.clientId}" data-id="${fav.hospId}"
										onclick="deleteFav(this)">삭제</button>
								</td>
							</tr>
						</c:forEach>
						<c:if test="${empty hospList}">
							<tr>
								<td colspan="4">즐겨찾기된 병원이 없습니다.</td>
							</tr>
						</c:if>
					</tbody>
				</table>
				<div id="hospPagination" class="pagination" style="text-align: center; margin-top: 20px;"></div>
			</div>
		</div>
		</section>
		</div>
		</div>
	</div>

		<jsp:include page="/WEB-INF/views/includes/footer.jsp" flush="true" />

		<script>
	  const tabPharm = document.getElementById('tab-pharm');
	    const tabHosp  = document.getElementById('tab-hosp');
	    const sectPharm = document.getElementById('pharmSection');
	    const sectHosp  = document.getElementById('hospSection');

	    tabPharm.addEventListener('click', ()=>{
	      tabPharm.classList.add('active');
	      tabHosp.classList.remove('active');
	      sectPharm.classList.add('active');
	      sectHosp.classList.remove('active');
	    });
	    tabHosp.addEventListener('click', ()=>{
	      tabHosp.classList.add('active');
	      tabPharm.classList.remove('active');
	      sectHosp.classList.add('active');
	      sectPharm.classList.remove('active');
	    });

	    // 삭제 함수 (약국/병원 공용)
	   function deleteFav(btn) {
  const type     = btn.dataset.type;        // "pharm" or "hosp"
  const clientId = btn.dataset.clientId;
  const id       = btn.dataset.id;
  
  const ctx = '${pageContext.request.contextPath}';  
  const url = window.location.origin 
            + ctx 
            + '/mypage/' 
            + type 
            + 'Delete';

  console.log('▶ 호출 URL:', url, 'payload:', { clientId, [type+'Id']: id });
  
  fetch(url, {
	    method: "POST",
	    headers: { "Content-Type": "application/json" },
	    body: JSON.stringify(
	      type === 'pharm'
	        ? { clientId, pharmId: id }
	        : { clientId, hospId:  id }
	    ),
	  })
	  .then(res => {
	    if (!res.ok) throw new Error(res.statusText);
	    return res.json();
	  })
	  .then(cnt => {
	  if (cnt > 0) {
	    // 삭제 성공 알림 대신 confirm 창
	    if (confirm("삭제하시겠습니까?")) {
	      // "확인" 버튼을 눌렀을 때만 행을 제거
	      btn.closest("tr").remove();
	    }
	  } else {
	    alert("삭제된 항목이 없습니다.");
	  }
	  })
	  .catch(err => {
	    console.error(err);
	    alert("삭제 중 오류가 발생했습니다.");
	  });
}

	    // 페이징 함수 공용
  /**
   * tableId       : 페이징할 테이블의 id
   * pagerId       : 페이지 버튼을 렌더링할 <div> id
   * rowsPerPage   : 한 페이지당 보여줄 행 개수
   * maxPageButtons: 한 화면에 보여줄 최대 페이지 버튼 개수
   */
  function setupPaging(tableId, pagerId, rowsPerPage = 10, maxPageButtons = 10) {
    const table       = document.getElementById(tableId);
    const tbody       = table.querySelector("tbody");
    const rows        = Array.from(tbody.querySelectorAll("tr"));
    const pager       = document.getElementById(pagerId);
    const totalPages  = Math.ceil(rows.length / rowsPerPage);
    let currentPage   = 1;

    function showPage(page) {
      currentPage = page;
      // 1) 행 표시
      rows.forEach((tr, idx) => {
        tr.style.display = (idx >= (page - 1) * rowsPerPage && idx < page * rowsPerPage)
                         ? "" : "none";
      });
      // 2) 페이지 버튼 재생성
      renderPager();
    }

    function renderPager() {
      pager.innerHTML = "";
      // 현재 페이지가 속한 버튼 묶음 계산
      const groupIndex = Math.floor((currentPage - 1) / maxPageButtons);
      const startPage  = groupIndex * maxPageButtons + 1;
      const endPage    = Math.min(startPage + maxPageButtons - 1, totalPages);

      // « 이전 묶음 버튼
      if (startPage > 1) {
        const prev = document.createElement("button");
        prev.innerText = "«";
        prev.addEventListener("click", () => showPage(startPage - 1));
        pager.appendChild(prev);
      }

      // 1,2,3...10 버튼
      for (let p = startPage; p <= endPage; p++) {
        const btn = document.createElement("button");
        btn.innerText = p;
        btn.style.margin = "0 4px";
        btn.classList.toggle("active-page", p === currentPage);
        btn.addEventListener("click", () => showPage(p));
        pager.appendChild(btn);
      }

      // » 다음 묶음 버튼
      if (endPage < totalPages) {
        const next = document.createElement("button");
        next.innerText = "»";
        next.addEventListener("click", () => showPage(endPage + 1));
        pager.appendChild(next);
      }
    }

    // 초기 렌더링 (1페이지만 보여주고 버튼 생성)
    showPage(1);
  }

  document.addEventListener("DOMContentLoaded", () => {
    setupPaging("pharmTable", "pharmPagination");
    setupPaging("hospTable",  "hospPagination");
  });
  </script>
</body>
</html>
