/* =========================================
   DevDesk 관리자 - 회원 관리 JS
   (admin_member.js)
========================================= */

const rowsPerPage = 10; // 한 페이지에 10명씩 보여주기
let currentPage = 1;
let filteredRows = [];

document.addEventListener("DOMContentLoaded", function () {
    const tbody = document.getElementById('member-tbody');
    if (!tbody) return;

    const allRows = Array.from(tbody.querySelectorAll('tr.member-row'));
    filteredRows = allRows; // 처음엔 모든 회원이 대상

    setupAdminPagination();

    // 💡 보너스: 검색창에서 엔터키 쳐도 바로 검색되게 하기!
    const searchInput = document.getElementById('memberSearch');
    if (searchInput) {
        searchInput.addEventListener('keypress', function (e) {
            if (e.key === 'Enter') searchMember();
        });
    }
});

// 1️⃣ 실시간 검색 기능
function searchMember() {
    const keyword = document.getElementById('memberSearch').value.toLowerCase();
    const allRows = Array.from(document.querySelectorAll('tr.member-row'));

    filteredRows = allRows.filter(row => {
        const email = row.querySelector('.email-cell').innerText.toLowerCase();
        const nickname = row.querySelector('.nickname-cell').innerText.toLowerCase();

        // 검색어가 포함되어 있으면 true (보이게 할 대상)
        if (email.includes(keyword) || nickname.includes(keyword)) {
            return true;
        } else {
            row.style.display = 'none'; // 조건 안 맞으면 바로 숨김
            return false;
        }
    });

    currentPage = 1; // 검색하면 무조건 1페이지로 리셋
    setupAdminPagination();
}

// 2️⃣ 똑똑한 페이징 처리 기능
function setupAdminPagination() {
    const pagination = document.getElementById('member-pagination');

    if (filteredRows.length === 0) {
        pagination.innerHTML = '<span style="color:#64748b; font-weight:bold;">검색 결과가 없습니다. 😢</span>';
        return;
    }

    const pageCount = Math.ceil(filteredRows.length / rowsPerPage);

    if (pageCount <= 1) {
        pagination.innerHTML = '';
        showAdminPage(1);
        return;
    }

    function render() {
        pagination.innerHTML = '';

        if (currentPage > 1) {
            const prevBtn = document.createElement('button');
            prevBtn.innerHTML = '&lt;';
            prevBtn.className = 'page-btn';
            prevBtn.onclick = () => {
                currentPage--;
                render();
            };
            pagination.appendChild(prevBtn);
        }

        for (let i = 1; i <= pageCount; i++) {
            const btn = document.createElement('button');
            btn.innerText = i;
            btn.className = 'page-btn';
            if (i === currentPage) btn.classList.add('active');
            btn.onclick = () => {
                currentPage = i;
                render();
            };
            pagination.appendChild(btn);
        }

        if (currentPage < pageCount) {
            const nextBtn = document.createElement('button');
            nextBtn.innerHTML = '&gt;';
            nextBtn.className = 'page-btn';
            nextBtn.onclick = () => {
                currentPage++;
                render();
            };
            pagination.appendChild(nextBtn);
        }

        showAdminPage(currentPage);
    }

    render();
}

function showAdminPage(page) {
    const start = (page - 1) * rowsPerPage;
    const end = start + rowsPerPage;

    // 먼저 검색된 모든 행을 숨기고
    filteredRows.forEach(row => row.style.display = 'none');

    // 현재 페이지에 해당하는 행만 보이게 켬
    for (let i = start; i < end && i < filteredRows.length; i++) {
        filteredRows[i].style.display = '';
    }
}

// ==========================================
// 3️⃣ 강제 탈퇴 (커스텀 알림 모달 연동 완료!)
// ==========================================
let targetDeleteId = null; // 현재 삭제 대기 중인 유저 ID 기억하기
let shouldReload = false;  // 성공 시 새로고침 여부를 기억할 변수

// 삭제 확인 모달 열기
function deleteMember(memberId, nickname) {
    targetDeleteId = memberId;

    // 모달창 안에 닉네임 글씨 갈아끼우기
    document.getElementById('modalTargetNickname').innerText = nickname;

    // 숨겨뒀던 확인 모달창 켜기
    document.getElementById('deleteConfirmModal').style.display = 'flex';

    // 확인 모달창 안의 [강제 탈퇴] 버튼에 진짜 삭제 이벤트 연결
    document.getElementById('btnConfirmDelete').onclick = function () {
        executeDelete(targetDeleteId, nickname);
    };
}

// 삭제 확인 모달 닫기 (취소)
function closeDeleteModal() {
    document.getElementById('deleteConfirmModal').style.display = 'none';
    targetDeleteId = null;
}

// 진짜 DB 삭제 AJAX 요청
function executeDelete(memberId, nickname) {
    closeDeleteModal(); // 확인 모달은 일단 닫기!

    const url = window.location.pathname + "Delete";

    fetch(url, {
        method: 'POST',
        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
        body: 'member_id=' + memberId
    })
        .then(response => response.text())
        .then(data => {
            if (data.trim() === "success") {
                // alert 대신 예쁜 알림 모달 띄우기
                showNotification("✅ 처리 완료", `'${nickname}' 회원이 강제 탈퇴 처리되었습니다.`, true);
            } else {
                showNotification("❌ 오류 발생", "탈퇴 처리 중 DB 오류가 발생했습니다.", false);
            }
        })
        .catch(error => {
            console.error("Error:", error);
            showNotification("❌ 통신 에러", "서버와 연결이 원활하지 않습니다.", false);
        });
}

// 🌟 처리 결과 알림 모달을 띄우는 함수
function showNotification(title, message, isSuccess) {
    document.getElementById('notiTitle').innerText = title;
    document.getElementById('notiText').innerText = message;
    document.getElementById('notiModal').style.display = 'flex';

    // 성공했을 때만 나중에 창 닫을 때 새로고침하게 설정
    shouldReload = isSuccess;
}

// 🌟 알림 모달을 닫는 함수
function closeNotiModal() {
    document.getElementById('notiModal').style.display = 'none';

    // 작업이 성공적이었다면 확인 버튼을 눌렀을 때 비로소 새로고침!
    if (shouldReload) {
        location.reload();
    }
}

// 🌟 4️⃣ 상세 정보 조회 및 모달 열기
function showDetail(memberId) {
    // 서버에 유저 상세 정보 요청 (AJAX GET)
    fetch(`${window.location.pathname.replace('/member', '')}/memberDetail?member_id=${memberId}`)
        .then(res => res.json())
        .then(user => {
            // 모달 데이터 채우기
            document.getElementById('detNickname').innerText = user.nickname;
            document.getElementById('detEmail').innerText = user.email;
            document.getElementById('detJob').innerText = user.job || '미입력(소셜)';
            document.getElementById('detCreated').innerText = user.created;
            document.getElementById('detLoginType').innerText = user.loginType;
            document.getElementById('detStatus').innerText = user.status === 'active' ? '정상 활동' : '탈퇴 회원';
            document.getElementById('detBoardCnt').innerText = user.boardCnt;
            document.getElementById('detCommentCnt').innerText = user.commentCnt;

            // 모달 띄우기
            document.getElementById('detailModal').style.display = 'flex';
        })
        .catch(err => {
            console.error(err);
            alert("정보를 가져오는 중 오류가 발생했습니다.");
        });
}

function closeDetailModal() {
    document.getElementById('detailModal').style.display = 'none';
}