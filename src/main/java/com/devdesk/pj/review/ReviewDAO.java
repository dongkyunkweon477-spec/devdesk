package com.devdesk.pj.review;

import com.devdesk.pj.main.DBManager_new;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ReviewDAO {
    public static final ReviewDAO REVIEW_DAO = new ReviewDAO();

    private ReviewDAO() {
    }

    public int getReviewCount(Integer companyId) {
        String sql;
        if (companyId != null) {
            sql = "select count(*) from review where r_company_id = ?";
        } else {
            sql = "select count(*) from review";
        }
        try (
                Connection con = DBManager_new.connect();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            if (companyId != null) {
                pstmt.setInt(1, companyId);
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
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


    public ArrayList<ReviewVO> getReviewAll(int page, int pageSize) {
        String sql = "SELECT * FROM ("
                + "  SELECT ROWNUM rn, t.* FROM ("
                + "    SELECT r.*, c.company_name"
                + "    FROM review r"
                + "    JOIN company c ON r.r_company_id = c.company_id"
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
            pstmt.setInt(1, start);
            pstmt.setInt(2, end);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    reviews.add(ReviewVO.fromResultSet(rs));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return reviews;
    }

    public void insertReview(ReviewVO vo) {
        String sql = "INSERT INTO review (r_id, r_company_id, r_member_id, r_title,"
                + " r_job_position, r_interview_type, r_difficulty, r_rating,"
                + " r_result, r_content, r_interviewer_count, r_student_count,"
                + " r_atmosphere, r_contact_method, r_contact_days)"
                + " VALUES (review_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
            pstmt.setInt(7, vo.getReviewRating());
            pstmt.setString(8, vo.getReviewResult());
            pstmt.setString(9, vo.getReviewContent());
            pstmt.setInt(10, vo.getReviewInterviewerCount());
            pstmt.setInt(11, vo.getReviewStudentCount());
            pstmt.setString(12, vo.getReviewAtmosphere());
            pstmt.setString(13, vo.getReviewContactMethod());
            pstmt.setInt(14, vo.getReviewContactDays());
            if (pstmt.executeUpdate() > 0) {
                System.out.println("insert success");
            } else {
                System.out.println("insert fail");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public int updateReview(ReviewVO vo) {
        String sql = "UPDATE review SET r_title=?, r_job_position=?, r_interview_type=?,"
                + " r_difficulty=?, r_result=?, r_content=?,"
                + " r_interviewer_count=?, r_student_count=?,"
                + " r_atmosphere=?, r_contact_method=?, r_contact_days=?,"
                + " r_updated_date=SYSDATE"
                + " WHERE r_id=?";
        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setString(1, vo.getReviewTitle());
            pstmt.setString(2, vo.getReviewJobPosition());
            pstmt.setString(3, vo.getReviewInterviewType());
            pstmt.setInt(4, vo.getReviewDifficulty());
            pstmt.setString(5, vo.getReviewResult());
            pstmt.setString(6, vo.getReviewContent());
            pstmt.setInt(7, vo.getReviewInterviewerCount());
            pstmt.setInt(8, vo.getReviewStudentCount());
            pstmt.setString(9, vo.getReviewAtmosphere());
            pstmt.setString(10, vo.getReviewContactMethod());
            pstmt.setInt(11, vo.getReviewContactDays());
            pstmt.setInt(12, vo.getReviewId());
            return pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public int deleteReview(int reviewId) {
        String sql = "delete from review where r_id = ?";
        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setInt(1, reviewId);
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public ReviewVO getReviewById(int reviewId) {
        String sql = "select r.*, c.company_name from review r " +
                "join company c on r.r_company_id = c.company_id " +
                "where r.r_id = ?";
        ReviewVO review = null;
        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setInt(1, reviewId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    review = ReviewVO.fromResultSet(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return review;
    }


}
