package com.devdesk.pj.main;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/main")
public class HelloServlet extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        
        if (session.getAttribute("user") != null) {
            // 로그인 한 상태라면 대시보드로 이동
            response.sendRedirect("dashboard");
        } else {
            // 로그인 전이라면 기존처럼 home.jsp 표시
            request.setAttribute("content", "home.jsp");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    public void destroy() {
    }
}