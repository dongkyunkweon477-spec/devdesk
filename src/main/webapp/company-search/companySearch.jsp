<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>

<html>
<head>
    <title>회사 검색</title>
    <script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<script src="${pageContext.request.contextPath}/js/companySearch.js"></script>
</head>
<body>

<h2>회사 검색</h2>

<%-- 검색 폼 --%>
<div id="searchForm">
    <input type="text" id="companyName" placeholder="회사명">
    <select id="companyIndustry">
        <option value="">전체 업종</option>
        <option value="IT">IT</option>
        <option value="금융">금융</option>
        <option value="제조">제조</option>
        <option value="유통">유통</option>
    </select>
    <select id="companyLocation">
        <option value="">전체 지역</option>
        <option value="서울">서울</option>
        <option value="경기">경기</option>
        <option value="인천">인천</option>
        <option value="부산">부산</option>
        <option value="대전">대전</option>
    </select>
    평점: <input type="number" id="minRating" min="0" max="5" value="0" step="0.5"> ~
    <input type="number" id="maxRating" min="0" max="5" value="5" step="0.5">
    규모 : <input type="number" id="minSize" min="0" value="0"> ~
    <input type="number" id="maxSize" min="0" value="10000">

    <button id="searchBtn">검색</button>
</div>
<%-- 검색 결과 (DAO 구현 후 request.setAttribute("companies", list)로 전달) --%>

        <table border="1" id="resultTable" style="display:none;">
            <thead>
            <tr>
                <th>회사명</th><th>업종</th><th>위치</th><th>평점</th><th>규모</th>
            </tr>
            </thead>
            <tbody id="resultBody"></tbody>
        </table>


<c:if test="${empty companies and not empty param.companyName}">
    <p>검색 결과가 없습니다.</p>
</c:if>

</body>
</html>