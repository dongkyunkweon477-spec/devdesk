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

        /* 1. 기본 상세 팝업 */
        #event-popup {
            display: none; position: absolute; background: #ffffff; border: 1px solid #e2e8f0;
            padding: 15px; box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1); border-radius: 8px; z-index: 1000; width: 250px;
        }
        #event-popup h3 { margin: 0 0 10px 0; color: #2b6cb0; font-size: 18px; }
        .pop-info { margin-bottom: 8px; font-size: 13px; color: #4a5568; }
        .pop-info strong { display: inline-block; width: 60px; color: #2d3748; }
        .pop-close { float: right; cursor: pointer; color: #a0aec0; font-weight: bold; margin-top: -2px; }

        /* 팝업 내 버튼들 */
        .btn-group-sm { display: flex; gap: 5px; margin-top: 15px; border-top: 1px solid #edf2f7; padding-top: 10px;}
        .btn-edit { background: #3182ce; color: white; flex: 1; border: none; padding: 6px; border-radius: 4px; cursor: pointer; font-size:12px;}
        .btn-delete { background: #e53e3e; color: white; flex: 1; border: none; padding: 6px; border-radius: 4px; cursor: pointer; font-size:12px;}

        /* 2. 일정 추가/수정 모달 */
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
    <input type="hidden" id="form-id"> <div class="form-group">
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
        </select>
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
        var currentEvent = null; // 현재 클릭한 일정을 기억하는 변수

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

            // ✨ 1. 기존 일정 클릭 시 (상세 팝업 띄우기)
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

            // ✨ 2. 빈 날짜 클릭 시 (등록 모달 띄우기)
            select: function (info) {
                $('#modal-title').text("새 일정 추가");
                $('#form-id').val(""); // 새 등록이므로 ID 비우기
                $('#form-date').val(info.startStr); // 클릭한 날짜 넣기

                $('#modal-backdrop, #schedule-modal').fadeIn(200);
            }
        });
        calendar.render();

        // [상세 팝업] 닫기
        $('#close-popup').click(function () { $('#event-popup').fadeOut(150); });

        // --- ✨ [수정 버튼] 클릭 시 모달 띄우기 ---
        $('#btn-go-edit').click(function() {
            $('#event-popup').hide(); // 팝업 숨기고
            $('#modal-title').text("일정 수정"); // 모달 제목 변경

            // 모달 안에 기존 데이터 채워주기
            $('#form-id').val(currentEvent.id);
            $('#form-company').val(currentEvent.extendedProps.company);
            $('#form-date').val(currentEvent.startStr);
            $('#form-time').val(currentEvent.extendedProps.time);
            $('#form-type').val(currentEvent.extendedProps.type);
            $('#form-memo').val(currentEvent.extendedProps.memo);

            $('#modal-backdrop, #schedule-modal').fadeIn(200);
        });

        // --- ✨ [삭제 버튼] 클릭 시 ---
        $('#btn-do-delete').click(function() {
            if(!confirm("정말 이 일정을 삭제하시겠습니까?")) return;

            $.ajax({
                url: '/delete-calendar', // 삭제 컨트롤러
                type: 'POST',
                data: { schedule_id: currentEvent.id },
                success: function() {
                    alert("삭제되었습니다.");
                    location.reload(); // 새로고침
                }
            });
        });

        // --- ✨ [모달 저장 버튼] (등록 & 수정 동시 처리) ---
        $('#btn-save-schedule').click(function() {
            var id = $('#form-id').val();
            // 숨겨진 ID 값이 있으면 '수정(Update)', 없으면 '추가(Add)' 로 판단
            var targetUrl = id ? '/update-calendar' : '/add-calender';

            var requestData = {
                schedule_id: id,
                app_id: $('#form-appId').val(),
                company_name: $('#form-company').val(),
                date: $('#form-date').val(),
                time: $('#form-time').val(),
                type: $('#form-type').val(),
                memo: $('#form-memo').val()
            };

            $.ajax({
                url: targetUrl,
                type: 'POST',
                data: requestData,
                success: function() {
                    alert("정상적으로 처리되었습니다.");
                    location.reload(); // 성공 시 새로고침
                },
                error: function() {
                    alert("처리 중 에러가 발생했습니다.");
                }
            });
        });

        // 모달 닫기
        $('#btn-modal-close, #modal-backdrop').click(function() {
            $('#modal-backdrop, #schedule-modal').fadeOut(200);
        });
    });
</script>
</body>
</html>