$(function () {
    initStarRating();
    initCharCount();
    initFormSubmit();
});

/* ===== 별점 클릭 ===== */
function initStarRating() {
    const labels = ['', '매우 쉬움', '쉬움', '보통', '어려움', '매우 어려움'];

    $('.difficulty-stars .star').on('click', function () {
        const val = $(this).data('value');
        $('#difficulty').val(val);

        $('.difficulty-stars .star').each(function () {
            $(this).toggleClass('active', $(this).data('value') <= val);
        });

        $('#difficultyLabel').text(labels[val]);
    });

    // 호버 미리보기
    $('.difficulty-stars .star').on('mouseenter', function () {
        const val = $(this).data('value');
        $('.difficulty-stars .star').each(function () {
            $(this).toggleClass('active', $(this).data('value') <= val);
        });
    });

    $('.difficulty-stars').on('mouseleave', function () {
        const selected = parseInt($('#difficulty').val()) || 0;
        $('.difficulty-stars .star').each(function () {
            $(this).toggleClass('active', $(this).data('value') <= selected);
        });
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
            { id: '#title', msg: '제목을 입력하세요' },
            { id: '#jobPosition', msg: '지원 직무를 입력하세요' },
            { id: '#interviewType', msg: '면접 유형을 선택하세요' },
            { id: '#result', msg: '전형 결과를 선택하세요' },
            { id: '#difficulty', msg: '난이도를 선택하세요' }
        ];

        required.forEach(function (field) {
            if (!$(field.id).val()) {
                $(field.id).closest('.field-group').addClass('error');
                hasError = true;
            }
        });

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
    });
}
