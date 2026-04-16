$(document).ready(function () {
    $('#likeBtn').on('click', function () {
        let reviewId = $(this).data('id');

        $.ajax({
            url: contextPath + '/review/like',
            type: 'post',
            data: {reviewId: reviewId},
            dataType: 'json'
        }).done(function (data) {
            // 성공적으로 처리된 경우
            $('#likeCount').text(data.count);
            if (data.liked) {
                $('#likeBtn').addClass('liked');
            } else {
                $('#likeBtn').removeClass('liked');
            }
        }).fail(function (xhr, status, error) {
            // 서버에서 에러(401 등)를 반환한 경우
            if (xhr.status === 401) {
                alert("로그인이 필요한 기능입니다.");
                // 필요시 로그인 페이지로 이동하는 로직 추가: window.location.href = contextPath + '/user/login.jsp';
            } else {
                alert("추천 처리 중 오류가 발생했습니다.");
            }
        });
    });




});