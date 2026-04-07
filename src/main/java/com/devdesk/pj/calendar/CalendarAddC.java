package com.devdesk.pj.calendar;

import com.devdesk.pj.application.ApplicationDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "CalendarAddC", value = "/add-calendar")
public class CalendarAddC extends HttpServlet {

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
            request.setCharacterEncoding("UTF-8");
            try {
                //등록~
                Schedule_newDAO.SCAO.addSchedule(request);

                // AJAX에게 "저장 성공했어!" 라고 알려주는 부분 (Redirect 금지)
                response.setContentType("text/plain; charset=UTF-8");
                response.getWriter().write("success");

            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        }
}