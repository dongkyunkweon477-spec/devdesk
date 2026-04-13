<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="../css/board-all.css">
<link rel="stylesheet" href="../css/report/report.css">

<div class="board-container">
    <div class="board-header">
        <h2>신고 관리</h2>

        <div class="board-actions">
            <form action="report" method="get" class="search-form">
                <select name="searchType" class="search-type">
                    <option value="repoContent" ${param.searchType == 'repoContent' ? 'selected' : ''}>신고 내용</option>
                    <option value="repoReason"  ${param.searchType == 'repoReason'  ? 'selected' : ''}>신고 사유</option>
                </select>
                <input type="text" name="keyword" class="search-input" placeholder="검색어를 입력하세요"
                       value="${param.keyword}">
                <button type="submit" class="search-btn">검색</button>
                <c:if test="${not empty param.keyword}">
                    <button type="button" class="reset-btn" onclick="location.href='report'">목록으로</button>
                </c:if>
            </form>

            <%-- 신고 대상 유형 필터 --%>
            <select name="targetType" onchange="location.href='report?targetType=' + this.value + '&status=${param.status}&searchType=${param.searchType}&keyword=${param.keyword}'">
                <option value="ALL"    ${param.targetType == 'ALL'    or empty param.targetType ? 'selected' : ''}>전체 유형</option>
                <option value="review" ${param.targetType == 'review' ? 'selected' : ''}>리뷰</option>
                <option value="board"  ${param.targetType == 'board'  ? 'selected' : ''}>게시글</option>
            </select>

            <%-- REPO_STATUS 필터 --%>
            <select name="status" onchange="location.href='report?status=' + this.value + '&targetType=${param.targetType}&searchType=${param.searchType}&keyword=${param.keyword}'">
                <option value="ALL"       ${param.status == 'ALL'       or empty param.status ? 'selected' : ''}>전체 상태</option>
                <option value="PENDING"   ${param.status == 'PENDING'   ? 'selected' : ''}>미처리</option>
                <option value="COMPLETED" ${param.status == 'COMPLETED' ? 'selected' : ''}>처리완료</option>
            </select>
        </div>
    </div>

    <div class="report-list">
        <c:choose>
            <c:when test="${not empty reports}">
                <c:forEach var="r" items="${reports}">
                    <div class="board-row" onclick="location.href='${pageContext.request.contextPath}/reportDetail?id=${r.reportId}'">
                        <%-- 신고 대상 유형 배지: reviewId 있으면 리뷰, boardId 있으면 게시글 --%>
                        <c:choose>
                            <c:when test="${r.repoReviewId > 0}">
                                <div class="report-type-badge review">리뷰</div>
                            </c:when>
                            <c:otherwise>
                                <div class="report-type-badge board">게시글</div>
                            </c:otherwise>
                        </c:choose>

                        <%-- 신고 사유 --%>
                        <div class="report-reason-badge"><c:out value="${r.repoReason}"/></div>

                        <%-- 신고 내용 요약 --%>
                        <div class="col-title"><c:out value="${r.repoContent}"/></div>

                        <%-- 날짜 + 처리 상태 --%>
                        <div class="col-date">
                            <div class="date-info">${r.repoCreated}</div>
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

    <div class="pagination">
        <c:if test="${currentPage > 1}">
            <a href="report?p=${currentPage - 1}&targetType=${param.targetType}&status=${param.status}&searchType=${param.searchType}&keyword=${param.keyword}"
               class="page-btn">이전</a>
        </c:if>

        <c:forEach begin="1" end="${totalPage}" var="i">
            <c:choose>
                <c:when test="${i == currentPage}">
                    <span class="current-page">[${i}]</span>
                </c:when>
                <c:otherwise>
                    <a href="report?p=${i}&targetType=${param.targetType}&status=${param.status}&searchType=${param.searchType}&keyword=${param.keyword}"
                       class="page-link">${i}</a>
                </c:otherwise>
            </c:choose>
        </c:forEach>

        <c:if test="${currentPage < totalPage}">
            <a href="report?p=${currentPage + 1}&targetType=${param.targetType}&status=${param.status}&searchType=${param.searchType}&keyword=${param.keyword}"
               class="page-btn">다음</a>
        </c:if>
    </div>
</div>
