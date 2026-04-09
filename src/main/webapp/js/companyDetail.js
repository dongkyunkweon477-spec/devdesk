var currentFilterPage = 1;

$(document).ready(function () {
// 필터/정렬 변경 시
    $('#filterType, #filterResult, #sortOrder').on('change', function () {
        currentFilterPage = 1;
        loadFilteredReviews(1);
    });
});

function loadFilteredReviews(page) {
    currentFilterPage = page;

    $.ajax({
        url: contextPath + '/review/filter/ajax',
        type: 'get',
        dataType: 'json',
        data: {
            companyId: companyId,
            interviewType: $('#filterType').val(),
            result: $('#filterResult').val(),
            sort: $('#sortOrder').val(),
            page: page
        }
    }).done(function (data) {
        renderReviewCards(data.reviews);
        renderReviewPaging(data.totalPages);
        $('.cd-count').text('총 ' + data.totalCount + '건의 후기');
    });
}

function renderReviewCards(reviews) {
    var container = $('#reviewListArea');
    container.empty();

    if (!reviews || reviews.length === 0) {
        container.html('<div class="cd-no-result">조건에 맞는 후기가 없습니다.</div>');
        return;
    }

    var html = '';
    $.each(reviews, function (i, r) {
        var stars = '';
        for (var s = 1; s <= 5; s++) {
            stars += '<span class="star-sm ' + (s <= r.reviewDifficulty ? 'on' : '') + '">★</span>';
        }

        var typeText = {'CODING': '코딩테스트', 'TECH': '기술면접', 'PERSONAL': '인성면접', 'EXEC': '임원면접', 'GROUP': '그룹면접'};
        var resultText = {'PASS': '합격', 'FAIL': '불합격', 'PENDING': '대기중'};
        var atmosText = {'FRIENDLY': '화기애애', 'NORMAL': '보통', 'SERIOUS': '엄숙', 'PRESSURE': '압박'};

        var tags = '<span class="cd-tag">' + (typeText[r.reviewInterviewType] || r.reviewInterviewType) + '</span>';
        if (r.reviewInterviewerCount > 0) {
            tags += '<span class="cd-tag">면접관 ' + r.reviewInterviewerCount + '명</span>';
        }
        if (r.reviewAtmosphere) {
            tags += '<span class="cd-tag">' + (atmosText[r.reviewAtmosphere] || r.reviewAtmosphere) + '</span>';
        }
        tags += '<span class="cd-tag result-' + r.reviewResult + '">' + (resultText[r.reviewResult] || r.reviewResult) + '</span>';

        var dateStr = '';
        if (r.reviewCreatedDate) {
            var d = new Date(r.reviewCreatedDate);
            dateStr = d.getFullYear() + '년 ' + (d.getMonth() + 1) + '월 ' + d.getDate() + '일';
        }

        html += '<div class="cd-card">'
            + '<div class="cd-card-top">'
            + '  <span class="cd-card-position">' + r.reviewJobPosition + '</span>'
            + '  <span class="cd-card-stars">' + stars + '</span>'
            + '</div>'
            + '<a href="' + contextPath + '/review/detail?reviewId=' + r.reviewId + '" class="cd-card-title">' + r.reviewTitle + '</a>'
            + '<div class="cd-card-tags">' + tags + '</div>'
            + '<div class="cd-card-bottom">'
            + '  <span>[추천] ' + r.reviewLikeCount + '</span>'
            + '  <span>' + dateStr + '</span>'
            + '</div>'
            + '</div>';
    });

    container.html(html);
}

function renderReviewPaging(totalPages) {
    var pagingArea = $('#reviewPaging');
    pagingArea.empty();

    if (totalPages <= 1) return;

    var html = '';
    if (currentFilterPage > 1) {
        html += '<a class="cd-page" onclick="loadFilteredReviews(' + (currentFilterPage - 1) + ')">이전</a>';
    }
    for (var i = 1; i <= totalPages; i++) {
        html += '<a class="cd-page ' + (i === currentFilterPage ? 'active' : '') + '" onclick="loadFilteredReviews(' + i + ')">' + i + '</a>';
    }
    if (currentFilterPage < totalPages) {
        html += '<a class="cd-page" onclick="loadFilteredReviews(' + (currentFilterPage + 1) + ')">다음</a>';
    }
    pagingArea.html(html);
}
