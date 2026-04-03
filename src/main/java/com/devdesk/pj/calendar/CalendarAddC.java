package com.devdesk.pj.calendar;

import com.devdesk.pj.application.ApplicationDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "CalendarAddC", value = "/add-calender")
public class CalendarAddC extends HttpServlet {

        public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {


            ApplicationDAO.SCAO.addSchedule(request);
            response.sendRedirect("application_list");


        }
}