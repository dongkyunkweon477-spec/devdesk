package com.devdesk.pj.calendar;

import com.devdesk.pj.main.DBManager_new;

import javax.servlet.http.HttpServletRequest;
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

        // 💡 3개의 테이블을 APP_ID와 COMPANY_ID로 엮는 마법의 쿼리
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
                        "VALUES (?, ?, ?, ?, 'APPLIED', TO_DATE(?, 'YYYY-MM-DD'), SYSDATE)";
                pstmt = con.prepareStatement(appSql);
                pstmt.setInt(1, newAppId);
                pstmt.setInt(2, memberId);
                pstmt.setInt(3, companyId);
                pstmt.setString(4, position); // ✨ 4번째 물음표에 직무 이름 쏙!
                pstmt.setString(5, applyDate);
            } else {
                appSql = "INSERT INTO APPLICATION (APP_ID, MEMBER_ID, COMPANY_ID, POSITION, STAGE, APPLY_DATE, CREATED_DATE) " +
                        "VALUES (?, ?, ?, ?, 'APPLIED', SYSDATE, SYSDATE)";
                pstmt = con.prepareStatement(appSql);
                pstmt.setInt(1, newAppId);
                pstmt.setInt(2, memberId);
                pstmt.setInt(3, companyId);
                pstmt.setString(4, position); // ✨ 4번째 물음표에 직무 이름 쏙!
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

    // --- 2. 일정 수정 (Update) ---
    public void updateSchedule(int scheduleId, String date, String time, String type, String memo) {
        Connection con = null;
        PreparedStatement pstmt = null;
        String sql = "UPDATE SCHEDULE SET SCHEDULE_DATE = TO_DATE(?, 'YYYY-MM-DD'), SCHEDULE_TIME = ?, INTERVIEW_TYPE = ?, MEMO = ? " +
                "WHERE SCHEDULE_ID = ?";
        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, date);
            pstmt.setString(2, time);
            pstmt.setString(3, type);
            pstmt.setString(4, memo);
            pstmt.setInt(5, scheduleId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, null);
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

