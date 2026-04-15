package com.devdesk.pj.companysearch;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/company-search/ajax")
public class CompanySearchAjaxC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");

        Map<String, String> conditions = new HashMap<>();

// 텍스트 필드
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

// 범위 필드 — 검색 전에 넣기
        Map<String, String> rangeFields = new HashMap<>();
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

// 페이징
        int page = 1;
        int pageSize = 10;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isBlank()) {
            page = Integer.parseInt(pageParam);
        }

// 검색 실행
        Map<String, Object> result = CompanySearchDAO.COMPANY_SEARCH_DAO
                .companySearchPaged(conditions, page, pageSize);

// JSON 응답
        com.google.gson.Gson gson = new com.google.gson.GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(gson.toJson(result));
    }

    public void destroy() {
    }
}