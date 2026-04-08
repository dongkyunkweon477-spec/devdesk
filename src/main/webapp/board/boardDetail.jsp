<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="css/board-detail.css">
    <link rel="stylesheet" href="css/comment.css">
    <link rel="stylesheet" href="css/board_like.css">
</head>
<body>
<div class="board-container">
    <div class="board-header">
        <h2>게시글 상세페이지</h2>
        <div class="board-actions">
            <!-- 좋아요 기능 -->
            <c:if test="${not empty sessionScope.user}">
                <div class="like-section">
                    <button type="button" class="like-btn" id="likeBtn" onclick="toggleLike()">
                        <span id="likeIcon">${isLiked ? '❤️' : '🤍'}</span>
                        <span id="likeCount">${board.like_count}</span>
                    </button>
                </div>
            </c:if>
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
                <div class="detail-content">(ID: ${board.nickname})</div>
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

    <c:if test="${sessionScope.user.member_id == board.member_id}">
        <div class="detail-buttons">
            <button class="edit-btn" onclick="location.href='board_update?id=${board.board_id}'">수정</button>
            <button class="delete-btn" onclick="deleteBoard(${board.board_id})">삭제</button>
        </div>
    </c:if>

    <div class="comment-section">
        <hr class="comment-divider">
        <h3>댓글</h3>
        <hr class="comment-divider">

        <c:if test="${not empty sessionScope.user}">
            <div class="comment-form">
                <form action="comment_add" method="post">
                    <input type="hidden" name="board_id" value="${board.board_id}">
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
                <!-- 부모 댓글만 표시 -->
                <c:if test="${empty comment.parent_id}">
                    <div class="comment-item" data-comment-id="${comment.comments_id}">
                        <div class="comment-info">
                            <span class="comment-writer">사용자(ID: ${comment.nickname})</span>
                            <span class="comment-date">${comment.created_date}</span>
                        </div>
                        <div class="comment-content">
                                ${comment.content}
                        </div>

                        <button class="reply-btn"
                                onclick="showReplyForm(${comment.comments_id}, ${board.board_id}, '${sessionScope.user.member_id}')">
                            답글
                        </button>

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

                    <!-- 이 부모 댓글에 대한 대댓글 표시 -->
                    <c:forEach var="reply" items="${commentList}">
                        <c:if test="${reply.parent_id == comment.comments_id}">
                            <div class="reply-item" data-reply-id="${reply.comments_id}">
                                <div class="reply-info">
                                    <span class="reply-writer">사용자(ID: ${reply.nickname})</span>
                                    <span class="reply-date">${reply.created_date}</span>
                                </div>
                                <div class="reply-content">
                                        ${reply.content}
                                </div>
                                <c:if test="${sessionScope.user.member_id == reply.member_id}">
                                    <div class="reply-actions">
                                        <button type="button" class="c-edit-btn"
                                                onclick="openEdit(${reply.comments_id})">수정
                                        </button>
                                        <button type="button" class="c-delete-btn"
                                                onclick="delComment(${reply.comments_id}, ${board.board_id})">삭제
                                        </button>
                                    </div>
                                </c:if>
                            </div>
                        </c:if>
                    </c:forEach>
                </c:if>
            </c:forEach>

            <c:if test="${empty commentList}">
                <p class="no-comments">아직 작성된 댓글이 없습니다.</p>
            </c:if>
        </div>
    </div>
</div>

<script>
    // 좋아요 상태 로드
    function loadLikeStatus() {
        const boardId = ${board.board_id};
        const memberId = ${sessionScope.user.member_id};

        fetch(`/like?board_id=${boardId}&member_id=${memberId}`)
            .then(response => response.json())
            .then(data => {
                isLiked = data.isLiked;
                likeCount = data.likeCount;
                updateLikeUI();
            })
            .catch(error => console.error('Error:', error));
    }

    // 초기 좋아요 상태 설정 (JSP 로딩 시 서버 데이터를 자바스크립트 변수에 저장)
    let currentIsLiked = ${isLiked} // 기본값

        // 1. 페이지 로드 시 서버가 보낸 초기 상태 (BoardDetailC에서 request.setAttribute한 값)
        let
    isLiked = ${isLiked};

    function toggleLike() {
        // 1. EL 태그 값을 자바스크립트 변수에 할당 (따옴표 필수!)
        const boardId = "${board.board_id}";
        const memberId = "${sessionScope.user.member_id}";

        // 콘솔에 값이 찍히는지 먼저 확인 (F12 콘솔창에서 확인 가능)
        console.log("전송 데이터 확인 - boardId:", boardId, "memberId:", memberId);

        // 2. 값이 비어있으면 서버에 보내지 않음
        if (!boardId || boardId === "" || !memberId || memberId === "") {
            alert("로그인 정보나 게시글 정보를 찾을 수 없습니다.");
            return;
        }

        // 3. fetch 호출
        fetch('like_add', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            // 문자열 연결 방식 확인
            body: "board_id=" + boardId + "&member_id=" + memberId
        })
            .then(res => res.json())
            .then(data => {
                if (data.success) {
                    currentIsLiked = data.liked;
                    updateLikeUI(data.liked, data.count);
                }
            })
            .catch(err => console.error("전송 에러:", err));
    }

    function updateLikeUI(liked, count) {
        const likeIcon = document.getElementById('likeIcon');
        const likeCountElement = document.getElementById('likeCount');

        // 서버에서 받은 liked 값(true/false)에 따라 하트 변경
        likeIcon.textContent = liked ? '❤️' : '🤍';
        likeCountElement.textContent = count;
    }

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

    // 댓글 수정 폼 표시 함수
    function openEdit(commentId) {
        console.log('openEdit called:', commentId);

        // 기존에 열린 수정 폼이 있다면 닫기
        hideAllEditForms();

        // 부모 댓글 찾기
        const parentComment = document.querySelector('[data-comment-id="' + commentId + '"] .comment-content');

        // 대댓글 찾기
        const replyComment = document.querySelector('[data-reply-id="' + commentId + '"] .reply-content');

        // 찾은 요소 선택
        const targetElement = parentComment || replyComment;

        if (!targetElement) {
            console.error('Comment element not found for commentId:', commentId);
            return;
        }

        const originalContent = targetElement.textContent.trim();

        // 수정 폼 HTML 생성
        const editFormHtml =
            '<div id="edit-form-' + commentId + '" class="edit-form">' +
            '<form action="comment_update" method="post">' +
            '<input type="hidden" name="comment_id" value="' + commentId + '">' +
            '<input type="hidden" name="board_id" value="${board.board_id}">' +
            '<div class="edit-input-wrapper">' +
            '<textarea name="content" required>' + originalContent + '</textarea>' +
            '<div class="edit-buttons">' +
            '<button type="submit">수정 완료</button>' +
            '<button type="button" onclick="hideEditForm(' + commentId + ')">취소</button>' +
            '</div>' +
            '</div>' +
            '</form>' +
            '</div>';

        // 댓글 내용을 수정 폼으로 교체
        targetElement.style.display = 'none';
        targetElement.insertAdjacentHTML('afterend', editFormHtml);
    }

    // 수정 폼 숨기기 함수
    function hideEditForm(commentId) {
        const editForm = document.getElementById('edit-form-' + commentId);

        // 부모 댓글 찾기
        const parentComment = document.querySelector('[data-comment-id="' + commentId + '"] .comment-content');

        // 대댓글 찾기
        const replyComment = document.querySelector('[data-reply-id="' + commentId + '"] .reply-content');

        // 찾은 요소 선택
        const targetElement = parentComment || replyComment;

        if (editForm && targetElement) {
            editForm.remove();
            targetElement.style.display = 'block';
        }
    }

    // 모든 수정 폼 숨기기 함수
    function hideAllEditForms() {
        const editForms = document.querySelectorAll('.edit-form');
        const commentContents = document.querySelectorAll('.comment-content, .reply-content');

        editForms.forEach(form => form.remove());
        commentContents.forEach(content => content.style.display = 'block');
    }

    // 대댓글 폼 표시 함수
    function showReplyForm(commentId, boardId, memberId) {
        console.log('showReplyForm called:', commentId, boardId, memberId);

        // 기존에 열린 대댓글 폼이 있다면 닫기
        hideAllReplyForms();

        // 대댓글 폼 HTML 생성 - 문자열 연결 방식으로 변경
        const replyFormHtml =
            '<div id="reply-form-' + commentId + '" class="reply-form">' +
            '<form action="comment_add" method="post">' +
            '<input type="hidden" name="board_id" value="' + boardId + '">' +
            '<input type="hidden" name="member_id" value="' + memberId + '">' +
            '<input type="hidden" name="parent_id" value="' + commentId + '">' +
            '<div class="reply-input-wrapper">' +
            '<textarea name="content" placeholder="답글을 입력하세요" required></textarea>' +
            '<div class="reply-buttons">' +
            '<button type="submit">답글 등록</button>' +
            '<button type="button" onclick="hideReplyForm(' + commentId + ')">취소</button>' +
            '</div>' +
            '</div>' +
            '</form>' +
            '</div>';

        // 해당 댓글 아래에 대댓글 폼 추가
        const commentItem = document.querySelector('[data-comment-id="' + commentId + '"]');

        if (commentItem) {
            commentItem.insertAdjacentHTML('beforeend', replyFormHtml);
        }
    }

    // 대댓글 폼 숨기기 함수
    function hideReplyForm(commentId) {
        // 해당 댓글 아이템 내에서 reply-form 찾기
        const commentItem = document.querySelector('[data-comment-id="' + commentId + '"]');
        
        if (commentItem) {
            const replyForm = commentItem.querySelector('.reply-form');
            
            if (replyForm) {
                replyForm.remove();
            }
        }
    }

    // 모든 대댓글 폼 숨기기 함수
    function hideAllReplyForms() {
        const replyForms = document.querySelectorAll('.reply-form');
        replyForms.forEach(form => form.remove());
    }
</script>
</body>
</html>
