package com.devdesk.pj.admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AdminC", value = "/admin")
public class AdminC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

// 1. 전체 회원 목록 (하단 표를 위해)
        List<AdminVO> members = AdminDAO.ADAO.getAllMembers();
        request.setAttribute("members", members);

        // 🌟 2. 진짜 대시보드 통계 데이터 가져오기!
        java.util.Map<String, Object> stats = AdminDAO.ADAO.getDashboardStats();

        // JSP에서 쓰기 편하게 꺼내서 세팅해줍니다.
        request.setAttribute("totalMembers", stats.get("totalMembers"));
        request.setAttribute("todayNewMembers", stats.get("todayNewMembers"));
        request.setAttribute("totalBoards", stats.get("totalBoards"));

        request.setAttribute("jobLabels", stats.get("jobLabels"));
        request.setAttribute("jobData", stats.get("jobData"));

        request.setAttribute("trendLabels", stats.get("trendLabels"));
        request.setAttribute("trendData", stats.get("trendData"));

        // 3. 페이지 이동
        request.setAttribute("content", "admin/admin.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }


    public void destroy() {
    }
}