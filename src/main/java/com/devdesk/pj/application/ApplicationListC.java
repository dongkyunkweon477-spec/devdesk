package com.devdesk.pj.application;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ApplicationListC", value = "/application_list")
public class ApplicationListC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ApplicationDAO.selectAllApplications(request);
        ApplicationDAO.selectAllCompanies(request);
        request.setAttribute("content", "/application/application_list.jsp");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }


    public void destroy() {
    }
}