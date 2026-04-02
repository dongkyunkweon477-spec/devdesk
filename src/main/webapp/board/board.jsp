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
    <table class="board-table">
        <thead>
        <tr>
            <th>번호</th>
            <th>카테고리</th>
            <th>제목</th>
            <th>작성자</th>
            <th>조회수</th>
            <th>작성일</th>
        </tr>
        </thead>

        <tbody>
        <tr>
            <td>1</td>
            <td>자유토크</td>
            <td>첫 글입니다</td>
            <td>admin</td>
            <td>12</td>
            <td>2026-04-02</td>
        </tr>

        <tr>
            <td>2</td>
            <td>코테후기</td>
            <td>오늘 코테 후기 공유합니다</td>
            <td>user1</td>
            <td>30</td>
            <td>2026-04-01</td>
        </tr>
        </tbody>
    </table>
</div>

</body>
</html>
