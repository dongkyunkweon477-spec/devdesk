const textarea = document.getElementById('reportContent');
const cntSpan  = document.getElementById('cntSpan');

if (textarea && cntSpan) {
    textarea.addEventListener('input', () => {
        cntSpan.textContent = textarea.value.length;
    });
}

function validateForm() {
    const reason  = document.getElementById('reportReason').value;
    const content = document.getElementById('reportContent').value.trim();

    if (!reason) {
        alert('신고 사유를 선택해 주세요.');
        return false;
    }
    if (content.length < 10) {
        alert('상세 내용을 10자 이상 입력해 주세요.');
        return false;
    }
    return true;
}
