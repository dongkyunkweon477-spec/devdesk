<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/review-board.css">


<jsp:include page="/company-search/companySearch.jsp"/>

<div class="board-container">

    <c:if test="${empty reviews}">
        <div class="no-result">등록된 면접 후기가 없습니다.</div>
    </c:if>

    <%-- 임시로 5개의 항목을 반복 출력하는 부분 --%>
    <div class="review-toolbar">
        <a href="${pageContext.request.contextPath}/review/write" class="btn-write">
            + 후기 작성
        </a>
    </div>
    <c:forEach var="r" items="${reviews}">
        <div class="card">
            <div class="card-header">
                <div>
                    <c:set var="viewedReviews" value="${sessionScope.viewedReviews}"/>
                    <c:if test="${empty viewedReviews || !viewedReviews.contains(r.reviewId)}">
                        <span class="badge-new">NEW</span>
                    </c:if>
                    <a href="${pageContext.request.contextPath}/review?companyId=${r.reviewCompanyId}">
                            ${r.companyName}
                    </a>
                </div>
                <div>[북마크] ${r.reviewBookmarkCount}</div>
            </div>
            <h2 class="card-title">${r.reviewTitle} </h2>
            <div class="card-body">
                <div class="avatar"></div>
                <div class="info-grid">
                    <div class="info-row">
                        <span class="info-label">면접관/학생</span>
                        <span class="info-value">
                            면접관 ${r.reviewInterviewerCount}명
                            / 학생 ${r.reviewStudentCount}명
                        </span>
                        <span class="info-label">연락 방법</span>
                        <span class="tag">
                            <c:choose>
                                <c:when test="${r.reviewContactMethod == 'EMAIL'}">이메일</c:when>
                                <c:when test="${r.reviewContactMethod == 'PHONE'}">전화</c:when>
                                <c:when test="${r.reviewContactMethod == 'WEBSITE'}">채용 홈페이지</c:when>
                                <c:otherwise>${r.reviewContactMethod}</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                    <div class="info-row">
                        <span class="info-label">분위기</span>
                        <span class="info-value">
                            <c:choose>
                                <c:when test="${r.reviewAtmosphere == 'FRIENDLY'}">화기애애</c:when>
                                <c:when test="${r.reviewAtmosphere == 'NORMAL'}">보통</c:when>
                                <c:when test="${r.reviewAtmosphere == 'SERIOUS'}">엄숙</c:when>
                                <c:when test="${r.reviewAtmosphere == 'PRESSURE'}">압박</c:when>
                                <c:otherwise>${r.reviewAtmosphere}</c:otherwise>
                            </c:choose>
                        </span>
                        <span class="info-label">면접 유형</span>
                        <span class="tag">
                            <c:choose>
                                <c:when test="${r.reviewInterviewType == 'CODING'}">코딩테스트</c:when>
                                <c:when test="${r.reviewInterviewType == 'TECH'}">기술면접</c:when>
                                <c:when test="${r.reviewInterviewType == 'PERSONAL'}">인성면접</c:when>
                                <c:when test="${r.reviewInterviewType == 'EXEC'}">임원면접</c:when>
                                <c:when test="${r.reviewInterviewType == 'GROUP'}">그룹면접</c:when>
                                <c:otherwise>${r.reviewInterviewType}</c:otherwise>
                            </c:choose>
                        </span>
                    </div>
                </div>
            </div>

            <div class="read-more-container">
                <a href="${pageContext.request.contextPath}/review/detail?reviewId=${r.reviewId}"
                   class="read-more-btn">계속 읽기</a>
            </div>

            <div class="card-footer">
                <div>[추천] ${r.reviewLikeCount}</div>
                <div class="footer-right">
                    <span>
                        <fmt:formatDate value="${r.reviewCreatedDate}" pattern="yyyy년 M월 d일"/>
                    </span>
                </div>
            </div>
        </div>
    </c:forEach>


    <%-- 하단 페이징 영역 (디자인용) --%>
    <div class="pagination">
        <c:if test="${currentPage > 1}">
            <a href="${pageContext.request.contextPath}/review?page=${currentPage - 1}${not empty companyId ? '&companyId='.concat(companyId) : ''}"
               class="page-btn">이전</a>
        </c:if>

        <c:forEach begin="1" end="${totalPages}" var="p">
            <a href="${pageContext.request.contextPath}/review?page=${p}${not empty companyId ? '&companyId='.concat(companyId) : ''}"
               class="page-btn ${p == currentPage ? 'active' : ''}">${p}</a>
        </c:forEach>

        <c:if test="${currentPage < totalPages}">
            <a href="${pageContext.request.contextPath}/review?page=${currentPage + 1}${not empty companyId ? '&companyId='.concat(companyId) : ''}"
               class="page-btn">다음</a>
        </c:if>
    </div>
</div>