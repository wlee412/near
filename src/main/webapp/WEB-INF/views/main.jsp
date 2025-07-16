<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8" />
<title>n:ear</title>
<link rel="stylesheet" href="/css/common.css" />

<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body>

   <c:if test="${not empty message}">
        <script>
            alert("${message}");  // 상담하기 회원접근
            window.location.href = "/";  
        </script>
    </c:if>
    
	<div class="wrapper">
		<%@ include file="includes/header.jsp"%>


    
		<section class="main-banner">
			<h1>가까이 귀 기울이는 누군가, n:ear</h1>
			<p>당신의 마음을 편안히 들어주는 온라인 화상상담 플랫폼</p>
			<button onclick="window.location.href='/reservation'">상담 예약하기</button>
		</section>
	</div>

	<%@ include file="includes/footer.jsp"%>
</body>
</html>

