<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>DevDesk - 내 면접 일정</title>

    <link href='https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.css' rel='stylesheet' />
    <script src='https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.js'></script>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

    <style>
        /* 2. 도화지 예쁘게 다듬기 */
        body { font-family: 'Arial', sans-serif; padding: 20px; }
        #calendar { max-width: 900px; margin: 0 auto; }
    </style>
</head>
<body>

<div id='calendar'></div>

<script>
    $(document).ready(function() {
        // 4. "자, 이제 달력을 그려라!"라고 명령하기
        var calendarEl = document.getElementById('calendar');
        var calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth', // 처음엔 '월' 단위로 보여줘!
            headerToolbar: {
                left: 'prev,next today',
                center: 'title',
                right: 'dayGridMonth,timeGridWeek' // 주 단위 버튼도 추가!
            },
            locale: 'ko', // 한국어로 나오게 하기
            editable: true,
            selectable: true,

            // db연결
            events: [
                { title: ${db데이터}, start: '2026-04-05' },
                { title: '카카오 면접', start: '2026-04-10' }
            ]
        });

        calendar.render(); // 진짜로 그리기!
    });
</script>
</body>
</html>