<%@ page contentType="text/html;charset=UTF-8" %>

<form action="application_update" method="post">

    <input type="hidden" name="app_id" value="${app.appId}">

    회사명: ${app.companyName} <br><br>

    직무:
    <input type="text" name="position" value="${app.position}"><br><br>

    상태:
    <select name="status">
        <option value="APPLIED" ${app.status=='APPLIED'?'selected':''}>지원완료</option>
        <option value="INTERVIEW" ${app.status=='INTERVIEW'?'selected':''}>면접</option>
        <option value="PASS" ${app.status=='PASS'?'selected':''}>합격</option>
        <option value="FAIL" ${app.status=='FAIL'?'selected':''}>불합격</option>
    </select><br><br>

    날짜:
    <input type="date" name="appDate" value="${app.appDate}"><br><br>

    메모:
    <textarea name="memo">${app.memo}</textarea><br><br>

    <button type="submit">수정 완료</button>

</form>