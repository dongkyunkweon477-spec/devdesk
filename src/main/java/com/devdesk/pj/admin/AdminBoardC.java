package com.devdesk.pj.admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

// 🌟 어드민 보안 필터가 지켜주는 안전한 경로!
@WebServlet("/admin/board")
public class AdminBoardC extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. DAO에서 전체 게시글 목록 가져오기
        List<AdminBoardVO> boards = AdminDAO.ADAO.getAllAdminBoards();
        request.setAttribute("boards", boards);

        // 2. 게시글 관리 화면으로 포워딩
        request.setAttribute("content", "admin/admin_board.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        boolean isSuccess = false;

        if ("delete".equals(action)) {
            int boardId = Integer.parseInt(request.getParameter("board_id"));
            // 🌟 아까 만든 DAO의 무적 삭제 메서드 호출!
            isSuccess = AdminDAO.ADAO.deleteBoardAdmin(boardId);
        }

        // JS로 결과 응답 (success or fail)
        response.setContentType("text/plain;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.print(isSuccess ? "success" : "fail");
        out.flush();
    }
}