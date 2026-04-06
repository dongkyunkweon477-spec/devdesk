package com.devdesk.pj.Comment;

import com.devdesk.pj.main.DBManager_new;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class CommentDAO {
    public static void addComment(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "insert into comments (c_comments_id, b_board_id, member_id, c_content, c_created_date) " +
                "values (seq_comments.nextval, ?, ?, ?, sysdate)";

        try {
            System.out.println("board_id: [" + request.getParameter("board_id") + "]");
            System.out.println("member_id: [" + request.getParameter("member_id") + "]");
            System.out.println("content: [" + request.getParameter("content") + "]");
            request.setCharacterEncoding("UTF-8");

            con = DBManager_new.connect();
            ps = con.prepareStatement(sql);

            ps.setInt(1, Integer.parseInt(request.getParameter("board_id")));
            ps.setInt(2, Integer.parseInt(request.getParameter("member_id")));
            ps.setString(3, request.getParameter("content"));

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

    public static ArrayList<CommentVO> getComment(HttpServletRequest request, int boardId) {

        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<CommentVO> comments = new ArrayList<>();

        String sql = "SELECT c.*, m.nickname FROM comments c JOIN member m ON c.member_id = m.member_id WHERE c.b_board_id = ? ORDER BY c.c_created_date ASC";


        try {
            con = DBManager_new.connect();
            ps = con.prepareStatement(sql);
            ps.setInt(1, boardId);

            rs = ps.executeQuery();

            while (rs.next()) {
                CommentVO c = new CommentVO();
                c.setComments_id(rs.getInt("c_comments_id"));
                c.setMember_id(rs.getInt("member_id"));
                c.setContent(rs.getString("c_content"));
                c.setCreated_date(String.valueOf(rs.getDate("c_created_date")));
                c.setNickname(rs.getString("nickname"));
                comments.add(c);
            }

            System.out.println("댓글 개수: " + comments.size());
            request.setAttribute("commentList", comments);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, ps, rs);
        }

        return comments;
    }

    public static void delComment(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "DELETE FROM comments WHERE c_comments_id = ?";

        try {
            request.setCharacterEncoding("UTF-8");
            con = DBManager_new.connect();
            ps = con.prepareStatement(sql);
            ps.setInt(1, Integer.parseInt(request.getParameter("id")));

            int result = ps.executeUpdate();

            if (result == 1) {
                System.out.println("delete success");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, ps, null);
        }
    }
}
