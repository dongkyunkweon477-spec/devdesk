<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>지원 목록</title>

  <style>
    body {
      font-family: Arial;
      background-color: #f5f6f7;
    }
    .container {
      width: 900px;
      margin: 50px auto;
    }
    h2 {
      text-align: center;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      background: white;
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
    }
    th, td {
      padding: 12px;
      border-bottom: 1px solid #ddd;
      text-align: center;
    }
    th {
      background-color: #4CAF50;
      color: white;
    }
    tr:hover {
      background-color: #f1f1f1;
    }
    .status {
      font-weight: bold;
    }
  </style>

</head>
<body>

<div class="container">
  <h2>지원한 회사 목록</h2>

  <table>
    <tr>
      <th>회사명</th>
      <th>직무</th>
      <th>상태</th>
      <th>지원일</th>
      <th>메모</th>
      <th>삭제</th>
    </tr>




      <c:forEach var="app" items="${applicationList}">
      <tr>
        <td>${app.companyName}</td>
        <td>${app.position}</td>
        <td class="status">${app.status}</td>
        <td>${app.appDate}</td>
        <td>${app.memo}</td>

        <!-- 🔥 삭제 버튼 -->
        <td>
          <form action="application_delete" method="post" style="display:inline;">
            <input type="hidden" name="app_id" value="${app.appId}">
            <button type="submit" onclick="return confirm('삭제하시겠습니까?');">
              삭제
            </button>
          </form>
        </td>
      </tr>
    </c:forEach>

    <!-- 데이터 없을 때 -->
    <c:if test="${empty applicationList}">
      <tr>
        <td colspan="5">지원 내역이 없습니다.</td>
      </tr>
    </c:if>

  </table>
</div>

</body>
</html>