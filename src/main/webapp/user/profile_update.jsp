<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DevDesk</title>
    <link rel="stylesheet" href="css/profile_update.css">
</head>
<body>
<div class="mypage-wrapper">
    <h2 class="mypage-title">프로필 수정</h2>

    <form action="profile-update" method="post" enctype="multipart/form-data">
        <div class="update-container">

            <div class="photo-upload-section">
                <div id="imagePreview" class="profile-avatar large-avatar">
                    ${sessionScope.user.nickname.substring(0,1)}
                </div>
                <input type="file" id="fileInput" name="profile_img" accept="image/*" onchange="previewImage(this)">
                <button type="button" class="btn-select-photo" onclick="document.getElementById('fileInput').click();">
                    사진 선택
                </button>
            </div>

            <div class="info-input-section">
                <div class="input-group static-group">
                    <p><strong>계정(이메일):</strong> ${sessionScope.user.email} <span class="badge-lock">(수정 불가)</span></p>
                </div>

                <div class="input-group">
                    <label for="nickname">닉네임</label>
                    <input type="text" id="nickname" name="nickname" value="${sessionScope.user.nickname}">
                </div>

                <div class="input-group">
                    <label for="job_category">관심 직무</label>
                    <select id="job_category" name="job_category">
                        <option value="프론트엔드" ${sessionScope.user.job_category == '프론트엔드' ? 'selected' : ''}>프론트엔드
                        </option>
                        <option value="백엔드" ${sessionScope.user.job_category == '백엔드' ? 'selected' : ''}>백엔드</option>
                        <option value="풀스택" ${sessionScope.user.job_category == '풀스택' ? 'selected' : ''}>풀스택</option>
                        <option value="디자인" ${sessionScope.user.job_category == '디자인' ? 'selected' : ''}>기획/디자인</option>
                    </select>
                </div>
            </div>
        </div>

        <div class="update-actions">
            <button type="button" onclick="history.back()" class="btn-cancel">취소</button>
            <button type="submit" class="btn-submit">수정 완료</button>
        </div>
    </form>
</div>

<script>
    function previewImage(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                var preview = document.getElementById('imagePreview');
                preview.innerText = '';
                preview.style.backgroundImage = 'url(' + e.target.result + ')';
                preview.style.backgroundSize = 'cover';
                preview.style.backgroundPosition = 'center';
            }
            reader.readAsDataURL(input.files[0]);
        }
    }
</script>

</body>
</html>
