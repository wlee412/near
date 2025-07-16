<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>심리검사 목록</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/common.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/selfSurveyList.css">
</head>

<body>
  <div class="wrapper">
	<%@ include file="../includes/header.jsp" %>
    
    <ul class="survey-list">
      <c:forEach var="survey" items="${surveyList}">
        <li class="survey-item">
          <div class="survey-content">
            <div class="survey-info">
              <h3>${survey.surveyName}</h3>
              <p>${survey.desc}</p>
            </div>
            <form action="${pageContext.request.contextPath}/survey/start" method="get" class="survey-form">
              <input type="hidden" name="surveyId" value="${survey.surveyId}" />
              <button type="submit">검사 시작</button>
            </form>
          </div>
        </li>
      </c:forEach>
    </ul>
	<%@ include file="../includes/footer.jsp" %>
  </div>

</body>
</html>
