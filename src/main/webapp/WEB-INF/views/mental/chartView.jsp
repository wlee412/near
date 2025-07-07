<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/includes/header.jsp" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mental-chart.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<div class="content-wrapper">
    <h2 class="chart-title">2010-2023 연령대별 정신건강 이슈</h2>
    <div class="chart-container">
        <canvas id="mentalChart"></canvas>
    </div>
</div>

<%@ include file="/includes/footer.jsp" %>

<script>
fetch('/mental/chart-data/total')
    .then(response => response.json())
    .then(data => {
        const labels = data.map(item => item.chtXCn); // 연령대
        const values = data.map(item => parseFloat(item.chtVl)); // 수치

        const ctx = document.getElementById('mentalChart').getContext('2d');
        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: '연령대별 정신건강 이슈',
                    data: values,
                    backgroundColor: 'rgba(93, 174, 197, 0.6)',
                    borderColor: 'rgba(93, 174, 197, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                scales: {
                    y: { beginAtZero: true }
                },
                plugins: {
                    legend: { display: false }
                }
            }
        });
    })
    .catch(error => {
        console.error("차트 데이터 로드 실패:", error);
    });
</script>
