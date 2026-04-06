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

    <style>
        body { font-family: 'Arial', sans-serif; padding: 20px; background-color: #f7fafc;}
        #calendar { max-width: 900px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 4px 6px rgba(0,0,0,0.05);}

        /* 팝업 공통 */
        #event-popup { display: none; position: absolute; background: #ffffff; border: 1px solid #e2e8f0; padding: 15px; box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1); border-radius: 8px; z-index: 1000; width: 250px; }
        #event-popup h3 { margin: 0 0 10px 0; color: #2b6cb0; font-size: 18px; }
        .pop-info { margin-bottom: 8px; font-size: 13px; color: #4a5568; }
        .pop-info strong { display: inline-block; width: 60px; color: #2d3748; }
        .pop-close { float: right; cursor: pointer; color: #a0aec0; font-weight: bold; margin-top: -2px; }
        .btn-group-sm { display: flex; gap: 5px; margin-top: 15px; border-top: 1px solid #edf2f7; padding-top: 10px;}
        .btn-edit { background: #3182ce; color: white; flex: 1; border: none; padding: 6px; border-radius: 4px; cursor: pointer; font-size:12px;}
        .btn-delete { background: #e53e3e; color: white; flex: 1; border: none; padding: 6px; border-radius: 4px; cursor: pointer; font-size:12px;}

        /* 모달 공통 */
        #modal-backdrop { display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%; background: rgba(0,0,0,0.5); z-index: 999; }
        #schedule-modal { display: none; position: fixed; top: 50%; left: 50%; transform: translate(-50%, -50%); background: white; padding: 25px; border-radius: 8px; width: 320px; z-index: 1000; }
        #schedule-modal h3 { margin-top: 0; color: #1a365d; border-bottom: 2px solid #edf2f7; padding-bottom: 10px;}
        .form-group { margin-bottom: 12px; }
        .form-group label { display: block; font-size: 13px; font-weight: bold; margin-bottom: 4px; color: #4a5568;}
        .form-group input, .form-group select { width: 100%; padding: 8px; box-sizing: border-box; border: 1px solid #cbd5e0; border-radius: 4px; }
        .btn-group { display: flex; justify-content: flex-end; gap: 10px; margin-top: 20px; }
        .btn-save { background: #2b6cb0; color: white; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer; }
        .btn-cancel { background: #e2e8f0; color: #4a5568; border: none; padding: 8px 15px; border-radius: 4px; cursor: pointer; }
    </style>
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

    <div class="form-group">
        <label>지원서 번호 (APP_ID)</label>
        <input type="number" id="form-appId" value="1">
    </div>
    <div class="form-group">
        <label>회사 이름</label>
        <input type="text" id="form-company" value="네이버">
    </div>
    <div class="form-group">
        <label>날짜</label>
        <input type="date" id="form-date">
    </div>
    <div class="form-group">
        <label>시간</label>
        <input type="time" id="form-time">
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

            // 빈 날짜 클릭 시 (새 일정 추가)
            select: function (info) {
                $('#modal-title').text("새 일정 추가");
                $('#form-id').val("");
                $('#form-date').val(info.startStr);

                // ✨ 모달 띄울 때 '직접 입력' 창 초기화
                $('#form-type').val("코딩테스트");
                $('#form-type-direct').hide().val("");

                $('#modal-backdrop, #schedule-modal').fadeIn(200);
            }
        });
        calendar.render();

        $('#close-popup').click(function () { $('#event-popup').fadeOut(150); });

        // --- ✨ 면접 전형 드롭다운 변경 이벤트 ---
        $('#form-type').change(function() {
            if ($(this).val() === 'direct') {
                $('#form-type-direct').show().focus(); // 직접입력 선택 시 입력창 표시
            } else {
                $('#form-type-direct').hide().val(""); // 다른 거 선택 시 입력창 숨김 및 초기화
            }
        });

        // 수정 버튼 클릭 시
        $('#btn-go-edit').click(function() {
            $('#event-popup').hide();
            $('#modal-title').text("일정 수정");

            $('#form-id').val(currentEvent.id);
            $('#form-company').val(currentEvent.extendedProps.company);
            $('#form-date').val(currentEvent.startStr);
            $('#form-time').val(currentEvent.extendedProps.time);
            $('#form-memo').val(currentEvent.extendedProps.memo);

            // ✨ 기존 면접 타입이 목록에 있는지 확인
            var existingType = currentEvent.extendedProps.type;
            var isOptionExists = $('#form-type option').filter(function() {
                return $(this).val() === existingType;
            }).length > 0;

            if(isOptionExists) {
                // 목록에 있으면 그걸 선택
                $('#form-type').val(existingType);
                $('#form-type-direct').hide().val("");
            } else {
                // 목록에 없으면(사용자가 직접 입력했던 거라면) '직접 입력' 선택 후 입력창에 값 띄우기
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

            // ✨ 어떤 값을 DB로 보낼지 결정!
            var selectedType = $('#form-type').val();
            var finalType = (selectedType === 'direct') ? $('#form-type-direct').val() : selectedType;

            // 직접 입력인데 칸이 비어있으면 경고
            if (selectedType === 'direct' && finalType.trim() === '') {
                alert("면접 전형을 직접 입력해 주세요.");
                $('#form-type-direct').focus();
                return;
            }

            var requestData = {
                schedule_id: id,
                app_id: $('#form-appId').val(),
                company_name: $('#form-company').val(),
                date: $('#form-date').val(),
                time: $('#form-time').val(),
                type: finalType, // ✨ 최종 결정된 타입을 보냄
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