package com.devdesk.pj.application;

import com.devdesk.pj.dashboard.StageCountVO;
import com.devdesk.pj.main.DBManager_new;
import com.devdesk.pj.resumeblock.ResumeBlockVO;
import com.devdesk.pj.user.MemberDTO;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ApplicationDAO {


    public static void addApplication(HttpServletRequest request) {

        String sql = "INSERT INTO application "
                + "(app_id, member_id, company_id, position, stage, apply_date, memo, created_date) "
                + "VALUES (APPLICATION_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?, SYSDATE)";

        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            // 파라미터 받기
            MemberDTO loginUser = (MemberDTO) request.getSession().getAttribute("user");
            if (loginUser == null) return;
            int memberId = loginUser.getMember_id();
            String companyId = request.getParameter("companyId");
            String position = request.getParameter("position");
            String stage = request.getParameter("stage");
            String applyDate = request.getParameter("apply_date");
            String memo = request.getParameter("memo");

            // 디버깅 출력
            System.out.println(memberId);
            System.out.println(companyId);
            System.out.println(position);
            System.out.println(stage);
            System.out.println(applyDate);
            System.out.println(memo);

            // 바인딩
            pstmt.setInt(1, memberId);
            pstmt.setString(2, companyId);
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
                System.out.println("application applied successfully");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void selectAllApplications(HttpServletRequest request) {
        String sql = "SELECT \n" +
                "a.app_id,\n" +
                "a.COMPANY_ID,\n" +
                "c.company_name,\n" +
                "a.position,\n" +
                "a.stage,\n" +
                "a.apply_date,\n" +
                "a.memo FROM application a JOIN company c ON a.company_id = c.company_id where member_id = ? ORDER BY a.created_date DESC";

        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            ArrayList<ApplicationV0> dtos = new ArrayList<>();
            MemberDTO loginUser = (MemberDTO) request.getSession().getAttribute("user");
            if (loginUser == null) return;
            int memberId = loginUser.getMember_id();
            pstmt.setInt(1, memberId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ApplicationV0 dto = new ApplicationV0();
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
            }
            System.out.println(dtos);

            request.setAttribute("applicationList", dtos);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void selectAllCompanies(HttpServletRequest request) {
        String sql = "select * from COMPANY";

        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            ArrayList<ApplicationV0> dtos = new ArrayList<>();

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ApplicationV0 dto = new ApplicationV0();
                    dto.setCompanyId(rs.getString("company_id"));
                    dto.setCompanyName(rs.getString("company_name"));
                    dtos.add(dto);
                }
            }
            request.setAttribute("companyList", dtos);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void selectStarApplication(HttpServletRequest request) {

        String sql = "SELECT a.app_id, a.company_id, c.company_name, a.position, " +
                "a.stage, a.apply_date, a.memo, a.is_star " +
                "FROM application a JOIN company c ON a.company_id = c.company_id " +
                "WHERE a.member_id = ? AND a.is_star = 1 ORDER BY a.created_date DESC";

        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            MemberDTO loginUser = (MemberDTO) request.getSession().getAttribute("user");
            if (loginUser == null) return;
            pstmt.setInt(1, loginUser.getMember_id());

            ArrayList<ApplicationV0> dtos = new ArrayList<>();
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    ApplicationV0 dto = new ApplicationV0();
                    dto.setAppId(rs.getString("app_id"));
                    dto.setCompanyId(rs.getString("company_id"));
                    dto.setCompanyName(rs.getString("company_name"));
                    dto.setPosition(rs.getString("position"));
                    dto.setStatus(rs.getString("stage"));
                    dto.setStatusName(getStatusName(rs.getString("stage")));
                    dto.setAppDate(rs.getDate("apply_date"));
                    dto.setMemo(rs.getString("memo"));
                    dto.setIsStar(rs.getInt("is_star"));
                    dtos.add(dto);
                }
            }
            request.setAttribute("applicationList", dtos);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void deleteApplication(HttpServletRequest request) {

        String sql = "DELETE FROM application WHERE app_id = ?";

        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ApplicationV0 selectApplication(HttpServletRequest request) {

        ApplicationV0 dto = null;

        String sql = "SELECT a.app_id, c.company_name, a.position, a.stage, a.apply_date, a.memo "
                + "FROM application a "
                + "JOIN company c ON a.company_id = c.company_id "
                + "WHERE a.app_id = ?";

        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            int appId = Integer.parseInt(request.getParameter("app_id"));
            pstmt.setInt(1, appId);

            try (ResultSet rs = pstmt.executeQuery()) {
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
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dto;
    }

    public static void updateApplication(HttpServletRequest request) {

        String sql = "UPDATE application "
                + "SET stage = ? "
                + "WHERE app_id = ?";

        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setString(1, request.getParameter("status"));
            pstmt.setInt(2, Integer.parseInt(request.getParameter("app_id")));

            int result = pstmt.executeUpdate();
            System.out.println("상태 변경 결과: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getStatusName(String status) {
        if (status == null) return "알 수 없음"; // null 체크 추가
        switch (status) {
            case "APPLIED":
                return "지원완료";
            case "DOCUMENT":
                return "서류 합격";
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

    public static StageCountVO countGroupByStage(int memberId) {
        String sql = "SELECT STAGE, COUNT(*) AS CNT " +
                "FROM APPLICATION " +
                "WHERE MEMBER_ID = ? " +
                "GROUP BY STAGE";

        StageCountVO vo = new StageCountVO();

        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String stage = rs.getString("STAGE");
                    int cnt = rs.getInt("CNT");

                    switch (stage) {
                        case "APPLIED":
                            vo.setApplied(cnt);
                            break;
                        case "DOCUMENT":
                            vo.setDocumentPass(cnt);
                            break;
                        case "FIRST_INTERVIEW":
                            vo.setFirstInterview(cnt);
                            break;
                        case "SECOND_INTERVIEW":
                            vo.setSecondInterview(cnt);
                            break;
                        case "THIRD_INTERVIEW":
                            vo.setThirdInterview(cnt);
                            break;
                        case "PASS":
                            vo.setPassed(cnt);
                            break;
                        case "FAIL":
                            vo.setFailed(cnt);
                            break;
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return vo;
    }

    public static void starApplication(HttpServletRequest request) {

        String sql = "UPDATE application "
                + "SET is_star = ? "
                + "WHERE app_id = ?";

        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql)) {

            int isStar = Integer.parseInt(request.getParameter("is_star"));
            pstmt.setInt(2, Integer.parseInt(request.getParameter("app_id")));
            if (isStar == 1) {
                pstmt.setInt(1, 0);
            } else {
                pstmt.setInt(1, 1);
            }
            int result = pstmt.executeUpdate();
            System.out.println("상태 변경 결과: " + result);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
