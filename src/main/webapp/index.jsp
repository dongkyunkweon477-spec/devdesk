<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <title>DevDesk</title>
    <%--    <link rel="stylesheet" href="css/index.css">--%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
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
            <img onclick="" src="${pageContext.request.contextPath}/images/Devlogo.png" alt="">
            <a href="${pageContext.request.contextPath}/main">DevDesk</a>
        </div>
        <!-- 메뉴 -->
        <div class="menu-left">
            <a href="${pageContext.request.contextPath}/ws">워크 스페이스</a>
            <a href="${pageContext.request.contextPath}/resume-block">이력서 관리</a>
            <a href="${pageContext.request.contextPath}/calendar">면접 일정</a>
            <a href="${pageContext.request.contextPath}/review">면접 후기</a>
            <a href="${pageContext.request.contextPath}/board">커뮤니티</a>
        </div>
        <!-- 로그인 -->
        <div class="menu-right">

            <c:choose>
                <%-- 1. 로그인 전 --%>
                <c:when test="${empty sessionScope.user}">
                    <a href="${pageContext.request.contextPath}/login" class="btn-text">로그인</a>
                    <span class="divider">|</span>
                    <a href="${pageContext.request.contextPath}/account" class="btn-primary">회원가입</a>
                </c:when>

                <%-- 2. 로그인 후 --%>
                <c:otherwise>
                    <span class="welcome-msg">${sessionScope.user.nickname}님 환영합니다!</span>

                    <a href="${pageContext.request.contextPath}/mypage" class="btn-text">마이페이지</a>
                    <a href="${pageContext.request.contextPath}/logout" class="btn-secondary">로그아웃</a>
                </c:otherwise>
            </c:choose>

        </div>
    </div>

    <div class="content">
        <jsp:include page="${content}"></jsp:include>
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