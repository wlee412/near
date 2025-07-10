<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원관리</title>
    <link rel="stylesheet" href="<c:url value='/css/adminMember.css' />">
</head>
<body>

    <h1>회원 관리</h1>
    <div class="menu-container">
    <div class="menu-box">
      <a href="/admin/adminHome">홈</a>
    </div>
    <div class="menu-box">
      <a href="/admin/surveyStats">통계</a>
    </div>
    <div class="menu-box">
      <a href="/admin/adminReservation">예약내역</a>
    </div>
  </div>

    <!-- 검색창 -->
    <div class="search-box">
        <form method="get" action="<c:url value='/admin/adminMember' />">
            <select name="type">
                <option value="all" ${param.type == 'all' ? 'selected' : ''}>전체</option>
                <option value="name" ${type == 'name' ? 'selected' : ''}>이름</option>
                <option value="email" ${type == 'email' ? 'selected' : ''}>이메일</option>
                <option value="state" ${type == 'state' ? 'selected' : ''}>상태</option>
            </select>
            <input type="text" name="keyword" placeholder="검색어" value="${keyword}">
            <button type="submit" class="button-main">검색</button>
        </form>
    </div>

    <!-- 회원 목록 -->
    <table>
        <thead>
            <tr>
                <th>번호</th>
                <th>아이디</th>
                <th>이름</th>
                <th>전화번호</th>
                <th>이메일</th>
                <th>주소</th>
                <th>상태</th>
                <th>관리</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="member" items="${memberList}" varStatus="status">
                <tr>
                    <td>${status.index + 1}</td>
                    <td>${member.clientId}</td>
                    <td>${member.name}</td>
                    <td>${member.phone}</td>
                    <td>${member.emailId}@${member.emailDomain}</td>
                    <td>${member.addrBase}</td>
                    <td>
                        <span class="state-badge state-${member.state}">
                            <c:choose>
                                <c:when test="${member.state == 0}">일반</c:when>
                                <c:when test="${member.state == 1}">탈퇴</c:when>
                                <c:when test="${member.state == 2}">블랙리스트</c:when>
                                <c:otherwise>알수없음</c:otherwise>
                            </c:choose>
                        </span>
                    </td>
                    <td>
                        <form method="post" action="<c:url value='/admin/updateState' />">
                            <input type="hidden" name="client_id" value="${member.clientId}" />
                            <select name="state">
                                <option value="0" ${member.state == 0 ? 'selected' : ''}>일반</option>
                                <option value="1" ${member.state == 1 ? 'selected' : ''}>탈퇴</option>
                                <option value="2" ${member.state == 2 ? 'selected' : ''}>블랙</option>
                            </select>
                            <button type="submit" class="change-btn">변경</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <!-- 페이지네이션 -->
    <div class="pagination">
        <c:forEach begin="${startPage}" end="${endPage}" var="i">
            <a href="<c:url value='/admin/adminMember?pageNum=${i}&type=${type}&keyword=${keyword}' />"
               class="${i == currentPage ? 'current' : ''}">
                ${i}
            </a>
        </c:forEach>
    </div>

</body>
<script src="<c:url value='/js/adminMember.js' />"></script>
</html>
