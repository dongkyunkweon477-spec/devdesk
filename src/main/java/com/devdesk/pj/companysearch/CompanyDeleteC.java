package com.devdesk.pj.companysearch;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/company/delete")
public class CompanyDeleteC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int companyId = Integer.parseInt(request.getParameter("companyId"));
        CompanySearchDAO.COMPANY_SEARCH_DAO.deleteCompany(companyId);
        response.sendRedirect(request.getContextPath() + "/review");
    }
}
