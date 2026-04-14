/* =========================================
   DevDesk 관리자 - 신고 상세 페이지 JS
   (경고창 및 페이지 이동 로직 전담)
========================================= */

document.addEventListener("DOMContentLoaded", function () {

    // 1. 원문(리뷰/게시글) 강제 삭제 폼 경고창
    const delContentForm = document.getElementById("deleteContentForm");
    if (delContentForm) {
        delContentForm.addEventListener("submit", function (e) {
            const isReview = this.dataset.type === 'review';
            const msg = isReview ? "리뷰를 영구 삭제하시겠습니까?" : "게시글을 영구 삭제하시겠습니까?";
            if (!confirm("🚨 " + msg)) {
                e.preventDefault(); // 취소 누르면 삭제 멈춤!
            }
        });
    }

    // 2. 신고 내역 폐기 폼 경고창
    const delReportForm = document.getElementById("deleteReportForm");
    if (delReportForm) {
        delReportForm.addEventListener("submit", function (e) {
            if (!confirm("⚠️ 이 신고 내역만 목록에서 폐기하시겠습니까?")) {
                e.preventDefault();
            }
        });
    }

    // 3. '목록으로' 버튼 이동 이벤트
    const btnGoList = document.getElementById("btnGoList");
    if (btnGoList) {
        btnGoList.addEventListener("click", function () {
            // 버튼에 숨겨둔 URL(data-url)로 이동
            location.href = this.dataset.url;
        });
    }
});