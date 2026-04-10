package com.devdesk.pj.admin;


import com.devdesk.pj.main.DBManager_new;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AdminDAO {

    public static final AdminDAO ADAO = new AdminDAO();

    private AdminDAO() {
    }

    // [회원 관리] 전체 회원 목록 조회
    public List<AdminVO> getAllMembers() {
        List<AdminVO> memberList = new ArrayList<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT member_id, email, nickname, job_category, login_type, role, status, TO_CHAR(created_date, 'YYYY-MM-DD') as created_date " +
                "FROM member ORDER BY member_id DESC";

        try {
            con = DBManager_new.connect();
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                AdminVO vo = new AdminVO();

                vo.setMember_id(rs.getInt("member_id"));
                vo.setEmail(rs.getString("email"));
                vo.setNickname(rs.getString("nickname"));
                vo.setJob_category(rs.getString("job_category"));
                vo.setLogin_type(rs.getString("login_type"));
                vo.setRole(rs.getString("role"));

                String status = rs.getString("status");
                vo.setStatus(status != null ? status : "active");

                vo.setCreated_date(rs.getString("created_date"));

                memberList.add(vo);
            }
        } catch (Exception e) {
            System.out.println("관리자 회원 목록 조회 중 DB 에러!");
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, ps, rs);
        }

        return memberList;
    }
}
