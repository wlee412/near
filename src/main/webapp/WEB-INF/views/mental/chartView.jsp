<%@ page contentType="text/html; charset=UTF-8" %>
<%@ include file="/includes/header.jsp" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/mental-chart.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<div class="content-wrapper">
    <h2 class="chart-title">2010-2023 연령대별 정신건강 이슈</h2>
    <div class="chart-container">
        <canvas id="summaryChart"></canvas>
    </div>
</div>

<%@ include file="/includes/footer.jsp" %>

<script>
    fetch('/mental/chart-data/summary')
        .then(response => response.json())
        .then(data => {
            const labels = data.map(item => item.age_group);
            const values = data.map(item => item.avg_mental_score);

            const ctx = document.getElementById('summaryChart').getContext('2d');
            new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '연령대별 평균 정신건강 점수',
                        data: values,
                        backgroundColor: 'rgba(93, 174, 197, 0.6)', // 팀 메인 포인트 색상
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
        });
</script>
