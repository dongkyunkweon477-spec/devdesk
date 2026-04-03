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

        try{
            con = DBManager_new.connect();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()){
                bo = new BoardVO();
                bo.setCategory(rs.getString("b_category"));
                bo.setTitle(rs.getString("b_title"));
                bo.setMember_id(rs.getInt("member_id"));
                bo.setCreated_date(rs.getString("b_created_date"));
                bo.setView_count(rs.getInt("b_view_count"));
                boards.add(bo);
            }
            request.setAttribute("boards", boards);
            return boards;

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBManager_new.close(con,ps,rs);
        }
        return null;
    }

    public static void delBoard(HttpServletRequest request) {

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
