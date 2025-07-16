//유튜브 추천 키워드 
function openYoutubeModal() {
    document.getElementById("youtubeModal").style.display = "block";
    document.getElementById("keyword-box").innerHTML = "";
    document.getElementById("youtubeContainer").innerHTML = "";
}

function closeYoutubeModal() {
    document.getElementById("youtubeModal").style.display = "none";
}

function selectMood(mood) {
    fetch('/youtube/api/recommend?mood=' + mood)
        .then(res => res.json())
        .then(data => {
            document.getElementById("keyword-box").innerHTML =
                `추천 키워드: <strong>${data.keyword}</strong>`;

            const container = document.getElementById("youtubeContainer");
            container.innerHTML = "";

            data.videoIds.forEach(id => {
                const iframe = document.createElement('iframe');
                iframe.width = "400";
                iframe.height = "225";
                iframe.src = `https://www.youtube.com/embed/${id}`;
                iframe.frameBorder = "0";
                iframe.allowFullscreen = true;
                iframe.style.marginBottom = "10px";
                container.appendChild(iframe);
            });
        })
        .catch(() => {
            alert("추천 영상을 불러오는 데 실패했습니다.");
        });
}

// 유튜브 모달 외부 클릭 시 닫기
window.addEventListener("click", function(event) {
    if (event.target === document.getElementById("youtubeModal")) {
        closeYoutubeModal();
    }
});
