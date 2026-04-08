package com.devdesk.pj.calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "calendarC", value = "/calendar")
public class Calendar_newC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Object sessionMemberId = request.getSession().getAttribute("memberId");
        int memberId = 6; // 테스트용 6번 유저!

        if (sessionMemberId != null) {
            memberId = (int) sessionMemberId;
        }

        // 달력일정
        ArrayList<Schedule_newDTO> schList = Schedule_newDAO.SCAO.getCalendarEvents(memberId);
        request.setAttribute("list", schList);

        // company list
        ArrayList<String> companyList = Schedule_newDAO.SCAO.getAllCompanyNames();
        request.setAttribute("companyList", companyList);

        // 되돌아가기(달력화면)
        request.getRequestDispatcher("calendar/index_cal.jsp").forward(request, response);
    }
}