package com.devdesk.pj.review;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "ReviewC", value = "/review/detail")
public class ReviewDetailC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int reviewId = Integer.parseInt(request.getParameter("reviewId"));
        ReviewVO review = ReviewDAO.REVIEW_DAO.getReviewById(reviewId);
    }

    public void destroy() {
    }
}