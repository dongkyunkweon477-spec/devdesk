<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>DevDesk - 마이페이지</title>
    <link rel="stylesheet" href="css/mypage.css">
</head>
<body>

<div class="mypage-wrapper">
    <h2 class="mypage-title">마이페이지</h2>

    <div class="profile-info-box">
        <div class="profile-avatar">
            ${sessionScope.user.nickname.substring(0,1)}
        </div>

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

        <button class="menu-card danger" onclick="openDeleteModal()">
            <h4>❌ 회원 탈퇴</h4>
            <p>더 이상 DevDesk를 이용하지 않으실 경우 진행합니다.</p>
        </button>
    </div>
</div>

<div id="deleteModalOverlay" class="pw-modal-overlay" style="display: none;">
    <div class="pw-modal-box">
        <div class="pw-modal-icon">
            <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="#ef4444" stroke-width="2.5"
                 stroke-linecap="round" stroke-linejoin="round">
                <circle cx="12" cy="12" r="10"></circle>
                <line x1="15" y1="9" x2="9" y2="15"></line>
                <line x1="9" y1="9" x2="15" y2="15"></line>
            </svg>
        </div>
        <h3 class="pw-modal-title" style="color: #ef4444;">회원 탈퇴</h3>

        <div class="modal-warning-box">
            안전한 처리를 위해<br> <strong>현재 비밀번호</strong>를 입력해주세요.
            <span>(게시글/댓글은 유지되며 개인 데이터는 영구 삭제됩니다)</span>
        </div>

        <form action="delete-account" method="post">
            <input type="password" name="confirm_password" class="modal-input" placeholder="비밀번호 입력" required>

            <div class="modal-btn-group">
                <button type="button" class="pw-modal-btn btn-cancel" onclick="closeDeleteModal()">취소</button>
                <button type="submit" class="pw-modal-btn btn-danger">탈퇴하기</button>
            </div>
        </form>
    </div>
</div>

<c:if test="${not empty errorMsg}">
    <div id="errorModalOverlay" class="pw-modal-overlay" style="display: flex;">
        <div class="pw-modal-box">
            <div class="pw-modal-icon">
                <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="#ef4444" stroke-width="2.5"
                     stroke-linecap="round" stroke-linejoin="round">
                    <circle cx="12" cy="12" r="10"></circle>
                    <line x1="12" y1="8" x2="12" y2="12"></line>
                    <line x1="12" y1="16" x2="12.01" y2="16"></line>
                </svg>
            </div>
            <h3 class="pw-modal-title" style="color: #ef4444;">오류 발생</h3>
            <p class="pw-modal-desc">${errorMsg}</p>
            <button type="button" class="pw-modal-btn btn-danger" onclick="closeErrorModal()">확인</button>
        </div>
    </div>
    </div>
</c:if>

<c:if test="${sessionScope.pwSuccess == 'true'}">
    <div id="successModalOverlay" class="pw-modal-overlay" style="display: flex;">
        <div class="pw-modal-box">
            <div class="pw-modal-icon">
                <svg width="40" height="40" viewBox="0 0 24 24" fill="none" stroke="#10b981" stroke-width="2.5"
                     stroke-linecap="round" stroke-linejoin="round">
                    <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"></path>
                    <polyline points="22 4 12 14.01 9 11.01"></polyline>
                </svg>
            </div>
            <h3 class="pw-modal-title">변경 완료!</h3>
            <p class="pw-modal-desc">비밀번호가 성공적으로 변경되었습니다.</p>
            <button type="button" class="pw-modal-btn" onclick="closeSuccessModal()">확인</button>
        </div>
    </div>
    <c:remove var="pwSuccess" scope="session"/>
</c:if>

<script src="js/mypage.js"></script>
</body>
</html>