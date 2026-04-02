package com.devdesk.pj.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AccountC", value = "/account")
public class AccountC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {



        request.setAttribute("content", "user/account.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        boolean isSuccess = MemberDAO.MBAO.signUp(req);

        if (isSuccess) {
            System.out.println("회원가입 성공!");

            // 가입 성공 시 메인 화면으로 완전히 이동 (URL 변경)
            resp.sendRedirect(req.getContextPath() + "/index.jsp");
        } else {
            System.out.println("회원가입 실패");

            // 실패 시 다시 회원가입 화면(알맹이)을 세팅해서 껍데기로 보냄
            req.setAttribute("content", "user/account.jsp");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }


    }

    public void destroy() {



    }
}