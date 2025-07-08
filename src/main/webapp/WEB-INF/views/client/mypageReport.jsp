<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>검사 기록</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/mypageReport.css" />

  <!-- Chart.js & Luxon adapter -->
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/luxon@3.4.4/build/global/luxon.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/chartjs-adapter-luxon@1.3.1"></script>
</head>
<body>
  <jsp:include page="/WEB-INF/views/includes/header.jsp" flush="true" />

  <div class="mypage-wrapper">
    <div class="mypage-sidebar">
      <h1 class="mypage-title">
        <a href="${pageContext.request.contextPath}/client/mypage" style="text-decoration: none; color: inherit;">마이페이지</a>
      </h1>
      <div class="mypage-divider"></div>
      <div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/client/mypageReservation'">예약확인</div>
      <div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/client/mypageReport'">검사기록</div>
      <div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/client/mypageFavorite'">병원 즐겨찾기</div>
      <div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/client/mypageProfile'">프로필</div>
      <div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/client/mypageUpdate'">정보수정</div>
      <div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/client/mypagePassword'">비밀번호 변경</div>
      <div class="menu-item" onclick="location.href='${pageContext.request.contextPath}/client/mypageDelete'">회원탈퇴</div>
    </div>

    <div class="report-content">
      <h2>검사 기록</h2>
      <div class="chart-container">
        <canvas id="lineChart"></canvas>
      </div>
      <table class="report-table">
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
              <td><fmt:formatDate value='${report.regDate}' pattern='yyyy-MM-dd' />T00:00:00</td>
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
                  <button class="toggle-btn" onclick="toggleDetail('detail-${status.index}')">자세히</button>
                </div>
              </td>
            </tr>
            <tr id="detail-${status.index}" class="feedback-detail" style="display: none;">
              <td colspan="4">${report.feedback}</td>
            </tr>
          </c:forEach>
        </tbody>
      </table>
    </div>
  </div>

  <jsp:include page="/WEB-INF/views/includes/footer.jsp" flush="true" />

  <script>
  const surveyMap = {
    1001: "GAD-7", 1002: "PHQ-9", 1003: "ASRS", 1004: "CES-D",
    1005: "PSS", 1006: "K-EDI", 1007: "K-PSQI", 1008: "ASRS",
    1009: "PSS", 1010: "GSE"
  };

  // JSTL로 삽입되는 데이터
  const chartData = [
    <c:forEach var="report" items="${feedbackList}" varStatus="loop">
      {
        x: "<fmt:formatDate value='${report.regDate}' pattern='yyyy-MM-dd' />",
        y: ${report.score},
        survey: "${report.surveyId}"
      }<c:if test="${!loop.last}">,</c:if>
    </c:forEach>
  ];

  // 차트 객체를 저장할 변수 (중복 차트 생성 방지용)
  let myChart;

  // 데이터가 2개 이상일 때만 차트 생성
  if (chartData.length <= 1) {
    document.getElementById('lineChart').style.display = 'none';
    const msg = document.createElement('p');
    msg.innerText = '그래프를 표시하려면 최소 2개 이상의 검사 기록이 필요합니다.';
    msg.style.textAlign = 'center';
    msg.style.marginTop = '20px';
    document.querySelector('.chart-container').appendChild(msg);
  } else {
    // 데이터 그룹핑: 설문조사별로
    const grouped = {};
    chartData.forEach(item => {
      const key = surveyMap[item.survey] || item.survey;
      if (!grouped[key]) grouped[key] = [];
      grouped[key].push({ x: item.x, y: item.y });
    });

    const datasets = Object.entries(grouped).map(([label, data]) => ({
      label,
      data,
      borderWidth: 2,
      tension: 0.2
    }));

    // 기존 차트가 있다면 제거
    const ctx = document.getElementById('lineChart').getContext('2d');
    if (myChart) {
      myChart.destroy();
    }

    // 차트 생성
    myChart = new Chart(ctx, {
      type: 'line',
      data: { datasets },
      options: {
        responsive: true,
        parsing: false,
        scales: {
          x: {
            type: 'time',
            time: { unit: 'day' },
            title: { display: true, text: '날짜' }
          },
          y: {
            beginAtZero: true,
            title: { display: true, text: '점수' }
          }
        },
        plugins: {
          tooltip: {
            callbacks: {
              label: (ctx) => `${ctx.dataset.label}: ${ctx.raw.y}점`
            }
          }
        }
      }
    });
  }

  // 해석 토글 함수
  function toggleDetail(id) {
    const detailRow = document.getElementById(id);
    if (detailRow && detailRow.style) {
      detailRow.style.display = (detailRow.style.display === 'none') ? '' : 'none';
    }
  }
</script>

</body>
</html>
