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

        // 1. 택배 상자(request)에서 AJAX가 보낸 데이터 하나씩 꺼내기
        // (이름은 JSP의 AJAX data에서 보낸 키값과 100% 똑같아야 합니다!)
        int memberId = 6;
        int appId = Integer.parseInt(request.getParameter("app_id"));
        String companyName = request.getParameter("company_name");
        String date = request.getParameter("date");
        String time = request.getParameter("time");
        String type = request.getParameter("type");
        String memo = request.getParameter("memo");

        // 2. 어떤 컬럼에 넣을지 명시적으로 적어주는 가장 안전한 SQL!
        // (CREATED_DATE는 DEFAULT SYSDATE가 있으니 생략하면 알아서 들어갑니다)
        String sql = "INSERT INTO SCHEDULE " +
                "(SCHEDULE_ID, MEMBER_ID, COMPANY_NAME, SCHEDULE_DATE, SCHEDULE_TIME, INTERVIEW_TYPE, MEMO, APP_ID) " +
                "VALUES (SEQ_SCHEDULE.nextval, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?, ?)";

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);

            // 3. 물음표(?) 순서와 타입(Int, String)에 맞게 데이터 장전!
            pstmt.setInt(1, memberId);        // 첫 번째 ? : MEMBER_ID
            pstmt.setString(2, companyName);  // 두 번째 ? : COMPANY_NAME
            pstmt.setString(3, date);         // 세 번째 ? : SCHEDULE_DATE
            pstmt.setString(4, time);         // 네 번째 ? : SCHEDULE_TIME
            pstmt.setString(5, type);         // 다섯 번째 ? : INTERVIEW_TYPE
            pstmt.setString(6, memo);         // 여섯 번째 ? : MEMO
            pstmt.setInt(7, appId);           // 일곱 번째 ? : APP_ID

            // 4. 발사!
            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, null);
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

