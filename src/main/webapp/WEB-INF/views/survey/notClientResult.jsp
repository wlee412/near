<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>검사 결과</title>
  
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/clientResult.css">
</head>
<body class="nonClientResult">
  <div class="wrapper">
  	<%@ include file="../includes/header.jsp" %>
  
  <div class="content-wrapper">
    <h2>${surveyName} 결과</h2>

    <div class="score-box">
      총 점수: <strong>${totalScore}</strong>점
    </div>

    <div class="feedback-box">
      ${feedbackText}
    </div>

    <div class="history-link">
      <a href="${pageContext.request.contextPath}/survey/list">다른 검사하기</a>
    </div>
  </div>
  </div>
  <%@ include file="../includes/footer.jsp" %>
</body>
</html>
