<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>지원 목록</title>

    <style>
        body {
            font-family: Arial;
            background-color: #f5f6f7;
        }

        .container {
            width: 900px;
            margin: 50px auto;
        }

        h2 {
            text-align: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: white;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }

        th, td {
            padding: 12px;
            border-bottom: 1px solid #ddd;
            text-align: center;
        }

        th {
            background-color: #4CAF50;
            color: white;
        }

        tr:hover {
            background-color: #f1f1f1;
        }

        .status {
            font-weight: bold;
        }

        .modal {
            display: none;
            position: fixed;
            z-index: 999;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
        }

        .modal-content {
            background: white;
            margin: 10% auto;
            padding: 20px;
            width: 400px;
            border-radius: 10px;
        }

        .close {
            float: right;
            font-size: 20px;
            cursor: pointer;
        }
    </style>
</head>
<body>

<div class="container">
    <h2>지원한 회사 목록</h2>

    <table>
        <tr>
            <th>회사명</th>
            <th>직무</th>
            <th>상태</th>
            <th>지원일</th>
            <th>메모</th>
            <th>삭제</th>
        </tr>


        <c:forEach var="app" items="${applicationList}">
            <tr>
                <td>${app.companyName}</td>
                <td>${app.position}</td>
                <td><span id="status_text_${app.appId}">${app.statusName}</span> <select id="status_select_${app.appId}"
                                                                                         style="display:none;">
                    <option value="APPLIED">지원완료</option>
                    <option value="FIRST_INTERVIEW">1차 면접</option>
                    <option value="SECOND_INTERVIEW">2차 면접</option>
                    <option value="THIRD_INTERVIEW">3차 면접</option>
                    <option value="PASS">합격</option>
                    <option value="FAIL">불합격</option>
                </select>
                    <button onclick="editStatus('${app.appId}', '${app.status}')">변경</button>
                </td>
                <td>${app.appDate}</td>
                <td>${app.memo}</td> <!-- 🔥 삭제 버튼 -->
                <td>
                    <form action="application_delete" method="post" style="display:inline;"><input type="hidden"
                                                                                                   name="app_id"
                                                                                                   value="${app.appId}">
                        <button type="submit" onclick="return confirm('삭제하시겠습니까?');"> 삭제</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        <!-- 데이터 없을 때 -->
        <c:if test="${empty applicationList}">
            <tr>
                <td colspan="5">지원 내역이 없습니다.</td>
            </tr>
        </c:if>

    </table>
</div>


<script>
    function openModal(status) {

        document.getElementById("modal").style.display = "block";


        document.getElementById("modal_status").value = status;

    }

    function closeModal() {
        document.getElementById("modal").style.display = "none";
    }

    // 바깥 클릭 시 닫기
    window.onclick = function (event) {
        const modal = document.getElementById("modal");
        if (event.target === modal) {
            modal.style.display = "none";
        }
    }

    function editStatus(id, currentStatus) {

        // 텍스트 숨기기
        document.getElementById("status_text_" + id).style.display = "none";

        // select 보이기
        const select = document.getElementById("status_select_" + id);
        select.style.display = "inline";

        // 현재 값 선택
        select.value = currentStatus;

        // 변경 시 바로 서버 전송
        select.onchange = function () {
            updateStatus(id, this.value);
        };
    }

    function updateStatus(id, status) {

        // 폼 동적 생성
        const form = document.createElement("form");
        form.method = "post";
        form.action = "application_update";

        const inputId = document.createElement("input");
        inputId.type = "hidden";
        inputId.name = "app_id";
        inputId.value = id;

        const inputStatus = document.createElement("input");
        inputStatus.type = "hidden";
        inputStatus.name = "status";
        inputStatus.value = status;

        form.appendChild(inputId);
        form.appendChild(inputStatus);

        document.body.appendChild(form);
        form.submit();
    }

    function getStatusText(status) {
        switch (status) {
            case 'APPLIED':
                return '지원완료';
            case 'FIRST_INTERVIEW':
                return '1차 면접';
            case 'SECOND_INTERVIEW':
                return '2차 면접';
            case 'THIRD_INTERVIEW':
                return '3차 면접';
            case 'PASS':
                return '합격';
            case 'FAIL':
                return '불합격';
            default:
                return status;
        }
    }

    document.write(getStatusText('${app.status}'));

</script>
</body>
</html>