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
        String category = request.getParameter("category");

        // 검색 파라미터가 있으면 검색 실행
        if (searchType != null && keyword != null && !keyword.trim().isEmpty()) {
            BoardDAO.searchBoards(request);
        } else if (category != null && !category.trim().isEmpty() && !category.equals("전체") &&
                (sort != null && !sort.trim().isEmpty())) {
            // 카테고리와 정렬이 함께 있는 경우 결합 처리
            BoardDAO.searchBoardsByCategoryAndSort(request);
        } else if (category != null && !category.trim().isEmpty() && !category.equals("전체")) {
            // 카테고리 필터링 실행
            BoardDAO.searchBoardsByCategory(request);
        } else if ("popular".equals(sort)) {
            BoardDAO.showPopularBoard(request);
        } else if ("viewcount".equals(sort)) {
            BoardDAO.showViewCountBoard(request);
        } else {
            BoardDAO.showAllBoard(request);
        }

        String p = request.getParameter("p");
        int page = 1;
        if (p != null && !p.trim().isEmpty()) {
            try {
                page = Integer.parseInt(p.trim());
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        BoardDAO.paging(request, page);

        request.setAttribute("content", "board/board.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    public void destroy() {
    }
}
