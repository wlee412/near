console.log("✅ hospitalMap.js 실행됨");

let map;
let markers = [];
let openInfoWindow = null;
let openMarker = null;
let myLocationMarker = null;
let myLocationCircle = null;

// ★ 변경: 현재 내 좌표에서 역지오코딩으로 얻은 "구" 이름 저장
let currentGuName = "";   // 예: "강남구"

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

  // ★ 변경: 내 위치 탐색 후 역지오코딩 -> 구 추출 -> 병원 로드
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(pos => {
      const lat = pos.coords.latitude;
      const lng = pos.coords.longitude;
      const accuracy = pos.coords.accuracy;

      console.log("📍 위도:", lat);
      console.log("📍 경도:", lng);
      console.log("📏 위치 정확도 (m):", accuracy);

      const locPosition = new kakao.maps.LatLng(lat, lng);
      setMyLocation(locPosition);
      map.setCenter(locPosition);
      map.setLevel(2);

      // ★ 변경: 좌표 → 구 이름 얻어와서 검색필드 채우고 병원 로드
      resolveGuFromCoords(lat, lng, gu => {
        currentGuName = gu || "";
        syncAreaInput(gu);  // UI에 자동입력
        showLoading();
        loadMarkers(); // 구 기반 병원 로드
      });
    });
  }

  // 내 위치 버튼
  const myLocationBtn = document.getElementById("goMyLocationBtn");
  if (myLocationBtn) {
    myLocationBtn.addEventListener("click", () => {
      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(pos => {
          const lat = pos.coords.latitude;
          const lng = pos.coords.longitude;
          const loc = new kakao.maps.LatLng(lat, lng);
          setMyLocation(loc);
          map.setCenter(loc);
          map.setLevel(2);
          // ★ 변경: 다시 구 이름 추출 후 재검색
          resolveGuFromCoords(lat, lng, gu => {
            currentGuName = gu || "";
            syncAreaInput(gu);
            loadMarkers();
          });
        });
      } else {
        alert("이 브라우저는 위치 정보를 지원하지 않습니다.");
      }
    });
  }

  // 지도 클릭 시 그 지점으로 내 위치 이동 + 구 재계산 + 병원 재로드
  kakao.maps.event.addListener(map, 'click', function(mouseEvent) {
    const latlng = mouseEvent.latLng;
    console.log("🖱️ 지도 클릭 위치:", latlng.getLat(), latlng.getLng());
    setMyLocation(latlng);
    map.setCenter(latlng);
    // ★ 변경: 클릭 지점 기준 구 이름 재계산 후 검색
    resolveGuFromCoords(latlng.getLat(), latlng.getLng(), gu => {
      currentGuName = gu || "";
      syncAreaInput(gu);
      loadMarkers();
    });
  });

  // ★ 변경: 상단 "검색" 버튼이 눌리면 입력값 기준으로 로드
  const searchBtn = document.querySelector("#searchBtn"); // id=검색 버튼에 맞게 조정
  if (searchBtn) {
    searchBtn.addEventListener("click", () => {
      // 사용자가 수동으로 구/병원명 입력 후 검색 눌렀을 경우
      loadMarkers();
    });
  }
}

// 내 위치 마커 및 원
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

// ★ 변경: 좌표 -> 구 이름 (카카오 좌표-행정구역 변환)
function resolveGuFromCoords(lat, lng, callback) {
  const geocoder = new kakao.maps.services.Geocoder();
  geocoder.coord2RegionCode(lng, lat, function(result, status) {
    if (status === kakao.maps.services.Status.OK && result.length > 0) {
      // result[0] 또는 행정구 레벨이 B, H 등 여러 건 나올 수 있음
      // region_type==="H" (행정동) / "B" (법정동). 구 단위는 2depth.
      const gu = result[0].region_2depth_name; // 예: "강남구"
      console.log("🧭 현재 구 추출:", gu);
      callback(gu);
    } else {
      console.warn("구 정보를 찾지 못했습니다.", status, result);
      callback(null);
    }
  });
}

// ★ 변경: 구 자동입력 (UI input#searchArea 값과 currentGuName 동기화)
function syncAreaInput(gu) {
  const areaInput = document.getElementById("searchArea");
  if (areaInput && gu) {
    areaInput.value = gu;
  }
}

// 병원 마커 불러오기 - ★ 변경: 반경 대신 "구(area)" 기반
function loadMarkers() {
  console.log("📡 병원 데이터 로드 시작");
  showLoading();

  const name = encodeURIComponent(document.getElementById("searchName").value.trim());
  // 사용자가 수동으로 입력했을 수도 있으므로 input에서 우선 가져오고, 없으면 currentGuName 사용
  let areaRaw = document.getElementById("searchArea").value.trim();
  const typeRaw = document.getElementById("typeFilter").value.trim();

  // ★ 변경: area 값 fallback
  if (!areaRaw && currentGuName) {
    areaRaw = currentGuName;
  }
  const area = encodeURIComponent(areaRaw);
  const type = encodeURIComponent(typeRaw);

  // 디버그
  console.log("🔁 검색 파라미터:", { name, areaRaw, typeRaw });

  // ★ 변경: 이제 /nearby 호출 안 함, /list 사용 (구 필터 기반)
  fetch(`/api/hospitals/list?name=${name}&area=${area}&type=${type}`)
    .then(res => {
      if (!res.ok) throw new Error("병원 목록 응답 실패: " + res.status);
      return res.json();
    })
    .then(data => {
      console.log("📦 응답 병원 수(구 필터):", data.length);
      console.log("📦 병원 데이터(구 필터):", data);

      // 기존 마커 제거
      markers.forEach(m => m.setMap(null));
      markers = [];

      const listContainer = document.getElementById("hospitalList");
      if (listContainer) listContainer.innerHTML = "";

      data.forEach(h => {
        // 반환 데이터 키: id, name, address, tel, dept, type, lat, lng (searchHospitals 쿼리 기준)
        const lat = h.lat ?? h.hosp_lat;
        const lng = h.lng ?? h.hosp_lng;
        if (lat == null || lng == null) return;

        const marker = new kakao.maps.Marker({
          position: new kakao.maps.LatLng(lat, lng),
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
              <img src="/images/heart.png" alt="즐겨찾기" style="width:16px; height:14px; vertical-align:middle; margin-right:5px; position:relative; top:-1px;">
              즐겨찾기
            </button>
            <button onclick="goToMap('${h.address}')">
              <img src="/images/my-location.png" alt="길찾기" style="width:14px; height:16px; vertical-align:middle; margin-right:5px; position:relative; top:-1px;">
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

        // 병원 리스트 패널
        if (listContainer) {
          const item = document.createElement("div");
          item.className = "item";
          item.textContent = `${h.name} (${h.type || '종류 없음'})`;
          item.addEventListener("click", () => {
            map.setCenter(new kakao.maps.LatLng(lat, lng));
            map.setLevel(3);
            if (openInfoWindow) openInfoWindow.close();
            infoWindow.open(map, marker);
            openInfoWindow = infoWindow;
            openMarker = marker;
          });
          listContainer.appendChild(item);
        }
      });
    })
    .catch(err => {
      console.error("❌ 병원 정보 로딩 에러:", err);
    })
    .finally(() => {
      hideLoading();
    });
}

// 주소 검색 (사용자가 직접 입력한 주소 기준 이동 + 구 재계산 + 병원 다시 로드)
function searchAddress() {
  const addr = document.getElementById("addressInput").value;
  if (!addr) return alert("주소를 입력하세요!");

  const geocoder = new kakao.maps.services.Geocoder();
  geocoder.addressSearch(addr, function(result, status) {
    if (status === kakao.maps.services.Status.OK) {
      const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
      console.log("🔍 주소 검색 위치:", coords.getLat(), coords.getLng());
      setMyLocation(coords);
      map.setCenter(coords);
      map.setLevel(2);
      // ★ 변경: 주소 → 구
      resolveGuFromCoords(coords.getLat(), coords.getLng(), gu => {
        currentGuName = gu || "";
        syncAreaInput(gu);
        loadMarkers();
      });
    } else {
      alert("주소를 찾을 수 없습니다.");
    }
  });
}

// 즐겨찾기 추가
//function addFavorite(hospId, hospName) {
//  const clientId = window.clientId;
//  if (!clientId || clientId === 'null') {
//    alert("로그인이 필요합니다.");
//    return;
//  }
//
//  fetch('/favorite/add', {
//    method: 'POST',
//    headers: { 'Content-Type': 'application/json' },
//    body: JSON.stringify({ clientId, hospId })
//  })
//    .then(res => res.text())
//    .then(msg => alert(msg));
//}

// 즐겨찾기 추가
function addFavorite(hospId, hospName) {

  // 서버에서 세션으로 clientId 판단하므로 따로 보낼 필요 없음

  fetch('/api/favorite/hosp/add?hospId=' + encodeURIComponent(hospId), {

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

// 외부 지도 이동 (변경 없음)
function goToMap(address) {
  const encoded = encodeURIComponent(address);
  window.open(`https://map.kakao.com/?q=${encoded}`, "_blank");
}

// 로딩 오버레이
function showLoading() {
  const overlay = document.getElementById("loadingOverlay");
  if (overlay) overlay.style.display = "flex";
}
function hideLoading() {
  const overlay = document.getElementById("loadingOverlay");
  if (overlay) overlay.style.display = "none";
}