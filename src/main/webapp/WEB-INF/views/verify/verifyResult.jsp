<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>회원 인증 결과</title>
</head>
<body>

  <c:choose>
    <c:when test="${result == 1}">
      <c:choose>
        <c:when test="${type == 'MEMBER_JOIN'}">
          <script>
            alert("회원가입 인증 성공! 로그인 페이지로 이동합니다.");
            location.href = "/client/login";
          </script>
        </c:when>

        <c:when test="${type == 'FIND_ID'}">
          <script>
            alert("아이디 인증 성공! 아이디를 확인하세요.");
            location.href = "/client/showId"; //아이디 표시
          </script>
        </c:when>

        <c:when test="${type == 'FIND_PASSWORD'}">
          <script>
            alert("인증 성공! 비밀번호 재설정 페이지로 이동합니다.");
            location.href = "/client/changePassword"; //비밀번호 재설정 페이지
          </script>
        </c:when>
      </c:choose>
    </c:when>

    <c:when test="${result == -1}">
      <script>
        alert("인증 시간이 만료되었습니다. 다시 시도해주세요.");
        location.href = "/client/login";
      </script>
    </c:when>

    <c:otherwise>
      <script>
        alert("인증 실패. 인증번호를 확인해주세요.");
        location.href = "/client/login";
      </script>
    </c:otherwise>
  </c:choose>

</body>
</html>