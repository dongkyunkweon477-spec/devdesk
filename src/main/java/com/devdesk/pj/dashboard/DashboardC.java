package com.devdesk.pj.dashboard;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/dashboard")
public class DashboardC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        DashboardDAO.countGroupbystage(request);
        request.setAttribute("content", "/dashboard/dashboard.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }


    public void destroy() {
    }
}