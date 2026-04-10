<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<%-- 🌟 분리한 외부 CSS 파일 연결 --%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin/admin.css">

<%-- 🌟 Chart.js 라이브러리 연결 --%>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>

<%-- 관리자 화면 뼈대 시작 --%>
<div class="admin-wrapper">

    <div class="admin-sidebar">
        <h3>Admin Panel</h3>
        <ul>
            <li><a href="${pageContext.request.contextPath}/admin" class="active">📊 대시보드</a></li>
            <li><a href="#">👥 회원 관리</a></li>
            <li><a href="#">📝 게시글 관리</a></li>
            <li><a href="#">🏢 기업 정보 관리</a></li>
        </ul>
    </div>

    <div class="admin-content">
        <h2>📊 관리자 대시보드</h2>
        <p style="color: #64748b; margin-bottom: 30px;">DevDesk의 실시간 서비스 현황입니다.</p>

        <div class="dashboard-layout">

            <div class="dashboard-cards">
                <div class="card">
                    <div class="card-title">총 가입자 수</div>
                    <div class="card-value">${totalMembers} 명</div>
                </div>

                <div class="card">
                    <div class="card-title">총 커뮤니티 게시글</div>
                    <div class="card-value">${totalBoards} 개</div>
                </div>

                <div class="card">
                    <div class="card-title">오늘의 신규 가입</div>
                    <div class="card-value" style="color: var(--success-color);">+ ${todayNewMembers} 명</div>
                </div>
            </div>

            <div class="chart-container">
                <h3 class="chart-title">📈 가입자 트렌드 (최근 7일)</h3>
                <div class="chart-canvas-wrapper">
                    <canvas id="memberTrendChart"></canvas>
                </div>
            </div>

            <div class="chart-container">
                <h3 class="chart-title">👥 직무 카테고리 분포</h3>
                <div class="chart-canvas-wrapper">
                    <canvas id="jobDistributionChart"></canvas>
                </div>
            </div>

            <div class="latest-members-section">
                <h3 style="margin-bottom: 15px; color: #334155;">최근 가입한 회원 (Top 5)</h3>
                <table class="admin-table">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>닉네임</th>
                        <th>직무</th>
                        <th>권한</th>
                        <th>가입일</th>
                        <th>관리</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="m" items="${members}" begin="0" end="4">
                        <tr>
                            <td>${m.member_id}</td>
                            <td class="nickname">${m.nickname}</td>
                            <td>${m.job_category}</td>
                            <td class="${m.role == 'admin' ? 'role-admin' : ''}">${m.role}</td>
                            <td>${m.created_date}</td>
                            <td>
                                <button style="padding: 5px 10px; border:1px solid #e2e8f0; background:white; color:#475569; border-radius:4px; cursor:pointer;">
                                    상세 보기
                                </button>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

                <div style="text-align: center; margin-top: 20px;">
                    <a href="${pageContext.request.contextPath}/admin/member"
                       style="color: var(--primary-color); font-weight: 600; text-decoration: underline;">전체 회원 목록 보러가기
                        ➔</a>
                </div>
            </div>

        </div>
    </div>
</div>

<%-- 🌟 JavaScript 데이터 세팅 (서버 데이터를 JS 변수로 넘겨줌) --%>
<script>
    const chartData_memberTrend = [
        <c:forEach var="val" items="${memberTrend}" varStatus="status">
        ${val}${!status.last ? ',' : ''}
        </c:forEach>
    ];

    const chartData_jobDistribution = [
        <c:forEach var="val" items="${jobDistribution}" varStatus="status">
        ${val}${!status.last ? ',' : ''}
        </c:forEach>
    ];
</script>

<%-- 🌟 차트 그리는 외부 JS 파일 불러오기 --%>
<script src="${pageContext.request.contextPath}/js/admin_charts.js"></script>