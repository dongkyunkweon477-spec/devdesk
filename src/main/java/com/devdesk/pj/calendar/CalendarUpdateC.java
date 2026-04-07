package com.devdesk.pj.calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/update-calendar")
public class CalendarUpdateC extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        try {
            int scheduleId = Integer.parseInt(request.getParameter("schedule_id"));
            String companyName = request.getParameter("company_name");
            String position = request.getParameter("position");
            String date = request.getParameter("date");
            String time = request.getParameter("time");
            String type = request.getParameter("type");
            String memo = request.getParameter("memo");

            // DAO 호출 (새로 만든 updateSchedule 사용)
            Schedule_newDAO.SCAO.updateSchedule(request, response);
            response.setContentType("text/plain; charset=UTF-8");
            response.getWriter().write("success");

            System.out.println("update success");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }
}