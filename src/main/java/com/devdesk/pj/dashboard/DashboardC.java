package com.devdesk.pj.dashboard;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/dashboard")
public class DashboardC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        DashboardDAO.countGroupbystage(request);
        DashboardDAO.getFunnelData(DashboardDAO.countGroupbystage(request));
        List<Map<String, Object>> funnelData = DashboardDAO.getFunnelData(DashboardDAO.countGroupbystage(request));
        request.setAttribute("funnelData", funnelData);
        request.setAttribute("content", "/dashboard/dashboard.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);

    }


    public void destroy() {
    }
}