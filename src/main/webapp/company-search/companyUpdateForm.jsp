<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/review-write.css">

<form action="${pageContext.request.contextPath}/company/edit" method="post">
    <input type="hidden" name="companyId" value="${company.companyId}"/>

    <div class="field-row">
        <div class="field-group">
            <label class="field-label required">기업명</label>
            <input type="text" name="companyName" value="${company.companyName}"/>
        </div>
    </div>

    <div class="field-row two-col">
        <div class="field-group">
            <label class="field-label required">업종</label>
            <select name="companyIndustry">
                <option value="">선택하세요</option>
                <option value="IT/서비스" ${company.companyIndustry == 'IT/서비스' ? 'selected' : ''}>IT/서비스</option>
                <option value="IT/전자" ${company.companyIndustry == 'IT/전자' ? 'selected' : ''}>IT/전자</option>
                <option value="IT/플랫폼" ${company.companyIndustry == 'IT/플랫폼' ? 'selected' : ''}>IT/플랫폼</option>
                <option value="IT/게임" ${company.companyIndustry == 'IT/게임' ? 'selected' : ''}>IT/게임</option>
                <option value="IT/보안" ${company.companyIndustry == 'IT/보안' ? 'selected' : ''}>IT/보안</option>
                <option value="IT/SI" ${company.companyIndustry == 'IT/SI' ? 'selected' : ''}>IT/SI</option>
                <option value="IT/솔루션" ${company.companyIndustry == 'IT/솔루션' ? 'selected' : ''}>IT/솔루션</option>
                <option value="기타" ${company.companyIndustry == '기타' ? 'selected' : ''}>기타</option>
            </select>
        </div>
        <div class="field-group">
            <label class="field-label required">지역</label>
            <input type="text" name="companyLocation" value="${company.companyLocation}"/>
        </div>
    </div>

    <div class="field-row two-col">
        <div class="field-group">
            <label class="field-label">평점</label>
            <input type="number" name="companyRating" value="${company.companyRating}"
                   min="0" max="5" step="0.1"/>
        </div>
        <div class="field-group">
            <label class="field-label">규모 (인원)</label>
            <input type="number" name="companySize" value="${company.companySize}" min="0"/>
        </div>
    </div>

    <div class="form-actions">
        <button type="button" class="btn-cancel" onclick="history.back()">취소</button>
        <button type="submit" class="btn-submit">수정하기</button>
    </div>
</form>
