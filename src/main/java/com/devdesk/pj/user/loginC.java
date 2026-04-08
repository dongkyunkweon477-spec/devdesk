package com.devdesk.pj.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "loginC", value = "/login")
public class loginC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        request.setAttribute("content", "user/login.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 로그인 하는 일
        request.setCharacterEncoding("UTF-8");
        MemberDAO.MBAO.login(request);

        HttpSession session = request.getSession();

        if (session.getAttribute("user") != null) {

            String dest = (String) session.getAttribute("dest");

            if (dest != null) {
                session.removeAttribute("dest");
                response.sendRedirect(dest);
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }

        } else {
            request.setAttribute("content", "user/login.jsp");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    public void destroy() {
    }
}