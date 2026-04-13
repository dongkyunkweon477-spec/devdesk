package com.devdesk.pj.report;

import com.devdesk.pj.main.DBManager_new;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public enum ReportDAO {
    RDAO;

    // 1. 신고 등록 (Insert)
    public int insertReport(ReportVO r) {
        String sql = "INSERT INTO report (report_id, review_id, member_id, repo_reason, repo_content) "
                   + "VALUES (report_seq.NEXTVAL, ?, ?, ?, ?)";
        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, r.getRepoReviewId());
            pstmt.setInt(2, r.getRepoMemberId());
            pstmt.setString(3, r.getRepoReason());
            pstmt.setString(4, r.getRepoContent());
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 2. 전체 신고 목록 조회 (Select All)
    public List<ReportVO> getAllReports() {
        List<ReportVO> reports = new ArrayList<>();
        String sql = "SELECT * FROM report ORDER BY repo_created_date DESC";
        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                ReportVO r = new ReportVO();
                r.setReportId(rs.getInt("report_id"));
                r.setRepoReviewId(rs.getInt("review_id"));
                r.setRepoMemberId(rs.getInt("member_id"));
                r.setRepoReason(rs.getString("repo_reason"));
                r.setRepoContent(rs.getString("repo_content"));
                r.setRepoStatus(rs.getString("repo_status"));
                r.setReopCreated(rs.getDate("repo_created_date"));
                reports.add(r);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reports;
    }

    // 3. 상태 변경 (Update)
    public int updateReportStatus(int reportId, String status) {
        String sql = "UPDATE report SET repo_status = ? WHERE report_id = ?";
        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, status);
            pstmt.setInt(2, reportId);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 4. 신고 삭제 (Delete)
    public int deleteReport(int reportId) {
        String sql = "DELETE FROM report WHERE report_id = ?";
        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, reportId);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 5. 중복 신고 방지 체크
    public boolean checkDuplicate(int memberId, int reviewId) {
        String sql = "SELECT COUNT(*) FROM report WHERE member_id = ? AND review_id = ?";
        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, memberId);
            pstmt.setInt(2, reviewId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
