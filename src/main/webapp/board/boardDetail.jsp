<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="css/board-detail.css">
    <link rel="stylesheet" href="css/comment.css">
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
            <c:if test="${not empty board.updated_date}">
                <div class="detail-row">
                    <div class="detail-label">최종 수정일</div>
                    <div class="detail-content">${board.updated_date}</div>
                </div>
            </c:if>
        </div>

        <div class="detail-row content-area">
            <div class="detail-label">내용</div>
            <div class="detail-content text-box">${board.content}</div>
        </div>
    </div>

    <c:if test="${sessionScope.loginMember.member_id == board.member_id}">
        <div class="detail-buttons">
            <button class="edit-btn" onclick="location.href='board_update?id=${board.board_id}'">수정</button>
            <button class="delete-btn" onclick="deleteBoard(${board.board_id})">삭제</button>
        </div>
    </c:if>

    <div class="comment-section">
        <hr class="comment-divider">
        <h3>댓글</h3>

        <c:if test="${not empty sessionScope.user}">
            <div class="comment-form">
                <form action="comment_add" method="post">
                    <input type="hidden" name="board_id" value="${board.board_id}">
                    유저: ${sessionScope.user} <br>
                    member_id1: ${sessionScope.user.member_id} <br>
                    <input type="hidden" name="member_id" value="${sessionScope.user.member_id}">
                    <div class="comment-input-wrapper">
                        <textarea name="content" placeholder="댓글을 입력하세요" required></textarea>
                        <button type="submit">댓글 등록</button>
                    </div>
                </form>
            </div>
        </c:if>

        <div class="comment-list">
            <c:forEach var="comment" items="${commentList}">
                <div class="comment-item">
                    <div class="comment-info">
                        <span class="comment-writer">사용자(ID: ${comment.member_id})</span>
                        <span class="comment-date">${comment.created_date}</span>
                    </div>
                    <div class="comment-content">
                            ${comment.content}
                    </div>

                    <c:if test="${sessionScope.user.member_id == comment.member_id}">
                        <div class="comment-actions">
                            <button type="button" class="c-edit-btn"
                                    onclick="openEdit(${comment.comments_id})">수정
                            </button>
                            <button type="button" class="c-delete-btn"
                                    onclick="delComment(${comment.comments_id}, ${board.board_id})">삭제
                            </button>
                        </div>
                    </c:if>
                </div>
            </c:forEach>

            <c:if test="${empty commentList}">
                <p class="no-comments">아직 작성된 댓글이 없습니다.</p>
            </c:if>
        </div>
    </div>
</div>

<script>
    function deleteBoard(id) {
        if (confirm("정말 삭제하시겠습니까?")) {
            location.href = "board_del?id=" + id;
        }
    }

    function delComment(no, boardId) {
        if (confirm("댓글을 삭제하시겠습니까?")) {
            location.href = "comment_del?id=" + no + "&board_id=" + boardId;
        }
    }
</script>
</body>
</html>
