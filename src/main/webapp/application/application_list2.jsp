<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


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
    </div>
</c:forEach>