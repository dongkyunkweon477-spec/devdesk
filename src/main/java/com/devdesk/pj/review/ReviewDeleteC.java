package com.devdesk.pj.review;

import com.devdesk.pj.user.MemberDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/review/delete")
public class ReviewDeleteC extends HttpServlet {

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int reviewId = Integer.parseInt(request.getParameter("reviewId"));

        // 세션에서 MemberDTO 객체 가져오기
        MemberDTO user = (MemberDTO) request.getSession().getAttribute("user");

        // 로그인 상태 확인
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login.jsp");
            return;
        }
        int memberId = user.getMember_id();

        ReviewVO review = ReviewDAO.REVIEW_DAO.getReviewById(reviewId);

        // 리뷰가 존재하지 않거나 작성자가 아닐 경우
        if (review == null || review.getReviewMemberId() != memberId) {
            response.sendRedirect(request.getContextPath() + "/review");
            return;
        }

        ReviewDAO.REVIEW_DAO.deleteReview(reviewId);
        response.sendRedirect(request.getContextPath() + "/review");
    }

    public void destroy() {
    }
}