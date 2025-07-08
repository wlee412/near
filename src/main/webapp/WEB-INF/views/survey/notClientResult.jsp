<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>검사 결과</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/survey.css">
  <style>
    body {
      font-family: 'Apple SD Gothic Neo', 'Noto Sans KR', sans-serif;
      background-color: #ffffff;
      color: #222;
      margin: 0;
      padding: 0;
    }

    .result-wrapper {
      max-width: 700px;
      margin: 60px auto;
      padding: 30px 40px;
      background-color: #f9f9f9;
      border: 2px solid #5daec5;
      border-radius: 16px;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
    }

    .result-wrapper h2 {
      font-size: 28px;
      margin-bottom: 20px;
      color: #5daec5;
      text-align: center;
    }

    .score-box {
      font-size: 20px;
      font-weight: bold;
      margin-bottom: 20px;
      text-align: center;
      color: #222;
    }

    .feedback-box {
      font-size: 17px;
      line-height: 1.6;
      background-color: #e8f6fb;
      border-left: 5px solid #5daec5;
      padding: 20px;
      margin-bottom: 30px;
      border-radius: 8px;
      color: #222;
    }

    .history-link {
      text-align: center;
    }

    .history-link a {
      text-decoration: none;
      color: white;
      background-color: #5daec5;
      padding: 10px 24px;
      border-radius: 6px;
      font-weight: bold;
      transition: background-color 0.3s ease;
    }

    .history-link a:hover {
      background-color: #4897b0;
    }
  </style>
</head>
<body>
  <div class="result-wrapper">
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
</body>
</html>
