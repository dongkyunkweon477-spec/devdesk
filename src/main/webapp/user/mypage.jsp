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
        <%-- 프로필 사진 (닉네임 첫 글자로 고정) --%>
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

        <button class="menu-card danger" onclick="location.href='account-delete'">
            <h4>❌ 회원 탈퇴</h4>
            <p>더 이상 DevDesk를 이용하지 않으실 경우 진행합니다.</p>
        </button>
    </div>
</div>

<c:if test="${sessionScope.pwSuccess == 'true'}">
    <div id="successModalOverlay" class="pw-modal-overlay">
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

    <script>
        function closeSuccessModal() {
            // 확인 버튼을 누르면 모달을 화면에서 숨깁니다.
            document.getElementById('successModalOverlay').style.display = 'none';
        }
    </script>

    <%-- 🌟 새로고침 시 다시 뜨지 않도록 세션에서 도장 지우기 (핵심!) --%>
    <c:remove var="pwSuccess" scope="session"/>
</c:if>


</body>
</html>