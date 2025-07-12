<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>검사 기록</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypageReport.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css" />

  <!-- Chart.js & Luxon adapter -->
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/luxon@3.4.4/build/global/luxon.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/chartjs-adapter-luxon@1.3.1"></script>
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
			<a href="${pageContext.request.contextPath}/mypage/mypageClientReservation" class="sidebar-button">예약확인</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageReport" class="sidebar-button">검사기록</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageFavorite" class="sidebar-button">즐겨찾기</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageProfile" class="sidebar-button">프로필</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageUpdate" class="sidebar-button">정보수정</a>
			<a href="${pageContext.request.contextPath}/mypage/mypagePassword" class="sidebar-button">비밀번호 변경</a>
			<a href="${pageContext.request.contextPath}/mypage/mypageDelete" class="sidebar-button">회원탈퇴</a>
	</aside>
    <section class="main-section" id="contentArea">
      <h3>검사기록</h3>
			<div class="divider"></div>
      <div class="chart-container">
        <canvas id="lineChart"></canvas>
      </div>
      <table id="reportTable" class="report-table">
        <thead>
          <tr>
            <th>날짜</th>
            <th>검사명</th>
            <th>점수</th>
            <th>해석</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="report" items="${feedbackList}" varStatus="status">
            <tr>
              <td><fmt:formatDate value='${report.regDate}' pattern="yyyy-MM-dd'T'HH:mm:ss" /></td>
              <td>${report.surveyName}</td>
              <td>${empty report.score ? 0 : report.score}</td>
              <td>
                <div class="feedback-preview">
                  <c:choose>
                    <c:when test="${fn:length(report.feedback) > 30}">
                      ${fn:substring(report.feedback, 0, 30)}...
                    </c:when>
                    <c:otherwise>
                      ${report.feedback}
                    </c:otherwise>
                  </c:choose>
                  <button class="toggle-btn"
                          onclick="toggleDetail('detail-${status.index}')">자세히</button>
                </div>
              </td>
            </tr>
            <tr id="detail-${status.index}" class="feedback-detail" style="display: none;">
              <td colspan="4">${report.feedback}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
      
      <div id="pagination" style="text-align:center; margin-top: 20px;"></div>
      
    </section>
    </div>
    </div>
  </div>

  <jsp:include page="/WEB-INF/views/includes/footer.jsp" flush="true" />

     <script>
    document.addEventListener('DOMContentLoaded', function() {
      // 1) JSP → JS
      const chartData = [];
      <c:forEach var="report" items="${feedbackList}">
        chartData.push({
          x: "<fmt:formatDate value='${report.regDate}' pattern='yyyy-MM-dd' />T00:00:00",
          y: ${empty report.score ? 0 : report.score},
          survey: "${report.surveyId}"
        });
      </c:forEach>

      // 2) 그룹핑
      const surveyMap = {
        "1001":"GAD-7","1002":"PHQ-9","1003":"ASRS","1004":"CES-D",
        "1005":"PSS","1006":"K-EDI","1007":"K-PSQI","1008":"ASRS",
        "1009":"PSS","1010":"GSE"
      };
      const grouped = {};
      chartData.forEach(item => {
        const key = surveyMap[item.survey] || item.survey;
        (grouped[key]||(grouped[key]=[])).push(item);
      });
      const datasets = Object.entries(grouped).map(([label,data])=>({
        label,
        data,
        borderWidth:2,
        tension:0.2
      }));

      // 3) 차트 생성
      new Chart(
        document.getElementById('lineChart').getContext('2d'),
        {
          type: 'line',
          data: { datasets },
          options: {
            responsive: true,
            interaction: { mode:'nearest', intersect:false },
            scales: {
              x: {
                type: 'time',
                time: {
                  unit: 'day',
                  tooltipFormat: 'yyyy-MM-dd',
                  displayFormats:{ day:'yyyy-MM-dd' }
                },
                title:{ display:true, text:'날짜' },
                ticks:{ maxRotation:0, autoSkip:true }
              },
              y: {
                beginAtZero:true,
                title:{ display:true, text:'점수' }
              }
            },
            plugins: {
              tooltip: {
                enabled: true,
                callbacks: {
                  label: function(ctx) {
                    // parsing 기본값(=true)이므로 ctx.parsed.y 가 숫자
                    return ctx.parsed.y + '점';
                  }
                }
              },
              legend: { display:true, position:'bottom' }
            }
          }
        }
      );
    });
    
    function toggleDetail(id) {
        const detailRow = document.getElementById(id);
        if (detailRow) {
          detailRow.style.display = detailRow.style.display === 'none' ? '' : 'none';
        }
     }
    
    document.addEventListener('DOMContentLoaded', function() {
    	  const rowsPerPage = 10;
    	  const table       = document.getElementById("reportTable");
    	  const tbody       = table.querySelector("tbody");
    	  const allRows     = Array.from(tbody.querySelectorAll("tr"));
    	  const pagination  = document.getElementById("pagination");

    	  // 2개씩 묶어서 [내용Row, 디테일Row] 배열로 만들기
    	  const rowPairs = [];
    	  for (let i = 0; i < allRows.length; i += 2) {
    	    rowPairs.push([ allRows[i], allRows[i+1] ]);
    	  }

    	  // 총 페이지 수
    	  const totalPages = Math.ceil(rowPairs.length / rowsPerPage);

    	  // 페이징 시 호출: 항상 "내용 행"만 보이고 "디테일 행"은 숨김
    	  function showPage(page) {
    	    rowPairs.forEach((pair, idx) => {
    	      const inThisPage = idx >= (page-1)*rowsPerPage && idx < page*rowsPerPage;
    	      // 내용 행은 페이지만큼만
    	      pair[0].style.display = inThisPage ? "" : "none";
    	      // 디테일 행은 무조건 숨김
    	      pair[1].style.display = "none";
    	    });
    	    // 버튼 활성화 스타일 토글
    	    Array.from(pagination.children).forEach((btn, i) => {
    	      btn.classList.toggle("active-page", i === page-1);
    	    });
    	  }

    	  // 페이지 버튼 만들기
    	  if (totalPages > 1) {
    	    for (let i = 1; i <= totalPages; i++) {
    	      const btn = document.createElement("button");
    	      btn.innerText = i;
    	      btn.style.margin = "0 4px";
    	      btn.addEventListener("click", () => showPage(i));
    	      pagination.appendChild(btn);
    	    }
    	  }

    	  // 초기 표시 (1페이지)
    	  showPage(1);
    	});
    	  
    	// 토글 버튼 누르면 디테일만 토글
    	function toggleDetail(id) {
    	  const row = document.getElementById(id);
    	  row.style.display = row.style.display === "none" ? "" : "none";
    	}
  </script>
</body>
</html>
