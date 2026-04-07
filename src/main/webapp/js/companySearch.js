$(function () {
    var totalCompanyCount = parseInt($('#resultCount').text()) || 0;
    initToggleBtns();
    initSearch();
    initClear(totalCompanyCount);
    // doSearch();
    $('#resultArea').html('<div class="cs-no-result">검색 조건을 선택하고 검색 버튼을 눌러주세요.</div>');
});

/* ===== 토글 버튼 (업종, 지역) ===== */
function initToggleBtns() {
    $(document).on('click', '.cs-opt-btn', function () {
        console.log('clicked:', $(this).attr('data-value'));
        $(this).closest('.cs-options').find('.cs-opt-btn').removeClass('active');
        $(this).addClass('active');
    });
}

/* ===== 검색 ===== */
function initSearch() {
    $('#searchBtn').on('click', function () {
        doSearch();
    });

    // 엔터키
    $('#companyName').on('keypress', function (e) {
        if (e.which === 13) doSearch();
    });
}

function doSearch() {
    var activeIndustry = $('#industryBtns .cs-opt-btn.active');
    var activeLocation = $('#locationBtns .cs-opt-btn.active');

    var industry = $('#industryBtns .cs-opt-btn.active').attr('data-value') || '';
    var location = $('#locationBtns .cs-opt-btn.active').attr('data-value') || '';

    var contextPath = $('#contextPath').val();
    $.ajax({
        url: contextPath + '/company-search/ajax',
        dataType: 'json',
        data: {
            companyName: $('#companyName').val(),
            companyIndustry: industry,
            companyLocation: location,
            minRating: $('#minRating').val(),
            maxRating: $('#maxRating').val(),
            minSize: $('#minSize').val(),
            maxSize: $('#maxSize').val()
        }
    }).done(function (data) {
        $('#resultCount').text(data.length);
        showResult(data);
    }).fail(function (xhr, status, error) {
        console.error('Search failed: ' + error);
    });
}

/* ===== 결과 카드 렌더링 ===== */
function showResult(data) {
    var container = $('#resultArea');
    container.empty();

    if (!data || data.length === 0) {
        container.html('<div class="cs-no-result">검색 결과가 없습니다.</div>');
        return;
    }

    var role = $('#userRole').val();
    var html = '<div class="cs-grid">';

    $.each(data, function (i, c) {
        var stars = '';
        for (var s = 1; s <= 5; s++) {
            stars += '<span class="' + (s <= c.companyRating ? 'on' : '') + '">★</span>';
        }

        var editLink = '';
        if (role === 'ADMIN') {
            editLink = '<span class="cs-action" onclick="event.preventDefault(); location.href=\'/company/edit?companyId=' + c.companyId + '\'">수정</span>';
        }

        html += '<a class="cs-card" href="/review/company?companyId=' + c.companyId + '">'
            + '<div class="cs-card-top">'
            + '  <div class="cs-logo">' + c.companyName.substring(0, 1) + '</div>'
            + '  <div class="cs-info">'
            + '    <p class="cs-name">' + c.companyName + '</p>'
            + '    <span class="cs-industry-text">' + c.companyIndustry + ' · ' + c.companyLocation + '</span>'
            + '  </div>'
            + '</div>'
            + '<div class="cs-card-body">'
            + '  <div class="cs-meta-item">'
            + '    <span class="cs-meta-label">평점</span>'
            + '    <span class="cs-stars">' + stars + '</span>'
            + '  </div>'
            + '  <div class="cs-meta-item">'
            + '    <span class="cs-meta-label">규모</span>'
            + '    <span class="cs-meta-value">' + c.companySize.toLocaleString() + '명</span>'
            + '  </div>'
            + '  <div class="cs-actions">'
            + editLink
            + '    <span class="cs-action primary" onclick="event.preventDefault(); location.href=\'/review/write?companyId=' + c.companyId + '\'">후기쓰기</span>'
            + '  </div>'
            + '</div>'
            + '</a>';
    });

    html += '</div>';
    container.html(html);
}

/* ===== 조건 초기화 ===== */
function initClear(totalCompanyCount) {
    $('#clearBtn').on('click', function () {
        // 모든 입력 필드 및 버튼 상태 초기화
        $('.cs-options').each(function () {
            $(this).find('.cs-opt-btn').removeClass('active');
            $(this).find('.cs-opt-btn').first().addClass('active');
        });
        $('#companyName').val('');
        $('#minRating').val('0');
        $('#maxRating').val('5.0');
        $('#minSize').val('');
        $('#maxSize').val('');

        // 결과 영역을 초기 상태 메시지로 변경
        $('#resultArea').html('<div class="cs-no-result">검색 조건을 선택하고 검색 버튼을 눌러주세요.</div>');

        // 개수 표시를 초기 전체 개수로 복구
        $('#resultCount').text(totalCompanyCount);
    });

}
