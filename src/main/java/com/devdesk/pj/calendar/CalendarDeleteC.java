package com.devdesk.pj.calendar;

import com.devdesk.pj.user.MemberDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "CalendarDeleteC", value = "/delete-calendar")
public class CalendarDeleteC extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        try {
            int scheduleId = Integer.parseInt(request.getParameter("schedule_id"));

            Schedule_newDAO.SCAO.deleteSchedule(scheduleId);

            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write("success");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}