<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="css/board-detail.css">
</head>
<body>
<div class="board-container">
    <div class="board-header">
        <h2>게시글 상세페이지</h2>
        <div class="board-actions">
            <button type="button" class="write-btn" onclick="location.href='board'">목록으로</button>
        </div>
    </div>

    <div class="detail-view">
        <div class="detail-row">
            <div class="detail-label">제목</div>
            <div class="detail-content title-bold">${board.title}</div>
        </div>

        <div class="detail-info-group">
            <div class="detail-row">
                <div class="detail-label">작성자</div>
                <div class="detail-content">(ID: ${board.member_id})</div>
            </div>
            <div class="detail-row">
                <div class="detail-label">작성일</div>
                <div class="detail-content">${board.created_date}</div>
            </div>
        </div>

        <div class="detail-row content-area">
            <div class="detail-label">내용</div>
            <div class="detail-content text-box">${board.content}</div>
        </div>
    </div>

    <c:if test="${sessionScope.loginMember.member_id == board.member_id}">
        <div class="detail-buttons">
            <button class="edit-btn" onclick="location.href='BoardUpdateC?id=${board.member_id}'">수정</button>
            <button class="delete-btn" onclick="deleteBoard(${board.member_id})">삭제</button>
        </div>
    </c:if>
</div>

<script>
    function deleteBoard(id) {
        if (confirm("정말 삭제하시겠습니까?")) {
            location.href = "BoardDeleteC?id=" + id;
        }
    }
</script>
</body>
</html>
