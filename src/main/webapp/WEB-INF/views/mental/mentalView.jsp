<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>정신건강 통계</title>
    <style>
        table {
            width: 80%;
            margin: 20px auto;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid #ccc;
            padding: 8px;
            text-align: center;
        }
        th {
            background-color: #f2f2f2;
        }
    </style>
</head>
<body>
    <h2 style="text-align:center;">정신건강 통계 데이터</h2>

    <table>
        <thead>
            <tr>
                <th>제목</th>
                <th>문제유형</th>
                <th>년도</th>
                <th>학교급</th>
                <th>X Index</th>
                <th>Y Index</th>
                <th>값</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${mentalList}">
                <tr>
                    <td>${item.chtTtlNm}</td>
                    <td>${item.chtSeNm}</td>
                    <td>${item.chtYCn}</td>
                    <td>${item.chtXCn}</td>
                    <td>${item.xIdx}</td>
                    <td>${item.yIdx}</td>
                    <td>${item.chtVl}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>