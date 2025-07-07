// 차트 불러오기 함수
function loadChartData() {
    fetch(`/mental/chart-data/total`)  // 여기서 type을 "total"로 고정
        .then(res => res.json())
        .then(data => {
            const labels = data.map(item => item.chtXCn); // 예: '고등학생', '대학생'
            const values = data.map(item => parseFloat(item.chtVl)); // 수치

            const ctx = document.getElementById('mentalChart').getContext('2d');

            if (window.myChart) {
                window.myChart.destroy(); // 기존 차트 제거
            }

            window.myChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '정신건강 이슈',
                        data: values,
                        backgroundColor: 'rgba(75, 192, 192, 0.4)',
                        borderColor: 'rgba(75, 192, 192, 1)',
                        borderWidth: 1
                    }]
                },
                options: {
                    responsive: true,
                    scales: {
                        y: {
                            beginAtZero: true
                        }
                    }
                }
            });
        })
        .catch(error => {
            console.error("차트 데이터 로드 실패:", error);
        });
}

// 모달 열기
function openModal() {
    document.getElementById('chartModal').style.display = 'block';
    loadChartData(); // ← type 없이 호출 (total로 고정됨)
}

// 모달 닫기
function closeModal() {
    document.getElementById('chartModal').style.display = 'none';
}
