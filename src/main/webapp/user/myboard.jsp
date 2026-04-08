<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="UTF-8">
    <title>DevDesk</title>
    <link rel="stylesheet" href="css/index.css">
    <link rel="stylesheet" href="css/myboard.css">
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
                <tbody>
                <c:if test="${empty myBoardList}">
                    <tr>
                        <td colspan="5" class="empty-msg">아직 작성한 게시글이 없습니다. 첫 글을 남겨보세요! 📝</td>
                    </tr>
                </c:if>

                <c:forEach var="board" items="${myBoardList}">
                    <tr>
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
        </div>

        <div id="tab-comments" style="display: none;">
            <div class="empty-msg">댓글 데이터는 잠시 후에 붙여볼게요! 🚀</div>
        </div>

    </div>
</main>

<script>
    /* 탭 클릭 시 화면 전환해주는 자바스크립트 함수 */
    function showTab(tabName) {
        // 모든 탭 숨기기 및 버튼 보라색 빼기
        document.getElementById('tab-posts').style.display = 'none';
        document.getElementById('tab-comments').style.display = 'none';
        document.getElementById('btn-posts').classList.remove('active');
        document.getElementById('btn-comments').classList.remove('active');

        // 클릭한 탭만 보여주고 보라색 칠하기
        if (tabName === 'posts') {
            document.getElementById('tab-posts').style.display = 'block';
            document.getElementById('btn-posts').classList.add('active');
        } else {
            document.getElementById('tab-comments').style.display = 'block';
            document.getElementById('btn-comments').classList.add('active');
        }
    }
</script>
</body>
</html>