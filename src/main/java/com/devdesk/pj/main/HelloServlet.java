package com.devdesk.pj.main;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/main")
public class HelloServlet extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
<<<<<<< HEAD
//        AccountDAO.ADAO.loginCheck(request); // 로그인 하고 확인
        request.setAttribute("content", "home.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);

=======
        HttpSession session = request.getSession();
        
        if (session.getAttribute("user") != null) {
            // 로그인 한 상태라면 대시보드로 이동
            response.sendRedirect("dashboard");
        } else {
            // 로그인 전이라면 기존처럼 home.jsp 표시
            request.setAttribute("content", "home.jsp");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
>>>>>>> fe386025346c39d7bb8156705713e531893523f8
    }

    public void destroy() {
    }
}