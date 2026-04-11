package com.devdesk.pj.admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminMemberC", value = "/admin/member")
public class AdminMemberC extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. AdminDAO에 이미 만들어둔 전체 회원 조회 메서드 재활용!
        List<AdminVO> members = AdminDAO.ADAO.getAllMembers();
        request.setAttribute("members", members);

        // 2. 회원 관리 전용 JSP 화면으로 이동
        request.setAttribute("content", "admin/admin_member.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}