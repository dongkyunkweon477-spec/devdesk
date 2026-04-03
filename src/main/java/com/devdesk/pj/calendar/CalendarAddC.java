package com.devdesk.pj.calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "CalendarAddC", value = "/add-calender")
public class CalendarAddC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //전체조회
        ArrayList<ScheduleDTO> schList = ScheduleDAO.SCAO.showAllSch();
        request.setAttribute("list", schList);

    //index_cal.jsp로 이동
        request.getRequestDispatcher("calendar/index_cal.jsp").forward(request,response);
    }

    public void destroy() {
    }
}