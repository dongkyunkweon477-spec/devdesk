package com.devdesk.pj.companysearch;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/company-search/ajax")
public class CompanySearchAjaxC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        Map<String,String> conditions = new HashMap<>();
        Map<String, String> textFields = new HashMap<>();
        textFields.put("companyName", "company_name");
        textFields.put("companyIndustry", "company_industry");
        textFields.put("companyLocation", "company_location");

        for (Map.Entry<String, String> e : textFields.entrySet()) {
            String val = request.getParameter(e.getKey());
            if (val != null && !val.isBlank()) {
                conditions.put(e.getValue(), val.trim());
            }
        }
        Map<String,String> rangeFields = new HashMap<>();
        rangeFields.put("minRating", "min_company_rating");
        rangeFields.put("maxRating", "max_company_rating");
        rangeFields.put("minSize", "min_company_size");
        rangeFields.put("maxSize", "max_company_size");
        for (Map.Entry<String, String> e : rangeFields.entrySet()) {
            String val = request.getParameter(e.getKey());
            if (val != null && !val.isBlank()) {
                conditions.put(e.getValue(), val.trim());
            }
        }


        List<String> results = CompanySearchDAO.COMPANY_SEARCH_DAO.companySearch(conditions);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write("[" + String.join(",", results) + "]");
    }

    public void destroy() {
    }
}