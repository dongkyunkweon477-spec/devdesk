package com.devdesk.pj.user;

import com.devdesk.pj.main.DBManager_new;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
import sun.security.util.Password;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberDAO {

    public static final MemberDAO MBAO = new MemberDAO();

    private MemberDAO() {
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
                    memberDTO.setMember_id(rs.getInt("member_id")); // 추가
                    memberDTO.setMember_id(rs.getInt("member_id")); // 선민 추가
                    memberDTO.setEmail(rs.getString("email"));
                    memberDTO.setNickname(rs.getString("nickname"));
                    memberDTO.setJob_category(rs.getString("job_category"));
                    memberDTO.setMember_id(rs.getInt("member_id"));
                    memberDTO.setProfile_img(rs.getString("profile_img"));

                    HttpSession hs = request.getSession();
                    hs.setAttribute("user", memberDTO);
                    hs.setMaxInactiveInterval(5 * 60);
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

        String path = request.getServletContext().getRealPath("images/profile");

        java.io.File folder = new java.io.File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }

        int maxSize = 5 * 1024 * 1024; // 5MB

        try {
            com.oreilly.servlet.MultipartRequest mr = new com.oreilly.servlet.MultipartRequest(
                    request, path, maxSize, "UTF-8", new com.oreilly.servlet.multipart.DefaultFileRenamePolicy());

            String nickname = mr.getParameter("nickname");
            String job_category = mr.getParameter("job_category");
            String new_img = mr.getFilesystemName("profile_img"); // 새로 업로드된 파일명

            HttpSession hs = request.getSession();
            MemberDTO user = (MemberDTO) hs.getAttribute("user");

            String sql = "";
            if (new_img != null) {
                sql = "UPDATE MEMBER SET nickname = ?, job_category = ?, profile_img = ? WHERE email = ?";
            } else {
                sql = "UPDATE MEMBER SET nickname = ?, job_category = ? WHERE email = ?";
            }

            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);

            if (new_img != null) {
                pstmt.setString(1, nickname);
                pstmt.setString(2, job_category);
                pstmt.setString(3, new_img);
                pstmt.setString(4, user.getEmail());
            } else {
                pstmt.setString(1, nickname);
                pstmt.setString(2, job_category);
                pstmt.setString(3, user.getEmail());
            }

            if (pstmt.executeUpdate() == 1) {
                System.out.println("프로필 DB 수정 성공!");

                user.setNickname(nickname);
                user.setJob_category(job_category);
                if (new_img != null) {
                    user.setProfile_img(new_img);
                }

                hs.setAttribute("user", user); // 바뀐 정보를 다시 세션에 덮어쓰기
                return true;
            }

        } catch (Exception e) {
            System.out.println("프로필 수정 중 에러 발생!");
            e.printStackTrace();
            return false;
        } finally {
            DBManager_new.close(con, pstmt, null);
        }
        return false;
    }


}
