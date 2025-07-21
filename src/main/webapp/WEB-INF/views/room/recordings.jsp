<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>녹화 다운로드</title>
<script src="https://code.jquery.com/jquery-latest.js"></script>
<link rel="stylesheet" href="/css/common.css" type="text/css" />
<link rel="stylesheet" href="/css/recordings.css" type="text/css" />
</head>
<body>
	<div class="wrapper">
		<jsp:include page="/WEB-INF/views/includes/header.jsp" flush="true" />
		<div class="container">
			<h2>상담 녹화 영상</h2>
			<div id="search-group" align="right">
				<button type="button" class="btn" onclick="location.href='/counselor/mypage'">검색</button>
				<form method="get" action="/vid">
					<input type="date" name="startFilter" value="${param.startFilter}">
					<input type="text" name="clientName" placeholder="내담자" value="${param.clientName}">
					<button type="submit" class="btn">검색</button>
				</form>
			</div>
			<table>
				<thead>
					<tr>
						<th>내담자</th>
						<th>상담 일시</th>
						<th>다운로드</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${recordings }" var="video">
						<tr>
							<td>${video.clientName }</td>
							<td><fmt:formatDate value="${video.startDate }" pattern="yyyy-M-d HH:mm"/></td>
							<td><button id="${video.recName }" class="btn dl-btn">
							<svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round" class="lucide lucide-download-icon lucide-download"><path d="M12 15V3"/><path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/><path d="m7 10 5 5 5-5"/></svg>
							</button></td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
		<jsp:include page="/WEB-INF/views/includes/footer.jsp" flush="true" />
	</div>
	<script type="text/javascript" src="/js/recordingDL.js"></script>
</body>
</html>