package com.devdesk.pj.companysearch;

import com.devdesk.pj.review.ReviewDAO;
import com.devdesk.pj.review.ReviewVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@WebServlet("/company-detail")
public class CompanyDetailC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        int companyId = Integer.parseInt(request.getParameter("companyId"));
        int totalCount = ReviewDAO.REVIEW_DAO.getReviewCount(companyId);
        int totalPages = (int) Math.ceil((double) totalCount / 5);
        int page = 1;
        int pageSize = 10;
        if (request.getParameter("page") != null) {
            page = Integer.parseInt(request.getParameter("page"));
        }
        CompanySearchVO company = CompanySearchDAO.COMPANY_SEARCH_DAO.getCompanyById(companyId);
        Map<String, Object> stats = CompanySearchDAO.COMPANY_SEARCH_DAO.getCompanyStats(companyId);
        ArrayList<ReviewVO> reviews = ReviewDAO.REVIEW_DAO.getReviewsByCompany(companyId, page, pageSize);

        request.setAttribute("company", company);
        request.setAttribute("reviews", reviews);
        request.setAttribute("stats", stats);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("content", "/company-detail/companyDetail.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }


    public void destroy() {
    }
}