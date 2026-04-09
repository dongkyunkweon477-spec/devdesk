/* ===== myboard.js ===== */

/* 탭 클릭 시 화면 전환 */
function showTab(tabName) {
    document.getElementById('tab-posts').style.display = 'none';
    document.getElementById('tab-comments').style.display = 'none';
    document.getElementById('btn-posts').classList.remove('active');
    document.getElementById('btn-comments').classList.remove('active');

    if (tabName === 'posts') {
        document.getElementById('tab-posts').style.display = 'block';
        document.getElementById('btn-posts').classList.add('active');
    } else {
        document.getElementById('tab-comments').style.display = 'block';
        document.getElementById('btn-comments').classList.add('active');
    }
}

/* 드래그 복사 방지 기능이 포함된 이동 함수 */
function goToDetail(url) {
    var selectedText = window.getSelection().toString();
    if (selectedText.length > 0) return;
    location.href = url;
}

/* 🔥 이전/다음 버튼 똑똑하게 숨기는 페이징 마법! */
function setupPagination(tbodyId, paginationId, rowsPerPage) {
    const tbody = document.getElementById(tbodyId);
    if (!tbody) return;

    const rows = Array.from(tbody.querySelectorAll('tr:not(.empty-msg-row)'));
    const pagination = document.getElementById(paginationId);

    if (rows.length === 0) return;

    const pageCount = Math.ceil(rows.length / rowsPerPage);

    if (pageCount <= 1) {
        pagination.style.display = 'none';
        return;
    }

    let currentPage = 1; // 현재 페이지 기억하기

    // 페이징 버튼들을 화면에 그리는 함수
    function render() {
        pagination.innerHTML = '';

        // 🌟 [<] 이전 버튼 (현재 페이지가 1보다 클 때만 그립니다!)
        if (currentPage > 1) {
            const prevBtn = document.createElement('button');
            prevBtn.innerHTML = '&lt;'; // < 기호
            prevBtn.className = 'page-btn';
            prevBtn.onclick = function () {
                currentPage--;
                render();
            };
            pagination.appendChild(prevBtn);
        }

        // 🌟 [1] [2] [3] 숫자 버튼들
        for (let i = 1; i <= pageCount; i++) {
            const btn = document.createElement('button');
            btn.innerText = i;
            btn.className = 'page-btn';

            if (i === currentPage) {
                btn.classList.add('active'); // 현재 페이지 보라색 칠하기
            }

            btn.onclick = function () {
                currentPage = i;
                render();
            };
            pagination.appendChild(btn);
        }

        // 🌟 [>] 다음 버튼 (현재 페이지가 마지막 페이지보다 작을 때만 그립니다!)
        if (currentPage < pageCount) {
            const nextBtn = document.createElement('button');
            nextBtn.innerHTML = '&gt;'; // > 기호
            nextBtn.className = 'page-btn';
            nextBtn.onclick = function () {
                currentPage++;
                render();
            };
            pagination.appendChild(nextBtn);
        }

        showPage(currentPage); // 해당 페이지의 리스트만 보여주기
    }

    // 리스트 자르기 함수
    function showPage(page) {
        const start = (page - 1) * rowsPerPage;
        const end = start + rowsPerPage;

        rows.forEach((row, index) => {
            if (index >= start && index < end) {
                row.style.display = '';
            } else {
                row.style.display = 'none';
            }
        });
    }

    // 최초 실행!
    render();
}

// 문서가 모두 로드되면 두 탭 모두 10개씩 페이징을 시작합니다!
document.addEventListener("DOMContentLoaded", function () {
    setupPagination('posts-tbody', 'posts-pagination', 7);
    setupPagination('comments-tbody', 'comments-pagination', 7);
});