package com.devdesk.pj.calendar;

import com.devdesk.pj.main.DBManager_new;
import com.devdesk.pj.user.MemberDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Schedule_newDAO {
    public static final Schedule_newDAO SCAO = new Schedule_newDAO();

    private Schedule_newDAO() {}

    public ArrayList<Schedule_newDTO> getCalendarEvents(int memberId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Schedule_newDTO> list = new ArrayList<>();

        String sql = "SELECT s.SCHEDULE_ID, s.SCHEDULE_DATE, s.SCHEDULE_TIME, s.INTERVIEW_TYPE, s.MEMO, " +
                "s.COMPANY_NAME, a.POSITION, a.STAGE " +
                "FROM SCHEDULE s " +
                "JOIN APPLICATION a ON s.APP_ID = a.APP_ID " +
                "WHERE a.MEMBER_ID = ? " +
                "ORDER BY s.SCHEDULE_DATE ASC";

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, memberId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Schedule_newDTO dto = new Schedule_newDTO(
                        rs.getInt("SCHEDULE_ID"),
                        rs.getDate("SCHEDULE_DATE"),
                        rs.getString("SCHEDULE_TIME"),
                        rs.getString("INTERVIEW_TYPE"),
                        rs.getString("MEMO"),
                        rs.getString("COMPANY_NAME"),
                        rs.getString("POSITION"),
                        rs.getString("STAGE")
                );
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, rs);
        }
        return list;
    }

    private String mapTypeToStage(String type) {
        if (type == null) return "APPLIED";
        switch (type) {
            case "코딩테스트": return "TECH_INTERVIEW";
            case "1차면접": return "FIRST_INTERVIEW";
            case "2차면접": return "SECOND_INTERVIEW";
            case "임원면접": return "THIRD_INTERVIEW";
            default: return "ETC";
        }
    }
    public void addSchedule(HttpServletRequest request) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
        MemberDTO user = (MemberDTO) request.getSession().getAttribute("user");
        int memberId = user.getMember_id();

        String companyName = request.getParameter("company_name");
        String position = request.getParameter("position");
        String applyDate = request.getParameter("apply_date");
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String type = request.getParameter("type");
        String memo = request.getParameter("memo");
        String stage = mapTypeToStage(type);

            int companyId = 0;
            String findCompanySql = "SELECT COMPANY_ID FROM COMPANY WHERE COMPANY_NAME = ?";
            pstmt = con.prepareStatement(findCompanySql);
            pstmt.setString(1, companyName);
            rs = pstmt.executeQuery();


            if(rs.next()) {
                companyId = rs.getInt("COMPANY_ID");
            } else {
                throw new Exception("등록하려는 회사가 존재하지 않습니다: " + companyName);
            }
            pstmt.close();
            rs.close();



            int newAppId = 0;
            pstmt = con.prepareStatement("SELECT APPLICATION_SEQ.NEXTVAL FROM DUAL");
            rs = pstmt.executeQuery();
            if(rs.next()) newAppId = rs.getInt(1);
            pstmt.close();
            rs.close();

            String appSql = "INSERT INTO APPLICATION (APP_ID, MEMBER_ID, COMPANY_ID, POSITION, STAGE, APPLY_DATE, CREATED_DATE) " +
                    "VALUES (?, ?, ?, ?, ?, ?, SYSDATE)";
            pstmt = con.prepareStatement(appSql);
            pstmt.setInt(1, newAppId);
            pstmt.setInt(2, memberId);
            pstmt.setInt(3, companyId);
            pstmt.setString(4, (position == null || position.isEmpty()) ? "미정" : position);
            pstmt.setString(5, stage);

            if (applyDate != null && !applyDate.isEmpty()) {
                pstmt.setDate(6, java.sql.Date.valueOf(applyDate));
            } else {
                pstmt.setTimestamp(6, new java.sql.Timestamp(System.currentTimeMillis()));
            }
            pstmt.executeUpdate();
            pstmt.close();

            // 자식 테이블(SCHEDULE) 인서트
            String schSql = "INSERT INTO SCHEDULE (SCHEDULE_ID, MEMBER_ID, COMPANY_NAME, SCHEDULE_DATE, SCHEDULE_TIME, INTERVIEW_TYPE, MEMO, APP_ID) " +
                    "VALUES (SEQ_SCHEDULE.nextval, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?, ?)";
            pstmt = con.prepareStatement(schSql);
            pstmt.setInt(1, memberId);
            pstmt.setString(2, companyName);
            pstmt.setString(3, date);
            pstmt.setString(4, time);
            pstmt.setString(5, type);
            pstmt.setString(6, memo);
            pstmt.setInt(7, newAppId);
            pstmt.executeUpdate();

            con.commit();
        } catch (Exception e) {
            try { if(con != null) con.rollback(); } catch (Exception ex) {}
            throw e;
        } finally {
            try { if(con != null) con.setAutoCommit(true); } catch (Exception ex) {}
            DBManager_new.close(con, pstmt, rs);
        }
    }

    // --- Update로직 ---
    public void updateSchedule(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            MemberDTO user = (MemberDTO) request.getSession().getAttribute("user");
            int memberId = user.getMember_id();

            int scheduleId = Integer.parseInt(request.getParameter("schedule_id"));
            String companyName = request.getParameter("company_name");
            String position = request.getParameter("position");
            String date = request.getParameter("date");
            String time = request.getParameter("time");
            String type = request.getParameter("type");
            String memo = request.getParameter("memo");

            con = DBManager_new.connect();
            con.setAutoCommit(false);

            int companyId = 0;
            pstmt = con.prepareStatement("SELECT COMPANY_ID FROM COMPANY WHERE COMPANY_NAME = ?");
            pstmt.setString(1, companyName);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                companyId = rs.getInt("COMPANY_ID");
            } else {
                throw new Exception("존재하지 않는 회사입니다: " + companyName);
            }
            pstmt.close();

            int appId = 0;
            pstmt = con.prepareStatement("SELECT APP_ID FROM SCHEDULE WHERE SCHEDULE_ID = ?");
            pstmt.setInt(1, scheduleId);
            rs = pstmt.executeQuery();
            if(rs.next()) appId = rs.getInt("APP_ID");
            pstmt.close();

            String updateAppSql = "UPDATE APPLICATION SET COMPANY_ID = ?, POSITION = ?, STAGE = ? WHERE APP_ID = ?";
            pstmt = con.prepareStatement(updateAppSql);
            pstmt.setInt(1, companyId);
            pstmt.setString(2, (position == null || position.isEmpty()) ? "미정" : position);
            pstmt.setString(3, mapTypeToStage(type));
            pstmt.setInt(4, appId);
            pstmt.executeUpdate();
            pstmt.close();

            String updateSchSql = "UPDATE SCHEDULE SET COMPANY_NAME = ?, SCHEDULE_DATE = TO_DATE(?, 'YYYY-MM-DD'), " +
                    "SCHEDULE_TIME = ?, INTERVIEW_TYPE = ?, MEMO = ? WHERE SCHEDULE_ID = ?";
            pstmt = con.prepareStatement(updateSchSql);
            pstmt.setString(1, companyName);
            pstmt.setString(2, date);
            pstmt.setString(3, time);
            pstmt.setString(4, type);
            pstmt.setString(5, memo);
            pstmt.setInt(6, scheduleId);
            pstmt.executeUpdate();

            con.commit();
        } catch (Exception e) {
            try { if(con!=null) con.rollback(); } catch (Exception ex) {}
            throw e;
        } finally {
            try { if(con!=null) con.setAutoCommit(true); } catch (Exception ex) {}
            DBManager_new.close(con, pstmt, rs);
        }
    }
    // --- 3. 일정 삭제 (Delete) ---
    public void deleteSchedule(int scheduleId) {
        Connection con = null;
        PreparedStatement pstmt = null;
        String sql = "DELETE FROM SCHEDULE WHERE SCHEDULE_ID = ?";
        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, scheduleId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, null);
        }
    }
    // --- 캘린더 모달의 회사 드롭다운을 채우기 위해 이름만 가져오는 메서드 ---
    public ArrayList<String> getAllCompanyNames() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<String> names = new ArrayList<>();

        String sql = "SELECT COMPANY_NAME FROM COMPANY ORDER BY COMPANY_NAME ASC";

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                names.add(rs.getString("COMPANY_NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, rs);
        }
        return names;
    }
}

