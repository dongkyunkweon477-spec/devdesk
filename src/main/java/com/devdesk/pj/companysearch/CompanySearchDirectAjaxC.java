package com.devdesk.pj.companysearch;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/company-search/direct-insert/ajax")
public class CompanySearchDirectAjaxC extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String companyName = request.getParameter("companyName");
        if (companyName == null || companyName.isBlank()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"회사명이 비어있습니다.\"}");
            return;
        }
        if (companyName.length() > 50) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"회사명은 50자 이내로 입력해주세요.\"}");
            return;
        }


        int newId = CompanySearchDAO.COMPANY_SEARCH_DAO.directInsertCompany(companyName);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("{\"newId\":" + newId + "}");


    }

    public void destroy() {
    }
}