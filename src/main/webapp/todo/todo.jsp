<link href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/main.min.css" rel="stylesheet">
<script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.js"></script>
<div id="calendar"></div>

<script>
    document.addEventListener('DOMContentLoaded', function() {

        var calendarEl = document.getElementById('calendar');

        var calendar = new FullCalendar.Calendar(calendarEl, {

            initialView: 'dayGridMonth',

            // 날짜 클릭
            dateClick: function(info) {
                var title = prompt("일정 제목 입력");

                if (title) {
                    fetch("scheduleAdd.do", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/x-www-form-urlencoded"
                        },
                        body: "title=" + title + "&date=" + info.dateStr
                    }).then(() => {
                        calendar.refetchEvents();
                    });
                }
            },

            // 일정 불러오기
            events: function(fetchInfo, successCallback, failureCallback) {
                fetch("scheduleList.do")
                    .then(res => res.json())
                    .then(data => {
                        successCallback(data);
                    });
            }

        });

        calendar.render();
    });
</script>