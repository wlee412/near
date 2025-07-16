// 성인 정신건강 차트 로딩 함수 (질병 필터 포함)
function loadAdultChartDataByDisease() {
    fetch('/json/mentalAdult.json')
        .then(response => response.json())
        .then(data => {
            // 연령 필터: 20세 이상
            const ageGroups = [
                "20~29세", "30~39세", "40~49세", 
                "50~59세", "60~69세", "70~79세",
                "80~89세", "90~99세", "100세 이상"
            ];

            // 사용자가 선택한 질병명 가져오기
            const selectedDisease = document.getElementById("diseaseSelect").value;

            // 연령 + 질병 조건 필터링
            const filtered = data.filter(item => {
                const matchAge = ageGroups.includes(item['연령군']);
                const matchDisease = selectedDisease === '전체' || item['상병구분'] === selectedDisease;
                return matchAge && matchDisease;
            });

            // 라벨은 연령순으로 고정
            const labels = ageGroups;

            // 연령별 환자수 합계 계산
            const values = labels.map(label => {
                return filtered
                    .filter(item => item['연령군'] === label)
                    .reduce((sum, item) => sum + parseInt(item['환자수'] || 0), 0);
            });

            // 기존 차트가 존재하면 제거 (중복 방지)
            if (window.mentalAdultChart) {
                window.mentalAdultChart.destroy();
            }

            // 차트 생성
            const ctx = document.getElementById('mentalAdult').getContext('2d');
            window.mentalAdultChart = new Chart(ctx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: `정신문제 호소 (${selectedDisease})`,
                        data: values,
                        backgroundColor: 'rgba(255, 99, 132, 0.4)',
                        borderColor: 'rgba(255, 99, 132, 1)',
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
            console.error('성인 차트 로딩 중 오류:', error);
        });
}
