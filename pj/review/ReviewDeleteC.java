package com.devdesk.pj.review;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ReviewC", value = "/review/delete")
public class ReviewDeleteC extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int reviewId = Integer.parseInt(request.getParameter("reviewId"));
        int memberId = (int) request.getSession().getAttribute("memberId");

        ReviewVO review = ReviewDAO.REVIEW_DAO.getReviewById(reviewId);
        if (review.getReviewMemberId() != memberId) {
            response.sendRedirect(request.getContextPath() + "/review");
            return;
        }
        ReviewDAO.REVIEW_DAO.deleteReview(reviewId);
        response.sendRedirect(request.getContextPath() + "/review");
    }

    public void destroy() {
    }
}