<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>신고 관리</title>
    <link rel="stylesheet" href="../css/board-all.css">
    <link rel="stylesheet" href="../css/report/report.css">
</head>
<body>
<div class="board-container">
    <div class="board-header">
        <h2>신고 관리</h2>

        <div class="board-actions">
            <form action="report" method="get" class="search-form">
                <select name="searchType" class="search-type">
                    <option value="content" ${param.searchType == 'content' ? 'selected' : ''}>신고 내용</option>
                    <option value="reporter" ${param.searchType == 'reporter' ? 'selected' : ''}>신고자</option>
                </select>
                <input type="text" name="keyword" class="search-input" placeholder="검색어를 입력하세요" value="${param.keyword}">
                <button type="submit" class="search-btn">검색</button>
            </form>

            <select name="status" onchange="location.href='report?status=' + this.value">
                <option value="ALL" ${param.status == 'ALL' ? 'selected' : ''}>전체 상태</option>
                <option value="PENDING" ${param.status == 'PENDING' ? 'selected' : ''}>미처리</option>
                <option value="COMPLETED" ${param.status == 'COMPLETED' ? 'selected' : ''}>처리완료</option>
            </select>
        </div>
    </div>

    <div class="report-list">
        <c:choose>
            <c:when test="${not empty reports}">
                <c:forEach var="r" items="${reports}">
                    <div class="board-row" onclick="location.href='reportDetail?id=${r.reportId}'">
                        <div class="report-type-badge post">리뷰</div>
                        <div class="col-title"><c:out value="${r.repoContent}"/></div>
                        <div class="report-col-reporter">회원 ID: ${r.repoMemberId}</div>
                        <div class="col-date">
                            <div class="date-info">${r.reopCreated}</div>
                            <div class="report-status-badge ${r.repoStatus == 'PENDING' ? 'pending' : 'done'}">
                                ${r.repoStatus == 'PENDING' ? '미처리' : '처리완료'}
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <div style="text-align: center; padding: 40px; color: #888;">
                    신고 내역이 없습니다.
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</body>
</html>
