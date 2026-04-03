package com.devdesk.pj.board;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "BoardDelC", value = "/board_del")
public class BoardDelC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    //일
        BoardDAO.delBoard(request);

//        request.setAttribute("content", "board/boardadd.jsp");
//        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    public void destroy() {
    }
}