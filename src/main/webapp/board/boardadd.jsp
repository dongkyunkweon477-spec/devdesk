<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="../css/board-all.css">
</head>
<body>
<div class="write-container">
    <form action="supa-upload" method="post" enctype="multipart/form-data">
        <input type="file" name="file"/>
        <button type="submit">supabase upload</button>
    </form>

    <h2>✍ 글 작성하기</h2>

    <form action="board_add" method="post">

        <!-- hidden: 컨트롤러 분기용 -->
        <input type="hidden" name="cmd" value="write">

        <!-- hidden: 로그인 사용자 ID -->
        <input type="hidden" name="member_id" value="${sessionScope.user.member_id}">

        <!-- 카테고리 -->
        <div class="form-group">
            <label>카테고리</label>
            <select name="category" required>
                <option value="">선택하세요</option>
                <option value="자유토크">자유토크</option>
                <option value="TIL">TIL</option>
                <option value="이력서">이력서</option>
                <option value="TIP">자기만의 TIP</option>
            </select>
        </div>

        <!-- 제목 -->
        <div class="form-group">
            <label>제목</label>
            <input type="text" name="title" placeholder="제목을 입력하세요" required>
        </div>

        <!-- 내용 -->
        <div class="form-group">
            <label>내용</label>
            <textarea name="txt" maxlength="1500" rows="10" placeholder="내용을 입력하세요" required></textarea>
            <br> <span id="cntSpan">0</span> / 1500
        </div>

        <!-- 버튼 -->

        <div class="form-actions">
            <button type="submit" class="submit-btn" onclick="uploadSupabase(e)">등록</button>
            <button type="button" class="cancel-btn" onclick="history.back()">취소</button>
        </div>
    </form>

    </form>

    <script type="text/javascript">
        const textarea = document.querySelector("textarea[name='txt']");
        const cntSpan = document.querySelector("#cntSpan");
        textarea.addEventListener('input', () => {
            const len = textarea.value.length;
            cntSpan.innerText = len;
        });

    </script>
</div>
</body>
</html>
