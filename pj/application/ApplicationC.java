package com.devdesk.pj.application;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ApplicationC", value = "/application")
public class ApplicationC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ApplicationDAO.selectAllCompanies(request);
        request.getRequestDispatcher("/application/application.jsp").forward(request,response);
    }



    public void destroy() {
    }
}