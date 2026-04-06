package com.devdesk.pj.review;

import com.devdesk.pj.companysearch.CompanySearchDAO;
import com.devdesk.pj.companysearch.CompanySearchVO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@WebServlet("/review/write")
public class ReviewWriteFormC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String companyIdP = request.getParameter("companyId");
        if (companyIdP != null && !companyIdP.isBlank()) {
            CompanySearchVO company = CompanySearchDAO.COMPANY_SEARCH_DAO.getCompanyById(Integer.parseInt(companyIdP));
            request.setAttribute("company", company);
        }

        request.setAttribute("content", "/review/reviewWrite.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("utf-8");

        String companyId = request.getParameter("companyId");
        String difficulty = request.getParameter("difficulty");

        if (companyId == null || companyId.isBlank() || difficulty == null || difficulty.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/review/write");
            return;
        }

        ReviewVO vo = new ReviewVO();
        vo.setReviewCompanyId(Integer.parseInt(companyId));
        vo.setReviewMemberId((1)); //request.getSession().getAttribute("memberId"));
        vo.setReviewTitle(request.getParameter("title"));
        vo.setReviewJobPosition(request.getParameter("jobPosition"));
        vo.setReviewInterviewType(request.getParameter("interviewType"));
        vo.setReviewDifficulty(Integer.parseInt(difficulty));
        vo.setReviewResult(request.getParameter("result"));
        vo.setReviewContent(request.getParameter("content"));

        String ic = request.getParameter("interviewerCount");
        if (ic != null && !ic.isBlank()) vo.setReviewInterviewerCount(Integer.parseInt(ic));
        String sc = request.getParameter("studentCount");
        if (sc != null && !sc.isBlank()) vo.setReviewStudentCount(Integer.parseInt(sc));
        vo.setReviewAtmosphere(request.getParameter("atmosphere"));
        vo.setReviewContactMethod(request.getParameter("contactMethod"));
        String cd = request.getParameter("contactDays");
        if (cd != null && !cd.isBlank()) vo.setReviewContactDays(Integer.parseInt(cd));

        ReviewDAO.REVIEW_DAO.insertReview(vo);
        response.sendRedirect(request.getContextPath() + "/review");


    }

    public void destroy() {
    }
}