console.log("✅ hospitalMap.js 실행됨");

let map;
let markers = [];
let openInfoWindow = null;
let openMarker = null;

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initMap);
} else {
  initMap();
}

function initMap() {
  console.log("✅ DOM 로드 완료");

  const container = document.getElementById('map');
  const options = {
    center: new kakao.maps.LatLng(37.5665, 126.9780), // 서울 시청
    level: 5
  };
  map = new kakao.maps.Map(container, options);

  // ✅ 내 위치 마커 및 원
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(pos => {
      const lat = pos.coords.latitude;
      const lng = pos.coords.longitude;
      const locPosition = new kakao.maps.LatLng(lat, lng);

      new kakao.maps.Marker({
        position: locPosition,
        image: new kakao.maps.MarkerImage("/images/my-location.png", new kakao.maps.Size(30, 35)),
        map: map
      });

      new kakao.maps.Circle({
        center: locPosition,
        radius: 100,
        strokeWeight: 2,
        strokeColor: '#007aff',
        strokeOpacity: 0.8,
        fillColor: '#007aff',
        fillOpacity: 0.2,
        map: map
      });

      map.setCenter(locPosition);
      map.setLevel(2);
    });
  }

  // ✅ 초기 병원 마커 로드
  showLoading();
  loadMarkers();

  // ✅ "내 위치로" 버튼 이벤트
  const myLocationBtn = document.getElementById("goMyLocationBtn");
  if (myLocationBtn) {
    myLocationBtn.addEventListener("click", () => {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(pos => {
          const loc = new kakao.maps.LatLng(pos.coords.latitude, pos.coords.longitude);
          map.setCenter(loc);
          map.setLevel(2);
        });
      } else {
        alert("이 브라우저는 위치 정보를 지원하지 않습니다.");
      }
    });
  }
}

// ✅ 병원 마커 불러오기
function loadMarkers() {
  showLoading();

  const name = encodeURIComponent(document.getElementById("searchName").value);
  const area = encodeURIComponent(document.getElementById("searchArea").value);
  const type = encodeURIComponent(document.getElementById("typeFilter").value);

  fetch(`/api/hospitals/list?name=${name}&area=${area}&type=${type}`)
    .then(res => res.json())
    .then(data => {
      markers.forEach(m => m.setMap(null));
      markers = [];

      const listContainer = document.getElementById("hospitalList");
      listContainer.innerHTML = "";

      data.forEach(h => {
        if (!h.lat || !h.lng) return;

        const marker = new kakao.maps.Marker({
          position: new kakao.maps.LatLng(h.lat, h.lng),
          image: new kakao.maps.MarkerImage("/images/hospital-marker.png", new kakao.maps.Size(24.5, 37.5)),
          map: map
        });

		const content = `
		  <div class="infowindow-box">
		    <b>${h.name}</b><br/>
		    ${h.address}<br/>
		    ☎ ${h.tel || '-'}<br/>
		    병원종류: ${h.type || '-'}<br/><br/>
		    <button onclick="addFavorite('${h.id}', '${h.name}')">
		      <img src="/images/heart.png" alt="즐겨찾기"
		           style="width:16px; height:14px; vertical-align:middle; margin-right:5px; position:relative; top:-1px;">
		      즐겨찾기
		    </button>
		    <button onclick="goToMap('${h.address}')">
		      <img src="/images/my-location.png" alt="길찾기"
		           style="width:14px; height:16px; vertical-align:middle; margin-right:5px; position:relative; top:-1px;">
		      길찾기
		    </button>
		  </div>`;

        const infoWindow = new kakao.maps.InfoWindow({ content });

        kakao.maps.event.addListener(marker, 'click', () => {
          if (openMarker === marker) {
            infoWindow.close();
            openInfoWindow = null;
            openMarker = null;
          } else {
            if (openInfoWindow) openInfoWindow.close();
            infoWindow.open(map, marker);
            openInfoWindow = infoWindow;
            openMarker = marker;
          }
        });

        markers.push(marker);

        const item = document.createElement("div");
        item.className = "item";
        item.textContent = `${h.name} (${h.type || '종류 없음'})`;
        item.addEventListener("click", () => {
          map.setCenter(new kakao.maps.LatLng(h.lat, h.lng));
          map.setLevel(3);
          if (openInfoWindow) openInfoWindow.close();
          infoWindow.open(map, marker);
          openInfoWindow = infoWindow;
          openMarker = marker;
        });

        listContainer.appendChild(item);
      });
    })
    .catch(err => {
      console.error("❌ 병원 정보 로딩 에러:", err);
    })
    .finally(() => {
      hideLoading();
    });
}

// ✅ 즐겨찾기 추가
function addFavorite(hospId, hospName) {
  const clientId = window.clientId;
  if (!clientId || clientId === 'null') {
    alert("로그인이 필요합니다.");
    return;
  }

  fetch('/favorite/add', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ clientId, hospId })
  })
    .then(res => res.text())
    .then(msg => alert(msg));
}

// ✅ 외부 지도 이동
function goToMap(address) {
  const encoded = encodeURIComponent(address);
  window.open(`https://map.kakao.com/?q=${encoded}`, "_blank");
}

// ✅ 로딩 오버레이 제어
function showLoading() {
  const overlay = document.getElementById("loadingOverlay");
  if (overlay) overlay.style.display = "flex";
}

function hideLoading() {
  const overlay = document.getElementById("loadingOverlay");
  if (overlay) overlay.style.display = "none";
}