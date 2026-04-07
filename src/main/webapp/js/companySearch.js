$(function () {
    searchCompany();
})

function searchCompany() {
    $('#searchBtn').on('click', function () {
        $.ajax({
            url: '/company-search/ajax',
            type: 'get',
            dataType: 'json',
            data: {
                companyName: $('#companyName').val(),
                companyIndustry: $('#companyIndustry').val(),
                companyLocation: $('#companyLocation').val(),
                minRating: $('#minRating').val(),
                maxRating: $('#maxRating').val(),
                minSize: $('#minSize').val(),
                maxSize: $('#maxSize').val()
            }
        }).done(function (data) {
            let tBody = $('#resultBody');
            tBody.empty();
            if (data && data.length > 0) {
                $('#resultTable').show();
                showResult(data);
            } else {
                $('#resultTable').hide();
            }
        }).fail(function (xhr, status, error) {
            console.error("Search failed: " + error);
        });
    });
}

function showResult(data) {
    let container = $('#resultArea');
    container.empty();

    if (!data || data.length === 0) {
        container.html('<div class="no-result">검색 결과가 없습니다.</div>');
        return;
    }

    let role = $('#userRole').val();
    let html = '<div class="cs-grid">';

    $.each(data, function (i, c) {
        let stars = '';
        for (let s = 1; s <= 5; s++) {
            stars += '<span class="' + (s <= c.companyRating ? 'on' : '') + '">★</span>';
        }

        let editLink = '';
        if (role === 'ADMIN') {
            editLink = '<span class="cs-action" onclick="event.preventDefault(); location.href=\'/company/edit?companyId=' + c.companyId + '\'">수정</span>';
        }

        html += '<a class="cs-card" href="/company-detail?companyId=' + c.companyId + '">'
            + '<div class="cs-card-top">'
            + '  <div class="cs-logo">' + c.companyName.substring(0, 1) + '</div>'
            + '  <div class="cs-info">'
            + '    <p class="cs-name">' + c.companyName + '</p>'
            + '    <span class="cs-industry">' + c.companyIndustry + ' · ' + c.companyLocation + '</span>'
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

function confirmDelete(companyId) {
    document.getElementById('deleteCompanyId').value = companyId;
    document.getElementById('deleteModal').style.display = 'flex';
}