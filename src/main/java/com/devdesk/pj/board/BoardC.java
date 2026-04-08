package com.devdesk.pj.board;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/board")
public class BoardC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String sort = request.getParameter("sort");
        String searchType = request.getParameter("searchType");
        String keyword = request.getParameter("keyword");
        
        // 검색 파라미터가 있으면 검색 실행
        if (searchType != null && keyword != null && !keyword.trim().isEmpty()) {
            BoardDAO.searchBoards(request);
        } else if ("popular".equals(sort)) {
            BoardDAO.showPopularBoard(request);
        } else if ("viewcount".equals(sort)) {
            BoardDAO.showViewCountBoard(request);
        } else {
            BoardDAO.showAllBoard(request);
        }

        request.setAttribute("content", "board/board.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    public void destroy() {
    }
}
