// 청소년 정신건강 차트 (young)
fetch('/chart-data/young')
  .then(res => res.json())
  .then(data => {
    const labels = data.map(d => d.chtTtlNm + ' - ' + d.chtXCn);
    const values = data.map(d => parseFloat(d.chtVl));

    new Chart(document.getElementById('mentalChartYoung'), {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: '청소년 정신건강 지표',
          data: values,
          backgroundColor: '#5daec5'
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { display: false },
          title: {
            display: false
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              precision: 0
            }
          }
        }
      }
    });
  })
  .catch(err => console.error('청소년 차트 오류:', err));


// 대학생 이상 정신건강 차트 (old)
fetch('/chart-data/old')
  .then(res => res.json())
  .then(data => {
    const labels = data.map(d => d.chtTtlNm + ' - ' + d.chtXCn);
    const values = data.map(d => parseFloat(d.chtVl));

    new Chart(document.getElementById('mentalChartOld'), {
      type: 'bar',
      data: {
        labels: labels,
        datasets: [{
          label: '대학생 이상 정신건강 지표',
          data: values,
          backgroundColor: '#4a9cb1'
        }]
      },
      options: {
        responsive: true,
        plugins: {
          legend: { display: false },
          title: {
            display: false
          }
        },
        scales: {
          y: {
            beginAtZero: true,
            ticks: {
              precision: 0
            }
          }
        }
      }
    });
  })
  .catch(err => console.error('대학생 차트 오류:', err));
