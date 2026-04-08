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
            ps.setInt(1, Integer.parseInt(request.getParameter("member_id")));
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
        String sql = "SELECT b.*, " +
                "(SELECT COUNT(*) FROM comments WHERE b_board_id = b.b_board_id) as comment_count, " +
                "COALESCE(b.b_like_count, 0) as like_count " +
                "FROM board b ORDER BY b.b_board_id DESC";

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
                bo.setComment_count(rs.getInt("comment_count"));
                bo.setLike_count(rs.getInt("like_count"));
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

    public static ArrayList<BoardVO> showPopularBoard(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BoardVO bo = null;
        ArrayList<BoardVO> boards = new ArrayList<>();
        String sql = "SELECT b.*, " +
                "(SELECT COUNT(*) FROM comments WHERE b_board_id = b.b_board_id) as comment_count, " +
                "COALESCE(b.b_like_count, 0) as like_count " +
                "FROM board b ORDER BY b.b_like_count DESC, b.b_board_id DESC";

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
                bo.setComment_count(rs.getInt("comment_count"));
                bo.setLike_count(rs.getInt("like_count"));
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

    public static ArrayList<BoardVO> showViewCountBoard(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BoardVO bo = null;
        ArrayList<BoardVO> boards = new ArrayList<>();
        String sql = "SELECT b.*, " +
                "(SELECT COUNT(*) FROM comments WHERE b_board_id = b.b_board_id) as comment_count, " +
                "COALESCE(b.b_like_count, 0) as like_count " +
                "FROM board b ORDER BY b.b_view_count DESC, b.b_board_id DESC";

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
                bo.setComment_count(rs.getInt("comment_count"));
                bo.setLike_count(rs.getInt("like_count"));
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

    public static void increaseViewCount(int boardId) {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "UPDATE board SET b_view_count = b_view_count + 1 WHERE b_board_id = ?";

        try {
            con = DBManager_new.connect();
            ps = con.prepareStatement(sql);
            ps.setInt(1, boardId);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, ps, null);
        }
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
//        String sql = "select * from board where b_board_id = ?";
        String sql = "SELECT b.*, m.nickname FROM board b JOIN member m ON b.member_id = m.member_id WHERE b.b_board_id = ?";

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
                String updated_date = rs.getString("b_updated_date");
                // rs에서 꺼낼 때 DB 테이블 컬럼명과 토씨 하나 안 틀리게 맞추세요!
                int view_count = rs.getInt("b_view_count"); // b_가 없다면 제거
                int like_count = rs.getInt("b_like_count");
                char hidden_yn = rs.getString("b_hidden_yn").charAt(0); // b_가 없다면 제거
                String nickname = rs.getString("nickname");

                boardVO = new BoardVO();
                boardVO.setMember_id(member_id);
                boardVO.setBoard_id(board_id);
                boardVO.setTitle(title);
                boardVO.setContent(content);
                boardVO.setCategory(category);
                boardVO.setCreated_date(created_date);
                boardVO.setUpdated_date(updated_date);
                boardVO.setView_count(view_count);
                boardVO.setLike_count(like_count);
                boardVO.setHidden_yn(hidden_yn);
                boardVO.setNickname(nickname);

            }
            System.out.println(boardVO);
            request.setAttribute("board", boardVO);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DBManager_new.close(con, pstmt, rs);
        }


    }

    // [검색 기능] 게시글 검색 메서드
    public static ArrayList<BoardVO> searchBoards(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BoardVO bo = null;
        ArrayList<BoardVO> boards = new ArrayList<>();

        String searchType = request.getParameter("searchType");
        String keyword = request.getParameter("keyword");

        // 검색어가 없으면 전체 목록 반환
        if (keyword == null || keyword.trim().isEmpty()) {
            return showAllBoard(request); // 검색어 없으면 전체 목록 반환
        }

        String sql = "SELECT b.*, " +
                "(SELECT COUNT(*) FROM comments WHERE b_board_id = b.b_board_id) as comment_count, " +
                "COALESCE(b.b_like_count, 0) as like_count, " + // 좋아요 수 null 방지, 서브쿼리로 댓글 수 계산
                "m.nickname " +
                "FROM board b " +
                "JOIN member m ON b.member_id = m.member_id "; // 작성자 닉네임 가져오기

        // 검색 타입에 따른 WHERE 절 추가
        switch (searchType) {
            case "title":
                sql += "WHERE b.b_title LIKE ? ";
                break;
            case "content":
                sql += "WHERE b.b_content LIKE ? ";
                break;
            case "author":
                sql += "WHERE m.nickname LIKE ? ";
                break;
            default:
                return showAllBoard(request);
        }

        sql += "ORDER BY b.b_board_id DESC";

        try {
            con = DBManager_new.connect();
            ps = con.prepareStatement(sql);
            ps.setString(1, "%" + keyword + "%"); // like 검색을 위한 와일드 카드 추가
            rs = ps.executeQuery();         // %keyboard% 는 부분 일치 검색

            while (rs.next()) {
                bo = new BoardVO();
                bo.setBoard_id(rs.getInt("b_board_id"));
                bo.setCategory(rs.getString("b_category"));
                bo.setTitle(rs.getString("b_title"));
                bo.setMember_id(rs.getInt("member_id"));
                bo.setCreated_date(rs.getString("b_created_date"));
                bo.setView_count(rs.getInt("b_view_count"));
                bo.setComment_count(rs.getInt("comment_count"));
                bo.setLike_count(rs.getInt("like_count"));
                bo.setNickname(rs.getString("nickname"));
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

    // [카테고리별 검색 기능] 카테고리별 게시글 검색 메서드
    public static ArrayList<BoardVO> searchBoardsByCategory(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BoardVO bo = null;
        ArrayList<BoardVO> boards = new ArrayList<>();
        
        String category = request.getParameter("category");
        
        // 카테고리가 없거나 "전체"이면 전체 목록 반환
        if (category == null || category.trim().isEmpty() || category.equals("전체")) {
            return showAllBoard(request);
        }
        
        String sql = "SELECT b.*, " +
                "(SELECT COUNT(*) FROM comments WHERE b_board_id = b.b_board_id) as comment_count, " +
                "COALESCE(b.b_like_count, 0) as like_count, " +
                "m.nickname " +
                "FROM board b " +
                "JOIN member m ON b.member_id = m.member_id " +
                "WHERE b.b_category = ? " +
                "ORDER BY b.b_board_id DESC";

        try {
            con = DBManager_new.connect();
            ps = con.prepareStatement(sql);
            ps.setString(1, category);
            rs = ps.executeQuery();

            while (rs.next()) {
                bo = new BoardVO();
                bo.setBoard_id(rs.getInt("b_board_id"));
                bo.setCategory(rs.getString("b_category"));
                bo.setTitle(rs.getString("b_title"));
                bo.setMember_id(rs.getInt("member_id"));
                bo.setCreated_date(rs.getString("b_created_date"));
                bo.setView_count(rs.getInt("b_view_count"));
                bo.setComment_count(rs.getInt("comment_count"));
                bo.setLike_count(rs.getInt("like_count"));
                bo.setNickname(rs.getString("nickname"));
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

    public static int updateBoard(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "UPDATE board SET b_title = ?, b_content = ?, b_updated_date = SYSDATE WHERE b_board_id = ?";

        try {
            request.setCharacterEncoding("UTF-8");

            con = DBManager_new.connect();
            ps = con.prepareStatement(sql);

            ps.setString(1, request.getParameter("title"));
            ps.setString(2, request.getParameter("content"));
            ps.setString(3, request.getParameter("board_id"));

            int result = ps.executeUpdate();
            if (result == 1) {
                System.out.println("update success");
            }
            return result;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, ps, null);
        }
        return 0;
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
