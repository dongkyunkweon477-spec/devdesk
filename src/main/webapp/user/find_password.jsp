<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // 컨트롤러에서 넘겨준 step 값으로 어느 단계를 보여줄지 결정
    String step = (String) request.getAttribute("step");    // "reset" or null
    String errorMsg = (String) request.getAttribute("errorMsg");
    boolean isReset = "reset".equals(step);
%>
<html>
<head>
    <meta charset="utf-8">
    <title>DevDesk - 비밀번호 찾기</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user/account.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user/find_password.css">
</head>
<body class="account-body">

<div class="signup-wrapper">
    <div class="signup-card">

        <!-- ===== STEP 1 : 본인 확인 ===== -->
        <% if (!isReset) { %>

        <div class="signup-header">
            <h2>비밀번호 찾기</h2>
            <p>가입 시 등록한 닉네임과 이메일을 입력해주세요.</p>
        </div>

        <% if (errorMsg != null) { %>
        <div class="error-msg"><%= errorMsg %>
        </div>
        <% } %>

        <form action="find-password" method="post" class="signup-form">

            <div class="form-group">
                <label>닉네임 <span class="required">*</span></label>
                <input type="text" name="nickname" placeholder="가입 시 사용한 닉네임" required>
            </div>

            <div class="form-group">
                <label>이메일 (아이디) <span class="required">*</span></label>
                <input type="email" name="email" placeholder="example@gmail.com" required>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn-submit">본인 확인</button>
                <button type="button" class="btn-cancel" onclick="history.back()">돌아가기</button>
            </div>

        </form>

        <!-- ===== STEP 2 : 새 비밀번호 입력 ===== -->
        <% } else { %>

        <div class="signup-header">
            <h2>비밀번호 재설정</h2>
            <p>새로운 비밀번호를 입력해주세요.</p>
        </div>

        <% if (errorMsg != null) { %>
        <div class="error-msg"><%= errorMsg %>
        </div>
        <% } %>

        <form action="find-password?step=reset" method="post" class="signup-form" id="resetForm">

            <div class="form-group">
                <label>새 비밀번호 <span class="required">*</span></label>
                <input type="password" name="new_password" id="new_password"
                       placeholder="문자, 숫자, 특수문자 포함 8~20자" required>
            </div>

            <div class="form-group">
                <label>새 비밀번호 확인 <span class="required">*</span></label>
                <input type="password" name="confirm_password" id="confirm_password"
                       placeholder="비밀번호를 한 번 더 입력해주세요" required>
                <span id="pwMatchMsg" class="check-msg"></span>
            </div>

            <div class="form-actions">
                <button type="submit" class="btn-submit" id="submitBtn">비밀번호 재설정</button>
            </div>

        </form>

        <% } %>

    </div>
</div>

<script>
    // ── 비밀번호 일치 여부 실시간 확인 (STEP 2에서만 실행) ──
    const pwInput = document.getElementById('new_password');
    const confirmInput = document.getElementById('confirm_password');
    const pwMatchMsg = document.getElementById('pwMatchMsg');
    const submitBtn = document.getElementById('submitBtn');

    if (confirmInput) {
        function checkMatch() {
            if (confirmInput.value === '') {
                pwMatchMsg.innerText = '';
                return;
            }
            if (pwInput.value === confirmInput.value) {
                pwMatchMsg.style.color = '#7C3AED';
                pwMatchMsg.innerText = '비밀번호가 일치합니다.';
                if (submitBtn) submitBtn.disabled = false;
            } else {
                pwMatchMsg.style.color = '#FF4D4F';
                pwMatchMsg.innerText = '비밀번호가 일치하지 않습니다.';
                if (submitBtn) submitBtn.disabled = true;
            }
        }

        pwInput.addEventListener('input', checkMatch);
        confirmInput.addEventListener('input', checkMatch);
    }
</script>

</body>
</html>
