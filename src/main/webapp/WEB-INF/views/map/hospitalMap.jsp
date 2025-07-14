<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>병원 지도</title>
  <script type="text/javascript" src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=fbeb1de12354ca4a38d48cbfbb131e4c&libraries=services&autoload=false"></script>
<script>
  // ✅ 이제 정상 작동됨
  kakao.maps.load(() => {
    const script = document.createElement("script");
    script.src = "/js/hospitalMap.js";
    document.body.appendChild(script);
  });
</script>

  <!-- 로그인 세션 전달 (필요 시) -->
  <script>
    window.clientId = "${sessionScope.clientId}";
  </script>

  <!-- ✅ 외부 CSS 적용 -->
  <link rel="stylesheet" href="<c:url value='/css/hospitalMap.css' />">
  <link rel="stylesheet" href="/css/common.css" />
</head>


<body>

<!-- ✅ 전체 로딩 오버레이 -->
<div id="loadingOverlay" class="loading-overlay">
  <div class="spinner"></div>
  <div class="loading-text">Loading...</div>
</div>

<jsp:include page="/WEB-INF/views/includes/header.jsp" />

<div class="content-wrapper">

<div class="container">
  <!-- 탭 영역 -->
  <!-- 탭 -->
  <div class="tab-header">
    <a href="/hospital/map" class="tab" >병원</a> /
    <a href="/pharmacy/map" class="tab active">약국</a>
  </div>

  <!-- 필터 -->
  <div class="filter-bar">
    <input type="text" id="searchName" placeholder="병원명">
    <input type="text" id="searchArea" placeholder="지역 (예: 강남구)">
    <select id="typeFilter">
      <option value="">전체 병원 종류</option>
      <option value="의원">의원</option>
      <option value="종합병원">종합병원</option>
      <option value="상급종합">상급종합</option>
      <option value="정신병원">정신병원</option>
      <option value="병원">병원</option>
      <option value="요양병원">요양병원</option>
    </select>
    <button onclick="loadMarkers()">🔍 검색</button>
    
        <!-- ✅ 내 위치로 이동 버튼 -->
  <button id="goMyLocationBtn">📍 내 위치로</button>
  </div>

  <!-- 지도 + 리스트 -->
  <div class="main-content">
    <div id="map"></div>
    <div id="hospitalList"></div>
  </div>
</div>



</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
</body>
</html>