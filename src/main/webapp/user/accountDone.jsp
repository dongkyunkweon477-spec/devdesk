<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="signup-wrapper">
    <div class="signup-card" style="text-align: center; padding: 60px 20px;">

        <div style="font-size: 50px; color: #7c3aed; margin-bottom: 20px;">✔</div>

        <h2 style="color: #1e293b; font-size: 28px; margin-bottom: 15px;">회원가입 완료</h2>

        <p style="color: #64748b; font-size: 16px; margin-bottom: 40px; line-height: 1.5;">
            <strong style="color: #7c3aed; font-size: 18px;">${welcomeName}</strong>님의 회원가입이<br>
            성공적으로 완료되었습니다.
        </p>

        <button type="button" class="btn-submit"
                onclick="location.href='${pageContext.request.contextPath}/user'"
                style="width: 200px;">
            로그인 바로가기
        </button>

    </div>
</div>


</body>
</html>
