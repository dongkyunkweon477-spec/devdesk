<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>지원 목록</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@300;400;500;600;700&display=swap"
          rel="stylesheet">
    <link rel="stylesheet" href="/css/application_list.css">

</head>
<body>

<div class="wrap">

    <!-- 페이지 헤더 -->
    <div class="page-header">
        <div>
            <h1 class="page-title">지원한 회사 목록</h1>
            <p class="page-sub">지원 현황을 한눈에 확인하고 단계를 관리하세요</p>
        </div>
        <button class="btn-add" onclick="openInsertModal()">+ 지원 추가</button>
    </div>

    <!-- 단계별 요약 카운트 -->
    <div class="stage-bar" id="stageBar">
        <div class="stage-chip" style="--chip-c:#9da3b8">
            <span class="stage-chip-icon">📄</span>
            <div class="stage-chip-cnt" id="cnt-APPLIED">0</div>
            <div class="stage-chip-name">지원완료</div>
        </div>
        <%-- ← 여기서 닫기 --%>

        <div class="stage-chip" style="--chip-c:#ffd166">  <%-- ← 형제 요소로 --%>
            <span class="stage-chip-icon">📋</span>
            <div class="stage-chip-cnt" id="cnt-DOCUMENT">0</div>
            <div class="stage-chip-name">서류통과</div>
        </div>
        <div class="stage-chip" style="--chip-c:#4ecdc4">
            <span class="stage-chip-icon">🗣</span>
            <div class="stage-chip-cnt" id="cnt-FIRST_INTERVIEW">0</div>
            <div class="stage-chip-name">1차 면접</div>
        </div>
        <div class="stage-chip" style="--chip-c:#5b7cf8">
            <span class="stage-chip-icon">💬</span>
            <div class="stage-chip-cnt" id="cnt-SECOND_INTERVIEW">0</div>
            <div class="stage-chip-name">2차 면접</div>
        </div>
        <div class="stage-chip" style="--chip-c:#8b6ef5">
            <span class="stage-chip-icon">🔮</span>
            <div class="stage-chip-cnt" id="cnt-THIRD_INTERVIEW">0</div>
            <div class="stage-chip-name">3차 면접</div>
        </div>
        <div class="stage-chip" style="--chip-c:#56e39f">
            <span class="stage-chip-icon">🎉</span>
            <div class="stage-chip-cnt" id="cnt-PASS">0</div>
            <div class="stage-chip-name">합격</div>
        </div>
        <div class="stage-chip" style="--chip-c:#ff6b6b">
            <span class="stage-chip-icon">💔</span>
            <div class="stage-chip-cnt" id="cnt-FAIL">0</div>
            <div class="stage-chip-name">불합격</div>
        </div>
    </div>

    <!-- 카드 그리드 -->
    <div class="card-grid" id="cardGrid">

        <c:choose>
            <c:when test="${empty applicationList}">
                <div class="empty">
                    <div class="empty-icon">📭</div>
                    <p>아직 지원한 곳이 없어요.<br>+ 지원 추가 버튼을 눌러 첫 지원을 등록해 보세요!</p>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="app" items="${applicationList}" varStatus="vs">
                    <div class="app-card" id="card_${app.appId}" style="animation-delay:${vs.index * 40}ms">

                        <!-- 상단 -->
                        <div class="card-top">
                            <div class="card-company">${app.companyName}</div>
                            <span class="stage-badge" id="badge_${app.appId}"></span>
                        </div>

                        <!-- 직무 -->
                        <div class="card-position">💼 ${app.position}</div>

                        <!-- 메모 (있을 때만) -->
                        <c:if test="${not empty app.memo}">
                            <div class="card-memo">${app.memo}</div>
                        </c:if>

                        <!-- 상태 변경 영역 -->
                        <div class="status-wrap">
                            <span class="status-text" id="status_text_${app.appId}"></span>
                            <select class="status-select" id="status_select_${app.appId}">
                                <option value="APPLIED">지원완료</option>
                                <option value="DOCUMENT">서류 통과</option>
                                <option value="FIRST_INTERVIEW">1차 면접</option>
                                <option value="SECOND_INTERVIEW">2차 면접</option>
                                <option value="THIRD_INTERVIEW">3차 면접</option>
                                <option value="PASS">합격</option>
                                <option value="FAIL">불합격</option>
                            </select>
                            <button class="btn-status" onclick="editStatus('${app.appId}', '${app.status}')">단계 변경
                            </button>
                        </div>

                        <!-- 하단 -->
                        <div class="card-footer">
                            <span class="card-date">📅 ${app.appDate}</span>
                            <div class="card-actions">
                                <button class="btn-delete"
                                        onclick="openConfirm('${app.appId}', '${app.companyName}')">삭제
                                </button>
                            </div>
                        </div>

                    </div>
                    <%-- JS에서 쓸 상태값 초기화용 hidden input --%>
                    <input type="hidden" id="init_status_${app.appId}" value="${app.status}">
                </c:forEach>
            </c:otherwise>
        </c:choose>

    </div><!-- /.card-grid -->
</div><!-- /.wrap -->


<!-- ══════════════════════════════
     지원 등록 모달
══════════════════════════════ -->
<div class="modal-overlay" id="insertModal">
    <div class="modal-box">

        <div class="modal-header">
            <div class="modal-title">지원 등록</div>
            <button class="modal-close" onclick="closeInsertModal()">✕</button>
        </div>

        <form action="application-insert" method="post" id="insertForm">

            <!-- 회사 선택 -->
            <div class="form-group">
                <label class="form-label">회사 <span class="required">*</span></label>
                <select class="form-select" name="company_id" required>
                    <option value="">-- 선택하세요 --</option>
                    <c:forEach var="company" items="${companyList}">
                        <option value="${company.companyId}">${company.companyName}</option>
                    </c:forEach>
                </select>
            </div>

            <!-- 지원 직무 -->
            <div class="form-group">
                <label class="form-label">지원 직무 <span class="required">*</span></label>
                <input class="form-input" type="text" name="position"
                       placeholder="예: 백엔드 개발자" required>
            </div>

            <!-- 지원 상태 + 지원일 -->
            <div class="form-row">
                <div class="form-group">
                    <label class="form-label">지원 상태</label>
                    <select class="form-select" name="stage" id="modalStage">
                        <option value="APPLIED">지원완료</option>
                        <option value="DOCUMENT">서류통과</option>
                        <option value="FIRST_INTERVIEW">1차 면접</option>
                        <option value="SECOND_INTERVIEW">2차 면접</option>
                        <option value="THIRD_INTERVIEW">3차 면접</option>
                        <option value="PASS">합격</option>
                        <option value="FAIL">불합격</option>
                    </select>
                </div>
                <div class="form-group">
                    <label class="form-label">지원일</label>
                    <input class="form-input" type="date" name="apply_date" id="modalApplyDate">
                </div>
            </div>

            <!-- 메모 -->
            <div class="form-group">
                <label class="form-label">메모</label>
                <textarea class="form-textarea" name="memo"
                          placeholder="준비 사항, 특이 사항 등 자유롭게 입력하세요"></textarea>
            </div>

            <!-- hidden: 로그인 사용자 -->
            <input type="hidden" name="member_id" value="${sessionScope.loginUser.member_id}">

            <!-- 면접 일정 (INTERVIEW 선택 시 노출) -->
            <div class="interview-section" id="interviewSection">
                <div class="interview-divider"><span>면접 일정</span></div>

                <div class="form-row">
                    <div class="form-group">
                        <label class="form-label">면접 날짜</label>
                        <input class="form-input" type="date" name="interview_date">
                    </div>
                    <div class="form-group">
                        <label class="form-label">면접 시간</label>
                        <input class="form-input" type="time" name="interview_time">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label">면접 유형</label>
                    <select class="form-select" name="interview_type">
                        <option value="">-- 선택 --</option>
                        <option value="ONLINE">화상</option>
                        <option value="OFFLINE">대면</option>
                        <option value="PHONE">전화</option>
                    </select>
                </div>
            </div>

            <!-- 푸터 버튼 -->
            <div class="modal-footer">
                <button type="button" class="btn-modal-cancel" onclick="closeInsertModal()">취소</button>
                <button type="submit" class="btn-modal-submit">등록하기</button>
            </div>

        </form>
    </div>
</div>

<!-- 삭제 확인 다이얼로그 -->
<div class="confirm-overlay" id="confirmOverlay">
    <div class="confirm-box">
        <div class="confirm-title">정말 삭제할까요?</div>
        <div class="confirm-msg" id="confirmMsg">이 작업은 되돌릴 수 없습니다.</div>
        <div class="confirm-btns">
            <button class="btn-cancel" onclick="closeConfirm()">취소</button>
            <form id="deleteForm" action="application_delete" method="post" style="display:inline">
                <input type="hidden" name="app_id" id="deleteAppId">
                <button type="submit" class="btn-confirm-del">삭제</button>
            </form>
        </div>
    </div>
</div>


<script>
    /* ── 등록 모달 열기/닫기 ── */
    function openInsertModal() {
        // 오늘 날짜 기본값 세팅
        const today = new Date().toISOString().split('T')[0];
        document.getElementById('modalApplyDate').value = today;

        // 상태 초기화 및 면접 영역 리셋
        document.getElementById('modalStage').value = 'APPLIED';
        document.getElementById('interviewSection').classList.remove('visible');

        document.getElementById('insertModal').classList.add('open');
        document.body.style.overflow = 'hidden';
    }

    function closeInsertModal() {
        document.getElementById('insertModal').classList.remove('open');
        document.body.style.overflow = '';
        document.getElementById('insertForm').reset();
        document.getElementById('interviewSection').classList.remove('visible');
    }

    // 오버레이 바깥 클릭 → 닫기
    document.getElementById('insertModal').addEventListener('click', function (e) {
        if (e.target === this) closeInsertModal();
    });

    /* ── 면접 일정 섹션 토글 ── */
    document.getElementById('modalStage').addEventListener('change', function () {
        const section = document.getElementById('interviewSection');
        if (this.value === 'INTERVIEW') {
            section.classList.add('visible');
        } else {
            section.classList.remove('visible');
        }
    });

    /* ── ESC 키로 모달 닫기 ── */
    document.addEventListener('keydown', function (e) {
        if (e.key === 'Escape') {
            closeInsertModal();
            closeConfirm();
        }
    });


    const STAGE_MAP = {
        APPLIED: {label: '지원완료', icon: '📄', color: '#9da3b8', bg: 'rgba(157,163,184,0.12)', lineColor: '#9da3b8'},
        DOCUMENT: {
            label: '서류통과',
            icon: '📋',
            color: '#ffd166',
            bg: 'rgba(255,209,102,0.12)',
            lineColor: '#ffd166'
        },
        FIRST_INTERVIEW: {
            label: '1차 면접',
            icon: '🗣',
            color: '#4ecdc4',
            bg: 'rgba(78,205,196,0.12)',
            lineColor: '#4ecdc4'
        },
        SECOND_INTERVIEW: {
            label: '2차 면접',
            icon: '💬',
            color: '#5b7cf8',
            bg: 'rgba(91,124,248,0.12)',
            lineColor: '#5b7cf8'
        },
        THIRD_INTERVIEW: {
            label: '3차 면접',
            icon: '🔮',
            color: '#8b6ef5',
            bg: 'rgba(139,110,245,0.12)',
            lineColor: '#8b6ef5'
        },
        PASS: {label: '합격', icon: '🎉', color: '#56e39f', bg: 'rgba(86,227,159,0.12)', lineColor: '#56e39f'},
        FAIL: {label: '불합격', icon: '💔', color: '#ff6b6b', bg: 'rgba(255,107,107,0.12)', lineColor: '#ff6b6b'},
    };

    /* ── 페이지 로드 시 배지/카드 색상 초기화 ── */
    document.addEventListener('DOMContentLoaded', function () {
        // hidden input으로 각 카드 상태 초기화
        document.querySelectorAll('[id^="init_status_"]').forEach(function (el) {
            const id = el.id.replace('init_status_', '');
            applyStage(id, el.value);
        });
        updateCounts();
    });

    /* ── 배지 + 왼쪽 선 색상 적용 ── */
    function applyStage(id, status) {
        const cfg = STAGE_MAP[status] || STAGE_MAP['APPLIED'];
        const badge = document.getElementById('badge_' + id);
        const card = document.getElementById('card_' + id);
        const text = document.getElementById('status_text_' + id);

        if (badge) {
            badge.textContent = cfg.icon + ' ' + cfg.label;
            badge.style.background = cfg.bg;
            badge.style.color = cfg.color;
        }
        if (card) {
            card.style.setProperty('--stage-color', cfg.lineColor);
        }
        if (text) {
            text.textContent = cfg.label;
        }
    }

    /* ── 단계별 카운트 업데이트 ── */
    function updateCounts() {
        const counts = {APPLIED: 0, FIRST_INTERVIEW: 0, SECOND_INTERVIEW: 0, THIRD_INTERVIEW: 0, PASS: 0, FAIL: 0};
        document.querySelectorAll('[id^="init_status_"]').forEach(function (el) {
            const st = el.value;
            if (counts[st] !== undefined) counts[st]++;
        });
        Object.keys(counts).forEach(function (key) {
            const el = document.getElementById('cnt-' + key);
            if (el) el.textContent = counts[key];
        });
    }

    /* ── 단계 변경 버튼 클릭 ── */
    function editStatus(id, currentStatus) {
        const textEl = document.getElementById('status_text_' + id);
        const selectEl = document.getElementById('status_select_' + id);

        // 텍스트 배지 숨기고 select 노출
        const badge = document.getElementById('badge_' + id);
        if (badge) badge.style.display = 'none';
        if (textEl) textEl.style.display = 'none';

        selectEl.style.display = 'inline-block';
        selectEl.value = currentStatus;
        selectEl.focus();

        // 선택 변경 → 서버 전송
        selectEl.onchange = function () {
            updateStatus(id, this.value);
        };

        // 포커스 아웃 → 취소 (원래 상태 복원)
        selectEl.onblur = function () {
            setTimeout(function () {
                selectEl.style.display = 'none';
                if (badge) badge.style.display = '';
                if (textEl) textEl.style.display = '';
            }, 150);
        };
    }

    /* ── 서버에 상태 전송 ── */
    function updateStatus(id, status) {
        const form = document.createElement('form');
        form.method = 'post';
        form.action = 'application_update';

        const inputId = document.createElement('input');
        inputId.type = 'hidden';
        inputId.name = 'app_id';
        inputId.value = id;

        const inputStatus = document.createElement('input');
        inputStatus.type = 'hidden';
        inputStatus.name = 'status';
        inputStatus.value = status;

        form.appendChild(inputId);
        form.appendChild(inputStatus);
        document.body.appendChild(form);
        form.submit();
    }

    /* ── 삭제 확인 다이얼로그 ── */
    function openConfirm(appId, companyName) {
        document.getElementById('confirmMsg').textContent =
            '"' + companyName + '" 지원 내역을 삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.';
        document.getElementById('deleteAppId').value = appId;
        document.getElementById('confirmOverlay').classList.add('open');
    }

    function closeConfirm() {
        document.getElementById('confirmOverlay').classList.remove('open');
    }

    // 오버레이 바깥 클릭 시 닫기
    document.getElementById('confirmOverlay').addEventListener('click', function (e) {
        if (e.target === this) closeConfirm();
    });
</script>
</body>
</html>
