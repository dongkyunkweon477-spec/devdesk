package com.devdesk.pj.board;

import com.devdesk.pj.Comment.CommentDAO;
import com.devdesk.pj.Like.LikeDAO;
import com.devdesk.pj.user.MemberDTO;

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

        // 2. 게시글 정보 가져오기 (LikeDAO 선언 필요)
        BoardDAO.getBoard(request);

        // [추가된 부분] 좋아요 상태 체크 로직
        // 세션에서 로그인한 유저 정보를 가져옵니다.
        MemberDTO user = (MemberDTO) request.getSession().getAttribute("user");
        boolean isLiked = false;

        if (user != null) {
            // LikeDAO의 인스턴스를 가져오거나 static 메서드라면 바로 호출
            // 만약 LikeDAO가 싱글톤이라면 LikeDAO.getLikeDAO().isLiked(...) 형식일 수 있습니다.
            LikeDAO likeDAO = new LikeDAO();
            isLiked = likeDAO.isLiked(boardId, user.getMember_id());
        }

        // JSP로 좋아요 상태 전달 (매우 중요!)
        request.setAttribute("isLiked", isLiked);

        // 3. 댓글 리스트 조회
        CommentDAO.getComment(request, boardId);

        request.setAttribute("content", "board/boardDetail.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    public void destroy() {
    }
}