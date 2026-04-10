<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>게시글 수정</title>
    <link rel="stylesheet" href="../css/board-all.css">
</head>
<body>
<div class="write-container"><h2>✏️ 게시글 수정하기</h2>

    <form action="board_update" method="post">
        <input type="hidden" name="board_id" value="${board.board_id}">

        <div class="form-group">
            <label>카테고리</label>
            <select name="category" required>
                <option value="자유토크" ${board.category == '자유토크' ? 'selected' : ''}>자유토크</option>
                <option value="이력서" ${board.category == '이력서' ? 'selected' : ''}>이력서</option>
                <option value="TIP" ${board.category == 'TIP' ? 'selected' : ''}>자기만의 TIP</option>
            </select>
        </div>

        <div class="form-group">
            <label>제목</label>
            <input type="text" name="title" value="${board.title}" required>
        </div>

        <div class="form-group">
            <label>내용</label>
            <textarea name="content" rows="15" required>${board.content}</textarea>
        </div>

        <div class="form-actions">
            <button type="submit" class="submit-btn">수정 완료</button>
            <button type="button" class="cancel-btn" onclick="history.back()">취소</button>
        </div>
    </form>
</div>
</body>
</html>