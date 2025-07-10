console.log("✅ pharmacyMap.js 실행됨");

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
    center: new kakao.maps.LatLng(37.5665, 126.9780),
    level: 5
  };
  map = new kakao.maps.Map(container, options);

  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(pos => {
      const lat = pos.coords.latitude;
      const lng = pos.coords.longitude;
      const locPosition = new kakao.maps.LatLng(lat, lng);

      const marker = new kakao.maps.Marker({
        position: locPosition,
        image: new kakao.maps.MarkerImage("/images/my-location.png", new kakao.maps.Size(30, 35)),
        map: map
      });

      const circle = new kakao.maps.Circle({
        center: locPosition,
        radius: 50,
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

  loadMarkers();

  const myLocationBtn = document.getElementById("goMyLocationBtn");
  if (myLocationBtn) {
    myLocationBtn.addEventListener("click", function () {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
          const lat = position.coords.latitude;
          const lng = position.coords.longitude;
          const loc = new kakao.maps.LatLng(lat, lng);

          map.setCenter(loc);
          map.setLevel(2);
        });
      } else {
        alert("이 브라우저는 위치 정보를 지원하지 않습니다.");
      }
    });
  }
}

function loadMarkers() {
	showLoading();	
	
  const name = encodeURIComponent(document.getElementById("searchName").value);
  const area = encodeURIComponent(document.getElementById("searchArea").value);

  fetch(`/api/pharmacies/list?name=${name}&area=${area}`)
    .then(res => res.json())
    .then(data => {
      markers.forEach(m => m.setMap(null));
      markers = [];

      const listContainer = document.getElementById("pharmacyList");
      listContainer.innerHTML = "";

      data.forEach(p => {
        if (!p.lat || !p.lng) return;

        const marker = new kakao.maps.Marker({
          position: new kakao.maps.LatLng(p.lat, p.lng),
          image: new kakao.maps.MarkerImage("/images/pharmacy-marker.png", new kakao.maps.Size(24.5, 37.5)),
          map: map
        });

        const content = `
          <div style="font-size:13px; padding:5px; width:220px;">
            <b>${p.name}</b><br/>
            📍 ${p.address}<br/>
            ☎ ${p.tel || '-'}<br/><br/>
            <button onclick="addFavorite('${p.id}', '${p.name}')">🧡 즐겨찾기</button>
            <button onclick="goToMap('${p.address}')">📍 길찾기</button>
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
        item.textContent = `${p.name}`;
        item.addEventListener("click", () => {
          map.setCenter(new kakao.maps.LatLng(p.lat, p.lng));
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

function addFavorite(pharmId, pharmName) {
  const clientId = window.clientId;
  if (!clientId || clientId === 'null') {
    alert("로그인이 필요합니다.");
    return;
  }

  fetch('/favorite/add', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ clientId, pharmId })
  })
    .then(res => res.text())
    .then(msg => alert(msg));
}

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