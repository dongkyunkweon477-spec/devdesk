package com.devdesk.pj.calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "calenderC", value = "/calender")
public class Calendar_newC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Object sessionMemberId = request.getSession().getAttribute("memberId");
        int memberId = 3; // 💥 테스트용 3번 유저! (나중에 로그인 기능 붙이면 아래 if문이 작동합니다)

        if (sessionMemberId != null) {
            memberId = (int) sessionMemberId;
        }

        // 2. 알아낸 memberId로 일정 가져오기
        
        ArrayList<Schedule_newDTO> schList = Schedule_newDAO.SCAO.getCalendarEvents(memberId);
        request.setAttribute("list", schList);

        // 3. 달력 화면으로 이동!
        request.getRequestDispatcher("calendar/index_cal.jsp").forward(request, response);
    }
}