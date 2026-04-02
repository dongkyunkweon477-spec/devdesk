package com.devdesk.pj.main;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/main")
public class HelloServlet extends HttpServlet {

<<<<<<< HEAD
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

=======
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
//        AccountDAO.ADAO.loginCheck(request); // 로그인 하고 확인
        request.setAttribute("content","home.jsp");
        request.getRequestDispatcher("index.jsp").forward(request,response);
>>>>>>> b8ad14f885f9e1161f05260b8572471a1b34febc
    }

    public void destroy() {
    }
}