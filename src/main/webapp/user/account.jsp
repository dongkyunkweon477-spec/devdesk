<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>DevDesk - 회원가입</title>
</head>
<body>
<h2>회원가입</h2>
<form action="account" method="post">
    <label>이메일 (아이디) :</label>
    <input type="email" name="email" id="email" placeholder="example@gmail.com" required>
    <button type="button" onclick="checkId()">중복확인</button>
    <span id="idCheckMsg"></span>
    </div>

    <div>
        <label>비밀번호 :</label>
        <input type="password" name="password" placeholder="비밀번호 입력(문자, 숫자, 특수문자 포함 8~20자" required>
    </div>

    <div>
        <label>닉네임 :</label>
        <input type="text" name="nickname" required>
    </div>

    <div>
        <label>관심 직무 :</label>
        <select name="jobCategory">
            <option value="프론트엔드">프론트엔드</option>
            <option value="백엔드">백엔드</option>
            <option value="데이터/AI">데이터/AI</option>
            <option value="기획/디자인">기획/디자인</option>
        </select>
    </div>

    <br>
    <button type="submit">가입하기</button>


</form>

</body>

<script>
    // [중복확인] 버튼을 누르면 이 함수가 실행됩니다.
    function checkId() {
        let email = document.getElementById("email").value;
        let msgSpan = document.getElementById("idCheckMsg"); // 메시지가 뜰 <span> 태그

        // 빈칸 검사
        if(email === "") {
            alert("이메일을 먼저 입력해주세요!");
            return;
        }

        // 서버의 /user-checkId 주소로 이메일을 몰래 보냅니다 (AJAX 통신)
        fetch('user-checkId', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded', // 폼 데이터를 보낸다는 뜻
            },
            body: 'email=' + encodeURIComponent(email) // 이메일을 담아서 보냄
        })
            .then(response => response.text()) // 서버(CheckIdC)가 뱉어낸 숫자(0 또는 1)를 받습니다.
            .then(data => {
                // 받은 숫자에 따라 화면의 글씨를 바꿔줍니다!
                if (data.trim() === "1") {
                    msgSpan.style.color = "red";
                    msgSpan.innerText = "이미 사용 중인 이메일입니다.";
                } else {
                    msgSpan.style.color = "green";
                    msgSpan.innerText = "사용 가능한 이메일입니다.";
                }
            })
            .catch(error => {
                console.error('에러 발생:', error);
                alert("중복 체크 중 문제가 발생했습니다.");
            });
    }
</script>

</html>
