<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>${survey.surveyName} 설문</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/startSurvey.css">
</head>
<body>
  <div class="survey-wrapper">
    <h2>${survey.surveyName}</h2>
    <p>${survey.desc}</p>

    <form action="${pageContext.request.contextPath}/survey/submit" method="post">
      <input type="hidden" name="surveyId" value="${survey.surveyId}" />

      <c:forEach var="q" items="${questions}">
        <div class="question-block">
          <p><strong>Q${q.QNum}. ${q.question}</strong></p>
          <div class="answer-options">
            <label><input type="radio" name="q_${q.QNum}" value="0" required> 전혀 아니다</label>
            <label><input type="radio" name="q_${q.QNum}" value="1"> 아니다</label>
            <label><input type="radio" name="q_${q.QNum}" value="2"> 보통이다</label>
            <label><input type="radio" name="q_${q.QNum}" value="3"> 그렇다</label>
            <label><input type="radio" name="q_${q.QNum}" value="4"> 매우 그렇다</label>
          </div>
        </div>
      </c:forEach>

      <div class="form-group">
        <button type="submit">제출하기</button>
      </div>
    </form>
  </div>
</body>
</html>
