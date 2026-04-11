// $(".test").click(function () {
//     console.log($(".test").val());
//
//     if ($(".test").val() == 'night mode') {
//         $('body').css("background-color", "black").css("color", "#7c3aed");
//         $(".test").val("day mode");
//         $(".test").text("day mode"); // value가 없는 버튼은 text로 글자를 바꿔주면 됨
//     } else {
//         $('body').css("background-color", "white").css("color", "#7c3aed");
//         $(".test").val("night mode");
//         $(".test").text("night mode"); // value가 없는 버튼은 text로 글자를 바꿔주면 됨
//     }
// });
//
// $(document).ready(function () {
//     // 저장된 테마 불러오기
//     const savedTheme = localStorage.getItem('theme') || 'light';
//     applyTheme(savedTheme);
//
//     $(".test").click(function () {
//         const currentTheme = $("html").attr("data-theme") || 'light';
//         const newTheme = currentTheme === 'light' ? 'dark' : 'light';
//         applyTheme(newTheme);
//     });
//
//     function applyTheme(theme) {
//         $("html").attr("data-theme", theme);
//         localStorage.setItem('theme', theme);
//
//         if (theme === 'dark') {
//             $(".test").text("day mode");
//         } else {
//             $(".test").text("night mode");
//         }
//     }
// });

$(document).ready(function () {
    const savedTheme = localStorage.getItem('theme') || 'light';
    applyTheme(savedTheme);

    // .test 버튼을 클릭했을 때 아이콘과 테마 변경
    $(".test").click(function () {
        const currentTheme = $("html").attr("data-theme") || 'light';
        const newTheme = currentTheme === 'light' ? 'dark' : 'light';
        applyTheme(newTheme);
    });

    function applyTheme(theme) {
        $("html").attr("data-theme", theme);
        localStorage.setItem('theme', theme);

        // 아이콘 제어를 위해 버튼에도 상태 클래스를 추가해줍니다.
        if (theme === 'dark') {
            $(".test").addClass("is-dark");
        } else {
            $(".test").removeClass("is-dark");
        }
    }
});

