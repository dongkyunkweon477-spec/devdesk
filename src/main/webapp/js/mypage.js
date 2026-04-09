/* ===== js/mypage.js ===== */

/* 🌟 1. 탈퇴 모달 열고 닫기 */
function openDeleteModal() {
    document.getElementById('deleteModalOverlay').style.display = 'flex';
}

function closeDeleteModal() {
    document.getElementById('deleteModalOverlay').style.display = 'none';
}

/* 🌟 2. 비밀번호 변경 성공 모달 닫기 */
function closeSuccessModal() {
    const successModal = document.getElementById('successModalOverlay');
    if (successModal) {
        successModal.style.display = 'none';
    }
}

/* 🌟 에러 모달 닫기 */
function closeErrorModal() {
    const errorModal = document.getElementById('errorModalOverlay');
    if (errorModal) {
        errorModal.style.display = 'none';
    }
}