package com.devdesk.pj.calendar;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "calenderC", value = "/calender")
public class calenderC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    //index_cal.jsp로 이동
        request.getRequestDispatcher("calendar/index_cal.jsp").forward(request,response);

    }

    public void destroy() {
    }
}