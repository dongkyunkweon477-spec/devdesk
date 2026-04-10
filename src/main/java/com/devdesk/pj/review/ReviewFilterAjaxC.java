package com.devdesk.pj.review;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/review/filter/ajax")
public class ReviewFilterAjaxC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String companyIdParam = request.getParameter("companyId");
        Integer companyId = (companyIdParam != null && !companyIdParam.isBlank())
                ? Integer.parseInt(companyIdParam) : null;
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
        com.google.gson.Gson gson = new com.google.gson.Gson();
        String json = gson.toJson(data);

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(json);

    }

    public void destroy() {
    }
}