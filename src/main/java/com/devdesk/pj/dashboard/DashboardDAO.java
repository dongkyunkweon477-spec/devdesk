package com.devdesk.pj.dashboard;

import com.devdesk.pj.main.DBManager_new;
import com.devdesk.pj.user.MemberDTO;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class DashboardDAO {
    public static StageCountVO countGroupbystage(HttpServletRequest request) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "SELECT STAGE, COUNT(*) AS CNT " +
                "FROM APPLICATION " +
                "WHERE MEMBER_ID = ? " +
                "GROUP BY STAGE";
        StageCountVO vo = new StageCountVO();

        try {
            // ① 세션에서 꺼내는 것도 try 안으로
            MemberDTO loginUser = (MemberDTO) request.getSession().getAttribute("user");

            // ② null 체크 추가
            if (loginUser == null) {
                return vo; // 로그인 안 된 상태면 빈 VO 반환
            }

            int memberId = loginUser.getMember_id();
            System.out.println(memberId);
            conn = DBManager_new.connect();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, memberId);

            System.out.println("쿼리 실행됨, memberId    : " + memberId);
            rs = pstmt.executeQuery();
            System.out.println("RS received");


            while (rs.next()) {
                String stage = rs.getString("STAGE");
                int cnt = rs.getInt("CNT");

                System.out.println("STAGE: [" + stage + "], CNT: " + cnt);
                // 실제 DB에서 오는 STAGE 값 확인


                switch (stage) {
                    case "APPLIED":
                        vo.setApplied(cnt);
                        break;
                    case "DOCUMENT_PASS":
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
                    case "CODING_TEST":
                        vo.setCodingTest(cnt);
                        break;
                    case "PASSED":
                        vo.setPassed(cnt);
                        break;
                }
            }                               // while 닫기

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        request.setAttribute("stageCounts", vo);
        return vo;  // ✅ finally 밖으로
    }
}