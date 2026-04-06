package com.devdesk.pj.board;

import com.devdesk.pj.Comment.CommentDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "BoardDetailC", value = "/BoardDetailC")
public class BoardDetailC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        // 1. 게시글 번호 받기
        int boardId = Integer.parseInt(request.getParameter("id"));

        //일
        BoardDAO.getBoard(request);
        // 3. 해당 게시글의 댓글 리스트 조회 (추가할 부분)
        CommentDAO.getComment(request, boardId);
        // CommentDAO에 해당 게시글의 댓글을 다 가져오는 메서드를 만듭니다.
        // 어디로
        // loginCheck
        request.setAttribute("content", "board/boardDetail.jsp");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    public void destroy() {
    }
}