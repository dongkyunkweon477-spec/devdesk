package com.devdesk.pj.Comment;

import com.devdesk.pj.board.BoardDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "CommentAddC", value = "/comment_add")
public class CommentAddC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        CommentDAO.addComment(request);

        int boardId = Integer.parseInt(request.getParameter("board_id"));

        response.sendRedirect("BoardDetailC?id=" + boardId);
    }

    public void destroy() {
    }
}