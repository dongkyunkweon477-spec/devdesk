package com.devdesk.pj.admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "MemberDeleteC", value = "/admin/memberDelete")
public class MemberDeleteC extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. JS에서 보낸 타겟 회원의 ID 받기
        int memberId = Integer.parseInt(request.getParameter("member_id"));

        // 2. DAO의 철퇴 메서드 호출!
        boolean isSuccess = AdminDAO.ADAO.forceDeleteMember(memberId);

        // 3. 결과를 다시 JS(브라우저)로 알려주기 (화면 이동 없음!)
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();

        if (isSuccess) {
            out.print("success");
        } else {
            out.print("fail");
        }
        out.flush();
    }
}