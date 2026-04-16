<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>DevDesk - 비밀번호 변경</title>
    <link rel="stylesheet" href="/css/index.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/user/passwordup.css">
</head>
<body>
<main class="content">
    <div class="pw-wrap">
        <div class="pw-card">

            <div class="pw-icon-box">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="none"
                     stroke="#7c3aed" stroke-width="2.2" stroke-linecap="round" stroke-linejoin="round">
                    <rect x="3" y="11" width="18" height="11" rx="2"></rect>
                    <path d="M7 11V7a5 5 0 0 1 10 0v4"></path>
                </svg>
            </div>
            <h2 class="pw-title">비밀번호 변경</h2>
            <p class="pw-sub">보안을 위해 주기적으로 비밀번호를 변경해 주세요.</p>
            <hr class="pw-divider">

            <c:if test="${not empty errorMsg}">
                <div class="pw-alert pw-alert-error">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none"
                         stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <circle cx="12" cy="12" r="10"/>
                        <line x1="12" y1="8" x2="12" y2="12"/>
                        <line x1="12" y1="16" x2="12.01" y2="16"/>
                    </svg>
                        ${errorMsg}
                </div>
            </c:if>

            <c:if test="${not empty successMsg}">
                <div class="pw-alert pw-alert-success">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none"
                         stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                        <polyline points="20 6 9 17 4 12"/>
                    </svg>
                        ${successMsg}
                </div>
            </c:if>

            <form action="/password-update" method="post" id="pwForm">

                <div class="pw-field">
                    <label class="pw-label" for="old_password">현재 비밀번호</label>
                    <div class="pw-input-wrap">
                        <input class="pw-input" type="password"
                               id="old_password" name="old_password"
                               placeholder="현재 비밀번호를 입력하세요"
                               autocomplete="current-password" required>
                        <button type="button" class="pw-toggle-btn" onclick="togglePassword(this)">
                            <svg class="icon-eye-off" width="20" height="20" viewBox="0 0 24 24" fill="none"
                                 stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
                                <line x1="1" y1="1" x2="23" y2="23"></line>
                            </svg>
                            <svg class="icon-eye" width="20" height="20" viewBox="0 0 24 24" fill="none"
                                 stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </svg>
                        </button>
                    </div>
                </div>

                <div class="pw-field">
                    <label class="pw-label" for="new_password">새 비밀번호</label>
                    <div class="pw-input-wrap">
                        <input class="pw-input" type="password"
                               id="new_password" name="new_password"
                               placeholder="새 비밀번호를 입력하세요"
                               autocomplete="new-password" required>
                        <button type="button" class="pw-toggle-btn" onclick="togglePassword(this)">
                            <svg class="icon-eye-off" width="20" height="20" viewBox="0 0 24 24" fill="none"
                                 stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
                                <line x1="1" y1="1" x2="23" y2="23"></line>
                            </svg>
                            <svg class="icon-eye" width="20" height="20" viewBox="0 0 24 24" fill="none"
                                 stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </svg>
                        </button>
                    </div>
                    <p class="pw-hint">특수문자 포함 8자 이상, 영문 + 숫자 조합을 권장합니다.</p>
                </div>

                <div class="pw-field">
                    <label class="pw-label" for="new_password_confirm">새 비밀번호 확인</label>
                    <div class="pw-input-wrap">
                        <input class="pw-input" type="password"
                               id="new_password_confirm" name="new_password_confirm"
                               placeholder="새 비밀번호를 한 번 더 입력하세요"
                               autocomplete="new-password" required>
                        <button type="button" class="pw-toggle-btn" onclick="togglePassword(this)">
                            <svg class="icon-eye-off" width="20" height="20" viewBox="0 0 24 24" fill="none"
                                 stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"></path>
                                <line x1="1" y1="1" x2="23" y2="23"></line>
                            </svg>
                            <svg class="icon-eye" width="20" height="20" viewBox="0 0 24 24" fill="none"
                                 stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                <path d="M1 12s4-8 11-8 11 8 11 8-4 8-11 8-11-8-11-8z"></path>
                                <circle cx="12" cy="12" r="3"></circle>
                            </svg>
                        </button>
                    </div>
                    <p class="pw-error" id="confirmError">비밀번호가 일치하지 않습니다.</p>
                </div>

                <div class="pw-btn-row">
                    <a href="/mypage" class="pw-btn pw-btn-cancel">취소</a>
                    <button type="submit" class="pw-btn pw-btn-submit">변경하기</button>
                </div>

            </form>

        </div>
    </div>
</main>


<script>
    const form = document.getElementById('pwForm');
    const newPw = document.getElementById('new_password');
    const confirmPw = document.getElementById('new_password_confirm');
    const confirmError = document.getElementById('confirmError');

    /* 새 비밀번호 확인 실시간 검사 */
    confirmPw.addEventListener('input', function () {
        if (newPw.value !== confirmPw.value) {
            confirmError.style.display = 'block';
            confirmPw.classList.add('pw-input-error');
        } else {
            confirmError.style.display = 'none';
            confirmPw.classList.remove('pw-input-error');
        }
    });

    /* 제출 전 최종 검사 */
    form.addEventListener('submit', function (e) {
        if (newPw.value !== confirmPw.value) {
            e.preventDefault();
            confirmError.style.display = 'block';
            confirmPw.focus();
        }
    });

    /* 🔥 CSS 클래스(.is-visible)를 활용한 눈 모양 토글 함수 */
    function togglePassword(btn) {
        // 버튼의 부모(.pw-input-wrap) 안에서 input 요소 찾기
        const inputField = btn.previousElementSibling;

        // is-visible 클래스 토글 (CSS에서 아이콘 보이기/숨기기 처리됨)
        btn.classList.toggle('is-visible');

        // input의 type을 text와 password 사이에서 전환
        if (inputField.type === "password") {
            inputField.type = "text";
        } else {
            inputField.type = "password";
        }
    }
</script>
</body>
</html>