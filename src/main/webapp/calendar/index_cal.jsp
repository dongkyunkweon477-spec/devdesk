<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<title>DevDesk - 내 면접 일정</title>
<link href='https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.css' rel='stylesheet'/>
<<<<<<< HEAD
<script src='https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.js'></script>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="${pageContext.request.contextPath}/js/companySearchModal.js"></script>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/company/company-search-modal.css">
=======
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/calendar.css">
    <script src='https://cdn.jsdelivr.net/npm/fullcalendar@5.11.3/main.min.js'></script>
    <script src="${pageContext.request.contextPath}/js/companySearchModal.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/company/company-search-modal.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/calendar.css">
>>>>>>> 451020e210769e8f9b0823a37dcba8fc5b8a9242
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/sidebar.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/calendar.css">

<%-- ── 이번 주 날짜 범위 계산 ── --%>
<%
    java.util.Calendar cal = java.util.Calendar.getInstance();
    cal.set(java.util.Calendar.DAY_OF_WEEK, java.util.Calendar.SUNDAY);
    java.util.Date startDate = cal.getTime();
    cal.add(java.util.Calendar.DATE, 6);
    java.util.Date endDate = cal.getTime();
    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");

    request.setAttribute("weekStart", sdf.format(startDate));
    request.setAttribute("weekEnd", sdf.format(endDate));
%>

<<<<<<< HEAD
<%--
    ════════════════════════════════════════════════════════
    [사용 방법]
    sidebar.jsp (또는 layout include 파일)에서
    미니캘린더 위쪽에 아래 두 블록(이번 주 일정 / To-do)을 붙여넣고,
    calendar.jsp 본문엔 .calendar-page-wrapper 블록만 남기세요.
    ════════════════════════════════════════════════════════
--%>
=======
<div id="modal-backdrop" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; background:rgba(0,0,0,0.4); z-index:999;"></div>

<div id="schedule-modal">
    <h3 id="modal-title">새 일정 추가</h3>
    <input type="hidden" id="form-id">
    <input type="hidden" id="form-appId" value="1">
>>>>>>> 451020e210769e8f9b0823a37dcba8fc5b8a9242

<%-- ────────────────────────────────────────
     [A] 사이드바 삽입 블록
──────────────────────────────────────── --%>
<div class="sidebar">

    <%-- 1. 상단 스크롤 영역 (일정, 메모) --%>
    <div class="sidebar-nav">
        <span class="nav-section-label">일정</span>

        <%-- 이번 주 일정 토글 버튼 --%>
        <div class="week-nav-toggle" id="weekToggle">
            <span class="nav-icon">📅</span>
            <span class="toggle-label">이번 주 일정</span>
            <span class="toggle-arrow">▼</span>
        </div>

        <%-- 펼쳐지는 일정 목록 --%>
        <div class="week-schedule-dropdown" id="weekDropdown">
            <c:set var="hasWeekEvent" value="false"/>

            <c:forEach var="sch" items="${list}">
                <c:if test="${not empty sch.schedule_date}">
                    <fmt:parseDate value="${sch.schedule_date}" pattern="yyyy-MM-dd" var="schDate"/>
                    <fmt:parseDate value="${weekStart}" pattern="yyyy-MM-dd" var="start"/>
                    <fmt:parseDate value="${weekEnd}" pattern="yyyy-MM-dd" var="end"/>

                    <c:if test="${schDate.time >= start.time and schDate.time <= end.time}">
                        <div class="week-schedule-item"
                             onclick="if(window.calendar){window.calendar.gotoDate('${sch.schedule_date}');}">
                            <span class="week-item-day">${fn:substring(sch.schedule_date, 8, 10)}일</span>
                            <div class="week-item-info">
                                <span class="week-item-company">${sch.company_name}</span>
                                <span class="week-item-type">${sch.interview_type}</span>
                            </div>
                        </div>
                        <c:set var="hasWeekEvent" value="true"/>
                    </c:if>
                </c:if>
            </c:forEach>

            <c:if test="${hasWeekEvent == 'false'}">
                <div class="week-schedule-empty">이번 주 일정이 없어요 😊</div>
            </c:if>
        </div>

        <span class="nav-section-label" style="margin-top:4px;">메모</span>

        <%-- To-do 체크박스 리스트 --%>
        <div class="todo-section">
            <div class="todo-section-header">
                <div class="todo-section-title">
                    <span class="nav-icon">✅</span>
                    <span id="todo-month-title" style="font-size:13px; color:var(--text2);">To-do</span>
                </div>
                <button class="todo-add-btn" id="todo-add-btn" title="추가">+</button>
            </div>
            <ul class="todo-list" id="todo-list"></ul>
            <div class="todo-input-row" id="todo-input-row" style="display:none;">
                <input type="text" class="todo-input" id="todo-input" placeholder="할 일 입력..." maxlength="40"/>
                <button class="todo-input-confirm" id="todo-input-confirm">✓</button>
            </div>
        </div>
    </div> <%-- // sidebar-nav 종료 --%>

    <%-- 2. 하단 고정 영역 (미니 캘린더) --%>
    <div id="sidebar-mini-calendar" style="margin-top: auto; border-top: 1px solid var(--border, #e2e8f0); padding-top: 15px;">
        <div class="g-cal-header">
            <button class="g-nav-btn" id="g-prev-month">❮</button>
            <span class="g-cal-title" id="g-cal-title"></span>
            <button class="g-nav-btn" id="g-next-month">❯</button>
        </div>
        <div class="g-cal-weekdays">
            <div class="sun">일</div><div>월</div><div>화</div><div>수</div><div>목</div><div>금</div><div>토</div>
        </div>
        <div class="g-cal-days" id="g-cal-days"></div>
    </div>

</div> <%-- // 사이드바 컨테이너 종료 --%>

<%-- ────────────────────────────────────────
     [B] 캘린더 메인 본문 (calendar.jsp)
──────────────────────────────────────── --%>

<div class="calendar-page-wrapper">
    <div class="calendar-main">
        <div id='calendar'></div>

        <div class="fab-container">
            <button class="fab-main" id="fabMain">+</button>
            <div class="fab-menu" id="fabMenu">
                <div class="fab-item" id="fabAddSchedule"><span>📅</span><span class="fab-label">일정 추가</span></div>
                <div class="fab-item" onclick="location.href='/application-list'"><span>📋</span><span class="fab-label">지원현황</span></div>
                <div class="fab-item" onclick="location.href='/dashboard'"><span>🏠</span><span class="fab-label">대시보드</span></div>
            </div>
        </div>
    </div>
</div>

<!-- 이벤트 클릭 팝업 -->
<div id="event-popup">
    <span class="pop-close" id="close-popup">✕</span>
    <h3 id="pop-title"></h3>
    <div class="pop-info"><strong>직무</strong><span id="pop-position"></span></div>
    <div class="pop-info"><strong>날짜</strong><span id="pop-date"></span></div>
    <div class="pop-info"><strong>시간</strong><span id="pop-time"></span></div>
    <div class="pop-info"><strong>전형</strong><span id="pop-type"></span></div>
    <div class="pop-info"><strong>메모</strong><span id="pop-memo"></span></div>
    <div class="btn-group-sm">
        <button class="btn-edit" id="btn-go-edit">수정</button>
        <button class="btn-delete" id="btn-do-delete">삭제</button>
    </div>
</div>

<!-- 일정 추가/수정 모달 -->
<div id="modal-backdrop"></div>
<div id="schedule-modal">
    <h3 id="modal-title">새 일정 추가</h3>
    <input type="hidden" id="form-id">
    <input type="hidden" id="form-appId" value="1">
    <input type="hidden" id="contextPath" value="${pageContext.request.contextPath}"/>

    <div class="form-group">
        <label>기업</label>
        <div style="display:flex;align-items:center;gap:8px;">
            <input type="text" id="selectedCompanyName" readonly placeholder="기업을 선택해주세요"
                   style="cursor:pointer;flex:1;" onclick="openCompanyModal()"/>
            <button type="button" onclick="openCompanyModal()" class="modal-btn-search">선택</button>
        </div>
        <jsp:include page="/company/company-search/companySearchModal.jsp"/>
        <input type="hidden" name="companyId" id="selectedCompanyId"/>
    </div>
    <div class="form-group">
        <label>지원 직무</label>
        <input type="text" id="form-position" placeholder="ex) 백엔드 개발자">
    </div>
    <div class="form-group">
        <label>서류 지원 일자 <span style="font-weight:400;color:var(--text3);">(선택)</span></label>
        <input type="date" id="form-apply-date">
    </div>
    <div class="form-group">
        <label>면접 날짜</label>
        <input type="date" id="form-date">
    </div>
    <div class="form-group">
        <label>시간</label>
        <div style="display:flex;gap:8px;">
            <select id="form-hour" style="flex:1;">
                <option value="08">08시</option><option value="09">09시</option>
                <option value="10">10시</option><option value="11">11시</option>
                <option value="12">12시</option><option value="13">13시</option>
                <option value="14">14시</option><option value="15">15시</option>
                <option value="16">16시</option><option value="17">17시</option>
                <option value="18">18시</option><option value="19">19시</option>
            </select>
            <select id="form-minute" style="flex:1;">
                <option value="00">00분</option><option value="10">10분</option>
                <option value="20">20분</option><option value="30">30분</option>
                <option value="40">40분</option><option value="50">50분</option>
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
        <input type="text" id="form-type-direct" placeholder="ex) SPI, 인성면접" style="display:none;margin-top:6px;">
    </div>
    <div class="form-group">
        <label>메모</label>
        <input type="text" id="form-memo">
    </div>
    <div class="btn-group">
        <button class="btn-cancel" id="btn-modal-close">취소</button>
        <button class="btn-save"   id="btn-save-schedule">저장</button>
    </div>
</div>

<!-- 알림/확인 모달 -->
<div class="modal-overlay" id="customAlertModal">
    <div class="modal-box">
        <p id="alertMessage"></p>
        <button class="btn-save" id="btn-alert-ok" style="width:100%;">확인</button>
    </div>
</div>
<div class="modal-overlay" id="customConfirmModal">
    <div class="modal-box">
        <p style="font-weight:700;color:#e53e3e;margin-bottom:6px;">정말 삭제하시겠습니까?</p>
        <p style="font-size:13px;color:var(--text3);margin-bottom:20px;">삭제된 일정은 복구할 수 없습니다.</p>
        <div style="display:flex;gap:10px;">
            <button class="btn-cancel" onclick="$('#customConfirmModal').fadeOut(200);">취소</button>
            <button class="btn-delete" id="btn-real-delete">삭제</button>
        </div>
    </div>
</div>

<script>
    $(document).ready(function(){

        $('#customAlertModal, #customConfirmModal').hide();
        var currentEvent = null;

        function showCustomAlert(msg, reload){
            $('#alertMessage').text(msg);
            $('#customAlertModal').css('display','flex').hide().fadeIn(200);
            $('#btn-alert-ok').off('click').on('click', function(){
                $('#customAlertModal').fadeOut(200);
                if(reload) location.reload();
            });
        }

        /* ── FullCalendar ─────────────────────────────── */
        var calendarEl = document.getElementById('calendar');
        window.calendar = new FullCalendar.Calendar(calendarEl, {
            initialView: 'dayGridMonth',
            locale: 'ko',
            selectable: true,
            headerToolbar: { left:'today', center:'prev title next', right:'' },
            editable: true,
            selectable: true,

            dayCellContent: function(info){ return info.dayNumberText.replace('일',''); },

            datesSet: function(){
                var d = window.calendar.getDate();
                var y = d.getFullYear(), m = d.getMonth()+1;

                var yHtml = '<select id="custom-year" class="fc-custom-select">';
                for(var i=2000;i<=2100;i++)
                    yHtml += '<option value="'+i+'"'+(i===y?' selected':'')+'>'+i+'년</option>';
                yHtml += '</select>';

                var mHtml = '<select id="custom-month" class="fc-custom-select">';
                for(var j=1;j<=12;j++){
                    var dm = j<10?'0'+j:''+j;
                    mHtml += '<option value="'+dm+'"'+(j===m?' selected':'')+'>'+j+'월</option>';
                }
                mHtml += '</select>';

                $('.fc-toolbar-title').html(yHtml+' '+mHtml);
                $('.fc-custom-select').off('change').on('change',function(){
                    window.calendar.gotoDate($('#custom-year').val()+'-'+$('#custom-month').val()+'-01');
                });
            },

            events: [
                <c:forEach var="sch" items="${list}" varStatus="status">
                {
                    id: '${sch.schedule_id}',
                    title: '${sch.company_name} 면접',
                    start: '${sch.schedule_date}',
                    extendedProps:{
                        company:'${sch.company_name}', position:'${sch.position}',
                        time:'${sch.schedule_time}', type:'${sch.interview_type}',
                        memo:'${sch.schedule_memo}'
                    }
                }<c:if test="${!status.last}">,</c:if>
                </c:forEach>
            ],

            eventClick: function(info){
                currentEvent = info.event;
                var x=info.jsEvent.pageX, y=info.jsEvent.pageY;
                $('#pop-title').text(currentEvent.title);
                $('#pop-position').text(currentEvent.extendedProps.position||'미정');
                $('#pop-date').text(currentEvent.startStr);
                $('#pop-time').text(currentEvent.extendedProps.time||'미정');
                $('#pop-type').text(currentEvent.extendedProps.type||'-');
                $('#pop-memo').text(currentEvent.extendedProps.memo||'-');
                var popW=260, winW=$(window).width();
                $('#event-popup').css({top:y+15+'px', left:(x+15+popW>winW?x-popW-15:x+15)+'px'}).fadeIn(150);
            },

            select: function(info){
                resetModal();
                $('#form-date').val(info.startStr);
                $('#modal-backdrop, #schedule-modal').fadeIn(200);
            }
        });
        window.calendar.render();

        /* ── 팝업/모달 닫기 ── */
        $('#close-popup').click(function(){ $('#event-popup').fadeOut(150); });
        $(document).click(function(e){
            if(!$(e.target).closest('#event-popup,.fc-event').length) $('#event-popup').fadeOut(150);
        });

        /* ── 전형 직접입력 ── */
        $('#form-type').change(function(){
            $(this).val()==='direct' ? $('#form-type-direct').show().focus() : $('#form-type-direct').hide().val('');
        });

        /* ── 수정 ── */
        $('#btn-go-edit').click(function(){
            $('#event-popup').hide();
            $('#modal-title').text('일정 수정');
            $('#form-id').val(currentEvent.id);
            $('#selectedCompanyName').val(currentEvent.extendedProps.company);
            $('#form-position').val(currentEvent.extendedProps.position);
            $('#form-date').val(currentEvent.startStr);
            $('#form-memo').val(currentEvent.extendedProps.memo);
            var t=(currentEvent.extendedProps.time||'14:00').split(':');
            $('#form-hour').val(t[0]); $('#form-minute').val(t[1]);
            var et=currentEvent.extendedProps.type;
            var exists=$('#form-type option').filter(function(){return $(this).val()===et;}).length>0;
            if(exists){$('#form-type').val(et);$('#form-type-direct').hide().val('');}
            else{$('#form-type').val('direct');$('#form-type-direct').show().val(et);}
            $('#modal-backdrop, #schedule-modal').fadeIn(200);
        });

        /* ── 삭제 ── */
        $('#btn-do-delete').click(function(){
            $('#event-popup').fadeOut(150);
            $('#customConfirmModal').css('display','flex').hide().fadeIn(200);
            $('#btn-real-delete').off('click').on('click',function(){
                $('#customConfirmModal').fadeOut(200);
                $.ajax({url:'/delete-calendar',type:'POST',data:{schedule_id:currentEvent.id},
                    success:function(){showCustomAlert('일정이 삭제되었습니다.',true);},
                    error:function(){showCustomAlert('DELETE ERROR');}
                });
            });
        });

        /* ── 저장 ── */
        $('#btn-save-schedule').click(function(){
            var id=$('#form-id').val(), st=$('#form-type').val();
            var ft=(st==='direct')?$('#form-type-direct').val():st;
            if(!$('#selectedCompanyName').val().trim()||(st==='direct'&&!ft.trim())){
                showCustomAlert('회사 이름과 면접 전형을 확인해 주세요.'); return;
            }
            $.ajax({
                url:id?'/update-calendar':'/add-calendar', type:'POST',
                data:{schedule_id:id,app_id:$('#form-appId').val(),
                    company_name:$('#selectedCompanyName').val(),position:$('#form-position').val(),
                    apply_date:$('#form-apply-date').val(),date:$('#form-date').val(),
                    time:$('#form-hour').val()+':'+$('#form-minute').val(),type:ft,memo:$('#form-memo').val()},
                success:function(){showCustomAlert('저장되었습니다!',true);},
                error:function(){showCustomAlert('저장 중 오류가 발생했습니다.');}
            });
        });

        $('#btn-modal-close, #modal-backdrop').click(function(){
            $('#modal-backdrop, #schedule-modal').fadeOut(200);
        });

        function resetModal(){
            $('#modal-title').text('새 일정 추가');
            $('#form-id,#form-position,#form-memo,#form-apply-date').val('');
            $('#selectedCompanyName').val('');
            $('#form-hour').val('14'); $('#form-minute').val('00');
            $('#form-type').val('코딩테스트');
            $('#form-type-direct').hide().val('');
        }

        /* ── FAB ── */
        $('#fabMain').click(function(e){
            e.stopPropagation();
            $(this).toggleClass('active');
            $('#fabMenu').fadeToggle(200).css('display','flex');
        });
        $('#fabAddSchedule').click(function(){
            resetModal();
            $('#form-date').val(new Date().toISOString().split('T')[0]);
            $('#modal-backdrop, #schedule-modal').fadeIn(200);
            $('#fabMain').removeClass('active'); $('#fabMenu').fadeOut(100);
        });
        $(document).on('click',function(e){
            if(!$(e.target).closest('.fab-container').length){
                $('#fabMain').removeClass('active'); $('#fabMenu').fadeOut(200);
            }
        });

        /* ── 이번 주 일정 토글 ── */
        $('#weekToggle').click(function(){
            $(this).toggleClass('open');
            $('#weekDropdown').slideToggle(250);
        });

        /* ── To-do ── */
        var TODO_KEY='devdesk_todos_v2';
        function loadTodos(){try{return JSON.parse(localStorage.getItem(TODO_KEY))||[];}catch(e){return[];}}
        function saveTodos(t){localStorage.setItem(TODO_KEY,JSON.stringify(t));}

        function renderTodos(){
            var todos=loadTodos(), $list=$('#todo-list');
            $list.empty();
            if(!todos.length){$list.append('<li class="todo-empty">+ 버튼으로 추가해보세요</li>');return;}
            todos.forEach(function(item,idx){
                var $li=$('<li class="todo-item'+(item.done?' done':'')+'"></li>');
                var $cb=$('<input type="checkbox"'+(item.done?' checked':'')+'>');
                $cb.on('change',function(){todos[idx].done=this.checked;saveTodos(todos);renderTodos();});
                var $txt=$('<span class="todo-text"></span>').text(item.text);
                var $del=$('<button class="todo-del" title="삭제">×</button>');
                $del.on('click',function(){todos.splice(idx,1);saveTodos(todos);renderTodos();});
                $li.append($cb,$txt,$del); $list.append($li);
            });
        }

        $('#todo-add-btn').click(function(e){
            e.stopPropagation();
            $('#todo-input-row').toggle();
            if($('#todo-input-row').is(':visible')) $('#todo-input').focus();
        });
        function addTodo(){
            var text=$('#todo-input').val().trim(); if(!text) return;
            var todos=loadTodos(); todos.push({text:text,done:false}); saveTodos(todos);
            $('#todo-input').val(''); $('#todo-input-row').hide(); renderTodos();
        }
        $('#todo-input-confirm').click(addTodo);
        $('#todo-input').keydown(function(e){
            if(e.key==='Enter') addTodo();
            if(e.key==='Escape'){$('#todo-input-row').hide();$(this).val('');}
        });
        renderTodos();
        $('#todo-month-title').text((new Date().getMonth()+1)+'월 To-do');

        /* ── 미니 캘린더 (점 표시) ── */
        var eventDates={};
        <c:forEach var="sch" items="${list}">
        (function(){var d='${sch.schedule_date}';if(d)eventDates[d]=(eventDates[d]||0)+1;})();
        </c:forEach>

        var currentDispDate=new Date();
        function renderMiniCalendar(d){
            var year=d.getFullYear(), month=d.getMonth();
            var today=new Date();
            var todayStr=today.getFullYear()+'-'+String(today.getMonth()+1).padStart(2,'0')+'-'+String(today.getDate()).padStart(2,'0');
            document.getElementById('g-cal-title').textContent=year+'년 '+(month+1)+'월';

            var firstDay=new Date(year,month,1), lastDay=new Date(year,month+1,0);
            var prevLast=new Date(year,month,0).getDate();
            var firstIdx=(firstDay.getDay()+6)%7;
            var html='';

            for(var i=firstIdx;i>0;i--)
                html+='<div class="g-day-cell"><div class="g-day-num other-month">'+(prevLast-i+1)+'</div></div>';

            for(var day=1;day<=lastDay.getDate();day++){
                var ds=year+'-'+String(month+1).padStart(2,'0')+'-'+String(day).padStart(2,'0');
                var cls=(ds===todayStr?' today':'')+(new Date(year,month,day).getDay()===0?' sun':'');
                var cnt=eventDates[ds]||0, dots='';
                if(cnt>0){
                    dots='<div class="g-dots">';
                    for(var k=0;k<Math.min(cnt,3);k++) dots+='<span class="g-dot"></span>';
                    dots+='</div>';
                }
                html+='<div class="g-day-cell" onclick="window.calendar.gotoDate(\''+ds+'\')">'
                    +'<div class="g-day-num'+cls+'">'+day+'</div>'+dots+'</div>';
            }

            var remain=(firstIdx+lastDay.getDate())%7;
            if(remain>0) for(var nd=1;nd<=7-remain;nd++)
                html+='<div class="g-day-cell"><div class="g-day-num other-month">'+nd+'</div></div>';

            document.getElementById('g-cal-days').innerHTML=html;
        }

        document.getElementById('g-prev-month').addEventListener('click',function(){
            currentDispDate.setMonth(currentDispDate.getMonth()-1); renderMiniCalendar(currentDispDate);
        });
        document.getElementById('g-next-month').addEventListener('click',function(){
            currentDispDate.setMonth(currentDispDate.getMonth()+1); renderMiniCalendar(currentDispDate);
        });
        renderMiniCalendar(currentDispDate);

    }); // end ready
</script>
