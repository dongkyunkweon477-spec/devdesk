<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>DevDesk</title>
    <link rel="stylesheet" href="css/mypage.css">
</head>
<body>

<div class="mypage-wrapper">
    <h2 class="mypage-title">마이페이지</h2>

    <div class="profile-info-box">
        <%-- 프로필 사진 분기 처리 --%>
        <c:choose>
            <c:when test="${empty sessionScope.user.profile_img}">
                <div class="profile-avatar">
                        ${sessionScope.user.nickname.substring(0,1)}
                </div>
            </c:when>
            <c:otherwise>
                <div class="profile-avatar"
                     style="background-image: url('images/profile/${sessionScope.user.profile_img}'); background-size: cover; background-position: center; color: transparent;">
                </div>
            </c:otherwise>
        </c:choose>

        <div class="profile-details">
            <p class="nickname">${sessionScope.user.nickname} 님</p>
            <p><strong>계정(이메일) :</strong> ${sessionScope.user.email}</p>
            <p><strong>관심 직무 :</strong> ${sessionScope.user.job_category}</p>
        </div>
    </div>

    <div class="mypage-menu-grid">
        <button class="menu-card" onclick="location.href='profile-update'">
            <h4>👤 프로필 수정</h4>
            <p>닉네임, 연락처 등 내 정보를 변경합니다.</p>
        </button>

        <button class="menu-card" onclick="location.href='password-update'">
            <h4>🔒 비밀번호 변경</h4>
            <p>주기적인 변경으로 계정을 안전하게 보호하세요.</p>
        </button>

        <button class="menu-card" onclick="location.href='my-board'">
            <h4>📝 작성한 글 · 댓글</h4>
            <p>내가 커뮤니티에 남긴 기록을 확인합니다.</p>
        </button>

        <button class="menu-card danger" onclick="location.href='account-delete'">
            <h4>❌ 회원 탈퇴</h4>
            <p>더 이상 DevDesk를 이용하지 않으실 경우 진행합니다.</p>
        </button>
    </div>
</div>

</body>
</html>