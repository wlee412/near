<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>상담 예약 현황</title>
  <link rel="stylesheet" href="/css/common.css" />
  <link rel="stylesheet" href="/css/counselor.css" />
</head>
<body>
<div class="wrapper">
  <%@ include file="../includes/header.jsp" %>

  <div class="counselor-container">
    <div class="mypage-title">
      <a href="/counselor/mypage"> <h2>상담사 마이페이지</h2></a>
    </div>

    <div class="mypage-body">
      <aside class="mypage-sidebar">
        <a href="/counselor/profile" class="sidebar-button">프로필</a>
        <a href="/counselor/time" class="sidebar-button">상담 가능시간 설정</a>
        <a href="/counselor/reservation" class="sidebar-button active">상담 예약현황</a>
        <a href="/vid" class="sidebar-button">상담 영상</a>
      </aside>

      <section class="main-section">
        <div class="reservation-box">
          <h3 class="section-title">상담 예약 현황</h3>

          <table class="reservation-table">
            <thead>
              <tr>
                <th><input type="checkbox" id="select-all"></th>
                <th>상태</th>
                <th>상담일시</th>
                <th>이름</th>
                <th>생년월일</th>
                <th>전화번호</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="r" items="${reservationList}">
                <tr class="reservation-row" data-id="${r.reservationNo}">
                  <td><input type="checkbox" class="row-check" data-id="${r.reservationNo}"></td>
                  <td>${r.state}</td>
                  <td><fmt:formatDate value="${r.start}" pattern="yyyy-MM-dd HH:mm" /></td>
                  <td><a href="/counselor/reservation/detail/${r.reservationNo}">${r.name}</a></td>
                  <td>${fn:substring(r.birth, 2, 4)}${fn:substring(r.birth, 5, 7)}${fn:substring(r.birth, 8, 10)} / ${r.gender}</td>
                  <td>${r.phone}</td>
                </tr>
              </c:forEach>
            </tbody>
          </table>

          <div id="loading-overlay" style="display: none;">
          <div class="spinner"></div>
          <div class="loading-text">로딩 중입니다...</div>
          </div>
          <button class="cancelSelectedBtn">예약 취소하기</button>

          <div class="pagination">
            <c:if test="${currentPage > 1}">
              <a href="?page=1&sortColumn=${sortColumn}&sortOrder=${sortOrder}" class="page-btn"><<</a>
              <a href="?page=${currentPage - 1}&sortColumn=${sortColumn}&sortOrder=${sortOrder}" class="page-btn"><</a>
            </c:if>

            <c:forEach begin="1" end="${totalPages}" var="i">
              <c:choose>
                <c:when test="${i == currentPage}">
                  <span class="current-page">${i}</span>
                </c:when>
                <c:otherwise>
                  <a href="?page=${i}&sortColumn=${sortColumn}&sortOrder=${sortOrder}" class="page-btn">${i}</a>
                </c:otherwise>
              </c:choose>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
              <a href="?page=${currentPage + 1}&sortColumn=${sortColumn}&sortOrder=${sortOrder}" class="page-btn">></a>
              <a href="?page=${totalPages}&sortColumn=${sortColumn}&sortOrder=${sortOrder}" class="page-btn">>></a>
            </c:if>
          </div>
        </div>
      </section>
    </div>
  </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
  $(document).ready(function () {
    $("#select-all").on("change", function () {
      $(".row-check").prop("checked", this.checked);
    });

    $(".cancelSelectedBtn").on("click", function () {
      const selected = $(".row-check:checked").map(function () {
        return $(this).data("id");
      }).get();

      if (selected.length && confirm("선택된 예약을 취소하시겠습니까?")) {
        $.post("/counselor/reservation/cancel", { reservationNos: selected }, function () {
          alert("예약이 취소되었습니다.");
          location.reload();
        }).fail(function (_, __, error) {
          console.error("Error:", error);
          alert("예약 취소 중 오류가 발생했습니다.");
        });
      }
    });

    $("a").on("click", function () {
      const target = $(this).attr("href");
      if (target && target !== "#") {
        $("#loading-overlay").show();
      }
    });
  });
</script>
<%@ include file="../includes/footer.jsp" %>

</body>
</html>
