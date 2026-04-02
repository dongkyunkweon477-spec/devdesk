<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="utf-8">
    <title>DevDesk - 회원가입</title>
</head>
<body>
<h2>회원가입</h2>
<form action="user-signup" method="post">
    <label>이메일 (아이디) :</label>
    <input type="email" name="email" id="email" placeholder="example@gmail.com" required>
    <button type="button" onclick="checkId()">중복확인</button>
    <span id="idCheckMsg"></span>
    </div>

    <div>
        <label>비밀번호 :</label>
        <input type="password" name="password" placeholder="대소문자+숫자 조합" required>
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
    // 이메일 중복 체크 버튼을 누르면 실행될 자바스크립트 함수 (AJAX 연동 예정)
    function checkId() {
        let email = document.getElementById("email").value;
        if(email === "") {
            alert("이메일을 먼저 입력해주세요!");
            return;
        }
        alert("입력하신 이메일 '" + email + "' 중복 체크 기능을 연결할 예정입니다!");
        // TODO: 여기에 userC로 중복 체크 요청을 보내는 AJAX 코드가 들어갈 자리입니다.
    }
</script>

</html>
