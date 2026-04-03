package com.devdesk.pj.todo;

import com.devdesk.pj.application.ApplicationDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "todoC", value = "/todo")
public class todoC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/todo/todo.jsp").forward(request,response);
    }



    public void destroy() {
    }
}