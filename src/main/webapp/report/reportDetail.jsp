<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="../css/board/board-all.css">
<link rel="stylesheet" href="../css/report/report.css">

<div class="board-container detail">
    <div class="board-header detail">
        <h2>신고 상세</h2>
        <div class="board-actions">
            <button class="write-btn" onclick="location.href='${pageContext.request.contextPath}/admin/report'">목록으로
            </button>
        </div>
    </div>

    <div class="detail-view">

        <%-- 신고 대상 유형 + 처리 상태 --%>
        <div class="detail-info-group">
            <div class="detail-row">
                <div class="detail-label">신고 유형</div>
                <div class="detail-content">
                    <c:choose>
                        <c:when test="${report.repoReviewId > 0}">
                            <span class="report-type-badge review">리뷰</span>
                        </c:when>
                        <c:otherwise>
                            <span class="report-type-badge board">게시글</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
            <div class="detail-row">
                <div class="detail-label">처리 상태</div>
                <div class="detail-content">
                    <span class="report-status-badge ${report.repoStatus == 'PENDING' ? 'pending' : 'done'}">
                        ${report.repoStatus == 'PENDING' ? '미처리' : '처리완료'}
                    </span>
                </div>
            </div>
            <div class="detail-row">
                <div class="detail-label">신고 일시</div>
                <div class="detail-content">${report.repoCreated}</div>
            </div>
            <div class="detail-row">
                <div class="detail-label">신고자 ID</div>
                <div class="detail-content">${report.repoMemberId}</div>
            </div>
        </div>

        <%-- 신고 사유 --%>
        <div class="detail-row">
            <div class="detail-label">신고 사유</div>
            <div class="detail-content">
                <span class="report-reason-badge"><c:out value="${report.repoReason}"/></span>
            </div>
        </div>

        <%-- 신고 대상 ID --%>
        <div class="detail-row">
            <div class="detail-label">대상 ID</div>
            <div class="detail-content">
                <c:choose>
                    <c:when test="${report.repoReviewId > 0}">
                        리뷰 #${report.repoReviewId}
                    </c:when>
                    <c:otherwise>
                        게시글 #${report.repoBoardId}
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <%-- 신고 내용 --%>
        <div class="detail-row content-area">
            <div class="detail-label">상세 내용</div>
            <div class="detail-content text-box">
                <c:out value="${report.repoContent}"/>
            </div>
        </div>

        <%-- 신고 대상 원문 --%>
        <c:choose>
            <c:when test="${not empty targetReview}">
                <div class="detail-row" style="margin-top: 16px;">
                    <div class="detail-label">리뷰 원문</div>
                    <div class="detail-content">
                        <div style="font-weight: 600; margin-bottom: 4px;"><c:out
                                value="${targetReview.reviewTitle}"/></div>
                        <div style="color: #555; font-size: 13px; margin-bottom: 6px;">
                                ${targetReview.companyName} &nbsp;·&nbsp; ${targetReview.reviewCreatedDate}
                        </div>
                        <div class="detail-content text-box"><c:out value="${targetReview.reviewContent}"/></div>
                    </div>
                </div>
            </c:when>
            <c:when test="${not empty targetBoard}">
                <div class="detail-row" style="margin-top: 16px;">
                    <div class="detail-label">게시글 원문</div>
                    <div class="detail-content">
                        <div style="font-weight: 600; margin-bottom: 4px;"><c:out value="${targetBoard.title}"/></div>
                        <div style="color: #555; font-size: 13px; margin-bottom: 6px;">
                                ${targetBoard.nickname} &nbsp;·&nbsp; ${targetBoard.created_date}
                        </div>
                        <div class="detail-content text-box"><c:out value="${targetBoard.content}"/></div>
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="detail-row" style="margin-top: 16px;">
                    <div class="detail-label">원문</div>
                    <div class="detail-content" style="color: #999;">이미 삭제된 콘텐츠입니다.</div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <%-- 신고 대상 삭제 버튼 --%>
    <div class="detail-buttons" style="margin-bottom: 8px;">
        <c:choose>
            <c:when test="${report.repoReviewId > 0}">
                <form method="post" action="${pageContext.request.contextPath}/reportDetail"
                      style="display:inline;" onsubmit="return confirm('리뷰를 삭제하시겠습니까?')">
                    <input type="hidden" name="reportId" value="${report.reportId}">
                    <input type="hidden" name="reviewId" value="${report.repoReviewId}">
                    <input type="hidden" name="cmd" value="delReview">
                    <button type="submit" class="delete-btn">리뷰 삭제</button>
                </form>
            </c:when>
            <c:otherwise>
                <form method="post" action="${pageContext.request.contextPath}/reportDetail"
                      style="display:inline;" onsubmit="return confirm('게시글을 삭제하시겠습니까?')">
                    <input type="hidden" name="reportId" value="${report.reportId}">
                    <input type="hidden" name="boardId" value="${report.repoBoardId}">
                    <input type="hidden" name="cmd" value="delBoard">
                    <button type="submit" class="delete-btn">게시글 삭제</button>
                </form>
            </c:otherwise>
        </c:choose>
    </div>

    <%-- 상태 변경 + 신고 삭제 버튼 --%>
    <div class="detail-buttons">
        <%-- 상태 변경 --%>
        <form method="post" action="${pageContext.request.contextPath}/reportDetail" style="display:inline;">
            <input type="hidden" name="reportId" value="${report.reportId}">
            <input type="hidden" name="cmd" value="status">
            <c:choose>
                <c:when test="${report.repoStatus == 'PENDING'}">
                    <input type="hidden" name="status" value="COMPLETED">
                    <button type="submit" class="edit-btn">처리완료로 변경</button>
                </c:when>
                <c:otherwise>
                    <input type="hidden" name="status" value="PENDING">
                    <button type="submit" class="write-btn detail">미처리로 되돌리기</button>
                </c:otherwise>
            </c:choose>
        </form>

        <%-- 삭제 --%>
        <form method="post" action="${pageContext.request.contextPath}/reportDetail"
              style="display:inline;" onsubmit="return confirm('신고를 삭제하시겠습니까?')">
            <input type="hidden" name="reportId" value="${report.reportId}">
            <input type="hidden" name="cmd" value="delete">
            <button type="submit" class="delete-btn">삭제</button>
        </form>
    </div>
</div>