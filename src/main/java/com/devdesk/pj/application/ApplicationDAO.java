package com.devdesk.pj.application;

import com.devdesk.pj.main.DBManager_new;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ApplicationDAO {


    public static void addApplication(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement pstmt = null;

        String sql = "INSERT INTO application "
                + "(app_id, member_id, company_id, position, stage, apply_date, memo, created_date) "
                + "VALUES (APPLICATION_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, SYSDATE)";

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);

            // 파라미터 받기
//            String memberId = request.getParameter("member_id");
            String memberId = "3";
//            String companyId = request.getParameter("company_id");
            String companyId = "1";
            String position = request.getParameter("position");
            String stage = request.getParameter("stage");
            String applyDate = request.getParameter("apply_date");
            String memo = request.getParameter("memo");
            String interviewDate = request.getParameter("interview_date");
            String interviewTime = request.getParameter("interview_time");
            String interviewType = request.getParameter("interview_type");

            // 디버깅 출력
            System.out.println(memberId);
            System.out.println(companyId);
            System.out.println(position);
            System.out.println(stage);
            System.out.println(applyDate);
            System.out.println(memo);

            // 바인딩
            pstmt.setInt(1, Integer.parseInt(memberId));
            pstmt.setInt(2, Integer.parseInt(companyId));
            pstmt.setString(3, position);
            pstmt.setString(4, stage);

            // 날짜 처리
            if (applyDate != null && !applyDate.isEmpty()) {
                pstmt.setDate(5, java.sql.Date.valueOf(applyDate));
            } else {
                pstmt.setNull(5, java.sql.Types.DATE);
            }

            pstmt.setString(6, memo);


            // 실행
            int result = pstmt.executeUpdate();

            if (result == 1) {
                System.out.println("지원 등록 성공");
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DBManager_new.close(con, pstmt, null);
        }

    }

    public static void selectAllApplications(HttpServletRequest request) {
        //1. 값 or db

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT \n" +
                "a.app_id,\n" +
                "a.COMPANY_ID,\n" +
                "c.company_name,\n" +
                "a.position,\n" +
                "a.stage,\n" +
                "a.apply_date,\n" +
                "a.memo FROM application a JOIN company c ON a.company_id = c.company_id ORDER BY a.created_date DESC";
        ;


        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            ApplicationV0 dto = null;
            ArrayList<ApplicationV0> dtos = new ArrayList<>();

            while (rs.next()) {
                dto = new ApplicationV0();
                dto.setAppId(rs.getString("app_id"));
                dto.setCompanyId(rs.getString("company_id"));
                dto.setCompanyName(rs.getString("company_name"));
                dto.setPosition(rs.getString("position"));
                dto.setStatus(rs.getString("stage"));
                dto.setStatusName(getStatusName(rs.getString("stage"))); // 한글
                dto.setAppDate(rs.getDate("apply_date"));
                dto.setMemo(rs.getString("memo"));
                dtos.add(dto);
            }
            System.out.println(dtos);

            request.setAttribute("applicationList", dtos);


        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DBManager_new.close(con, pstmt, rs);
        }

    }

    public static void selectAllCompanies(HttpServletRequest request) {
        //1. 값 or db

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from COMPANY";


        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            ApplicationV0 dto = null;
            ArrayList<ApplicationV0> dtos = new ArrayList<>();

            while (rs.next()) {
                dto = new ApplicationV0();
                dto.setCompanyId(rs.getString("company_id"));
                dto.setCompanyName(rs.getString("company_name"));
                dtos.add(dto);
            }
            request.setAttribute("companyList", dtos);


        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DBManager_new.close(con, pstmt, rs);
        }

    }

    public static void deleteApplication(HttpServletRequest request) {

        Connection con = null;
        PreparedStatement pstmt = null;

        String sql = "DELETE FROM application WHERE app_id = ?";

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);

            // 👉 파라미터 받기
            String appIdStr = request.getParameter("app_id");
            System.out.println("app_id = " + appIdStr);
            int appId = Integer.parseInt(appIdStr);

            pstmt.setInt(1, appId);

            int result = pstmt.executeUpdate();

            if (result == 1) {
                System.out.println("삭제 성공");
            } else {
                System.out.println("삭제 실패");
            }

            if (pstmt.executeUpdate() == 1) {
                System.out.println("delete success");

            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            DBManager_new.close(con, pstmt, null);
        }
    }

    public static ApplicationV0 selectApplication(HttpServletRequest request) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        ApplicationV0 dto = null;

        String sql = "SELECT a.app_id, c.company_name, a.position, a.stage, a.apply_date, a.memo "
                + "FROM application a "
                + "JOIN company c ON a.company_id = c.company_id "
                + "WHERE a.app_id = ?";

        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);

            int appId = Integer.parseInt(request.getParameter("app_id"));
            pstmt.setInt(1, appId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                dto = new ApplicationV0();
                dto.setAppId(rs.getString("app_id"));
                dto.setCompanyName(rs.getString("company_name"));
                dto.setPosition(rs.getString("position"));
                dto.setStatus(rs.getString("stage")); // DB값
                dto.setStatusName(getStatusName(rs.getString("stage"))); // 한글
                dto.setAppDate(rs.getDate("apply_date"));
                dto.setMemo(rs.getString("memo"));

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, rs);
        }

        return dto;
    }

    public static void updateApplication(HttpServletRequest request) {

        Connection con = null;
        PreparedStatement pstmt = null;

        String sql = "UPDATE application "
                + "SET stage = ? "
                + "WHERE app_id = ?";
        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1, request.getParameter("status"));
            pstmt.setInt(2, Integer.parseInt(request.getParameter("app_id")));

            int result = pstmt.executeUpdate();
            System.out.println("상태 변경 결과: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, null);
        }
    }

    public static String getStatusName(String status) {
        if (status == null) return "알 수 없음"; // null 체크 추가
        switch (status) {
            case "APPLIED":
                return "지원완료";
            case "FIRST_INTERVIEW":
                return "1차 면접";
            case "SECOND_INTERVIEW":
                return "2차 면접";
            case "THIRD_INTERVIEW":
                return "3차 면접";
            case "PASS":
                return "합격";
            case "FAIL":
                return "불합격";
            default:
                return status;
        }
    }

}

