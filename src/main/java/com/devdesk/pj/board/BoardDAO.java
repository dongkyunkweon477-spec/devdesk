package com.devdesk.pj.board;

import com.devdesk.pj.main.DBManager_new;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class BoardDAO {
    public static void addBoard(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement ps = null;

        String sql = "INSERT INTO board (b_board_id, member_id,b_category, b_title, b_content) " +
                " VALUES (board_seq.NEXTVAL, ?, ?, ?, ?)";

        try {
            request.setCharacterEncoding("UTF-8");

            con = DBManager_new.connect();
            ps = con.prepareStatement(sql);

            // ✔ 파라미터 세팅 먼저
            ps.setInt(1, 3);
            ps.setString(2, request.getParameter("category"));
            ps.setString(3, request.getParameter("title"));
            ps.setString(4, request.getParameter("txt"));

            // ✔ 실행은 한 번만
            int result = ps.executeUpdate();

            if (result == 1) {
                System.out.println("add success");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, ps, null);
        }
    }


    public static ArrayList<BoardVO> showAllBoard(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BoardVO bo = null;
        ArrayList<BoardVO> boards = new ArrayList<>();
        String sql = "SELECT * FROM board ORDER BY b_board_id DESC";

        try {
            con = DBManager_new.connect();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                bo = new BoardVO();
                bo.setBoard_id(rs.getInt("b_board_id"));
                bo.setCategory(rs.getString("b_category"));
                bo.setTitle(rs.getString("b_title"));
                bo.setMember_id(rs.getInt("member_id"));
                bo.setCreated_date(rs.getString("b_created_date"));
                bo.setView_count(rs.getInt("b_view_count"));
                boards.add(bo);
            }
            request.setAttribute("boards", boards);
            return boards;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, ps, rs);
        }
        return null;
    }

    public static int delBoard(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "DELETE FROM board WHERE b_board_id = ?";

        try {
            con = DBManager_new.connect();
            ps = con.prepareStatement(sql);
            ps.setString(1, request.getParameter("id"));
            int result = ps.executeUpdate();
            request.setCharacterEncoding("UTF-8");

            if (result == 1) {
                System.out.println("delete success");
            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, ps, null);
        }

        return 0;
    }

    public static void getBoard(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from board where b_board_id = ?";

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, request.getParameter("id"));
            rs = pstmt.executeQuery();
            BoardVO boardVO = null;

            if (rs.next()) {
                int member_id = rs.getInt("member_id");
                int board_id = rs.getInt("b_board_id");
                String title = rs.getString("b_title");
                String content = rs.getString("b_content");
                String category = rs.getString("b_category");
                String created_date = rs.getString("b_created_date");
                int view_count = rs.getInt("b_view_count");
                int like_count = rs.getInt("b_like_count");
                char hidden_yn = rs.getString("b_hidden_yn").charAt(0);

                boardVO = new BoardVO();
                boardVO.setMember_id(member_id);
                boardVO.setBoard_id(board_id);
                boardVO.setTitle(title);
                boardVO.setContent(content);
                boardVO.setCategory(category);
                boardVO.setCreated_date(created_date);
                boardVO.setView_count(view_count);
                boardVO.setLike_count(like_count);
                boardVO.setHidden_yn(hidden_yn);

            }
            System.out.println(boardVO);
            request.setAttribute("board", boardVO);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DBManager_new.close(con, pstmt, rs);
        }


    }


//    public void paging(HttpServletRequest request, int pageNum) {
//        request.setAttribute("currentPage", pageNum);
//        ArrayList<BoardVO> reviews = showAllReview(request);
//        int total = reviews.size();
//        int cnt = 5;
//
//        // 페이지수
//        int totalPage = (int) Math.ceil((double) total / cnt);
//        request.setAttribute("totalPage", totalPage);
//
//        int start = total - (cnt * (pageNum - 1));
//        int end = (pageNum == totalPage) ? -1 : start - (cnt + 1);
//
//        ArrayList<BoardVO> items = new ArrayList<>();
//        for (int i = start - 1; i > end; i--) {
//            items.add(reviews.get(i));
//        }
//
//        request.setAttribute("reviews", items);
//
//    }

}
