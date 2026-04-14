/* TAG_STATS, RAW_EVENTS, CTX_PATH 는 til2.jsp 인라인 스크립트에서 주입됩니다. */

const TAG_CONFIG = {
    'Java':       {color: '#ff9f69', bg: 'rgba(255,159,105,0.12)'},
    'Spring':     {color: '#56e39f', bg: 'rgba(86,227,159,0.12)'},
    'SQL':        {color: '#4ecdc4', bg: 'rgba(78,205,196,0.12)'},
    'JavaScript': {color: '#ffd166', bg: 'rgba(255,209,102,0.12)'},
    'Git':        {color: '#ff6b6b', bg: 'rgba(255,107,107,0.12)'},
    'Python':     {color: '#5b7cf8', bg: 'rgba(91,124,248,0.12)'},
    'CSS':        {color: '#8b6ef5', bg: 'rgba(139,110,245,0.12)'},
    'React':      {color: '#4ecdc4', bg: 'rgba(78,205,196,0.12)'},
    '기타':       {color: '#9da3b8', bg: 'rgba(157,163,184,0.12)'}
};

/* ── 도넛 차트 ── */
function drawDonut() {
    if (!TAG_STATS.length) return;
    var total = TAG_STATS.reduce(function (a, d) { return a + d.count; }, 0);
    var canvas = document.getElementById('donutCanvas');
    var ctx = canvas.getContext('2d');
    var cx = 65, cy = 65, r = 50, ir = 30, gap = 0.04;
    var angle = -Math.PI / 2;

    TAG_STATS.forEach(function (d) {
        var cfg = TAG_CONFIG[d.tag] || TAG_CONFIG['기타'];
        var sweep = (d.count / total) * Math.PI * 2 - gap;
        ctx.beginPath();
        ctx.moveTo(cx, cy);
        ctx.arc(cx, cy, r, angle + gap / 2, angle + sweep + gap / 2);
        ctx.closePath();
        ctx.fillStyle = cfg.color;
        ctx.fill();
        angle += sweep + gap;
    });

    ctx.beginPath();
    ctx.arc(cx, cy, ir, 0, Math.PI * 2);
    ctx.fillStyle = getComputedStyle(document.documentElement).getPropertyValue('--surface') || '#141720';
    ctx.fill();

    document.getElementById('chartLegend').innerHTML = TAG_STATS.map(function (d) {
        var cfg = TAG_CONFIG[d.tag] || TAG_CONFIG['기타'];
        var pct = Math.round(d.count / total * 100);
        return '<div class="legend-row">' +
            '<div class="legend-dot" style="background:' + cfg.color + '"></div>' +
            '<span class="legend-name">' + d.tag + '</span>' +
            '<span class="legend-pct" style="color:' + cfg.color + '">' + pct + '%</span>' +
            '</div>';
    }).join('');
}

drawDonut();

/* ── 등록 / 수정 모달 ── */
function openTilEditor(id) {
    var form = document.getElementById('tilForm');
    document.getElementById('editorTitle').textContent = id ? 'TIL 수정' : 'TIL 작성';
    form.action = id ? 'til_update' : 'til_insert';

    document.getElementById('editorPreview').style.display = 'none';
    document.getElementById('tilContent').style.display = 'block';
    document.getElementById('previewBtn').textContent = '👁 미리보기';

    if (id) {
        var el = document.getElementById('til_data_' + id);
        document.getElementById('formTilId').value = id;
        document.getElementById('tilTitle').value = el.dataset.title;
        document.getElementById('tilTag').value = el.dataset.tag;
        document.getElementById('tilTime').value = el.dataset.time;
        document.getElementById('tilContent').value = el.dataset.content;
    } else {
        document.getElementById('formTilId').value = '';
        document.getElementById('tilTitle').value = '';
        document.getElementById('tilTag').value = 'Java';
        document.getElementById('tilTime').value = '';
        document.getElementById('tilContent').value = '';
    }

    document.getElementById('tilEditorModal').classList.add('open');
    document.getElementById('tilTitle').focus();
}

function closeEditor() {
    document.getElementById('tilEditorModal').classList.remove('open');
}

/* ── 상세 모달 ── */
function openDetail(id) {
    var el = document.getElementById('til_data_' + id);
    if (!el) return;
    var cfg = TAG_CONFIG[el.dataset.tag] || TAG_CONFIG['기타'];

    document.getElementById('detailTitle').textContent = el.dataset.title;
    document.getElementById('detailMeta').innerHTML =
        '<span class="badge" style="background:' + cfg.bg + ';color:' + cfg.color + '">' + el.dataset.tag + '</span>' +
        '<span style="font-size:12px;color:var(--text3);margin-left:8px">' + el.dataset.date + '</span>' +
        (el.dataset.time > 0
            ? '<span style="font-size:12px;color:var(--text3);margin-left:8px">⏱ ' + el.dataset.time + 'h</span>'
            : '');

    document.getElementById('detailContent').innerHTML = renderMarkdown(el.dataset.content);
    document.getElementById('detailEditBtn').onclick = function () {
        closeDetail();
        openTilEditor(id);
    };
    document.getElementById('detailDeleteBtn').onclick = function () {
        closeDetail();
        openDeleteConfirm(id, el.dataset.title);
    };

    document.getElementById('tilDetailModal').classList.add('open');
}

function closeDetail() {
    document.getElementById('tilDetailModal').classList.remove('open');
}

/* ── 삭제 확인 ── */
function openDeleteConfirm(id, title) {
    document.getElementById('confirmMsg').textContent = '"' + title + '" 를 삭제할까요? 이 작업은 되돌릴 수 없습니다.';
    document.getElementById('deleteTilId').value = id;
    document.getElementById('confirmOverlay').classList.add('open');
}

function closeConfirm() {
    document.getElementById('confirmOverlay').classList.remove('open');
}

/* ── 마크다운 렌더러 ── */
function renderMarkdown(text) {
    if (!text) return '<p style="color:var(--text3)">내용이 없어요.</p>';
    return text
        .replace(/```([\s\S]*?)```/g, '<pre class="md-code">$1</pre>')
        .replace(/`([^`]+)`/g, '<code class="md-inline">$1</code>')
        .replace(/^### (.+)$/gm, '<h3 class="md-h3">$1</h3>')
        .replace(/^## (.+)$/gm, '<h2 class="md-h2">$1</h2>')
        .replace(/^# (.+)$/gm, '<h1 class="md-h1">$1</h1>')
        .replace(/\*\*([^*]+)\*\*/g, '<strong>$1</strong>')
        .replace(/^- (.+)$/gm, '<li class="md-li">$1</li>')
        .replace(/\n\n/g, '</p><p class="md-p">')
        .replace(/\n/g, '<br>');
}

/* ── 에디터 툴바 ── */
function insertMd(prefix) {
    var ta = document.getElementById('tilContent');
    var s = ta.selectionStart;
    ta.value = ta.value.slice(0, s) + prefix + ta.value.slice(ta.selectionEnd);
    ta.selectionStart = ta.selectionEnd = s + prefix.length;
    ta.focus();
}

function wrapMd(open, close) {
    var ta = document.getElementById('tilContent');
    var s = ta.selectionStart, e = ta.selectionEnd;
    var sel = ta.value.slice(s, e) || 'text';
    ta.value = ta.value.slice(0, s) + open + sel + close + ta.value.slice(e);
    ta.selectionStart = s + open.length;
    ta.selectionEnd = s + open.length + sel.length;
    ta.focus();
}

var showPreview = false;

function togglePreview() {
    showPreview = !showPreview;
    var ta = document.getElementById('tilContent');
    var pre = document.getElementById('editorPreview');
    var btn = document.getElementById('previewBtn');
    if (showPreview) {
        pre.innerHTML = renderMarkdown(ta.value);
        pre.style.display = 'block';
        ta.style.display = 'none';
        btn.textContent = '✏️ 편집';
    } else {
        pre.style.display = 'none';
        ta.style.display = 'block';
        btn.textContent = '👁 미리보기';
    }
}

/* ── 키보드 / 오버레이 닫기 ── */
document.addEventListener('keydown', function (e) {
    if (e.key === 'Escape') {
        closeEditor();
        closeDetail();
        closeConfirm();
    }
});
document.getElementById('tilEditorModal').addEventListener('click', function (e) {
    if (e.target === e.currentTarget) closeEditor();
});
document.getElementById('tilDetailModal').addEventListener('click', function (e) {
    if (e.target === e.currentTarget) closeDetail();
});
document.getElementById('confirmOverlay').addEventListener('click', function (e) {
    if (e.target === e.currentTarget) closeConfirm();
});

/* ── 미니 캘린더 ── */
document.addEventListener('DOMContentLoaded', function () {
    var eventCounts = {};
    RAW_EVENTS.forEach(function (date) {
        if (date && date.trim() !== '') {
            var pureDate = date.split(' ')[0];
            eventCounts[pureDate] = (eventCounts[pureDate] || 0) + 1;
        }
    });

    var currentDispDate = new Date();

    function renderMiniCalendar(dateToRender) {
        var year = dateToRender.getFullYear();
        var month = dateToRender.getMonth();
        var today = new Date();
        var todayStr = today.getFullYear() + '-' +
            String(today.getMonth() + 1).padStart(2, '0') + '-' +
            String(today.getDate()).padStart(2, '0');

        document.getElementById('g-cal-title').textContent = year + '년 ' + (month + 1) + '월';

        var firstDay = new Date(year, month, 1);
        var lastDay = new Date(year, month + 1, 0);
        var prevMonthLastDay = new Date(year, month, 0).getDate();

        var firstDayIndex = firstDay.getDay() - 1;
        if (firstDayIndex === -1) firstDayIndex = 6;

        var daysHTML = '';

        for (var i = firstDayIndex; i > 0; i--) {
            daysHTML += '<div class="g-day-cell" onclick="location.href=\'' + CTX_PATH + '/calendar\'">' +
                '<div class="g-day-num other-month">' + (prevMonthLastDay - i + 1) + '</div>' +
                '</div>';
        }

        for (var d = 1; d <= lastDay.getDate(); d++) {
            var dateStr = year + '-' +
                String(month + 1).padStart(2, '0') + '-' +
                String(d).padStart(2, '0');
            var isToday = (dateStr === todayStr) ? ' today' : '';

            var dotsHTML = '';
            if (eventCounts[dateStr]) {
                dotsHTML = '<div class="g-dots">';
                var dotCount = Math.min(eventCounts[dateStr], 3);
                for (var k = 0; k < dotCount; k++) {
                    dotsHTML += '<span class="g-dot"></span>';
                }
                dotsHTML += '</div>';
            }

            daysHTML += '<div class="g-day-cell" onclick="location.href=\'' + CTX_PATH + '/calendar\'">' +
                '<div class="g-day-num' + isToday + '">' + d + '</div>' +
                dotsHTML +
                '</div>';
        }

        var totalCells = firstDayIndex + lastDay.getDate();
        var nextMonthDay = 1;
        while (totalCells + nextMonthDay - 1 < 42) {
            daysHTML += '<div class="g-day-cell" onclick="location.href=\'' + CTX_PATH + '/calendar\'">' +
                '<div class="g-day-num other-month">' + nextMonthDay + '</div>' +
                '</div>';
            nextMonthDay++;
        }

        document.getElementById('g-cal-days').innerHTML = daysHTML;
    }

    document.getElementById('g-prev-month').addEventListener('click', function () {
        currentDispDate.setMonth(currentDispDate.getMonth() - 1);
        renderMiniCalendar(currentDispDate);
    });
    document.getElementById('g-next-month').addEventListener('click', function () {
        currentDispDate.setMonth(currentDispDate.getMonth() + 1);
        renderMiniCalendar(currentDispDate);
    });

    renderMiniCalendar(currentDispDate);
});