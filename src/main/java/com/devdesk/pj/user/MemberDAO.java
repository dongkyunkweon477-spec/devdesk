package com.devdesk.pj.user;

import com.devdesk.pj.Comment.CommentVO;
import com.devdesk.pj.board.BoardVO;
import com.devdesk.pj.main.DBManager_new;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


public class MemberDAO {

    public static final MemberDAO MBAO = new MemberDAO();

    MemberDAO() {
    }

    // 로그인 상태를 확인하는 전용 문지기 메서드

    public int idCheak(String email) {
        int result = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select count(*) from member where email = ?";

        try {

            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                result = rs.getInt(1);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBManager_new.close(con, pstmt, rs);
        }
        return result;
    }

    public boolean signUp(HttpServletRequest request) {

        boolean result = false;
        Connection con = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO MEMBER (MEMBER_ID, EMAIL, PASSWORD, NICKNAME, JOB_CATEGORY) "
                + "VALUES (member_id_seq.NEXTVAL, ?, ?, ?, ?)";

        try {
            // 🚨 DAO 안에서 request 상자를 뜯어서 값을 꺼냅니다!
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String nickname = request.getParameter("nickname");
            String jobCategory = request.getParameter("jobCategory");

            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);

            // 꺼낸 값을 쿼리의 물음표에 채워 넣습니다.
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            pstmt.setString(3, nickname);
            pstmt.setString(4, jobCategory);

            if (pstmt.executeUpdate() == 1) { // 1줄 들어가면 성공!
                result = true;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBManager_new.close(con, pstmt, null);
        }

        return result;
    }

    public void login(HttpServletRequest request) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            String sql = "select * from member where email = ?";
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);

            rs = pstmt.executeQuery();
            String msg = null;

            if (rs.next()) {

                if (rs.getString("password").equals(password)) {
                    // 로그인 성공
                    System.out.println("로그인 성공");

                    MemberDTO memberDTO = new MemberDTO();

                    memberDTO.setMember_id(rs.getInt("member_id")); // 선민 추가
                    memberDTO.setEmail(rs.getString("email"));
                    memberDTO.setNickname(rs.getString("nickname"));
                    memberDTO.setJob_category(rs.getString("job_category"));


                    HttpSession hs = request.getSession();
                    hs.setAttribute("user", memberDTO);
                    hs.setMaxInactiveInterval(30 * 60);

                } else {
                    //비번
                    System.out.println("비번에러");
                    msg = "비밀번호가 일치하지 않습니다.";
                }
            } else {
                // 유저없음
                System.out.println("유저 없음");
                msg = "등록되지 않은 회원입니다.";
            }
            request.setAttribute("msg", msg);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBManager_new.close(con, pstmt, rs);
        }


    }

    public boolean updateProfile(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            String nickname = request.getParameter("nickname");
            String job_category = request.getParameter("job_category");

            HttpSession hs = request.getSession();
            MemberDTO user = (MemberDTO) hs.getAttribute("user");

            String sql = "UPDATE MEMBER SET nickname = ?, job_category = ? WHERE email = ?";

            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, nickname);
            pstmt.setString(2, job_category);
            pstmt.setString(3, user.getEmail());

            if (pstmt.executeUpdate() == 1) {
                System.out.println("프로필 텍스트 수정 성공!");

                // 세션 정보 업데이트
                user.setNickname(nickname);
                user.setJob_category(job_category);

                hs.setAttribute("user", user);
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            DBManager_new.close(con, pstmt, null);
        }
        return false;
    }


    public boolean passwordUpdate(HttpServletRequest request, HttpServletResponse response) {
        MemberDTO user = (MemberDTO) request.getSession().getAttribute("user");

        Connection con = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE MEMBER SET password = ? WHERE member_id = ? AND password = ?";

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            String new_password = request.getParameter("new_password");
            String old_password = request.getParameter("old_password");
            pstmt.setString(1, new_password);
            pstmt.setInt(2, user.getMember_id());
            pstmt.setString(3, old_password);

            if (pstmt.executeUpdate() == 1) {
                System.out.println("비밀번호 변경 성공!");
                return true;
            } else {
                System.out.println("비밀번호 변경 실패: 현재 비밀번호 불일치");
                return false;
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, null);
        }
        return false;
    }

    public ArrayList<BoardVO> getMyBoardList(int member_id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        BoardVO bo = null;
        ArrayList<BoardVO> boards = new ArrayList<>();

        String sql = "SELECT b.*, " +
                "(SELECT COUNT(*) FROM comments WHERE b_board_id = b.b_board_id) as comment_count, " +
                "COALESCE(b.b_like_count, 0) as like_count " +
                "FROM board b " +
                "WHERE member_id = ? " +
                "ORDER BY b.b_board_id DESC";

        try {
            con = DBManager_new.connect();
            ps = con.prepareStatement(sql);
            ps.setInt(1, member_id);
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, ps, rs);
        }
        return boards;
    }

    public ArrayList<CommentVO> getMyCommentList(int member_id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        ArrayList<CommentVO> comments = new ArrayList<>();

        String sql = "SELECT c.c_comments_id, c.b_board_id, c.c_content, c.c_created_date, b.b_title " +
                "FROM comments c " +
                "JOIN board b ON c.b_board_id = b.b_board_id " +
                "WHERE c.member_id = ? " +
                "ORDER BY c.c_comments_id DESC";

        try {
            con = DBManager_new.connect();
            ps = con.prepareStatement(sql);
            ps.setInt(1, member_id);
            rs = ps.executeQuery();

            while (rs.next()) {
                CommentVO c = new CommentVO();
                c.setComments_id(rs.getInt("c_comments_id"));
                c.setBoard_id(rs.getInt("b_board_id"));
                c.setContent(rs.getString("c_content"));

                c.setCreated_date(String.valueOf(rs.getDate("c_created_date")));
                c.setBoard_title(rs.getString("b_title"));

                comments.add(c);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, ps, rs);
        }
        return comments;
    }

    // [회원 탈퇴] 비밀번호 검증 + 개인 데이터 삭제 + 회원 정보 비식별화
    public int deleteAccount(int memberId, String inputPassword) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBManager_new.connect();

            // 🌟 1. 비밀번호가 맞는지 검증부터 합니다!
            String checkSql = "SELECT password FROM member WHERE member_id = ?";
            ps = con.prepareStatement(checkSql);
            ps.setInt(1, memberId);
            rs = ps.executeQuery();

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                // 입력한 비밀번호와 DB 비밀번호가 다르면? 0 반환하고 즉시 종료!
                if (!dbPassword.equals(inputPassword)) {
                    return 0;
                }
            } else {
                return -1; // 유저를 찾을 수 없음
            }
            ps.close(); // 검증 끝났으니 닫기

            // 🌟 2. 비밀번호가 맞으면 본격적인 탈퇴 로직 시작 (트랜잭션)
            con.setAutoCommit(false);

            // [하드 딜리트] 개인 워크스페이스 삭제
            String[] deleteQueries = {
                    // 1. 지원서 및 일정
                    "DELETE FROM schedule WHERE member_id = ?",
                    "DELETE FROM application WHERE member_id = ?",

                    // 2. TIL (오늘의 학습)
                    "DELETE FROM til WHERE member_id = ?",

                    // 3. 🌟 이력서 블록 (자식인 '버전' 먼저 -> 부모인 '블록' 나중)
                    "DELETE FROM resume_block_version WHERE block_id IN (SELECT block_id FROM resume_block WHERE member_id = ?)",
                    "DELETE FROM resume_block WHERE member_id = ?",

                    // 4. 🌟 기본 이력서 (자식인 '필드' 먼저 -> 부모인 '이력서' 나중)
                    "DELETE FROM resume_field WHERE resume_id IN (SELECT resume_id FROM resume WHERE member_id = ?)",
                    "DELETE FROM resume WHERE member_id = ?"
            };

            for (String sql : deleteQueries) {
                ps = con.prepareStatement(sql);
                ps.setInt(1, memberId);
                ps.executeUpdate();
                ps.close();
            }

            // [소프트 딜리트] 회원 개인정보 파기 (비식별화)
            String updateQuery = "UPDATE member SET " +
                    "email = 'del_' || member_id || '_' || TO_CHAR(SYSDATE, 'YYYYMMDDHH24MISS'), " +
                    "password = NULL, " +
                    "nickname = '탈퇴한 회원', " +
                    "job_category = NULL, " +
                    "social_id = NULL, " +
                    "profile_img = NULL, " +
                    "status = 'deleted', " +
                    "update_date = SYSDATE " +
                    "WHERE member_id = ?";

            ps = con.prepareStatement(updateQuery);
            ps.setInt(1, memberId);
            int updateCount = ps.executeUpdate();

            if (updateCount > 0) {
                con.commit(); // 모든 과정 성공 시 확정!
                System.out.println("회원 탈퇴 완료 (ID: " + memberId + ")");
                return 1;
            } else {
                con.rollback();
                return -1;
            }

        } catch (Exception e) {
            System.out.println("회원 탈퇴 중 DB 에러 발생! 롤백합니다.");
            e.printStackTrace();
            try {
                if (con != null) con.rollback();
            } catch (Exception re) {
            }
            return -1;
        } finally {
            DBManager_new.close(con, ps, rs);
        }
    }

    public MemberDTO getMemberByEmail(String email) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "SELECT * FROM member WHERE email = ?";

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                MemberDTO dto = new MemberDTO();
                dto.setMember_id(rs.getInt("member_id"));
                dto.setEmail(rs.getString("email"));
                dto.setNickname(rs.getString("nickname"));
                dto.setJob_category(rs.getString("job_category"));
                dto.setLogin_type(rs.getString("login_type"));
                dto.setSocial_id(rs.getString("social_id"));
                dto.setRole(rs.getString("role"));
                dto.setStatus(rs.getString("status"));
                dto.setProfile_img(rs.getString("profile_img"));
                return dto;
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBManager_new.close(con, pstmt, rs);
        }
        return null;
    }

    public void insertGoogleMember(String email, String nickname, String socialId) {
        Connection con = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO MEMBER (MEMBER_ID, EMAIL, NICKNAME, LOGIN_TYPE, SOCIAL_ID) "
                + "VALUES (member_id_seq.NEXTVAL, ?, ?, 'GOOGLE', ?)";

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, email);
            pstmt.setString(2, nickname);
            pstmt.setString(3, socialId);
            pstmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBManager_new.close(con, pstmt, null);
        }
    }
}
