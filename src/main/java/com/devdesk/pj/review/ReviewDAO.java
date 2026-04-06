package com.devdesk.pj.review;

import com.devdesk.pj.main.DBManager_new;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ReviewDAO {
    static final ReviewDAO REVIEW_DAO = new ReviewDAO();

    private ReviewDAO() {
    }

    public ArrayList<ReviewVO> getReviewsByCompany(int companyId, int page, int pageSize) {

        String sql = "SELECT * FROM ("
                + "  SELECT ROWNUM rn, t.* FROM ("
                + "    SELECT r.*, c.company_name, m.nickname AS member_nickname"
                + "    FROM review r"
                + "    JOIN company c ON r.r_company_id = c.company_id"
                + "    JOIN member m ON r.r_member_id = m.member_id"
                + "    WHERE r.r_company_id = ?"
                + "    ORDER BY r.r_created_date DESC"
                + "  ) t"
                + ") WHERE rn BETWEEN ? AND ?";
        int start = (page - 1) * pageSize + 1;
        int end = page * pageSize;
        ArrayList<ReviewVO> reviews = new ArrayList<>();
        try (
                Connection con = DBManager_new.connect();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setInt(1, companyId);
            pstmt.setInt(2, start);
            pstmt.setInt(3, end);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(ReviewVO.fromResultSet(rs));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return reviews;
    }


    public ArrayList<ReviewVO> getReviewAll() {
        String sql = "select r.* , c.company_name from review r join" +
                " company c on r.r_company_id = c.company_id" +
                " order by r_created_date desc";
        ArrayList<ReviewVO> reviews = new ArrayList<>();
        try (
                Connection con = DBManager_new.connect();
                PreparedStatement pstmt = con.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
        ) {
            while (rs.next()) {
                reviews.add(ReviewVO.fromResultSet(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviews;
    }

    public void insertReview(ReviewVO vo) {
        String sql = "insert into review(r_id , r_company_id , r_member_id , r_title " +
                ", r_job_position , r_interview_type , r_difficulty , r_result , r_content , " +
                "r_interviewer_count,r_student_count,r_atmosphere," +
                "r_contact_method,r_contact_days)"
                + "values(review_seq.nextval,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (
                Connection con = DBManager_new.connect();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setInt(1, vo.getReviewCompanyId());
            pstmt.setInt(2, vo.getReviewMemberId());
            pstmt.setString(3, vo.getReviewTitle());
            pstmt.setString(4, vo.getReviewJobPosition());
            pstmt.setString(5, vo.getReviewInterviewType());
            pstmt.setInt(6, vo.getReviewDifficulty());
            pstmt.setString(7, vo.getReviewResult());
            pstmt.setString(8, vo.getReviewContent());
            pstmt.setInt(9, vo.getReviewInterviewerCount());
            pstmt.setInt(10, vo.getReviewStudentCount());
            pstmt.setString(11, vo.getReviewAtmosphere());
            pstmt.setString(12, vo.getReviewContactMethod());
            pstmt.setInt(13, vo.getReviewContactDays());
            if (pstmt.executeUpdate() > 0) {
                System.out.println("insert success");
            } else {
                System.out.println("insert fail");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
