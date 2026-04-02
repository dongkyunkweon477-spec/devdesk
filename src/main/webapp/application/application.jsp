<%@ page language="java" contentType="text/html; charset=UTF-8"
          pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>지원 등록</title>

    <style>
        #interviewSection {
            transition: all 0.3s ease;
        }

        body {
            font-family: Arial;
            background-color: #f5f6f7;
        }
        .container {
            width: 500px;
            margin: 50px auto;
            background: white;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 {
            text-align: center;
        }
        label {
            display: block;
            margin-top: 15px;
        }
        input, select, textarea {
            width: 100%;
            padding: 10px;
            margin-top: 5px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }
        button {
            margin-top: 20px;
            width: 100%;
            padding: 12px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
        }
        button:hover {
            background-color: #45a049;
        }
    </style>

</head>
<body>

<div class="container">
    <h2>지원 등록</h2>

    <form action="application-insert" method="post">

        <!-- 회사 선택 -->
        <label>회사</label>
        <select name="company_id" required>
            <option value="">-- 선택 --</option>
            <!-- 서버에서 받아온 회사 리스트 -->
            <c:forEach var="company" items="${companyList}">
                <option value="${company.companyId}">
                        ${company.companyName}
                </option>
            </c:forEach>
        </select>

        <!-- 직무 -->
        <label>지원 직무</label>
        <input type="text" name="position" placeholder="예: 백엔드 개발자" required>

        <!-- 상태 -->
        <label>지원 상태</label>
        <select name="stage" required>
            <option value="APPLIED">지원완료</option>
            <option value="DOCUMENT">서류통과</option>
            <option value="INTERVIEW">면접</option>
            <option value="PASS">합격</option>
            <option value="FAIL">불합격</option>
        </select>

        <!-- 지원일 -->
        <label>지원일</label>
        <input type="date" name="apply_date">

        <!-- 메모 -->
        <label>메모</label>
        <textarea name="memo" rows="4" placeholder="추가 메모 입력"></textarea>

        <!-- hidden (로그인 사용자) -->
        <input type="hidden" name="member_id" value="${sessionScope.loginUser.member_id}">

        <!-- 면접 일정 (숨김 영역) -->
        <div id="interviewSection" style="display:none;">

            <label>면접 날짜</label>
            <input type="date" name="interview_date">

            <label>면접 시간</label>
            <input type="time" name="interview_time">

            <label>면접 유형</label>
            <select name="interview_type">
                <option value="">-- 선택 --</option>
                <option value="ONLINE">화상</option>
                <option value="OFFLINE">대면</option>
                <option value="PHONE">전화</option>
            </select>

        </div>



        <!-- 제출 -->
        <button type="submit">등록하기</button>

    </form>
</div>


<script>
    document.addEventListener("DOMContentLoaded", function() {
        const stageSelect = document.querySelector("select[name='stage']");
        const interviewSection = document.getElementById("interviewSection");

        function toggleInterview() {
            if (stageSelect.value === "INTERVIEW") {
                interviewSection.style.display = "block";
                interviewSection.style.opacity = "1";
            } else {
                interviewSection.style.display = "none";
            }
        }

        stageSelect.addEventListener("change", toggleInterview);

        toggleInterview();
    });
</script>
</body>
</html>