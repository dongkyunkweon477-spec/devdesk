// // 좋아요 상태 로드
// function loadLikeStatus() {
//     const boardId = ${board.board_id};
//     const memberId = ${sessionScope.user.member_id};
//
//     fetch(`/like?board_id=${boardId}&member_id=${memberId}`)
//         .then(response => response.json())
//         .then(data => {
//             isLiked = data.isLiked;
//             likeCount = data.likeCount;
//             updateLikeUI();
//         })
//         .catch(error => console.error('Error:', error));
// }
//
// // 초기 좋아요 상태 설정 (JSP 로딩 시 서버 데이터를 자바스크립트 변수에 저장)
// let currentIsLiked = ${isLiked} // 기본값
//
// // 1. 페이지 로드 시 서버가 보낸 초기 상태 (BoardDetailC에서 request.setAttribute한 값)
// let
//     isLiked = ${isLiked};
// boardDetail.jsp 에서 js가 안 먹음

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