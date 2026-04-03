<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>DevDesk - 로그인</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/account.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body class="account-body">

<div class="signup-wrapper">
    <div class="signup-card">

        <div class="signup-header">
            <h2>로그인</h2>
            <p>DevDesk에 오신 것을 환영합니다!</p>
        </div>

        <form action="login" method="post" class="signup-form">

            <div class="error-msg">
                ${msg}
            </div>

            <div class="form-group">
                <label>이메일 (아이디)</label>
                <input type="email" name="email" placeholder="example@gmail.com" required>
            </div>

            <div class="form-group">
                <label>비밀번호</label>
                <input type="password" name="password" placeholder="비밀번호를 입력해주세요" required>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn-submit" style="width: 100%;">로그인</button>
            </div>

            <div class="login-options">
                <span>아직 회원이 아니신가요?</span>
                <a href="account">회원가입 하기</a>
            </div>

        </form>

    </div>
</div>

</body>