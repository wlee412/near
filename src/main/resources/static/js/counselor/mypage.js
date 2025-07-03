
// 비동기 fetch 섹션 나누기 
function loadSection(section) {
  fetch(`/counselor/section/${section}`)
    .then(response => {
      if (!response.ok) throw new Error("불러오기 실패");
      return response.text();
    })
    .then(html => {
      document.getElementById('contentArea').innerHTML = html;
    })
    .catch(error => {
      document.getElementById('contentArea').innerHTML = "<p>오류가 발생했습니다.</p>";
      console.error(error);
    });
}

// 예약 가능시간 
fetch('/counselor/available-times', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    availableTimes: datetimes
  })
})
.then(res => res.json())
.then(data => {
  alert("저장 완료!");
})
.catch(err => {
  alert("저장 실패!");
});

