$('#companySearchInput').on('input', function () {
    const keyword = $(this).val().trim();
    if (keyword.length < 1) {
        $('#companyDropdown').hide();
        return;
    }

    $.ajax({
        url: contextPath + '/company-search/ajax',
        type: 'get',
        dataType: 'json',
        data: {companyName: keyword}
    }).done(function (data) {
        let html = '';
        $.each(data, function (i, c) {
            html += '<div class="dropdown-item" data-id="' + c.companyId
                + '" data-name="' + c.companyName + '">'
                + c.companyName
                + '<span class="dropdown-meta">' + c.companyIndustry + '</span>'
                + '</div>';
        });
        $('#companyDropdown').html(html).show();
    });
});

$(document).on('click', '.dropdown-item', function () {
    $('#selectedCompanyId').val($(this).data('id'));
    $('#companySearchInput').val($(this).data('name'));
    $('#companyDropdown').hide();
});