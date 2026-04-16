$(function () {
    initStarRating('difficultyStars', 'difficulty', 'difficultyLabel',
        ['', '매우 쉬움', '쉬움', '보통', '어려움', '매우 어려움']);
    initStarRating('ratingStars', 'rating', 'ratingLabel',
        ['', '매우 불만', '불만', '보통', '만족', '매우 만족']);
    initCharCount();
    initFormSubmit();

});

/* ===== 별점 클릭 ===== */
function initStarRating(containerId, inputId, labelId, labels) {
    let selected = 0;

    $('#' + containerId + ' .star').on('click', function () {
        selected = $(this).data('value');
        $('#' + inputId).val(selected);
        updateStars(containerId, selected);
        $('#' + labelId).text(labels[selected]);
    });
    $('#' + containerId + ' .star').on('mouseenter', function () {
        updateStars(containerId, $(this).data('value'));
    });
    $('#' + containerId).on('mouseleave', function () {
        updateStars(containerId, selected);

    });
    let initVal = parseInt($('#' + inputId).val()) || 0;
    if (initVal) {
        selected = initVal;
        updateStars(containerId, selected);
        $('#' + labelId).text(labels[selected]);
    }
}

function updateStars(containerId, val) {
    $('#' + containerId + ' .star').each(function () {
        $(this).toggleClass('active', $(this).data('value') <= val);


    });
}

/* ===== 글자수 카운터 ===== */
function initCharCount() {
    $('#title').on('input', function () {
        $('#titleCount').text($(this).val().length);
    });

    $('#content').on('input', function () {
        const len = $(this).val().length;
        $('#contentCount').text(len);

        if (len > 0 && len < 50) {
            $('.char-min').addClass('warning').text('50자 이상 작성해주세요 (' + (50 - len) + '자 남음)');
        } else {
            $('.char-min').removeClass('warning').text('50자 이상 작성해주세요');
        }
    });
}

/* ===== 폼 유효성 검사 & 제출 ===== */
function initFormSubmit() {
    $('#reviewForm').on('submit', function (e) {
        // 에러 초기화
        $('.field-group').removeClass('error');

        let hasError = false;

        // 필수 필드 체크
        const required = [
            {id: '#title', msg: '제목을 입력하세요'},
            {id: '#jobPosition', msg: '지원 직무를 입력하세요'},
            {id: '#interviewType', msg: '면접 유형을 선택하세요'},
            {id: '#result', msg: '전형 결과를 선택하세요'},
            {id: '#difficulty', msg: '난이도를 선택하세요'}
        ];

        required.forEach(function (field) {
            if (!$(field.id).val()) {
                $(field.id).closest('.field-group').addClass('error');
                hasError = true;
            }
        });

        // 기업 선택 여부 (기업검색 모달에서 선택하는 경우)
        if ($('#selectedCompanyId').length && !$('#selectedCompanyId').val()) {
            $('#companySearchInput').closest('.field-group').addClass('error');
            hasError = true;
        }

        // 상세 후기 50자 이상
        if ($('#content').val().length < 50) {
            $('#content').closest('.field-group').addClass('error');
            $('.char-min').addClass('warning');
            hasError = true;
        }

        if (hasError) {
            e.preventDefault();
            // 첫 번째 에러 필드로 스크롤
            const firstError = $('.field-group.error').first();
            $('html, body').animate({
                scrollTop: firstError.offset().top - 100
            }, 300);
        }
        console.log('companyId:', $('input[name="companyId"]').val());
        console.log('title:', $('input[name="title"]').val());
        console.log('jobPosition:', $('input[name="jobPosition"]').val());
        console.log('interviewType:', $('select[name="interviewType"]').val());
        console.log('difficulty:', $('input[name="difficulty"]').val());
        console.log('result:', $('select[name="result"]').val());
        console.log('content:', $('textarea[name="content"]').val().length + '자');
        console.log('interviewerCount:', $('select[name="interviewerCount"]').val());
        console.log('studentCount:', $('select[name="studentCount"]').val());
        console.log('atmosphere:', $('select[name="atmosphere"]').val());
        console.log('contactMethod:', $('select[name="contactMethod"]').val());
        console.log('contactDays:', $('select[name="contactDays"]').val());


    });
}


$(function () {
    // 수정 모드일 때 기존 난이도 표시
    let initDiff = $('#difficulty').val();
    if (initDiff) {
        let labels = ['', '매우 쉬움', '쉬움', '보통', '어려움', '매우 어려움'];
        $('.difficulty-stars .star').each(function () {
            $(this).toggleClass('active', $(this).data('value') <= initDiff);
        });
        $('#difficultyLabel').text(labels[initDiff]);
    }
});