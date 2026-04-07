package com.devdesk.pj.calendar;

import com.devdesk.pj.main.DBManager_new;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
                "c.COMPANY_NAME, a.POSITION, a.STAGE " +
                "FROM SCHEDULE s " +
                "JOIN APPLICATION a ON s.APP_ID = a.APP_ID " +
                "JOIN COMPANY c ON a.COMPANY_ID = c.COMPANY_ID " +
                "WHERE a.MEMBER_ID = ? " +
                "ORDER BY s.SCHEDULE_DATE ASC";

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, memberId); // 현재 로그인한 유저 번호
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
            case "기술면접": return "TECH_INTERVIEW";
            case "1차면접": return "FIRST_INTERVIEW";
            case "2차면접": return "SECOND_INTERVIEW";
            case "임원면접": return "THIRD_INTERVIEW";
            default:
                return "ETC";
        }
    }

    public void addSchedule(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        int memberId = 6; // **나중에 개인 멤버 아이디가 나오게 세팅을 해야할듯???
        String companyName = request.getParameter("company_name");
        String position = request.getParameter("position");
        String applyDate = request.getParameter("apply_date");
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String type = request.getParameter("type");
        String stage = mapTypeToStage(type);
        String memo = request.getParameter("memo");

        try {
            con = DBManager_new.connect();
            con.setAutoCommit(false);


            int newAppId = 0;
            String seqSql = "SELECT APPLICATION_SEQ.NEXTVAL FROM DUAL";
            pstmt = con.prepareStatement(seqSql);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                newAppId = rs.getInt(1);
            }
            pstmt.close();
            rs.close();

            int companyId = 1;
            String findCompanySql = "SELECT COMPANY_ID FROM COMPANY WHERE COMPANY_NAME = ?";
            pstmt = con.prepareStatement(findCompanySql);
            pstmt.setString(1, companyName);
            rs = pstmt.executeQuery();
            if(rs.next()) {
                companyId = rs.getInt("COMPANY_ID");
            }
            pstmt.close();
            rs.close();

            if(position == null || position.trim().isEmpty()) {
                position = "미정";
            }


            String appSql = "";
            if (applyDate != null && !applyDate.trim().isEmpty()) {
                // 🌟 POSITION 컬럼에 '미정'이라는 글자 대신 ? 를 넣습니다.
                appSql = "INSERT INTO APPLICATION (APP_ID, MEMBER_ID, COMPANY_ID, POSITION, STAGE, APPLY_DATE, CREATED_DATE) " +
                        "VALUES (?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), SYSDATE)";
                pstmt = con.prepareStatement(appSql);
                pstmt.setInt(1, newAppId);
                pstmt.setInt(2, memberId);
                pstmt.setInt(3, companyId);
                pstmt.setString(4, position);
                pstmt.setString(5, stage);
                pstmt.setString(6, applyDate);
            } else {
                appSql = "INSERT INTO APPLICATION (APP_ID, MEMBER_ID, COMPANY_ID, POSITION, STAGE, APPLY_DATE, CREATED_DATE) " +
                        "VALUES (?, ?, ?, ?, ?, SYSDATE, SYSDATE)";
                pstmt = con.prepareStatement(appSql);
                pstmt.setInt(1, newAppId);
                pstmt.setInt(2, memberId);
                pstmt.setInt(3, companyId);
                pstmt.setString(4, position);
                pstmt.setString(5, stage);
            }
            pstmt.executeUpdate();
            pstmt.close();


            String schSql = "INSERT INTO SCHEDULE " +
                    "(SCHEDULE_ID, MEMBER_ID, COMPANY_NAME, SCHEDULE_DATE, SCHEDULE_TIME, INTERVIEW_TYPE, MEMO, APP_ID) " +
                    "VALUES (SEQ_SCHEDULE.nextval, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?, ?)";

            pstmt = con.prepareStatement(schSql);
            pstmt.setInt(1, memberId);
            pstmt.setString(2, companyName);
            pstmt.setString(3, date);
            pstmt.setString(4, time);
            pstmt.setString(5, type);
            pstmt.setString(6, memo);
            pstmt.setInt(7, newAppId); // 달력 일정에도 같은 번호를 넣어 완벽하게 연결!

            pstmt.executeUpdate();

            con.commit(); // 모든 과정 성공 시 도장 쾅!

        } catch (Exception e) {
            try { if (con != null) con.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
        } finally {
            try { if (con != null) con.setAutoCommit(true); } catch (Exception ex) {}
            DBManager_new.close(con, pstmt, rs);
        }
    }

    // --- Update로직 ---
    public void updateSchedule(HttpServletRequest request, HttpServletResponse response) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            int scheduleId = Integer.parseInt(request.getParameter("schedule_id"));
            String companyName = request.getParameter("company_name");
            String position = request.getParameter("position");
            String date = request.getParameter("date");
            String time = request.getParameter("time");
            String type = request.getParameter("type");
            String memo = request.getParameter("memo");

            con = DBManager_new.connect();
            con.setAutoCommit(false);

            int appId = 0;
            String findAppSql = "SELECT APP_ID FROM SCHEDULE WHERE SCHEDULE_ID = ?";
            pstmt = con.prepareStatement(findAppSql);
            pstmt.setInt(1, scheduleId);
            rs = pstmt.executeQuery();
            if(rs.next()) appId = rs.getInt("APP_ID");
            pstmt.close();

            if(position == null || position.trim().isEmpty()) {
                position = "미정"; // 직무를 빈칸으로 두면 미정으로 처리
            }
            String updateAppSql = "UPDATE APPLICATION SET POSITION = ?, STAGE = ? WHERE APP_ID = ?";
            pstmt = con.prepareStatement(updateAppSql);
            pstmt.setString(1, position);
            pstmt.setString(2, mapTypeToStage(type));
            pstmt.setInt(3, appId);
            pstmt.executeUpdate();
            pstmt.close();

            // 3. 자식 테이블(SCHEDULE) 수정 (COMPANY_NAME도 함께 수정!)
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

            // 모든 과정이 정상적으로 끝나면 도장 쾅!
            con.commit();

        } catch (Exception e) {
            // 에러 나면 롤백!
            try { if(con != null) con.rollback(); } catch (Exception ex) {}
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            // 원상 복구 및 자원 반납
            try { if(con != null) con.setAutoCommit(true); } catch (Exception ex) {}
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

