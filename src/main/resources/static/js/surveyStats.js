document.addEventListener("DOMContentLoaded", function () {
  const tabButtons = document.querySelectorAll(".tab-button");
  const tabContents = document.querySelectorAll(".tab-content");

  let symptomChart = null;
  let riskChart = null;

  tabButtons.forEach((btn) => {
    btn.addEventListener("click", () => {
      tabButtons.forEach((b) => b.classList.remove("active"));
      tabContents.forEach((c) => (c.style.display = "none"));

      btn.classList.add("active");
      const activeTab = document.getElementById("tab-" + btn.dataset.tab);
      activeTab.style.display = "block";

      // 탭 전환 후 차트 리사이즈
      if (btn.dataset.tab === "risk" && riskChart) {
        riskChart.resize();
      } else if (btn.dataset.tab === "symptom" && symptomChart) {
        symptomChart.resize();
      }
    });
  });

  // ✅ 고위험군 통계
  if (typeof highRiskStats !== "undefined" && highRiskStats.length > 0) {
    const ctx1 = document.getElementById("highRiskChart").getContext("2d");
    riskChart = new Chart(ctx1, {
      type: "bar",
      data: {
        labels: highRiskStats.map((d) => d.name),
        datasets: [{
          label: "고위험군 비율 (%)",
          data: highRiskStats.map((d) => d.rate),
          backgroundColor: "rgba(93, 174, 197, 0.6)",
          borderColor: "rgba(93, 174, 197, 1)",
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
		indexAxis: "y",
        scales: {
			x: {
			       beginAtZero: true,
			       max: 100,
			       title: {
			         display: true,
			         text: "비율 (%)"
			}
			},
          y: {
            title: {
              display: false,
  
            }
          }
        }
      }
    });
  }

  // ✅ 증상별 평균
  if (typeof symptomAvgStats !== "undefined" && symptomAvgStats.length > 0) {
    const ctx2 = document.getElementById("symptomAvgChart").getContext("2d");
    symptomChart = new Chart(ctx2, {
      type: "bar",
      data: {
        labels: symptomAvgStats.map((d) => d.name),
        datasets: [{
          label: "평균 점수",
          data: symptomAvgStats.map((d) => d.avg),
          backgroundColor: "rgba(255, 99, 132, 0.6)",
          borderColor: "rgba(255, 99, 132, 1)",
          borderWidth: 1
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
		indexAxis: "y",
        scales: {
			x: {
			       beginAtZero: true,
			       max: 100,
			       title: {
			         display: true,
			         text: "점수"
			}
			},
          y: {
            title: {
              display: false,
            }
          }
        }
      }
    });
  }
});
