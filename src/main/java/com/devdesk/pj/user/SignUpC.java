package com.devdesk.pj.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "SignUpC", value = "/user-signup")
public class SignUpC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        MemberDAO.MBAO.signUp(request);


        request.setAttribute("content","user/signUp.jsp");
        request.getRequestDispatcher("user/signUp.jsp").forward(request,response);

    }

    public void destroy() {
    }
}