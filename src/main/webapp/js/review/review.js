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


    $('#bookmarkBtn').on('click', function () {
        let reviewId = $(this).data('id');

        $.ajax({
            url: contextPath + '/review/bookmark', // 북마크 컨트롤러 주소
            type: 'post',
            data: {reviewId: reviewId},
            dataType: 'json'
        }).done(function (data) {
            // 성공적으로 처리된 경우 화면 갱신
            $('#bookmarkCount').text(data.count);

            // 서버에서 반환한 bookmarked 값(true/false)에 따라 UI 클래스 토글
            if (data.bookmarked) {
                $('#bookmarkBtn').addClass('bookmarked');
            } else {
                $('#bookmarkBtn').removeClass('bookmarked');
            }
        }).fail(function (xhr, status, error) {
            // 서버에서 401(Unauthorized) 에러 등을 반환한 경우
            if (xhr.status === 401) {
                alert("로그인이 필요한 기능입니다.");
            } else {
                alert("북마크 처리 중 오류가 발생했습니다.");
            }
        });
    });


});