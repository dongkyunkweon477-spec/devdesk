<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>DevDesk</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user/account.css">
</head>
<body class="account-body">

<div class="signup-wrapper">
    <div class="signup-card">
        <div class="signup-header">
            <h2>회원가입</h2>
            <p>DevDesk의 회원이 되어 다양한 혜택을 경험해 보세요!</p>
        </div>

        <form action="account" method="post" class="signup-form">

            <div class="form-group">
                <label>이메일 (아이디) <span class="required">*</span></label>
                <div class="input-with-btn">
                    <input type="email" name="email" id="email" placeholder="example@gmail.com" required>
                    <button type="button" class="btn-check" onclick="checkId()">중복 확인</button>
                </div>
                <span id="idCheckMsg" class="check-msg"></span>
            </div>

            <div class="form-group">
                <label>비밀번호 <span class="required">*</span></label>
                <input type="password" name="password" placeholder="비밀번호 입력(문자, 숫자, 특수문자 포함 8~20자)" required>
            </div>

            <div class="form-group">
                <label>닉네임 <span class="required">*</span></label>
                <input type="text" name="nickname" placeholder="닉네임을 입력해주세요" required>
            </div>

            <div class="form-group">
                <label>관심 직무 <span class="required">*</span></label>
                <select name="jobCategory">
                    <option value="프론트엔드">프론트엔드</option>
                    <option value="백엔드">백엔드</option>
                    <option value="데이터/AI">데이터/AI</option>
                    <option value="기획/디자인">기획/디자인</option>
                </select>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn-submit">가입완료</button>
                <button type="button" class="btn-cancel" onclick="history.back()">가입취소</button>
            </div>

        </form>
    </div>
</div>

<script>
    // [중복확인] 버튼을 누르면 이 함수가 실행됩니다.
    function checkId() {
        let email = document.getElementById("email").value;
        let msgSpan = document.getElementById("idCheckMsg"); // 메시지가 뜰 <span> 태그

        // 1. 빈칸 검사 (alert 대신 화면에 바로 빨간 글씨로 띄워줍니다!)
        if (email.trim() === "") {
            msgSpan.style.color = "#FF4D4F"; // 에러는 붉은색
            msgSpan.innerText = "이메일을 먼저 입력해주세요!";
            return; // 여기서 함수 종료 (서버로 안 보냄)
        }

        // 2. 서버의 /user-checkId 주소로 이메일을 보냅니다 (AJAX 통신)
        fetch('user-checkId', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
            },
            body: 'email=' + encodeURIComponent(email)
        })
            .then(response => response.text())
            .then(data => {
                if (data.trim() === "1") {
                    msgSpan.style.color = "#FF4D4F"; // 이미 있으면 붉은색
                    msgSpan.innerText = "이미 사용 중인 이메일입니다.";
                } else {
                    msgSpan.style.color = "#7C3AED"; // 통과는 DevDesk 보라색!
                    msgSpan.innerText = "사용 가능한 이메일입니다.";
                }
            })
            .catch(error => {
                console.error('에러 발생:', error);
                // 통신 에러도 alert 대신 화면에 띄웁니다
                msgSpan.style.color = "#FF4D4F";
                msgSpan.innerText = "중복 체크 중 문제가 발생했습니다.";
            });
    }
</script>
</body>
</html>