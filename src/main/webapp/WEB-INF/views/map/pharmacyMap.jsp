<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>약국 지도</title>
  <script type="text/javascript" src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=fbeb1de12354ca4a38d48cbfbb131e4c&libraries=services"></script>
 <script src="/js/hospitalMap.js"></script>
  
   <script>
    window.clientId = "${sessionScope.clientId}";
  </script>

  <link rel="stylesheet" href="<c:url value='/css/pharmacyMap.css' />" />
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
  <!-- 탭 -->
  <div class="tab-header">
    <a href="/hospital/map" class="tab" style="text-decoration:none">병원</a> /
	<a href="/pharmacy/map" class="tab active" style="text-decoration:none">약국</a>
  </div>

  <!-- 필터 -->
  <div class="filter-bar">
    <input type="text" id="searchName" placeholder="약국명">
    <input type="text" id="searchArea" placeholder="지역 (예: 강남구)">
    <button onclick="loadMarkers()">
    	<img src="/images/magnifier.png" alt="검색"
  		style="width:14px; height:16px; vertical-align:middle; margin-right:5px; position:relative; top:-1px;">
    	검색
    </button>
    
    <button id="goMyLocationBtn">
    	<img src="/images/my-location.png" alt="내 위치로"
  		style="width:14px; height:16px; vertical-align:middle; margin-right:5px; position:relative; top:-1px;">
  		내 위치로
  	</button>
  </div>

  <!-- 지도 + 리스트 -->
  <div class="main-content">
    <div id="map"></div>
    <div id="pharmacyList"></div>
  </div>
</div>

</div>

<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
</body>
</html>