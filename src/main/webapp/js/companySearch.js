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
    let tbody = $('#resultBody');
    tbody.empty();

    let html = '';
    $.each(data, function (i, c) {
        html += `<tr>
            <td>${c.companyName}</td>
            <td>${c.companyIndustry}</td>
            <td>${c.companyLocation}</td>
            <td>${c.companyRating}</td>
            <td>${c.companySize}명</td>
            <td><a href="/review/write?companyId=${c.companyId}" class="write-link">면접 후기 쓰기"</a></td>
            
        </tr>`;
    });
    tbody.html(html);
}