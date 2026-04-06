<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>DevDesk - 내 면접 일정</title>

    <link href='https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.css' rel='stylesheet'/>
    <script src='https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.js'></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/calendar.css">
</head>
<body>

<div id='calendar'></div>

<div id="event-popup">
    <span class="pop-close" id="close-popup">✕</span>
    <h3 id="pop-title">회사 이름</h3>
    <div class="pop-info"><strong>날짜:</strong> <span id="pop-date"></span></div>
    <div class="pop-info"><strong>시간:</strong> <span id="pop-time"></span></div>
    <div class="pop-info"><strong>면접:</strong> <span id="pop-type"></span></div>
    <div class="pop-info"><strong>메모:</strong> <span id="pop-memo"></span></div>

    <div class="btn-group-sm">
        <button class="btn-edit" id="btn-go-edit">수정</button>
        <button class="btn-delete" id="btn-do-delete">삭제</button>
    </div>
</div>

<div id="modal-backdrop"></div>
<div id="schedule-modal">
    <h3 id="modal-title">새 일정 추가</h3>
    <input type="hidden" id="form-id">
    <input type="hidden" id="form-appId" value="87">

    <div class="form-group">
        <label>회사 이름</label>
        <input type="text" id="form-company" list="company-options" placeholder="회사명 검색 또는 직접 입력">
        <datalist id="company-options">
            <c:forEach var="company" items="${companyList}">
                <option value="${company}"></option>
            </c:forEach>
        </datalist>
    </div>

    <div class="form-group">
        <label>날짜</label>
        <input type="date" id="form-date">
    </div>

    <div class="form-group">
        <label>시간</label>
        <div style="display: flex; gap: 5px;">
            <select id="form-hour" style="flex: 1;">
                <option value="08">08시</option>
                <option value="09">09시</option>
                <option value="10">10시</option>
                <option value="11">11시</option>
                <option value="12">12시</option>
                <option value="13">13시</option>
                <option value="14">14시</option>
                <option value="15">15시</option>
                <option value="16">16시</option>
                <option value="17">17시</option>
                <option value="18">18시</option>
                <option value="19">19시</option>
            </select>

            <select id="form-minute" style="flex: 1;">
                <option value="00">00분</option>
                <option value="10">10분</option>
                <option value="20">20분</option>
                <option value="30">30분</option>
                <option value="40">40분</option>
                <option value="50">50분</option>
            </select>
        </div>
    </div>

    <div class="form-group">
        <label>면접 전형</label>
        <select id="form-type">
            <option value="코딩테스트">코딩테스트</option>
            <option value="1차면접">1차면접</option>
            <option value="2차면접">2차면접</option>
            <option value="임원면접">임원면접</option>
            <option value="direct">직접 입력...</option>
        </select>
        <input type="text" id="form-type-direct" placeholder="ex) SPI, 인성면접" style="display:none; margin-top:5px;">
    </div>

    <div class="form-group">
        <label>메모</label>
        <input type="text" id="form-memo">
    </div>

    <div class="btn-group">
        <button class="btn-cancel" id="btn-modal-close">취소</button>
        <button class="btn-save" id="btn-save-schedule">저장</button>
    </div>
</div>

<script>
    $(document).ready(function () {
        var currentEvent = null;

        var calendarEl = document.getElementById('calendar');
        var calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            locale: 'ko',
            headerToolbar: { left: 'prev,next today', center: 'title', right: 'dayGridMonth,timeGridWeek' },
            editable: true,
            selectable: true,

            events: [
                <c:forEach var="sch" items="${list}" varStatus="status">
                {
                    id: '${sch.schedule_id}',
                    title: '${sch.company_name} 면접',
                    start: '${sch.schedule_date}',
                    extendedProps: {
                        company: '${sch.company_name}',
                        time: '${sch.schedule_time}',
                        type: '${sch.interview_type}',
                        memo: '${sch.schedule_memo}'
                    }
                }<c:if test="${!status.last}">, </c:if>
                </c:forEach>
            ],

            eventClick: function (info) {
                currentEvent = info.event;
                var x = info.jsEvent.pageX; var y = info.jsEvent.pageY;

                $('#pop-title').text(currentEvent.title);
                $('#pop-date').text(currentEvent.startStr);
                $('#pop-time').text(currentEvent.extendedProps.time || "미정");
                $('#pop-type').text(currentEvent.extendedProps.type || "-");
                $('#pop-memo').text(currentEvent.extendedProps.memo || "-");

                $('#event-popup').css({ top: y + 15 + 'px', left: x + 15 + 'px' }).fadeIn(150);
            },

            select: function (info) {
                $('#form-hour').val("14");
                $('#form-minute').val("00");
                $('#modal-title').text("새 일정 추가");
                $('#form-id').val("");
                $('#form-date').val(info.startStr);

                // 모달 띄울 때 회사 입력창 초기화
                $('#form-company').val("");

                $('#form-type').val("코딩테스트");
                $('#form-type-direct').hide().val("");

                $('#modal-backdrop, #schedule-modal').fadeIn(200);
            }
        });
        calendar.render();

        $('#close-popup').click(function () { $('#event-popup').fadeOut(150); });

        $('#form-type').change(function() {
            if ($(this).val() === 'direct') {
                $('#form-type-direct').show().focus();
            } else {
                $('#form-type-direct').hide().val("");
            }
        });

        $('#btn-go-edit').click(function() {
            $('#event-popup').hide();
            $('#modal-title').text("일정 수정");

            $('#form-id').val(currentEvent.id);
            $('#form-company').val(currentEvent.extendedProps.company);
            $('#form-date').val(currentEvent.startStr);
            var existingTime = currentEvent.extendedProps.time;
            if (existingTime) {
                var timeArr = existingTime.split(':'); // ["14", "30"]
                $('#form-hour').val(timeArr[0]);
                $('#form-minute').val(timeArr[1]);
            } else {
                $('#form-hour').val("14");
                $('#form-minute').val("00");
            }
            $('#form-memo').val(currentEvent.extendedProps.memo);

            var existingType = currentEvent.extendedProps.type;
            var isOptionExists = $('#form-type option').filter(function() {
                return $(this).val() === existingType;
            }).length > 0;

            if(isOptionExists) {
                $('#form-type').val(existingType);
                $('#form-type-direct').hide().val("");
            } else {
                $('#form-type').val("direct");
                $('#form-type-direct').show().val(existingType);
            }

            $('#modal-backdrop, #schedule-modal').fadeIn(200);
        });

        $('#btn-do-delete').click(function() {
            if(!confirm("정말 이 일정을 삭제하시겠습니까?")) return;

            $.ajax({
                url: '/delete-calendar',
                type: 'POST',
                data: { schedule_id: currentEvent.id },
                success: function() {
                    alert("삭제되었습니다.");
                    location.reload();
                }
            });
        });

        $('#btn-save-schedule').click(function() {
            var id = $('#form-id').val();
            var targetUrl = id ? '/update-calendar' : '/add-calender';

            var selectedType = $('#form-type').val();
            var finalType = (selectedType === 'direct') ? $('#form-type-direct').val() : selectedType;

            // 회사 이름이나 면접 전형이 비어있으면 경고
            if (!$('#form-company').val().trim() || (selectedType === 'direct' && finalType.trim() === '')) {
                alert("회사 이름과 면접 전형을 정확히 입력해 주세요.");
                return;
            }

            var requestData = {
                schedule_id: id,
                app_id: $('#form-appId').val(),
                company_name: $('#form-company').val(),
                date: $('#form-date').val(),

                // ✨ 바꾼 코드: 선택한 시간과 분을 "14:30" 형태로 다시 합쳐서 서버로 보냄!
                time: $('#form-hour').val() + ":" + $('#form-minute').val(),

                type: finalType,
                memo: $('#form-memo').val()
            };

            $.ajax({
                url: targetUrl,
                type: 'POST',
                data: requestData,
                success: function() {
                    alert("정상적으로 처리되었습니다.");
                    location.reload();
                },
                error: function() {
                    alert("처리 중 에러가 발생했습니다.");
                }
            });
        });

        $('#btn-modal-close, #modal-backdrop').click(function() {
            $('#modal-backdrop, #schedule-modal').fadeOut(200);
        });
    });
</script>
</body>
</html>