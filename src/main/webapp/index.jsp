<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>DevDesk</title>
<%--    <link rel="stylesheet" href="css/index.css">--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">

</head>
<body>

<div class="login-area">
    <%--  <span style="color: red">${msg}</span>--%>
    <%--  <jsp:include page="${loginPage}"></jsp:include>--%>
</div>

<div class="container">
    <div class="header">
        <!-- 로고 -->
        <div class="title">
            <img onclick="" src="images/Devlogo.png" alt="">
            <a href="#">DevDesk</a>
        </div>
        <!-- 메뉴 -->
        <div class="menu-left">
            <a href="ws">워크 스페이스</a>
            <a href="report">이력서 관리</a>
            <a href="#">면접 일정</a>
            <a href="review">면접 후기</a>
        </div>
        <!-- 로그인 -->
        <div class="menu-right">
            <a href="user">로그인</a>
            <span>|</span>
            <a href="account">회원가입</a>
        </div>
    </div>
    <div class="content">
        <%--    <jsp:include page="${content}"></jsp:include>--%>
    </div>
</div>

<div class="footer">
    <div class="footer-inner">
        <div class="footer-left">
            © 2026 DevDesk
            <div class="footer-message">
                꾸준한 한 줄의 코드가 당신의 미래를 만듭니다.
            </div>
        </div>

        <div class="footer-right">
            <a href="#">소개</a>
            <a href="#">이용약관</a>
            <a href="#">문의</a>
        </div>
    </div>
</div>

</body>
</html>