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
    <div class="pop-info"><strong>직무:</strong> <span id="pop-position"></span></div>
    <div class="pop-info"><strong>날짜:</strong> <span id="pop-date"></span></div>
    <div class="pop-info"><strong>시간:</strong> <span id="pop-time"></span></div>
    <div class="pop-info"><strong>면접:</strong> <span id="pop-type"></span></div>
    <div class="pop-info"><strong>메모:</strong> <span id="pop-memo"></span></div>

    <div class="btn-group-sm">
        <button class="btn-edit" id="btn-go-edit">수정</button>
        <button class="btn-delete" id="btn-do-delete">삭제</button>
    </div>
</div>

<div id="schedule-modal">
    <h3 id="modal-title">새 일정 추가</h3>
    <input type="hidden" id="form-id">
    <input type="hidden" id="form-appId" value="1">

    <div class="form-group">
        <label>회사 이름</label>
        <input type="text" id="form-company" list="company-options" placeholder="회사명 검색">
        <datalist id="company-options">
            <c:forEach var="company" items="${companyList}">
                <option value="${company}"></option>
            </c:forEach>
        </datalist>
    </div>

    <div class="form-group">
        <label>지원 직무</label>
        <input type="text" id="form-position" placeholder="ex) 백엔드 개발자, 서비스 기획">
    </div>

    <div class="form-group">
        <label>서류 지원 일자 (선택)</label>
        <input type="date" id="form-apply-date">
    </div>

    <div class="form-group">
        <label>면접 날짜</label>
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
        $('#customAlertModal, #customConfirmModal').hide();
        var currentEvent = null;

        $('#form-company').on('click', function() {
            var currentVal = $(this).val();
            if (currentVal !== "") {
                $(this).data('saved-company', currentVal);
                $(this).val('');
            }
        }).on('blur', function() {
            if ($(this).val() === "" && $(this).data('saved-company')) {
                $(this).val($(this).data('saved-company'));
            }
        });

        function showCustomAlert(message, reloadAfter = false) {
            $('#alertMessage').text(message);
            $('#customAlertModal').css('display', 'flex').hide().fadeIn(200);

            $('#btn-alert-ok').off('click').on('click', function() {
                $('#customAlertModal').fadeOut(200);
                if(reloadAfter) location.reload(); // 확인 누르면 새로고침
            });
        }

        var calendarEl = document.getElementById('calendar');
        var calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            locale: 'ko',
            headerToolbar: {left: 'prev,next today', center: 'title', right: 'dayGridMonth,timeGridWeek'},
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
                        position: '${sch.position}', // ✨ 여기에 position이 꼭 있어야 합니다!
                        time: '${sch.schedule_time}',
                        type: '${sch.interview_type}',
                        memo: '${sch.schedule_memo}'
                    }
                }<c:if test="${!status.last}">, </c:if>
                </c:forEach>
            ],

            eventClick: function (info) {
                currentEvent = info.event;
                var x = info.jsEvent.pageX;
                var y = info.jsEvent.pageY;

                $('#pop-title').text(currentEvent.title);
                $('#pop-position').text(currentEvent.extendedProps.position || "미정");
                $('#pop-date').text(currentEvent.startStr);
                $('#pop-time').text(currentEvent.extendedProps.time || "미정");
                $('#pop-type').text(currentEvent.extendedProps.type || "-");
                $('#pop-memo').text(currentEvent.extendedProps.memo || "-");

                $('#event-popup').css({top: y + 15 + 'px', left: x + 15 + 'px'}).fadeIn(150);
            },

            select: function (info) {
                $('#form-hour').val("14");
                $('#form-minute').val("00");
                $('#modal-title').text("새 일정 추가");
                $('#form-id').val("");
                $('#form-date').val(info.startStr);

                $('#form-company').val("");
                $('#form-apply-date').val(""); // ✨ 지원일자 초기화 추가

                $('#form-type').val("코딩테스트");
                $('#form-type-direct').hide().val("");

                $('#modal-backdrop, #schedule-modal').fadeIn(200);
            }
        });
        calendar.render();

        $('#close-popup').click(function () {
            $('#event-popup').fadeOut(150);
        });

        $('#form-type').change(function () {
            if ($(this).val() === 'direct') {
                $('#form-type-direct').show().focus();
            } else {
                $('#form-type-direct').hide().val("");
            }
        });

        $('#btn-go-edit').click(function () {
            $('#event-popup').hide();
            $('#modal-title').text("일정 수정");

            $('#form-id').val(currentEvent.id);
            $('#form-company').val(currentEvent.extendedProps.company);
            $('#form-position').val(currentEvent.extendedProps.position);
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
            $('#modal-backdrop, #schedule-modal').fadeIn(200);
            $('#form-memo').val(currentEvent.extendedProps.memo);

            var existingType = currentEvent.extendedProps.type;
            var isOptionExists = $('#form-type option').filter(function () {
                return $(this).val() === existingType;
            }).length > 0;

            if (isOptionExists) {
                $('#form-type').val(existingType);
                $('#form-type-direct').hide().val("");
            } else {
                $('#form-type').val("direct");
                $('#form-type-direct').show().val(existingType);
            }

            $('#modal-backdrop, #schedule-modal').fadeIn(200);
        });

        $('#btn-do-delete').click(function() {
            $('#event-popup').fadeOut(150);
            $('#customConfirmModal').css('display', 'flex').hide().fadeIn(200);

            $('#btn-real-delete').off('click').on('click', function() {
                $('#customConfirmModal').fadeOut(200);

                $.ajax({
                    url: '/delete-calendar',
                    type: 'POST',
                    data: { schedule_id: currentEvent.id },
                    success: function() {
                        showCustomAlert("일정이 삭제되었습니다.", true);
                    },
                    error: function() {
                        showCustomAlert("DELETE ERROR");
                    }
                });
            });
        });

        $('#btn-save-schedule').click(function () {
            var id = $('#form-id').val();
            var targetUrl = id ? '/update-calendar' : '/add-calendar';

            var selectedType = $('#form-type').val();
            var finalType = (selectedType === 'direct') ? $('#form-type-direct').val() : selectedType;

            if (!$('#form-company').val().trim() || (selectedType === 'direct' && finalType.trim() === '')) {
                showCustomAlert("회사 이름과 면접 전형을 확인해 주세요.");
                return;
            }

            var requestData = {
                schedule_id: id,
                app_id: $('#form-appId').val(),
                company_name: $('#form-company').val(),
                position: $('#form-position').val(),
                apply_date: $('#form-apply-date').val(),
                date: $('#form-date').val(),
                time: $('#form-hour').val() + ":" + $('#form-minute').val(),
                type: finalType,
                memo: $('#form-memo').val()
            };

            $.ajax({
                url: targetUrl,
                type: 'POST',
                data: requestData,
                success: function() {
                    showCustomAlert("츄가완료!! ><", true);
                },
                error: function() {
                    showCustomAlert("저장 중 오류가 발생했습니다. (회사 이름을 확인해주세요)");
                }
            });
        });

        $('#btn-modal-close, #modal-backdrop').click(function () {
            $('#modal-backdrop, #schedule-modal').fadeOut(200);
        });
    });
</script>

<div class="modal-overlay" id="customAlertModal" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.5); z-index:10001; align-items:center; justify-content:center;">
    <div class="modal-box" style="background:#fff; padding:25px; border-radius:8px; width:300px; text-align:center; box-shadow:0 10px 15px rgba(0,0,0,0.1);">
        <p id="alertMessage" style="font-size:16px; font-weight:bold; color:#2d3748; margin-bottom:20px;">메시지 내용</p>
        <div class="modal-btns">
            <button class="btn-save" id="btn-alert-ok" style="width:100%; padding:10px; background:#2b6cb0; color:white; border:none; border-radius:4px; cursor:pointer;">확인</button>
        </div>
    </div>
</div>

<div class="modal-overlay" id="customConfirmModal" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.5); z-index:10001; align-items:center; justify-content:center;">
    <div class="modal-box" style="background:#fff; padding:25px; border-radius:8px; width:300px; text-align:center; box-shadow:0 10px 15px rgba(0,0,0,0.1);">
        <p style="font-size:16px; font-weight:bold; color:#e53e3e; margin-bottom:10px;">정말 삭제하시겠습니까?</p>
        <p class="modal-sub" style="font-size:13px; color:#718096; margin-bottom:20px;">삭제된 일정은 복구할 수 없습니다.</p>
        <div class="modal-btns" style="display:flex; gap:10px;">
            <button class="btn-cancel" onclick="$('#customConfirmModal').fadeOut(200);" style="flex:1; padding:10px; background:#e2e8f0; border:none; border-radius:4px; cursor:pointer;">취소</button>
            <button class="btn-delete" id="btn-real-delete" style="flex:1; padding:10px; background:#e53e3e; color:white; border:none; border-radius:4px; cursor:pointer;">삭제</button>
        </div>
    </div>
</div>

</body>
</html>