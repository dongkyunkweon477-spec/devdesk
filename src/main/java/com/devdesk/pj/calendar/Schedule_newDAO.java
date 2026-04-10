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
        ArrayList<Schedule_newDTO> list = new ArrayList<>();

        String sql = "SELECT s.SCHEDULE_ID, s.SCHEDULE_DATE, s.SCHEDULE_TIME, s.INTERVIEW_TYPE, s.MEMO, " +
                "s.COMPANY_NAME, a.POSITION, a.STAGE " +
                "FROM SCHEDULE s " +
                "JOIN APPLICATION a ON s.APP_ID = a.APP_ID " +
                "WHERE a.MEMBER_ID = ? " +
                "ORDER BY s.SCHEDULE_DATE ASC";

        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);

            try (ResultSet rs = pstmt.executeQuery()) {
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
            }
        } catch (Exception e) {
            e.printStackTrace();
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

        try (Connection con = DBManager_new.connect()) {
            MemberDTO user = (MemberDTO) request.getSession().getAttribute("user");
            int memberId = user.getMember_id();

            //기업명 양 옆 공백 제거
            String companyName = request.getParameter("company_name");
            if(companyName != null) companyName = companyName.trim();

            String position = request.getParameter("position");
            String applyDate = request.getParameter("apply_date");
            String date = request.getParameter("date");
            String time = request.getParameter("time");
            String type = request.getParameter("type");
            String memo = request.getParameter("memo");
            String stage = mapTypeToStage(type);

            System.out.println("✅ [일정 추가] 화면에서 넘어온 회사명: [" + companyName + "]");

            con.setAutoCommit(false);
            try {
                int companyId = 0;
                String findCompanySql = "SELECT COMPANY_ID FROM COMPANY WHERE COMPANY_NAME = ?";
                try (PreparedStatement pstmt = con.prepareStatement(findCompanySql)) {
                    pstmt.setString(1, companyName);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            companyId = rs.getInt("COMPANY_ID");
                            System.out.println("✅ [DB 검색 성공] 찾은 회사 번호: " + companyId);
                        } else {
                            System.out.println("❌ [DB 검색 실패] DB 명단에 일치하는 글자가 없습니다!");
                            throw new Exception("등록하려는 회사가 존재하지 않습니다: " + companyName);
                        }
                    }
                }

                int newAppId = 0;
                try (PreparedStatement pstmt = con.prepareStatement("SELECT APPLICATION_SEQ.NEXTVAL FROM DUAL");
                     ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) newAppId = rs.getInt(1);
                }

                String appSql = "INSERT INTO APPLICATION (APP_ID, MEMBER_ID, COMPANY_ID, POSITION, STAGE, APPLY_DATE, CREATED_DATE) " +
                        "VALUES (?, ?, ?, ?, ?, ?, SYSDATE)";
                try (PreparedStatement pstmt = con.prepareStatement(appSql)) {
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
                }

                // SCHEDULE 인서트 (기존 코드 유지)
                String schSql = "INSERT INTO SCHEDULE (SCHEDULE_ID, MEMBER_ID, COMPANY_NAME, SCHEDULE_DATE, SCHEDULE_TIME, INTERVIEW_TYPE, MEMO, APP_ID) " +
                        "VALUES (SEQ_SCHEDULE.nextval, ?, ?, TO_DATE(?, 'YYYY-MM-DD'), ?, ?, ?, ?)";
                try (PreparedStatement pstmt = con.prepareStatement(schSql)) {
                    pstmt.setInt(1, memberId);
                    pstmt.setString(2, companyName);
                    pstmt.setString(3, date);
                    pstmt.setString(4, time);
                    pstmt.setString(5, type);
                    pstmt.setString(6, memo);
                    pstmt.setInt(7, newAppId);
                    pstmt.executeUpdate();
                }

                // 🌟🌟🌟 여기서부터 추가된 구글 캘린더 연동 로직 🌟🌟🌟
                try {
                    com.google.api.services.calendar.Calendar service = GoogleCalendarHelper.getCalendarService();

                    // 구글 달력에 띄울 제목과 내용 세팅
                    com.google.api.services.calendar.model.Event event = new com.google.api.services.calendar.model.Event()
                            .setSummary("[" + companyName + "] " + type + " 일정")
                            .setDescription("직무: " + (position == null ? "미정" : position) + "\n메모: " + (memo == null ? "" : memo));

                    // 시간 세팅 (한국 시간 기준)
                    // 시간 세팅 (한국 시간 기준)
                    String startDateTimeStr = date + "T" + time + ":00+09:00";

                    // 🚨 여기서 주소가 client.util.DateTime 으로 바뀌었습니다! (빨간줄 해결)
                    com.google.api.client.util.DateTime startDateTime = new com.google.api.client.util.DateTime(startDateTimeStr);

                    event.setStart(new com.google.api.services.calendar.model.EventDateTime().setDateTime(startDateTime));
                    event.setEnd(new com.google.api.services.calendar.model.EventDateTime().setDateTime(startDateTime));
                    // 구글 서버로 발사!
                    service.events().insert("primary", event).execute();
                    System.out.println("✅ [구글 캘린더] 동기화 성공!");

                } catch (Exception googleEx) {
                    // 구글 연동이 실패해도 (권한 에러 등) 멈추지 않고 아래 DB 커밋으로 넘어가도록 처리!
                    System.out.println("⚠️ [구글 캘린더] 연동 실패! (오라클 DB에는 정상 저장됩니다.) 원인: " + googleEx.getMessage());
                }
                // 🌟🌟🌟 구글 연동 로직 끝 🌟🌟🌟

                // 오라클 DB 커밋! (구글이 실패해도 여기까지 무사히 도달합니다)
                con.commit();

            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        }
    }

    // --- Update로직 ---
    public void updateSchedule(HttpServletRequest request, HttpServletResponse response) throws Exception {

        try (Connection con = DBManager_new.connect()) {
            MemberDTO user = (MemberDTO) request.getSession().getAttribute("user");
            int memberId = user.getMember_id();

            int scheduleId = Integer.parseInt(request.getParameter("schedule_id"));
            String companyName = request.getParameter("company_name");
            String position = request.getParameter("position");
            String date = request.getParameter("date");
            String time = request.getParameter("time");
            String type = request.getParameter("type");
            String memo = request.getParameter("memo");

            con.setAutoCommit(false);
            try {
                int companyId = 0;
                try (PreparedStatement pstmt = con.prepareStatement("SELECT COMPANY_ID FROM COMPANY WHERE COMPANY_NAME = ?")) {
                    pstmt.setString(1, companyName);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) {
                            companyId = rs.getInt("COMPANY_ID");
                        } else {
                            throw new Exception("존재하지 않는 회사입니다: " + companyName);
                        }
                    }
                }

                int appId = 0;
                try (PreparedStatement pstmt = con.prepareStatement("SELECT APP_ID FROM SCHEDULE WHERE SCHEDULE_ID = ?")) {
                    pstmt.setInt(1, scheduleId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) appId = rs.getInt("APP_ID");
                    }
                }

                String updateAppSql = "UPDATE APPLICATION SET COMPANY_ID = ?, POSITION = ?, STAGE = ? WHERE APP_ID = ?";
                try (PreparedStatement pstmt = con.prepareStatement(updateAppSql)) {
                    pstmt.setInt(1, companyId);
                    pstmt.setString(2, (position == null || position.isEmpty()) ? "미정" : position);
                    pstmt.setString(3, mapTypeToStage(type));
                    pstmt.setInt(4, appId);
                    pstmt.executeUpdate();
                }

                String updateSchSql = "UPDATE SCHEDULE SET COMPANY_NAME = ?, SCHEDULE_DATE = TO_DATE(?, 'YYYY-MM-DD'), " +
                        "SCHEDULE_TIME = ?, INTERVIEW_TYPE = ?, MEMO = ? WHERE SCHEDULE_ID = ?";
                try (PreparedStatement pstmt = con.prepareStatement(updateSchSql)) {
                    pstmt.setString(1, companyName);
                    pstmt.setString(2, date);
                    pstmt.setString(3, time);
                    pstmt.setString(4, type);
                    pstmt.setString(5, memo);
                    pstmt.setInt(6, scheduleId);
                    pstmt.executeUpdate();
                }

                con.commit();
            } catch (Exception e) {
                con.rollback();
                throw e;
            }
        }
    }

    // --- Delete ---
    public void deleteSchedule(int scheduleId) {

        try (Connection con = DBManager_new.connect()) {
            con.setAutoCommit(false);
            try {
                int appId = 0;
                String findAppSql = "SELECT APP_ID FROM SCHEDULE WHERE SCHEDULE_ID = ?";
                try (PreparedStatement pstmt = con.prepareStatement(findAppSql)) {
                    pstmt.setInt(1, scheduleId);
                    try (ResultSet rs = pstmt.executeQuery()) {
                        if (rs.next()) appId = rs.getInt("APP_ID");
                    }
                }

                String delSchSql = "DELETE FROM SCHEDULE WHERE SCHEDULE_ID = ?";
                try (PreparedStatement pstmt = con.prepareStatement(delSchSql)) {
                    pstmt.setInt(1, scheduleId);
                    pstmt.executeUpdate();
                }

                if (appId > 0) {
                    String delAppSql = "DELETE FROM APPLICATION WHERE APP_ID = ?";
                    try (PreparedStatement pstmt = con.prepareStatement(delAppSql)) {
                        pstmt.setInt(1, appId);
                        pstmt.executeUpdate();
                    }
                }

                con.commit();
            } catch (Exception e) {
                con.rollback();
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getAllCompanyNames() {
        ArrayList<String> names = new ArrayList<>();

        String sql = "SELECT COMPANY_NAME FROM COMPANY ORDER BY COMPANY_NAME ASC";

        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                names.add(rs.getString("COMPANY_NAME"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }

    // 🌟🌟🌟 10분마다 실행될 구글 -> 웹 DB 양방향 동기화 메서드 (뼈대) 🌟🌟🌟
    public void syncGoogleCalendarToDB() {

        try (Connection con = DBManager_new.connect()) {

            // 1. 오라클 DB에서 "구글 연동을 한(REFRESH_TOKEN이 있는) 모든 회원"을 조회합니다.
            // (팀원이 MEMBER 테이블에 REFRESH_TOKEN 컬럼을 만들어줘야 이 쿼리가 완성됩니다!)
            String sql = "SELECT MEMBER_ID, GOOGLE_REFRESH_TOKEN FROM MEMBER WHERE GOOGLE_REFRESH_TOKEN IS NOT NULL";
            try (PreparedStatement pstmt = con.prepareStatement(sql);
                 ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    int memberId = rs.getInt("MEMBER_ID");
                    String refreshToken = rs.getString("GOOGLE_REFRESH_TOKEN");

                    System.out.println("▶ 회원번호 [" + memberId + "]의 구글 캘린더 동기화 진행 중...");

                    // ==============================================================
                    // 🛠️ [나중에 채울 부분] 🛠️
                    // 1. refreshToken을 사용해서 구글에서 새로운 accessToken을 발급받는다.
                    // 2. 구글 캘린더 API(service.events().list())를 호출해서,
                    //    최근 10분 동안 수정/추가된 일정만 긁어온다.
                    // 3. 긁어온 일정이 우리 오라클 DB(SCHEDULE 테이블)에 없으면 INSERT, 있으면 UPDATE 한다.
                    // ==============================================================
                }
            }

            System.out.println("✅ [DevDesk 스케줄러] 구글 캘린더 동기화 한 바퀴 완료!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
