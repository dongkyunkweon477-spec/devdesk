package com.devdesk.pj.report;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/report_form")
public class ReportFormC extends HttpServlet {

    // GET /report_form?targetType=review&targetId=1&targetTitle=...
    // → 신고 폼 페이지 표시 (URL 파라미터는 JSP에서 ${param.*}으로 접근)
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 로그인 체크
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        request.setAttribute("content", "report/report_form.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}