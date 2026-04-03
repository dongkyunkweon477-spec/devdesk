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
        body {
            font-family: 'Arial', sans-serif;
            padding: 20px;
        }

        #calendar {
            max-width: 900px;
            margin: 0 auto;
        }

        #event-popup {
            display: none;
            position: absolute; /* 마우스 위치를 따라다니게 */
            background: #ffffff;
            border: 1px solid #e2e8f0;
            padding: 15px;
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
            border-radius: 8px;
            z-index: 1000;
            width: 250px;
        }

        #event-popup h3 {
            margin: 0 0 10px 0;
            color: #2b6cb0;
            font-size: 18px;
        }

        .pop-info {
            margin-bottom: 8px;
            font-size: 13px;
            color: #4a5568;
        }

        .pop-info strong {
            display: inline-block;
            width: 60px;
            color: #2d3748;
        }

        .pop-close {
            float: right;
            cursor: pointer;
            color: #a0aec0;
            font-weight: bold;
            margin-top: -2px;
        }

        .pop-close:hover {
            color: #e53e3e;
        }
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
</div>

<script>
    $(document).ready(function () {
        var calendarEl = document.getElementById('calendar');
        var calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            locale: 'ko',

            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek'
            },
            editable: true,
            selectable: true,

            events: [
                <c:forEach var="sch" items="${list}" varStatus="status">
                {
                    id: '${sch.schedule_id}', // 소문자 id로 변경!
                    title: '${sch.company_name} 면접',
                    start: '${sch.schedule_date}', // .toString()은 빼도 잘 나옵니다.
                    extendedProps: {
                        time: '${sch.schedule_time}',
                        type: '${sch.interview_type}',
                        memo: '${sch.schedule_memo}' // DTO 이름에 맞춰서 schedule_memo로 변경!
                    }
                }<c:if test="${!status.last}">, </c:if>
                </c:forEach>
            ],

            // --- ✨ 클릭 시 마우스 옆에 팝업 띄우기 ---
            eventClick: function (info) {
                // 1. 마우스 클릭한 X, Y 좌표 가져오기
                var x = info.jsEvent.pageX;
                var y = info.jsEvent.pageY;

                var eventObj = info.event;

                $.ajax({
                    url: '/getScheduleDetail', // 상세 정보를 줄 서블릿 주소
                    type: 'GET',
                    data: {schedule_id: eventObj.id}, // 무슨 일정을 클릭했는지 DB에 알려줌
                    success: function (data) {
                        // DB에서 가져온 data로 아래 팝업 빈칸을 채워줍니다.
                    }
                });

                // 2. 일단은 미리 가져온 데이터로 빈칸 채우기
                $('#pop-title').text(eventObj.title);
                $('#pop-date').text(eventObj.startStr);
                $('#pop-time').text(eventObj.extendedProps.time || "시간 미정");
                $('#pop-type').text(eventObj.extendedProps.type || "-");
                $('#pop-memo').text(eventObj.extendedProps.memo || "메모 없음");

                // 3. 팝업 위치를 마우스 커서 살짝 아래(15px)로 이동시킨 후 보여주기
                $('#event-popup').css({
                    top: y + 15 + 'px',
                    left: x + 15 + 'px'
                }).fadeIn(150);
            }
        });

        calendar.render();

        // X 버튼 누르거나 팝업 바깥쪽을 클릭하면 팝업 닫기
        $('#close-popup').click(function () {
            $('#event-popup').fadeOut(150);
        });

        // 달력의 빈 공간을 클릭했을 때도 팝업이 부드럽게 닫히도록 설정
        $(document).mouseup(function (e) {
            var popup = $("#event-popup");
            // 클릭한 곳이 팝업 창 내부가 아니라면 닫기
            if (!popup.is(e.target) && popup.has(e.target).length === 0) {
                popup.fadeOut(150);
            }
        });
    });
</script>
</body>
</html>