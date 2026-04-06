package com.devdesk.pj.user;

import com.devdesk.pj.auth.AuthDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "MypageC", value = "/mypage")
public class MypageC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        if (AuthDAO.ADAO.isLoggedIn(request)) {

            request.setAttribute("content", "user/mypage.jsp");
            request.getRequestDispatcher("index.jsp").forward(request, response);

        } else {
            response.sendRedirect("login");
        }

    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    public void destroy() {
    }
}