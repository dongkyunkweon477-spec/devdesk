package com.devdesk.pj.review;

import com.devdesk.pj.user.MemberDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/review/bookmark")
public class ReviewBookmarkC extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        MemberDTO user = (MemberDTO) request.getSession().getAttribute("user");
        if (user == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\":\"login required\"}");
            return;
        }
        int memberId = user.getMember_id();
        int reviewId = Integer.parseInt(request.getParameter("reviewId"));

        boolean bookmarked = ReviewDAO.REVIEW_DAO.toggleBookmark(memberId, reviewId);
        ReviewVO review = ReviewDAO.REVIEW_DAO.getReviewById(reviewId);
        if (review == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("{\"error\":\"review not found\"}");
            return;
        }

        response.getWriter().write("{\"bookmarked\":" + bookmarked
                + ",\"count\":" + review.getReviewBookmarkCount() + "}");
    }

    public void destroy() {
    }
}