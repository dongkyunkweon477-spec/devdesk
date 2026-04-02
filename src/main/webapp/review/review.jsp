<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시판 목록 레이아웃</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/review-board.css">
</head>
<body>
<<<<<<< HEAD

<div class="board-container">
    <%-- 임시로 5개의 항목을 반복 출력하는 부분 --%>
    <% for(int i=1; i<=5; i++) { %>
    <div class="card">
        <div class="card-header">
            <div>
                <span class="badge-new">NEW</span>
                <span>27卒 / 理系 / 男性</span>
            </div>
            <div>[북마크] 0</div>
        </div>

        <h2 class="card-title">1次面接通過した学生の就活速報</h2>

        <div class="card-body">
            <div class="avatar"></div>
            <div class="info-grid">
                <div class="info-row">
                    <span class="info-label">面接官/学生</span>
                    <span class="info-value">面接官 2人 / 学生 1人</span>
                    <span class="info-label">連絡方法</span>
                    <span class="tag">メール</span>
                    <span class="tag">3日以内</span>
                </div>
                <div class="info-row">
                    <span class="info-label">雰囲気</span>
                    <span class="info-value">厳か</span>
                    <span class="info-label">質問内容</span>
                    <span class="tag">その他</span>
                    <span class="tag">なぜこの会社か？</span>
                </div>
            </div>
        </div>

        <div class="read-more-container">
            <button class="read-more-btn">続きを読む</button>
        </div>

        <div class="card-footer">
            <div>[추천] 0</div>
            <div class="footer-right">
                <span>公開日：2026年4月1日</span>
                <span>問題を報告する</span>
            </div>
        </div>
    </div>
    <% } %>

    <%-- 하단 페이징 영역 (디자인용) --%>
    <div class="pagination">
        <button class="page-btn">이전</button>
        <button class="page-btn active">1</button>
        <button class="page-btn">2</button>
        <button class="page-btn">3</button>
        <button class="page-btn">4</button>
        <button class="page-btn">5</button>
        <button class="page-btn">다음</button>
    </div>
</div>

=======
<h1>review page~~~~</h1>
>>>>>>> 5dfd6c4f8f2b2b86297ebf7782151d70f469e637
</body>
</html>