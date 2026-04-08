package com.devdesk.pj.review;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.devdesk.pj.companysearch.CompanySearchDAO;

@WebServlet(name = "ReviewC", value = "/review")
public class ReviewC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        int page = 1;
        int pageSize = 5;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        String companyIdP = request.getParameter("companyId");
        int totalCount;
        ArrayList<ReviewVO> reviews;
        if (companyIdP != null && !companyIdP.isBlank()) {
            int companyId = Integer.parseInt(request.getParameter("companyId"));
            reviews = ReviewDAO.REVIEW_DAO.getReviewsByCompany(companyId, page, pageSize);
            totalCount = ReviewDAO.REVIEW_DAO.getReviewCount(companyId);
            request.setAttribute("reviews", reviews);
        } else {
            reviews = ReviewDAO.REVIEW_DAO.getReviewAll(page, pageSize);
            totalCount = ReviewDAO.REVIEW_DAO.getReviewCount(null);

        }
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);
        request.setAttribute("reviews", reviews);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("pageSize", pageSize);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("companyId", companyIdP);
        List<String> industries = CompanySearchDAO.COMPANY_SEARCH_DAO.getAllIndustries();
        List<String> locations = CompanySearchDAO.COMPANY_SEARCH_DAO.getAllLocation();
        int totalCompanyCount = CompanySearchDAO.COMPANY_SEARCH_DAO.getTotalCompanyCount();
        request.setAttribute("industries", industries);
        request.setAttribute("locations", locations);
        request.setAttribute("totalCompanyCount", totalCompanyCount);
        request.setAttribute("content", "/review/review.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    public void destroy() {
    }
}