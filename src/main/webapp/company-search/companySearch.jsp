<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/companySearch.css">
<script src="https://code.jquery.com/jquery-3.7.1.js"></script>
<script src="${pageContext.request.contextPath}/js/companySearch.js"></script>

<div class="search-wrap">
    <h2>회사 검색</h2>

    <div class="search-form">
        <div class="form-group">
            <label>회사명</label>
            <input type="text" id="companyName" placeholder="회사명 입력">
        </div>
        <div class="form-group">
            <label>업종</label>
            <select id="companyIndustry">
                <option value="">전체</option>
                <c:forEach var="ind" items="${industries}">
                    <option value="${ind}">${ind}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label>지역</label>
            <select id="companyIndustry">
                <option value="">전체</option>
                <c:forEach var="loc" items="${locations}">
                    <option value="${loc}">${loc}</option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label>평점</label>
            <div class="range-group">
                <input type="number" id="minRating" min="0" max="5" value="0" step="0.5">
                <span class="tilde">~</span>
                <input type="number" id="maxRating" min="0" max="5" value="5" step="0.5">
            </div>
        </div>
        <div class="form-group">
            <label>규모(인원)</label>
            <div class="range-group">
                <input type="number" id="minSize" min="0" value="0">
                <span class="tilde">~</span>
                <input type="number" id="maxSize" min="0" value="10000">
            </div>
        </div>
        <button id="searchBtn">검색</button>
    </div>

    <div class="result-section">
        <div id="resultArea"></div>
    </div>
</div>

<c:if test="${empty companies and not empty param.companyName}">
    <p>검색 결과가 없습니다.</p>
</c:if>

<button onclick="location.href='${pageContext.request.contextPath}/company/insert';">기업등록</button>