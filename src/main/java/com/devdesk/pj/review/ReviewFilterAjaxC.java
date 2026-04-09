package com.devdesk.pj.review;

import com.devdesk.pj.companysearch.CompanySearchDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/review/filter/ajax")
public class ReviewFilterAjaxC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        int companyId = Integer.parseInt(request.getParameter("companyId"));
        String interviewType = request.getParameter("interviewType");
        String result = request.getParameter("result");
        String sort = request.getParameter("sort");
        int page = 1;
        int pageSize = 10;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        Map<String, Object> data = ReviewDAO.REVIEW_DAO
                .getFilteredReviews(companyId, interviewType, result, sort, page, pageSize);
        List<String> reviews = (List<String>) data.get("reviews");
        String json = "{"
                + "\"reviews\":[" + String.join(",", reviews) + "],"
                + "\"totalCount\":" + data.get("totalCount") + ","
                + "\"totalPages\":" + data.get("totalPages") + ","
                + "\"currentPage\":" + data.get("currentPage")
                + "}";
        response.setContentType("application/json;charset=UTP-8");
        response.getWriter().write(json);

    }

    public void destroy() {
    }
}