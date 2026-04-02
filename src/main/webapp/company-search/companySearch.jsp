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
                <option value="IT">IT</option>
                <option value="금융">금융</option>
                <option value="제조">제조</option>
                <option value="유통">유통</option>
            </select>
        </div>
        <div class="form-group">
            <label>지역</label>
            <select id="companyLocation">
                <option value="">전체</option>
                <option value="서울">서울</option>
                <option value="경기">경기</option>
                <option value="인천">인천</option>
                <option value="부산">부산</option>
                <option value="대전">대전</option>
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
        <table id="resultTable" style="display:none;">
            <thead>
            <tr>
                <th>회사명</th>
                <th>업종</th>
                <th>위치</th>
                <th>평점</th>
                <th>규모</th>
            </tr>
            </thead>
            <tbody id="resultBody"></tbody>
        </table>
    </div>
</div>

<c:if test="${empty companies and not empty param.companyName}">
    <p>검색 결과가 없습니다.</p>
</c:if>

