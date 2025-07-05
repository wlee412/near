<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>정신건강 대시보드</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            padding: 20px;
            background-color: #f5f5f5;
        }

        h2 {
            margin-top: 40px;
        }

        .chart-section {
            display: flex;
            gap: 30px;
            margin-bottom: 50px;
        }

        .chart-box {
            flex: 1;
            background-color: white;
            padding: 20px;
            border-radius: 16px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }

        .chart-box canvas {
            width: 100%;
            height: 300px;
        }
    </style>
</head>
<body>

    <h1>🧠 정신건강 대시보드</h1>

    <!-- 섹션 1: 청소년 -->
    <h2>📊 청소년 정신건강</h2>
    <div class="chart-section">
        <div class="chart-box">
            <canvas id="youngChart"></canvas>
        </div>
    </div>

    <!-- 섹션 2: 대학생 이상 -->
    <h2>📊 대학생 이상 정신건강</h2>
    <div class="chart-section">
        <div class="chart-box">
            <canvas id="oldChart"></canvas>
        </div>
    </div>

    <script>
        // 청소년 차트 데이터
        fetch('/mental/chart-data/young')
            .then(res => res.json())
            .then(data => {
                const labels = data.map(item => item.chtXCn);
                const values = data.map(item => parseFloat(item.chtVl));

                new Chart(document.getElementById('youngChart'), {
                    type: 'bar',
                    data: {
                        labels: labels,
                        datasets: [{
                            label: '청소년 정신건강 지표',
                            data: values,
                            backgroundColor: 'rgba(75, 192, 192, 0.6)',
                            borderRadius: 8
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: { legend: { display: true } }
                    }
                });
            });

        // 대학생 이상 차트 데이터
        fetch('/mental/chart-data/old')
            .then(res => res.json())
            .then(data => {
                const labels = data.map(item => item.chtXCn);
                const values = data.map(item => parseFloat(item.chtVl));

                new Chart(document.getElementById('oldChart'), {
                    type: 'bar',
                    data: {
                        labels: labels,
                        datasets: [{
                            label: '대학생 이상 정신건강 지표',
                            data: values,
                            backgroundColor: 'rgba(255, 99, 132, 0.6)',
                            borderRadius: 8
                        }]
                    },
                    options: {
                        responsive: true,
                        plugins: { legend: { display: true } }
                    }
                });
            });
    </script>

</body>
</html>
