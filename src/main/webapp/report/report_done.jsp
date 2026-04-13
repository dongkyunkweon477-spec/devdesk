<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="../css/report/report.css">

<div style="display: flex; flex-direction: column; align-items: center; justify-content: center;
            padding: 80px 20px; text-align: center;">

    <c:choose>
        <c:when test="${param.duplicate == 'true'}">
            <div style="font-size: 48px; margin-bottom: 20px;">⚠️</div>
            <h2 style="font-size: 22px; font-weight: 700; color: #d97706; margin-bottom: 12px;">
                이미 신고한 게시물입니다.
            </h2>
            <p style="font-size: 14px; color: var(--text-tertiary, #888); line-height: 1.8; margin-bottom: 32px;">
                동일한 게시물에 대한 중복 신고는 접수되지 않습니다.
            </p>
        </c:when>
        <c:otherwise>
            <div style="font-size: 48px; margin-bottom: 20px;">✅</div>
            <h2 style="font-size: 22px; font-weight: 700; color: var(--text-primary, #1a1a1a); margin-bottom: 12px;">
                신고가 접수되었습니다.
            </h2>
            <p style="font-size: 14px; color: var(--text-tertiary, #888); line-height: 1.8; margin-bottom: 32px;">
                신고 내용은 관리자 검토 후 처리됩니다.<br>
                허위 신고 시 서비스 이용이 제한될 수 있습니다.
            </p>
        </c:otherwise>
    </c:choose>

    <div style="display: flex; gap: 12px;">
        <button class="rd-nav-btn" onclick="history.go(-2)"
                style="padding: 10px 24px; border: 1px solid #ddd; border-radius: 6px;
                       background: #fff; cursor: pointer; font-size: 14px;">
            이전 페이지로
        </button>
        <a href="${pageContext.request.contextPath}/main"
           style="padding: 10px 24px; border-radius: 6px; background: #7c3aed; color: #fff;
                  text-decoration: none; font-size: 14px; font-weight: 600;">
            홈으로
        </a>
    </div>
</div>