console.log("✅ pharmacyMap.js 실행됨");

let map;
let markers = [];
let openInfoWindow = null;
let openMarker = null;
let myLocationMarker = null;
let myLocationCircle = null;

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
      setMyLocation(locPosition);
      map.setCenter(locPosition);
      map.setLevel(2);
      loadMarkers(); // ✅ 초기 로드 시 호출
    });
  }

  const myLocationBtn = document.getElementById("goMyLocationBtn");
  if (myLocationBtn) {
    myLocationBtn.addEventListener("click", function () {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (position) {
          const lat = position.coords.latitude;
          const lng = position.coords.longitude;
          const loc = new kakao.maps.LatLng(lat, lng);
          setMyLocation(loc);
          map.setCenter(loc);
          map.setLevel(2);
          loadMarkers(); // ✅ 위치 변경 시 다시 로드
        });
      } else {
        alert("이 브라우저는 위치 정보를 지원하지 않습니다.");
      }
    });
  }

  kakao.maps.event.addListener(map, 'click', function (mouseEvent) {
    const latlng = mouseEvent.latLng;
    console.log("🖱️ 지도 클릭 위치:", latlng.getLat(), latlng.getLng());
    setMyLocation(latlng);
    map.setCenter(latlng);
    loadMarkers(); // ✅ 클릭 시 로드
  });
}

function setMyLocation(latlng) {
  if (myLocationMarker) myLocationMarker.setMap(null);
  if (myLocationCircle) myLocationCircle.setMap(null);

  myLocationMarker = new kakao.maps.Marker({
    position: latlng,
    image: new kakao.maps.MarkerImage("/images/my-location.png", new kakao.maps.Size(30, 35)),
    map: map
  });

  myLocationCircle = new kakao.maps.Circle({
    center: latlng,
    radius: 100,
    strokeWeight: 2,
    strokeColor: '#007aff',
    strokeOpacity: 0.8,
    fillColor: '#007aff',
    fillOpacity: 0.2,
    map: map
  });
}

function searchAddress() {
  const addr = document.getElementById("addressInput").value;
  if (!addr) return alert("주소를 입력하세요!");

  const geocoder = new kakao.maps.services.Geocoder();
  geocoder.addressSearch(addr, function (result, status) {
    if (status === kakao.maps.services.Status.OK) {
      const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
      console.log("🔍 주소 검색 위치:", coords.getLat(), coords.getLng());
      setMyLocation(coords);
      map.setCenter(coords);
      loadMarkers(); // ✅ 검색 시 다시 로드
    } else {
      alert("주소를 찾을 수 없습니다.");
    }
  });
}

// ✅ 약국 마커 로드 함수 - 내 위치 기준 구 이름 필터링
function loadMarkers() {
  showLoading();

  if (!myLocationMarker) {
    console.warn("⚠️ 내 위치가 설정되지 않았습니다.");
    hideLoading();
    return;
  }

  const lat = myLocationMarker.getPosition().getLat();
  const lng = myLocationMarker.getPosition().getLng();

  const geocoder = new kakao.maps.services.Geocoder();
  geocoder.coord2RegionCode(lng, lat, function (result, status) {
    if (status === kakao.maps.services.Status.OK) {
      const gu = result.find(r => r.region_type === 'H' || r.region_type === 'B')?.region_3depth_name;
      if (!gu) {
        alert("위치의 행정구역(구)을 찾을 수 없습니다.");
        hideLoading();
        return;
      }

      const name = encodeURIComponent(document.getElementById("searchName").value);
      const area = encodeURIComponent(gu); // ✅ 구 이름으로 필터

      fetch(`/api/pharmacies/list?name=${name}&area=${area}`)
        .then(res => res.json())
        .then(data => {
          console.log("📦 약국 수:", data.length);
          renderPharmacyMarkers(data);
        })
        .catch(err => {
          console.error("❌ 약국 정보 로딩 에러:", err);
        })
        .finally(() => {
          hideLoading();
        });
    }
  });
}

// ✅ 마커 및 리스트 출력 분리 함수
function renderPharmacyMarkers(data) {
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
      <div class="infowindow-box">
        <b>${p.name}</b><br/>
        ${p.address}<br/>
        ☎ ${p.tel || '-'}<br/><br/>
        <button onclick="addFavorite('${p.id}', '${p.name}')">
          <img src="/images/heart.png" alt="즐겨찾기"
            style="width:16px; height:14px; vertical-align:middle; margin-right:5px; position:relative; top:-1px;">
          즐겨찾기</button>
        <button onclick="goToMap('${p.address}')">
          <img src="/images/my-location.png" alt="길찾기"
            style="width:14px; height:16px; vertical-align:middle; margin-right:5px; position:relative; top:-1px;">
          길찾기</button>
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
}

//function addFavorite(pharmId, pharmName) {
//  const clientId = window.clientId;
//  if (!clientId || clientId === 'null') {
//    alert("로그인이 필요합니다.");
//    return;
//  }
//
//  fetch('/favorite/add', {
//    method: 'POST',
//    headers: { 'Content-Type': 'application/json' },
//    body: JSON.stringify({ clientId, pharmId })
//  })
//    .then(res => res.text())
//    .then(msg => alert(msg));
//}

// 즐겨찾기 추가
function addFavorite(pharmId, pharmName) {

    // 서버에서 세션으로 clientId 판단하므로 따로 보낼 필요 없음

    fetch('/api/favorite/pharm/add?pharmId=' + encodeURIComponent(pharmId), {

      method: 'POST'

    })

      .then(res => {

        if (!res.ok) {

          return res.text().then(text => { throw new Error(text); });

        }

        return res.text();

      })

      .then(msg => alert(msg))

      .catch(err => alert("⚠️ " + err.message));

  }

function goToMap(address) {
  const encoded = encodeURIComponent(address);
  window.open(`https://map.kakao.com/?q=${encoded}`, "_blank");
}

function showLoading() {
  const overlay = document.getElementById("loadingOverlay");
  if (overlay) overlay.style.display = "flex";
}

function hideLoading() {
  const overlay = document.getElementById("loadingOverlay");
  if (overlay) overlay.style.display = "none";
}