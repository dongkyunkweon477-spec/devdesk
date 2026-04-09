package com.devdesk.pj.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "PasswordUpC", value = "/password-update")
public class PasswordUpC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("content", "user/password_update.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        if (MemberDAO.MBAO.passwordUpdate(request, response)) {
            request.getSession().setAttribute("pwSuccess", "true");
            response.sendRedirect("mypage");

        } else {
            request.setAttribute("errorMsg", "현재 비밀번호가 일치하지 않습니다.");
            request.setAttribute("content", "user/password_update.jsp");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    public void destroy() {
    }
}