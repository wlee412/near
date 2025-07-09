<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>예약 관리</title>
    <link rel="stylesheet" href="/css/adminReservation.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script src="/js/adminReservation.js"></script>
</head>
<body>

<div class="reservation-container">
    <h2>예약 관리</h2>
    <div class="menu-container">
    <div class="menu-box">
      <a href="/admin/adminHome">홈</a>
    </div>
    <div class="menu-box">
      <a href="/admin/adminMember">회원관리</a>
    </div>
    <div class="menu-box">
      <a href="/admin/surveyStats">통계</a>
    </div>
  </div>
  <form method="get" action="/admin/adminReservation" class="search-form">
    <select name="type">
        <option value="name" ${type == 'name' ? 'selected' : ''}>회원 이름</option>
        <option value="phone" ${type == 'phone' ? 'selected' : ''}>연락처</option>
        <option value="id" ${type == 'id' ? 'selected' : ''}>아이디</option>
    </select>
    <input type="text" name="keyword" value="${keyword}" placeholder="검색어 입력">
    <button type="submit">검색</button>
</form>
  
    <form id="bulkCancelForm" method="post">
        <table class="reservation-table">
            <thead>
                <tr>
                    <th><input type="checkbox" id="selectAll"></th>
                    <th>번호</th>
                    <th>상담사</th>
                    <th>내담자</th>
                    <th>내담자 아이디</th>
                    <th>연락처</th>
                    <th>예약일시</th>
                    <th>상태</th>
                    <th>취소</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="r" items="${reservations}">
                    <tr>
                        <td><input type="checkbox" name="reservationNo" value="${r.reservationNo}"></td>
                        <td>${r.reservationNo}</td>
                        <td>${r.counselorName}</td>
                        <td>${r.clientName}</td>
                        <td>${r.clientId}</td>
                        <td>${r.phone}</td>
                        <td>${r.start}</td>
                        <td>${r.state}</td>
                        <td>
    <c:if test="${r.state == '예약완료'}">
        <button type="button" class="cancel-btn" data-no="${r.reservationNo}">취소</button>
    </c:if>
    <c:if test="${r.state == '취소'}">
        <span style="color: gray;">취소됨</span>
    </c:if>
</td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>

        <button type="submit" class="bulk-cancel-btn">선택 취소</button>
    </form>

    <div class="pagination">
        <c:forEach var="i" begin="1" end="${totalPages}">
            <a href="?page=${i}" class="${i == currentPage ? 'active' : ''}">${i}</a>
        </c:forEach>
    </div>
</div>

</body>
</html>
