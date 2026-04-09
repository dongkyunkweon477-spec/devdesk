package com.devdesk.pj.dashboard;

import com.devdesk.pj.main.DBManager_new;
import com.devdesk.pj.user.MemberDTO;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
                    case "DOCUMENT":       // DOCUMENT_PASS → DOCUMENT
                        vo.setDocumentPass(cnt);
                        break;
                    case "PASS":           // PASSED → PASS
                        vo.setPassed(cnt);
                        break;
                    case "FAIL":           // 새로 추가
                        vo.setFailed(cnt);
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

    public static List<Map<String, Object>> getFunnelData(StageCountVO sc) {
        List<Map<String, Object>> funnelData = new ArrayList<>();

        addFunnel(funnelData, "이력서 제출", "서류 합격",
                "#9da3b8", "#ffd166",
                sc.getApplied(), sc.getDocumentPass());

        addFunnel(funnelData, "서류 합격", "1차 면접",
                "#ffd166", "#4ecdc4",
                sc.getDocumentPass(), sc.getFirstInterview());

        addFunnel(funnelData, "1차 면접", "2차 면접",
                "#4ecdc4", "#5b7cf8",
                sc.getFirstInterview(), sc.getSecondInterview());

        addFunnel(funnelData, "2차 면접", "3차 면접",
                "#5b7cf8", "#8b6ef5",
                sc.getSecondInterview(), sc.getThirdInterview());

        addFunnel(funnelData, "3차 면접", "합격",
                "#8b6ef5", "#56e39f",
                sc.getThirdInterview(), sc.getPassed());

        return funnelData;
    }


    // 헬퍼 메서드
    private static void addFunnel(List<Map<String, Object>> list,
                                  String fromLabel, String toLabel,
                                  String fromColor, String toColor,
                                  int from, int to) {
        Map<String, Object> map = new HashMap<>();
        map.put("fromLabel", fromLabel);
        map.put("toLabel", toLabel);
        map.put("fromColor", fromColor);
        map.put("toColor", toColor);
        // from이 0이면 0% 처리 (나누기 0 방지)
        int pct = (from == 0) ? 0 : (int) Math.round((double) to / from * 100);
        map.put("pct", pct);
        list.add(map);
    }


}