package com.devdesk.pj.review;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ReviewC", value = "/review")
public class ReviewC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        String companyIdP = request.getParameter("companyId");
        ArrayList<ReviewVO> reviews;
        if (companyIdP != null && !companyIdP.isBlank()) {
            int companyId = Integer.parseInt(request.getParameter("companyId"));
            reviews = ReviewDAO.REVIEW_DAO.getReviewsByCompany(companyId, 1, 5);
            request.setAttribute("reviews", reviews);
        } else {
            reviews = ReviewDAO.REVIEW_DAO.getReviewAll();
        }
        request.setAttribute("reviews", reviews);
        request.setAttribute("content", "/review/review.jsp");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    public void destroy() {
    }
}