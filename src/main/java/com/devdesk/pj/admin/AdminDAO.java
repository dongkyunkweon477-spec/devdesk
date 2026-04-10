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

    // 🌟 [대시보드] 통계 데이터 가져오기
    // 여러 종류의 데이터를 한 번에 담아오기 위해 Map을 사용합니다.
    public java.util.Map<String, Object> getDashboardStats() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            con = DBManager_new.connect();

            // 1️⃣ 총 가입자 수 & 오늘 신규 가입자 수
            String sql1 = "SELECT COUNT(member_id) as total_cnt, " +
                    "COUNT(CASE WHEN TRUNC(created_date) = TRUNC(SYSDATE) THEN 1 END) as today_cnt " +
                    "FROM member";
            ps = con.prepareStatement(sql1);
            rs = ps.executeQuery();
            if (rs.next()) {
                stats.put("totalMembers", rs.getInt("total_cnt"));
                stats.put("todayNewMembers", rs.getInt("today_cnt"));
            }
            rs.close();
            ps.close();

            // 2️⃣ 총 게시글 수
            String sql2 = "SELECT COUNT(*) as board_cnt FROM board"; // (게시판 테이블 이름이 board가 맞는지 확인!)
            ps = con.prepareStatement(sql2);
            rs = ps.executeQuery();
            if (rs.next()) {
                stats.put("totalBoards", rs.getInt("board_cnt"));
            }
            rs.close();
            ps.close();

            // 3️⃣ 직무 카테고리 분포 (도넛 차트용)
            // 💡 수정 완료: 0명이면 숨김 + 탈퇴한 회원(상태 변경 또는 닉네임 변경) 완벽 제외!
            String sql3 = "SELECT NVL(job_category, '미입력(소셜)') as job, COUNT(member_id) as cnt " +
                    "FROM member " +
                    "WHERE role != 'admin' " +
                    "  AND status = 'active' " + // 🌟 탈퇴(deleted) 상태 제외
                    "  AND nickname != '탈퇴한 회원' " + // 🌟 닉네임이 바뀐 회원도 확실히 제외
                    "  AND email NOT LIKE 'del_%' " + // 🌟 이메일이 del_로 시작하는 경우도 제외
                    "GROUP BY NVL(job_category, '미입력(소셜)') " +
                    "ORDER BY cnt DESC"; // 사람이 많은 직무부터 보여주기

            ps = con.prepareStatement(sql3);
            rs = ps.executeQuery();

            List<String> jobLabels = new ArrayList<>();
            List<Integer> jobData = new ArrayList<>();

            while (rs.next()) {
                jobLabels.add(rs.getString("job"));
                jobData.add(rs.getInt("cnt"));
            }
            stats.put("jobLabels", jobLabels);
            stats.put("jobData", jobData);

            rs.close();
            ps.close();

            // 4️⃣ 최근 7일 가입자 트렌드 (선 차트용)
            // 💡 로직 설명: CONNECT BY를 써서 최근 7일의 '날짜(MM/DD)'를 강제로 만들고, 회원 테이블과 JOIN 합니다.
            String sql4 = "SELECT TO_CHAR(d.dt, 'MM/DD') as label, NVL(COUNT(m.member_id), 0) as cnt " +
                    "FROM (SELECT TRUNC(SYSDATE - 7 + LEVEL) as dt FROM dual CONNECT BY LEVEL <= 7) d " +
                    "LEFT JOIN member m ON TRUNC(m.created_date) = d.dt " +
                    "GROUP BY d.dt ORDER BY d.dt ASC";
            ps = con.prepareStatement(sql4);
            rs = ps.executeQuery();
            List<String> trendLabels = new ArrayList<>();
            List<Integer> trendData = new ArrayList<>();
            while (rs.next()) {
                trendLabels.add(rs.getString("label"));
                trendData.add(rs.getInt("cnt"));
            }
            stats.put("trendLabels", trendLabels);
            stats.put("trendData", trendData);

        } catch (Exception e) {
            System.out.println("대시보드 통계 조회 중 DB 에러!");
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, ps, rs);
        }
        return stats;
    }


}
