<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>review</title>
    <%--    <link rel="stylesheet" href="css/board.css">--%>
    <link rel="stylesheet" href="css/board.css">

</head>

<body>
<div class="board-container">
    <!-- 상단 -->
    <div class="board-header">
        <h2>자유게시판</h2>

        <div class="board-actions">
            <!-- 검색 폼 -->
            <form action="board" method="get" class="search-form">
                <select name="searchType" class="search-type">
                    <option value="title" ${param.searchType == 'title' ? 'selected' : ''}>제목</option>
                    <option value="content" ${param.searchType == 'content' ? 'selected' : ''}>내용</option>
                    <option value="author" ${param.searchType == 'author' ? 'selected' : ''}>작성자</option>
                </select>
                <input type="text" name="keyword" class="search-input" placeholder="검색어를 입력하세요" value="${param.keyword}">
                <button type="submit" class="search-btn">검색</button>
                <c:if test="${not empty param.keyword}">
                    <button type="button" class="reset-btn" onclick="location.href='board'">목록으로</button>
                </c:if>
            </form>
            
            <select>
                <option>전체</option>
                <option>자유토크</option>
                <option>이력서</option>
                <option>TIP</option>
            </select>
            <select onchange="location.href='board?sort=' + this.value">
                <option value="" ${param.sort == null or param.sort == '' ? 'selected' : ''}>최신순</option>
                <option value="popular" ${param.sort == 'popular' ? 'selected' : ''}>인기순</option>
                <option value="viewcount" ${param.sort == 'viewcount' ? 'selected' : ''}>조회순</option>
            </select>

            <button class="write-btn"><a href="board_add">글쓰기</a></button>
        </div>
    </div>
    <%--                <div>--%>
    <%--                    <span onclick="location.href='review-detail?pk=${r.no}'">${r.title }</span> <br>--%>
    <%--                </div>--%>
    <!-- 게시글 리스트 -->
    <div>
        <c:forEach var="b" items="${boards}">
            <div class="board-row" onclick="location.href='BoardDetailC?id=${b.board_id}'">
                    <%--            <div class="board-row" onclick="location.href='BoardDetailC?id=${b.b_board_id}'">--%>
                <div class="col-category">${b.category}</div>
                <div class="col-title">
                        ${b.title}
                    <span class="comment-count">[${b.comment_count}]</span>
                    <span class="like-count">❤️ ${b.like_count}</span>
                    <c:if test="${b.like_count > 2}"> <%-- 2개 이상이여야 인기글 배지 --%>
                        <span class="popular-badge">🔥 인기글</span>
                    </c:if>
                </div>
                <div class="col-date">
                    <div class="col-date">
                        <c:choose>
                            <%-- 최종 수정일이 있으면 수정일 표시 --%>
                            <c:when test="${not empty b.updated_date}">
                                ${b.updated_date} <span style="font-size: 0.8em; color: #7c3aed;">(수정됨)</span>
                            </c:when>
                            <%-- 없으면 최초 작성일 표시 --%>
                            <c:otherwise>
                                ${b.created_date}
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </c:forEach>

    </div>

</div>

</body>
</html>
