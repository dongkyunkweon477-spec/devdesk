<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/board-all.css">

<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="../css/board-all.css">
</head>
<body>
<div class="write-container">
    <form action="supa-upload" method="post" enctype="multipart/form-data">
        <input type="file" name="file" id="imageFile"/>
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
                <option value="자기만의TIP">자기만의TIP</option>
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

    <script type="text/javascript">
        const textarea = document.querySelector("textarea[name='txt']");
        const cntSpan = document.querySelector("#cntSpan");
        textarea.addEventListener('input', () => {
            const len = textarea.value.length;
            cntSpan.innerText = len;
        });

        // Handle file selection for automatic upload
        document.getElementById('imageFile').addEventListener('change', function (e) {
            e.preventDefault();

            const fileInput = this;
            const formData = new FormData();
            formData.append('file', fileInput.files[0]);

            if (fileInput.files.length === 0) {
                return;
            }

            console.log('File selected, starting upload...');
            
            fetch('supa-upload', {
                method: 'POST',
                body: formData
            })
                .then(response => response.text())
                .then(data => {
                    console.log('Server response:', data);

                    // Try multiple patterns to extract URL
                    let imageUrl = null;

                    // Pattern 1: Full Supabase URL
                    let urlMatch = data.match(/(https:\/\/[a-zA-Z0-9-]+\.supabase\.co\/storage\/v1\/object\/public\/upload\/file\/[^\s]+)/);
                    if (urlMatch) {
                        imageUrl = urlMatch[1];
                    }

                    // Pattern 2: Any https URL (fallback)
                    if (!imageUrl) {
                        urlMatch = data.match(/(https:\/\/[^\s]+)/);
                        if (urlMatch) {
                            imageUrl = urlMatch[1];
                        }
                    }

                    if (imageUrl) {
                        console.log('Extracted URL:', imageUrl);
                        // Insert URL into textarea
                        textarea.value += '\n\n' + imageUrl;
                        // Update character count
                        cntSpan.innerText = textarea.value.length;
                        // Clear file input
                        fileInput.value = '';
                        console.log('Image uploaded and URL inserted:', imageUrl);
                        alert('Image uploaded successfully!');
                    } else {
                        console.error('URL extraction failed. Response:', data);
                        alert('Failed to extract image URL from response. Check console for details.');
                    }
                })
                .catch(error => {
                    console.error('Upload error:', error);
                    alert('Image upload failed. Please try again.');
                });
        });

    </script>
</div>
</body>
</html>
