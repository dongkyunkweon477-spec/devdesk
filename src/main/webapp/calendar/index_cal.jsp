<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
    <title>DevDesk - 내 면접 일정</title>
<link href='https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.css' rel='stylesheet'/>
    <script src='https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.js'></script>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/companySearchModal.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/company/company-search-modal.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/calendar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">

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

    <div class="company-header">
        <div class="company-badge">일정 등록</div>
        <div class="field-group">

            <input type="hidden" id="contextPath" value="${pageContext.request.contextPath}"/>

            <div style="display:flex; align-items:center; gap:8px;">
                <input type="text" id="selectedCompanyName" readonly placeholder="기업을 선택해주세요" style="cursor:pointer;"
                       onclick="openCompanyModal()"/>
                <button type="button" onclick="openCompanyModal()" class="modal-btn-search">기업 선택</button>
            </div>
            <jsp:include page="/company/company-search/companySearchModal.jsp"/>
            <input type="hidden" name="companyId" id="selectedCompanyId"/>
        </div>
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

<aside class="sidebar">
    <div class="sidebar-section">
        <div class="section-header" id="toggle-list" style="cursor:pointer; display:flex; justify-content:space-between; align-items:center;">
            <span>이번 달 일정</span>
            <span class="toggle-icon">▼</span>
        </div>
        <div class="section-content" id="upcoming-list">
            <ul class="schedule-list">...</ul>
        </div>
    </div>
                <%-- 현재 날짜의 월 구하기 (Java 코드로 간단히 처리) --%>
                <%
                    java.util.Calendar cal = java.util.Calendar.getInstance();
                    String currentMonth = String.format("%02d", cal.get(java.util.Calendar.MONTH) + 1);
                %>
                <c:set var="curM" value="<%= currentMonth %>" />

                <c:forEach var="sch" items="${list}">
                    <%-- 날짜 문자열(YYYY-MM-DD)에서 월 부분(MM) 추출하여 비교 --%>
                    <c:if test="${fn:substring(sch.schedule_date, 5, 7) == curM}">
                        <li class="list-item" onclick="calendar.gotoDate('${sch.schedule_date}')">
                            <span class="item-date">${sch.schedule_date}</span>
                            <span class="item-title">${sch.company_name}</span>
                        </li>
                    </c:if>
                </c:forEach>
            </ul>
        </div>
        <c:if test="${empty list}">
            <li class="list-empty">일정이 없습니다.</li>
        </c:if>
        </ul>
    </div>

    <div class="sidebar-section">
            <div class="section-header">
                <span id="todo-month-title">4월 To-do List</span>
            </div>
            <div class="section-content 메모장">
                <textarea id="todo-textarea" placeholder="이달의 목표를 적어보세요..."></textarea>
            </div>
        </div>

        <div id="sidebar-mini-calendar">
            <div class="g-cal-header">
                <div class="g-cal-title" id="g-cal-title">2026년 4월</div>
                <div class="g-cal-nav">
                    <button class="g-nav-btn" id="g-prev-month">‹</button>
                    <button class="g-nav-btn" id="g-next-month">›</button>
                </div>
            </div>
            <div class="g-cal-weekdays">
                <div>월</div><div>화</div><div>수</div><div>목</div><div>금</div><div>토</div><div>일</div>
            </div>
            <div class="g-cal-days" id="g-cal-days"></div>
        </div>
    </aside>

    <main class="calendar-main">
        <div id='calendar'></div>

        <div class="fab-container">
            <button class="fab-main" id="fabMain">+</button>
            <div class="fab-menu" id="fabMenu">
                <div class="fab-item" id="fabAddSchedule"><span>📅</span><span class="fab-label">일정 추가</span></div>
                <div class="fab-item" onclick="location.href='/application-list'"><span>📋</span><span class="fab-label">지원현황</span></div>
                <div class="fab-item" onclick="location.href='/dashboard'"><span>🏠</span><span class="fab-label">대시보드</span></div>
            </div>
        </div>
    </main>
</div>

<script>
    $(document).ready(function () {
        $('#customAlertModal, #customConfirmModal').hide();
        var currentEvent = null;


        function showCustomAlert(message, reloadAfter = false) {
            $('#alertMessage').text(message);
            $('#customAlertModal').css('display', 'flex').hide().fadeIn(200);

            $('#btn-alert-ok').off('click').on('click', function () {
                $('#customAlertModal').fadeOut(200);
                if (reloadAfter) location.reload(); // 확인 누르면 새로고침
            });
        }

        var calendarEl = document.getElementById('calendar');
        var calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            locale: 'ko',
            selectable: true,
            headerToolbar: {
                left: 'today',
                center: 'prev title next',
                right: ''
            },
            editable: true,

            // ✨👇 날짜에서 "일" 텍스트를 제거하고 숫자만 렌더링
            dayCellContent: function (info) {
                return info.dayNumberText.replace('일', '');
            },

            // ✨👇 커스텀 년/월 드롭다운 (아까 추가했던 기능)
            datesSet: function (info) {
                var currentDate = calendar.getDate();
                var year = currentDate.getFullYear();
                var month = currentDate.getMonth() + 1;

                var yearHtml = '<select id="custom-year" class="fc-custom-select">';
                for (var i = 2000; i <= 2100; i++) {
                    yearHtml += '<option value="' + i + '" ' + (i === year ? 'selected' : '') + '>' + i + '년</option>';
                }
                yearHtml += '</select>';

                var monthHtml = '<select id="custom-month" class="fc-custom-select">';
                for (var m = 1; m <= 12; m++) {
                    var displayMonth = m < 10 ? '0' + m : m;
                    monthHtml += '<option value="' + displayMonth + '" ' + (m === month ? 'selected' : '') + '>' + m + '월</option>';
                }
                monthHtml += '</select>';

                $('.fc-toolbar-title').html(yearHtml + ' ' + monthHtml);

                $('.fc-custom-select').off('change').on('change', function () {
                    var selectedYear = $('#custom-year').val();
                    var selectedMonth = $('#custom-month').val();
                    calendar.gotoDate(selectedYear + '-' + selectedMonth + '-01');
                });
            },

            // ✨👇 DB에서 불러온 이벤트 리스트
            events: [
                <c:forEach var="sch" items="${list}" varStatus="status">
                {
                    id: '${sch.schedule_id}',
                    title: '${sch.company_name} 면접',
                    start: '${sch.schedule_date}',
                    extendedProps: {
                        company: '${sch.company_name}',
                        position: '${sch.position}',
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

                $('#selectedCompanyName').val("");
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
            $('#selectedCompanyName').val(currentEvent.extendedProps.company);
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


        $('#btn-do-delete').click(function () {
            $('#event-popup').fadeOut(150);
            $('#customConfirmModal').css('display', 'flex').hide().fadeIn(200);

            $('#btn-real-delete').off('click').on('click', function () {
                $('#customConfirmModal').fadeOut(200);

                $.ajax({
                    url: '/delete-calendar',
                    type: 'POST',
                    data: {schedule_id: currentEvent.id},
                    success: function () {
                        showCustomAlert("일정이 삭제되었습니다.", true);
                    },
                    error: function () {
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

            if (!$('#selectedCompanyName').val().trim() || (selectedType === 'direct' && finalType.trim() === '')) {
                showCustomAlert("회사 이름과 면접 전형을 확인해 주세요.");
                return;
            }

            var requestData = {
                schedule_id: id,
                app_id: $('#form-appId').val(),
                company_name: $('#selectedCompanyName').val(),
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
                success: function () {
                    showCustomAlert("츄가완료!! ><", true);
                },
                error: function () {
                    showCustomAlert("저장 중 오류가 발생했습니다. (회사 이름을 확인해주세요)");
                }
            });
        });

        $('#btn-modal-close, #modal-backdrop').click(function () {
            $('#modal-backdrop, #schedule-modal').fadeOut(200);
        });
    });

    // 1. FAB 버튼 토글 애니메이션
    $('#fabMain').click(function() {
        $(this).toggleClass('active');
        $('#fabMenu').fadeToggle(200).css('display', 'flex');
    });

    // 2. FAB를 통한 일정 추가 모달 열기
    $('#fabAddSchedule').click(function() {
        // 기존 select 이벤트에서 쓰던 초기화 로직 그대로 활용
        $('#form-hour').val("14");
        $('#form-minute').val("00");
        $('#modal-title').text("새 일정 추가");
        $('#form-id').val("");

        // 날짜는 오늘 날짜로 기본 설정 (사용자가 직접 변경 가능)
        var today = new Date().toISOString().split('T')[0];
        $('#form-date').val(today);

        $('#selectedCompanyName').val("");
        $('#form-apply-date').val("");
        $('#form-position').val("");
        $('#form-memo').val("");
        $('#form-type').val("코딩테스트");
        $('#form-type-direct').hide().val("");

        // 모달 띄우기
        $('#modal-backdrop, #schedule-modal').fadeIn(200);

        // 메뉴 닫기
        $('#fabMain').removeClass('active');
        $('#fabMenu').fadeOut(100);
    });

    // 외부 클릭 시 메뉴 닫기 (선택사항)
    $(document).on('click', function(e) {
        if (!$(e.target).closest('.fab-container').length) {
            $('#fabMain').removeClass('active');
            $('#fabMenu').fadeOut(200);
        }
    });

    // 1. 리스트 접었다 펴기
    $('#toggle-list').click(function() {
        $('#upcoming-list').slideToggle(300);
        $(this).find('.toggle-icon').toggleClass('rotate');
    });

    // 2. To-do List 메모 저장 (로컬 스토리지 사용 - 새로고침해도 유지됨)
    $('#todo-textarea').val(localStorage.getItem('devdesk_todo'));
    $('#todo-textarea').on('input', function() {
        localStorage.setItem('devdesk_todo', $(this).val());
    });

    // 3. To-do List 제목에 해당 월 표시
    function updateTodoTitle(date) {
        const month = date.getMonth() + 1;
        $('#todo-month-title').text(month + '월 To-do List');
    }
    updateTodoTitle(new Date());

    $('#toggle-list').click(function() {
        $('#upcoming-list').slideToggle(300); // 부드럽게 접고 펴기
        // 아이콘 회전 효과 (선택사항)
        $(this).find('.toggle-icon').css('transform', function(i, v) {
            return v === 'rotate(180deg)' ? 'rotate(0deg)' : 'rotate(180deg)';
        });
    });

</script>

<div class="modal-overlay" id="customAlertModal"
     style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.5); z-index:10001; align-items:center; justify-content:center;">
    <div class="modal-box"
         style="background:#fff; padding:25px; border-radius:8px; width:300px; text-align:center; box-shadow:0 10px 15px rgba(0,0,0,0.1);">
        <p id="alertMessage" style="font-size:16px; font-weight:bold; color:#2d3748; margin-bottom:20px;">메시지 내용</p>
        <div class="modal-btns">
            <button class="btn-save" id="btn-alert-ok"
                    style="width:100%; padding:10px; background:#2b6cb0; color:white; border:none; border-radius:4px; cursor:pointer;">
                확인
            </button>
        </div>
    </div>
</div>

<div class="modal-overlay" id="customConfirmModal"
     style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.5); z-index:10001; align-items:center; justify-content:center;">
    <div class="modal-box"
         style="background:#fff; padding:25px; border-radius:8px; width:300px; text-align:center; box-shadow:0 10px 15px rgba(0,0,0,0.1);">
        <p style="font-size:16px; font-weight:bold; color:#e53e3e; margin-bottom:10px;">정말 삭제하시겠습니까?</p>
        <p class="modal-sub" style="font-size:13px; color:#718096; margin-bottom:20px;">삭제된 일정은 복구할 수 없습니다.</p>
        <div class="modal-btns" style="display:flex; gap:10px;">
            <button class="btn-cancel" onclick="$('#customConfirmModal').fadeOut(200);"
                    style="flex:1; padding:10px; background:#e2e8f0; border:none; border-radius:4px; cursor:pointer;">취소
            </button>
            <button class="btn-delete" id="btn-real-delete"
                    style="flex:1; padding:10px; background:#e53e3e; color:white; border:none; border-radius:4px; cursor:pointer;">
                삭제
            </button>
        </div>
    </div>
</div>
