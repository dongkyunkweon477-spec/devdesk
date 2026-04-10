<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>DevDesk</title>
    <link rel="stylesheet" href="css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user/myboard.css">
</head>
<body>
<main class="content">
    <div class="myboard-wrap">

        <div class="myboard-header">
            <h2 class="myboard-title">작성한 글 · 댓글</h2>
            <p class="myboard-sub">내가 DevDesk에 남긴 기록들을 확인하고 관리할 수 있습니다.</p>
        </div>

        <div class="tab-menu">
            <button class="tab-btn active" id="btn-posts" onclick="showTab('posts')">내가 쓴 글</button>
            <button class="tab-btn" id="btn-comments" onclick="showTab('comments')">내가 쓴 댓글</button>
        </div>

        <div id="tab-posts" style="display: block;">
            <table class="list-table">
                <colgroup>
                    <col width="15%">
                    <col width="45%">
                    <col width="10%">
                    <col width="10%">
                    <col width="20%">
                </colgroup>
                <thead>
                <tr>
                    <th>카테고리</th>
                    <th>제목</th>
                    <th>조회수</th>
                    <th>좋아요</th>
                    <th>작성일</th>
                </tr>
                </thead>
<<<<<<< HEAD
                <tbody>
                <c:if test="${empty myBoardList}">
                    <tr>
=======
                <tbody id="posts-tbody">
                <c:if test="${empty myBoardList}">
                    <tr class="empty-msg-row">
>>>>>>> 70d1f7ed23dddda95996e5de0aadcf41fffd4bd8
                        <td colspan="5" class="empty-msg">아직 작성한 게시글이 없습니다. 첫 글을 남겨보세요! 📝</td>
                    </tr>
                </c:if>

                <c:forEach var="board" items="${myBoardList}">
                    <tr onclick="goToDetail('BoardDetailC?id=${board.board_id}')" style="cursor: pointer;">
                        <td><span class="category-badge">${board.category}</span></td>
                        <td class="td-title">
                            <a href="BoardDetailC?id=${board.board_id}">${board.title}</a>
                            <span style="color: #ef4444; font-size: 12px; margin-left: 4px;">[${board.comment_count}]</span>
                        </td>
                        <td>${board.view_count}</td>
                        <td>${board.like_count}</td>
                        <td>${board.created_date.substring(0, 10)}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
<<<<<<< HEAD
=======
            <div id="posts-pagination" class="pagination"></div>
>>>>>>> 70d1f7ed23dddda95996e5de0aadcf41fffd4bd8
        </div>

        <div id="tab-comments" style="display: none;">
            <table class="list-table">
                <colgroup>
                    <col width="50%">
                    <col width="30%">
                    <col width="20%">
                </colgroup>
                <thead>
                <tr>
                    <th>글 제목</th>
                    <th>내 댓글</th>
                    <th>작성일</th>
                </tr>
                </thead>
<<<<<<< HEAD
                <tbody>
                <c:if test="${empty myCommentList}">
                    <tr>
=======
                <tbody id="comments-tbody">
                <c:if test="${empty myCommentList}">
                    <tr class="empty-msg-row">
>>>>>>> 70d1f7ed23dddda95996e5de0aadcf41fffd4bd8
                        <td colspan="3" class="empty-msg">아직 작성한 댓글이 없습니다. 💬</td>
                    </tr>
                </c:if>

                <c:forEach var="comment" items="${myCommentList}">
                    <tr onclick="goToDetail('BoardDetailC?id=${comment.board_id}')" style="cursor: pointer;">
                        <td class="td-title" style="color: #94a3b8; font-size: 14px;">
                                ${comment.board_title}
                        </td>
                        <td class="td-title">
                            <a href="BoardDetailC?id=${comment.board_id}">${comment.content}</a>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty comment.created_date and comment.created_date.length() >= 10}">
                                    ${comment.created_date.substring(0, 10)}
                                </c:when>
                                <c:otherwise>
                                    ${comment.created_date}
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
<<<<<<< HEAD
=======
            <div id="comments-pagination" class="pagination"></div>
>>>>>>> 70d1f7ed23dddda95996e5de0aadcf41fffd4bd8
        </div>

    </div>
</main>

<<<<<<< HEAD
<script>
    /* 탭 클릭 시 화면 전환해주는 자바스크립트 함수 */
    function showTab(tabName) {
        document.getElementById('tab-posts').style.display = 'none';
        document.getElementById('tab-comments').style.display = 'none';
        document.getElementById('btn-posts').classList.remove('active');
        document.getElementById('btn-comments').classList.remove('active');

        if (tabName === 'posts') {
            document.getElementById('tab-posts').style.display = 'block';
            document.getElementById('btn-posts').classList.add('active');
        } else {
            document.getElementById('tab-comments').style.display = 'block';
            document.getElementById('btn-comments').classList.add('active');
        }
    }

    /* 🌟 추가된 똑똑한 이동 함수! */
    function goToDetail(url) {
        // 유저가 마우스로 텍스트를 드래그해서 선택한 내용이 있는지 검사합니다.
        var selectedText = window.getSelection().toString();

        if (selectedText.length > 0) {
            // 선택한 텍스트가 있다면? (복사하려는 의도이므로 이동을 취소합니다!)
            return;
        }

        // 텍스트를 선택한 게 아니라 단순 클릭이라면 정상적으로 페이지를 이동합니다.
        location.href = url;
    }
</script>
=======
<script src="js/myboard.js"></script>
>>>>>>> 70d1f7ed23dddda95996e5de0aadcf41fffd4bd8
</body>
</html>