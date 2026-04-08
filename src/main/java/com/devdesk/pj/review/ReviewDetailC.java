package com.devdesk.pj.review;

import com.devdesk.pj.companysearch.CompanySearchDAO;
import com.devdesk.pj.companysearch.CompanySearchVO;
import com.devdesk.pj.user.MemberDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@WebServlet("/review/detail")
public class ReviewDetailC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        int reviewId = Integer.parseInt(request.getParameter("reviewId"));
        Set<Integer> viewed = (Set<Integer>) request.getSession().getAttribute("viewedReviews");
        if (viewed == null) {
            viewed = new HashSet<>();
            request.getSession().setAttribute("viewedReviews", viewed);
        }
        if (!viewed.contains(reviewId)) {
            ReviewDAO.REVIEW_DAO.increaseViewCount(reviewId);
            viewed.add(reviewId);
        }
        MemberDTO user = (MemberDTO) request.getSession().getAttribute("user");
        if (user != null) {
            boolean isLiked = ReviewDAO.REVIEW_DAO.isLiked(user.getMember_id(), reviewId);
            request.setAttribute("isLiked", isLiked);
        }
        ReviewVO review = ReviewDAO.REVIEW_DAO.getReviewById(reviewId);
        CompanySearchVO company = CompanySearchDAO.COMPANY_SEARCH_DAO.getCompanyById(review.getReviewCompanyId());
        request.setAttribute("r", review);
        request.setAttribute("company", company);
        request.setAttribute("content", "/review/reviewDetail.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    public void destroy() {
    }
}