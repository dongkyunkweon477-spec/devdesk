<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>대시보드 — 취뽀 워크스페이스</title>
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Gowun+Batang:wght@400;700&family=Noto+Sans+KR:wght@300;400;500;600;700&display=swap"
          rel="stylesheet">
    <style>

        /* ════════════════════════
           1. CSS 변수 (디자인 토큰)
        ════════════════════════ */
        :root {
            --bg: #0d0f14;
            --surface: #141720;
            --surface2: #1a1e2a;
            --surface3: #1f2435;
            --border: #252a3a;
            --border2: #2e3450;

            --accent: #5b7cf8;
            --accent2: #8b6ef5;
            --accent3: #4ecdc4;
            --accent4: #ffd166;
            --accent5: #ff6b6b;
            --green: #56e39f;
            --orange: #ff9f69;

            --text: #e8eaf0;
            --text2: #9da3b8;
            --text3: #5a6080;

            --radius: 14px;
            --radius-sm: 8px;
        }

        /* ════════════════════════
           2. 리셋 & 기본
        ════════════════════════ */
        *, *::before, *::after {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Noto Sans KR', sans-serif;
            background: var(--bg);
            color: var(--text);
            min-height: 100vh;
            line-height: 1.5;
        }

        /* 배경 격자 */
        body::before {
            content: '';
            position: fixed;
            inset: 0;
            background-image: linear-gradient(rgba(91, 124, 248, 0.025) 1px, transparent 1px),
            linear-gradient(90deg, rgba(91, 124, 248, 0.025) 1px, transparent 1px);
            background-size: 48px 48px;
            pointer-events: none;
            z-index: 0;
        }

        a {
            text-decoration: none;
            color: inherit;
        }

        ::-webkit-scrollbar {
            width: 5px;
        }

        ::-webkit-scrollbar-thumb {
            background: var(--border2);
            border-radius: 3px;
        }

        @keyframes fadeUp {
            from {
                opacity: 0;
                transform: translateY(16px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        /* ════════════════════════
           3. 페이지 레이아웃
              sidebar(240px 고정) + content-area(나머지)
              → display:flex 로 나란히 배치
        ════════════════════════ */
        .page-wrap {
            position: relative;
            z-index: 1;
            display: flex; /* ← 핵심: 사이드바와 본문을 가로로 나란히 */
            min-height: 100vh;
        }

        .content-area {
            flex: 1; /* ← 남은 공간 전부 차지 */
            margin-left: 240px; /* ← 사이드바 너비만큼 밀어냄 */
            padding: 32px 36px;
        }

        /* ════════════════════════
           4. 사이드바
        ════════════════════════ */
        .sidebar {
            position: fixed; /* 스크롤해도 고정 */
            left: 0;
            top: 0;
            bottom: 0;
            width: 240px;
            background: var(--surface);
            border-right: 1px solid var(--border);
            display: flex;
            flex-direction: column; /* 로고 / 메뉴 / 유저카드 세로 배치 */
            z-index: 200;
        }

        .sidebar-logo {
            padding: 24px 22px 20px;
            border-bottom: 1px solid var(--border);
        }

        .logo-mark {
            font-family: 'Gowun Batang', serif;
            font-size: 20px;
            font-weight: 700;
            background: linear-gradient(135deg, var(--accent), var(--accent2));
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            display: block;
        }

        .logo-sub {
            font-size: 10px;
            color: var(--text3);
            margin-top: 2px;
            letter-spacing: 0.5px;
        }

        .sidebar-nav {
            flex: 1; /* 남은 높이 전부 */
            padding: 12px 10px;
            overflow-y: auto;
        }

        .nav-section-label {
            font-size: 10px;
            font-weight: 600;
            color: var(--text3);
            letter-spacing: 1.2px;
            text-transform: uppercase;
            padding: 8px 12px 6px;
            margin-top: 8px;
        }

        .nav-item {
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 10px 12px;
            border-radius: 9px;
            font-size: 13px;
            color: var(--text2);
            transition: all 0.15s;
            border: 1px solid transparent;
        }

        .nav-item:hover {
            background: var(--surface2);
            color: var(--text);
        }

        .nav-item.active {
            background: linear-gradient(135deg, rgba(91, 124, 248, 0.12), rgba(139, 110, 245, 0.08));
            color: var(--accent);
            border-color: rgba(91, 124, 248, 0.2);
            font-weight: 500;
        }

        .nav-icon {
            font-size: 15px;
            width: 20px;
            text-align: center;
            flex-shrink: 0;
        }

        .sidebar-footer {
            padding: 16px 10px;
            border-top: 1px solid var(--border);
        }

        .user-card {
            display: flex;
            align-items: center;
            gap: 10px;
            padding: 10px 12px;
            border-radius: 10px;
            background: var(--surface2);
            border: 1px solid var(--border);
        }

        .user-avatar {
            width: 32px;
            height: 32px;
            border-radius: 50%;
            background: linear-gradient(135deg, var(--accent), var(--accent2));
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 13px;
            font-weight: 700;
            color: #fff;
            flex-shrink: 0;
        }

        .user-name {
            font-size: 13px;
            font-weight: 500;
            color: var(--text);
        }

        .user-role {
            font-size: 10px;
            color: var(--text3);
        }

        /* ════════════════════════
           5. 공통 컴포넌트
        ════════════════════════ */

        /* 카드 */
        .card {
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: var(--radius);
            padding: 22px 24px;
        }

        /* 카드 제목 (왼쪽 컬러 바 포함) */
        .card-title {
            font-size: 11px;
            font-weight: 600;
            letter-spacing: 1.5px;
            text-transform: uppercase;
            color: var(--text3);
            margin-bottom: 18px;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .card-title::before {
            content: '';
            width: 3px;
            height: 11px;
            border-radius: 2px;
            background: linear-gradient(var(--accent), var(--accent2));
            flex-shrink: 0;
        }

        /* 버튼 */
        .btn {
            display: inline-flex;
            align-items: center;
            gap: 6px;
            padding: 9px 18px;
            border-radius: var(--radius-sm);
            font-size: 13px;
            font-weight: 500;
            border: none;
            cursor: pointer;
            transition: all 0.18s;
            font-family: inherit;
        }

        .btn-primary {
            background: linear-gradient(135deg, var(--accent), var(--accent2));
            color: #fff;
        }

        .btn-primary:hover {
            opacity: 0.88;
            transform: translateY(-1px);
        }

        .btn-ghost {
            background: var(--surface2);
            color: var(--text2);
            border: 1px solid var(--border);
        }

        .btn-ghost:hover {
            border-color: var(--border2);
            color: var(--text);
        }

        .btn-sm {
            padding: 6px 12px;
            font-size: 12px;
        }

        /* 배지 */
        .badge {
            display: inline-flex;
            align-items: center;
            padding: 3px 10px;
            border-radius: 20px;
            font-size: 11px;
            font-weight: 600;
            white-space: nowrap;
        }

        /* 빈 상태 */
        .empty-state {
            text-align: center;
            padding: 40px 20px;
            color: var(--text3);
        }

        .empty-state .icon {
            font-size: 36px;
            margin-bottom: 10px;
            opacity: 0.5;
        }

        .empty-state p {
            font-size: 13px;
            line-height: 1.7;
        }

        /* ════════════════════════
           6. 대시보드 헤더
        ════════════════════════ */
        .dash-header {
            display: flex;
            align-items: flex-end;
            justify-content: space-between; /* 제목(왼쪽) + 버튼(오른쪽) */
            margin-bottom: 28px;
            gap: 16px;
            flex-wrap: wrap;
        }

        .dash-title {
            font-family: 'Gowun Batang', serif;
            font-size: 26px;
            font-weight: 700;
            letter-spacing: -0.5px;
        }

        .dash-sub {
            font-size: 13px;
            color: var(--text3);
            margin-top: 3px;
        }

        .dash-header-actions {
            display: flex;
            gap: 8px;
        }

        /* ════════════════════════
           7. 단계 요약 스트립
              7개 칩을 가로로 나열
        ════════════════════════ */
        .stage-strip {
            display: grid;
            grid-template-columns: repeat(7, 1fr); /* 7등분 */
            gap: 10px;
            margin-bottom: 24px;
        }

        .stage-chip {
            display: flex;
            flex-direction: column; /* 아이콘/이름/숫자 세로 배치 */
            align-items: center;
            gap: 4px;
            padding: 14px 8px 12px;
            background: var(--surface);
            border: 1px solid var(--border);
            border-radius: var(--radius);
            text-decoration: none;
            transition: all 0.18s;
            position: relative;
            overflow: hidden;
        }

        /* 하단 컬러 선: 호버 시 나타남 */
        .stage-chip::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            height: 3px;
            background: var(--chip-color, var(--accent));
            opacity: 0;
            transition: opacity 0.18s;
        }

        .stage-chip:hover {
            border-color: var(--chip-color, var(--accent));
            transform: translateY(-2px);
        }

        .stage-chip:hover::after {
            opacity: 1;
        }

        .stage-chip-icon {
            font-size: 20px;
        }

        .stage-chip-name {
            font-size: 10px;
            color: var(--text3);
            text-align: center;
        }

        .stage-chip-count {
            font-size: 22px;
            font-weight: 700;
            color: var(--chip-color, var(--text));
            line-height: 1;
        }

        .stage-chip-unit {
            font-size: 10px;
            color: var(--text3);
            margin-top: -4px;
        }

        /* ════════════════════════
           8. 대시보드 그리드
              2×2 카드 배치
        ════════════════════════ */
        .dash-grid {
            display: grid;
            grid-template-columns: 1fr 1fr; /* 2열 */
            gap: 20px;
        }

        .dash-card {
            animation: fadeUp 0.4s ease both;
        }

        /* ════════════════════════
           9. 파이프라인 (전환율 바)
        ════════════════════════ */
        .funnel-row {
            margin-bottom: 14px;
        }

        .funnel-row:last-child {
            margin-bottom: 0;
        }

        .funnel-labels {
            display: flex;
            justify-content: space-between;
            font-size: 12px;
            color: var(--text2);
            margin-bottom: 5px;
        }

        .funnel-track {
            height: 7px;
            background: var(--surface3);
            border-radius: 4px;
            overflow: hidden;
        }

        .funnel-fill {
            height: 100%;
            border-radius: 4px;
            /* 실제 너비는 JSP에서 style="width:


        ${pct}   %" 로 주입 */
        }

        /* ════════════════════════
           10. 예정 일정 행
        ════════════════════════ */
        .sch-row {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 11px 14px;
            border-radius: 10px;
            background: var(--surface2);
            border: 1px solid var(--border);
            margin-bottom: 8px;
            transition: all 0.15s;
        }

        .sch-row:last-child {
            margin-bottom: 0;
        }

        .sch-row:hover {
            border-color: rgba(91, 124, 248, 0.3);
            transform: translateX(2px);
        }

        /* 오늘 일정 강조 */
        .sch-today {
            border-color: rgba(91, 124, 248, 0.3) !important;
            background: rgba(91, 124, 248, 0.05) !important;
        }

        .sch-date {
            text-align: center;
            min-width: 36px;
        }

        .sch-mon {
            font-size: 9px;
            color: var(--text3);
            text-transform: uppercase;
        }

        .sch-day {
            font-size: 20px;
            font-weight: 700;
            color: var(--text);
            line-height: 1.1;
        }

        .sch-vline {
            width: 1px;
            background: var(--border);
            align-self: stretch;
            margin: 1px 0;
        }

        .sch-info {
            flex: 1;
        }

        .sch-company {
            font-size: 13px;
            font-weight: 500;
            color: var(--text);
        }

        .sch-time {
            font-size: 11px;
            color: var(--text3);
            margin-top: 1px;
        }

        /* ════════════════════════
           11. TIL 행
        ════════════════════════ */
        .til-row {
            display: flex;
            align-items: center;
            gap: 12px;
            padding: 10px 12px;
            border-radius: 10px;
            text-decoration: none;
            border: 1px solid transparent;
            transition: all 0.15s;
        }

        .til-row:hover {
            background: var(--surface2);
            border-color: var(--border);
        }

        .til-num {
            font-size: 11px;
            color: var(--text3);
            width: 18px;
            text-align: right;
        }

        .til-dot {
            width: 7px;
            height: 7px;
            border-radius: 50%;
            flex-shrink: 0;
        }

        .til-info {
            flex: 1;
            min-width: 0;
        }

        .til-title {
            font-size: 13px;
            font-weight: 500;
            color: var(--text);
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
        }

        .til-meta {
            font-size: 11px;
            color: var(--text3);
            margin-top: 3px;
            display: flex;
            align-items: center;
            gap: 7px;
        }

        .til-arrow {
            color: var(--text3);
            font-size: 16px;
            transition: transform 0.15s;
        }

        .til-row:hover .til-arrow {
            transform: translateX(3px);
            color: var(--accent);
        }

        /* ════════════════════════
           12. 도넛 차트 (범례만)
               실제 차트는 서버 연동 후 Canvas로 그림
        ════════════════════════ */
        .chart-wrap {
            display: flex;
            align-items: center;
            gap: 20px;
        }

        .donut-placeholder {
            width: 140px;
            height: 140px;
            border-radius: 50%;
            border: 12px solid var(--surface3);
            flex-shrink: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;
        }

        .donut-center-text {
            text-align: center;
        }

        .donut-num {
            font-size: 26px;
            font-weight: 700;
            color: var(--text);
        }

        .donut-label {
            font-size: 10px;
            color: var(--text3);
            margin-top: 1px;
        }

        .chart-legend {
            flex: 1;
            display: flex;
            flex-direction: column;
            gap: 8px;
        }

        .legend-row {
            display: flex;
            align-items: center;
            gap: 9px;
            padding: 3px 0;
        }

        .legend-dot {
            width: 8px;
            height: 8px;
            border-radius: 50%;
            flex-shrink: 0;
        }

        .legend-name {
            font-size: 12px;
            color: var(--text2);
            flex: 1;
        }

        .legend-pct {
            font-size: 12px;
            font-weight: 600;
        }

        /* ════════════════════════
           13. 반응형
        ════════════════════════ */
        @media (max-width: 1100px) {
            .stage-strip {
                grid-template-columns: repeat(4, 1fr);
            }
        }

        @media (max-width: 900px) {
            .content-area {
                margin-left: 0;
                padding: 20px 16px;
                padding-top: 72px;
            }

            .dash-grid {
                grid-template-columns: 1fr;
            }

            .stage-strip {
                grid-template-columns: repeat(4, 1fr);
            }
        }

        @media (max-width: 600px) {
            .stage-strip {
                grid-template-columns: repeat(2, 1fr);
            }
        }

    </style>
</head>
<body>
<div class="page-wrap">

    <!-- ════════════════════════════
         사이드바
    ════════════════════════════ -->
    <aside class="sidebar">

        <div class="sidebar-logo">
            <span class="logo-mark">취뽀 워크스페이스</span>
            <span class="logo-sub">IT 취업 준비 플랫폼</span>
        </div>

        <nav class="sidebar-nav">
            <div class="nav-section-label">메인</div>
            <a href="dashboard" class="nav-item active">
                <span class="nav-icon">🏠</span> 대시보드
            </a>

            <div class="nav-section-label">취업 관리</div>
            <a href="application-list" class="nav-item">
                <span class="nav-icon">📋</span> 지원 현황
            </a>
            <a href="schedule-list" class="nav-item">
                <span class="nav-icon">📅</span> 면접 일정
            </a>

            <div class="nav-section-label">학습</div>
            <a href="til-list" class="nav-item">
                <span class="nav-icon">📚</span> TIL
            </a>
        </nav>

        <div class="sidebar-footer">
            <div class="user-card">
                <div class="user-avatar">
                    <%-- 로그인 유저 이니셜 --%>
                    ${sessionScope.loginUser.nickname.substring(0,1)}
                </div>
                <div>
                    <div class="user-name">${sessionScope.loginUser.nickname}</div>
                    <div class="user-role">${sessionScope.loginUser.jobCategory}</div>
                </div>
            </div>
        </div>

    </aside>

    <!-- ════════════════════════════
         본문
    ════════════════════════════ -->
    <main class="content-area">

        <!-- 헤더 -->
        <div class="dash-header">
            <div>
                <h1 class="dash-title">안녕하세요, ${sessionScope.loginUser.nickname}님 👋</h1>
                <p class="dash-sub">
                    <%-- Java에서 넘겨준 오늘 날짜 문자열 --%>
                    ${todayStr}
                </p>
            </div>
            <div class="dash-header-actions">
                <a href="application-list" class="btn btn-ghost btn-sm">지원 현황 관리</a>
                <a href="til-write" class="btn btn-primary btn-sm">+ TIL 작성</a>
            </div>
        </div>

        <!-- ── 단계 요약 스트립 ────────────────────────── -->
        <%--
          Java(Servlet)에서 StageCountVO stageCounts 를 request에 담아서 전달.

          [StageCountVO.java]
            public class StageCountVO {
                private int applied;        // 이력서 제출
                private int documentPass;   // 서류 합격
                private int firstInterview; // 1차 면접
                private int secondInterview;// 2차 면접
                private int thirdInterview; // 3차 면접
                private int codingTest;     // 코딩테스트
                private int passed;         // 합격
                // + getter / setter
            }

          [DashboardServlet.java]
            StageCountVO stageCounts = new StageCountVO();
            stageCounts.setApplied(appDao.countByStage("APPLIED", memberId));
            stageCounts.setDocumentPass(appDao.countByStage("DOCUMENT_PASS", memberId));
            stageCounts.setFirstInterview(appDao.countByStage("FIRST_INTERVIEW", memberId));
            stageCounts.setSecondInterview(appDao.countByStage("SECOND_INTERVIEW", memberId));
            stageCounts.setThirdInterview(appDao.countByStage("THIRD_INTERVIEW", memberId));
            stageCounts.setCodingTest(appDao.countByStage("CODING_TEST", memberId));
            stageCounts.setPassed(appDao.countByStage("PASSED", memberId));
            request.setAttribute("stageCounts", stageCounts);
        --%>
        <div class="stage-strip">

            <a href="application-list?stage=APPLIED" class="stage-chip" style="--chip-color:#9da3b8">
                <span class="stage-chip-icon">📄</span>
                <span class="stage-chip-name">이력서 제출</span>
                <span class="stage-chip-count">${stageCounts.applied}</span>
                <span class="stage-chip-unit">회</span>
            </a>

            <a href="application-list?stage=DOCUMENT_PASS" class="stage-chip" style="--chip-color:#ffd166">
                <span class="stage-chip-icon">✅</span>
                <span class="stage-chip-name">서류 합격</span>
                <span class="stage-chip-count">${stageCounts.documentPass}</span>
                <span class="stage-chip-unit">회</span>
            </a>

            <a href="application-list?stage=FIRST_INTERVIEW" class="stage-chip" style="--chip-color:#4ecdc4">
                <span class="stage-chip-icon">🗣</span>
                <span class="stage-chip-name">1차 면접</span>
                <span class="stage-chip-count">${stageCounts.firstInterview}</span>
                <span class="stage-chip-unit">회</span>
            </a>

            <a href="application-list?stage=SECOND_INTERVIEW" class="stage-chip" style="--chip-color:#5b7cf8">
                <span class="stage-chip-icon">💬</span>
                <span class="stage-chip-name">2차 면접</span>
                <span class="stage-chip-count">${stageCounts.secondInterview}</span>
                <span class="stage-chip-unit">회</span>
            </a>

            <a href="application-list?stage=THIRD_INTERVIEW" class="stage-chip" style="--chip-color:#8b6ef5">
                <span class="stage-chip-icon">🔮</span>
                <span class="stage-chip-name">3차 면접</span>
                <span class="stage-chip-count">${stageCounts.thirdInterview}</span>
                <span class="stage-chip-unit">회</span>
            </a>

            <a href="application-list?stage=PASSED" class="stage-chip" style="--chip-color:#56e39f">
                <span class="stage-chip-icon">🎉</span>
                <span class="stage-chip-name">합격</span>
                <span class="stage-chip-count">${stageCounts.passed}</span>
                <span class="stage-chip-unit">회</span>
            </a>

        </div>

        <!-- ── 2×2 카드 그리드 ────────────────────────── -->
        <div class="dash-grid">

            <!-- ① 전환율 파이프라인 ─────────────────────── -->
            <%--
              Java에서 List<Map> funnelData 전달.
              각 Map: { fromLabel, toLabel, fromColor, toColor, pct }
            --%>
            <div class="card dash-card" style="animation-delay:.05s">
                <div class="card-title">전환율 파이프라인</div>

                <c:choose>
                    <c:when test="${empty funnelData}">
                        <div class="empty-state">
                            <div class="icon">📊</div>
                            <p>지원 데이터가 없습니다</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="row" items="${funnelData}">
                            <div class="funnel-row">
                                <div class="funnel-labels">
                                    <span>${row.fromLabel} → ${row.toLabel}</span>
                                    <span style="color:${row.toColor}; font-weight:600">${row.pct}%</span>
                                </div>
                                <div class="funnel-track">
                                    <div class="funnel-fill"
                                         style="width:${row.pct}%;
                                                 background: linear-gradient(90deg, ${row.fromColor}, ${row.toColor})">
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- ② 예정 일정 ──────────────────────────────── -->
            <%--
              Java에서 List<ScheduleVO> upcomingSchedules 전달 (최대 5건, 오늘 이후).
              ScheduleVO 필드: scheduleId, company, type, date(String "MM월 dd일"), day(int), month(String), time, isToday(boolean)
            --%>
            <div class="card dash-card" style="animation-delay:.1s">
                <div class="card-title">
                    예정 일정
                    <a href="schedule-list"
                       style="margin-left:auto; font-size:11px; color:var(--accent);
                    letter-spacing:0; text-transform:none; font-weight:400">
                        전체 보기 →
                    </a>
                </div>

                <c:choose>
                    <c:when test="${empty upcomingSchedules}">
                        <div class="empty-state">
                            <div class="icon">📅</div>
                            <p>예정된 일정이 없어요</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="s" items="${upcomingSchedules}">
                            <div class="sch-row ${s.isToday ? 'sch-today' : ''}">

                                    <%-- 날짜 박스 --%>
                                <div class="sch-date">
                                    <div class="sch-mon">${s.month}</div>
                                    <div class="sch-day">${s.day}</div>
                                </div>

                                <div class="sch-vline"></div>

                                    <%-- 회사명 + 시간 --%>
                                <div class="sch-info">
                                    <div class="sch-company">${s.company}</div>
                                    <div class="sch-time">⏰ ${empty s.time ? '시간 미정' : s.time}</div>
                                </div>

                                    <%-- 유형 배지: 색상은 Java에서 badgeBg, badgeColor 로 계산해서 전달 --%>
                                <span class="badge"
                                      style="background:${s.badgeBg}; color:${s.badgeColor}">
                                        ${s.type}
                                </span>

                            </div>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- ③ 최근 TIL ──────────────────────────────── -->
            <%--
              Java에서 List<TilVO> recentTils 전달 (최대 5건).
              TilVO 필드: tilId, title, tag, tagColor, tagBg, timeAgo
            --%>
            <div class="card dash-card" style="animation-delay:.15s">
                <div class="card-title">
                    최근 TIL
                    <a href="til-list"
                       style="margin-left:auto; font-size:11px; color:var(--accent);
                    letter-spacing:0; text-transform:none; font-weight:400">
                        전체 보기 →
                    </a>
                </div>

                <c:choose>
                    <c:when test="${empty recentTils}">
                        <div class="empty-state">
                            <div class="icon">📖</div>
                            <p>TIL을 작성해 보세요</p>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="t" items="${recentTils}" varStatus="vs">
                            <a href="til-detail?tilId=${t.tilId}" class="til-row">
                                <span class="til-num">${vs.count < 10 ? '0' : ''}${vs.count}</span>
                                <div class="til-dot" style="background:${t.tagColor}"></div>
                                <div class="til-info">
                                    <div class="til-title">${t.title}</div>
                                    <div class="til-meta">
                    <span class="badge"
                          style="background:${t.tagBg}; color:${t.tagColor};
                                  font-size:10px; padding:2px 7px">
                            ${t.tag}
                    </span>
                                        <span>${t.timeAgo}</span>
                                    </div>
                                </div>
                                <span class="til-arrow">›</span>
                            </a>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>

            <!-- ④ 이번 달 학습 분포 ──────────────────────── -->
            <%--
              Java에서 List<Map> tilTagStats 전달.
              각 Map: { tag, color, pct }
              도넛 차트는 나중에 Canvas로 그릴 자리.
              지금은 범례만 표시.
            --%>
            <div class="card dash-card" style="animation-delay:.2s">
                <div class="card-title">이번 달 학습 분포</div>

                <div class="chart-wrap">

                    <%-- 기존 donut-placeholder 제거하고 아래로 교체 --%>
                    <div class="donut-wrap" style="position:relative; width:140px; height:140px; flex-shrink:0;">
                        <canvas id="donutCanvas" width="140" height="140"></canvas>
                        <div class="donut-center-text"
                             style="position:absolute;inset:0;display:flex;flex-direction:column;align-items:center;justify-content:center;pointer-events:none;">
                            <div class="donut-num">${tilTagStats.size()}</div>
                            <div class="donut-label">총 항목</div>
                        </div>
                    </div>

                    <%-- 범례 --%>
                    <div class="chart-legend">
                        <c:choose>
                            <c:when test="${empty tilTagStats}">
                                <p style="font-size:12px; color:var(--text3)">학습 데이터가 없어요</p>
                            </c:when>
                            <c:otherwise>
                                <c:forEach var="stat" items="${tilTagStats}">
                                    <div class="legend-row">
                                        <div class="legend-dot" style="background:${stat.color}"></div>
                                        <span class="legend-name">${stat.tag}</span>
                                        <span class="legend-pct" style="color:${stat.color}">${stat.pct}%</span>
                                    </div>
                                </c:forEach>
                            </c:otherwise>
                        </c:choose>
                    </div>

                </div>
            </div>

        </div>
        <%-- /.dash-grid --%>
    </main>

</div>
<script>
    (function () {
        var stats = [
            <c:forEach var="stat" items="${tilTagStats}" varStatus="loop">
            {tag: "${stat.tag}", color: "${stat.color}", pct: ${stat.pct}}<c:if test="${!loop.last}">, </c:if>
            </c:forEach>
        ];

        if (!stats.length) return;

        var canvas = document.getElementById("donutCanvas");
        var ctx = canvas.getContext("2d");
        var cx = 70, cy = 70, outerR = 62, innerR = 38, gap = 0.03;
        var total = stats.reduce(function (a, s) {
            return a + s.pct;
        }, 0);
        var startAngle = -Math.PI / 2;

        stats.forEach(function (s) {
            var slice = (s.pct / total) * (Math.PI * 2);
            var endAngle = startAngle + slice - gap;
            ctx.beginPath();
            ctx.moveTo(cx + outerR * Math.cos(startAngle + gap / 2),
                cy + outerR * Math.sin(startAngle + gap / 2));
            ctx.arc(cx, cy, outerR, startAngle + gap / 2, endAngle);
            ctx.arc(cx, cy, innerR, endAngle, startAngle + gap / 2, true);
            ctx.closePath();
            ctx.fillStyle = s.color;
            ctx.fill();
            startAngle += slice;
        });
    })();
</script>
<%-- /.page-wrap --%>
</body>
</html>
