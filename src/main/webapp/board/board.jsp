<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
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
            <select>
                <option>전체</option>
                <option>자유토크</option>
                <option>이력서</option>
                <option>면접후기</option>
                <option>자기만의 TIP</option>
            </select>

            <button class="write-btn"><a href="board_add">글쓰기</a></button>
        </div>
    </div>

    <!-- 게시글 리스트 -->
    <div>
        <c:forEach var="b" items="${boards}">
            <div class="board-row">
                <div>
<%--                    <span onclick="location.href='review-detail?pk=${r.no}'">${r.title }</span> <br>--%>
                </div>
                <div>${b.date}</div>
            </div>
        </c:forEach>
    </div>
</div>

</body>
</html>
