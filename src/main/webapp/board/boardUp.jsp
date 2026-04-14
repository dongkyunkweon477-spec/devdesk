<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/board/board-all.css">

<html>
<head>
    <title>게시글 수정</title>
</head>
<body>
<div class="write-container">

    <h2>✏️ 게시글 수정하기</h2>

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
            <div style="margin-bottom: 10px; text-align: right;">
                <label for="imageFile" style="font-weight: 500; cursor: pointer; border: 1px solid #ddd; padding: 6px 12px; border-radius: 4px; background-color: #f8f9fa; font-size: 0.9rem;">📷 이미지 첨부</label>
                <input type="file" id="imageFile" style="display: none;" accept="image/*"/>
            </div>
            <textarea name="content" rows="15" required>${board.content}</textarea>
        </div>
        
        <div class="form-actions">
            <button type="submit" class="submit-btn">수정 완료</button>
            <button type="button" class="cancel-btn" onclick="history.back()">취소</button>
        </div>
    </form>
    <script type="text/javascript">
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

                    let imageUrl = null;

                    let urlMatch = data.match(/(https:\/\/[a-zA-Z0-9-]+\.supabase\.co\/storage\/v1\/object\/public\/upload\/file\/[^\s]+)/);
                    if (urlMatch) {
                        imageUrl = urlMatch[1];
                    }

                    if (!imageUrl) {
                        urlMatch = data.match(/(https:\/\/[^\s]+)/);
                        if (urlMatch) {
                            imageUrl = urlMatch[1];
                        }
                    }

                    if (imageUrl) {
                        console.log('Extracted URL:', imageUrl);
                        const textarea = document.querySelector("textarea[name='content']");
                        textarea.value += '\n\n' + imageUrl;
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