package com.devdesk.pj.review;

import com.devdesk.pj.main.DBManager_new;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

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

        String deleteLikeSql = "DELETE FROM review_like WHERE review_id = ?";
        String deleteBookmarkSql = "DELETE FROM review_bookmark WHERE review_id = ?";

        String deleteReviewSql = "DELETE FROM review WHERE r_id = ?";

        Connection con = null;
        try {
            con = DBManager_new.connect();
            con.setAutoCommit(false); // 트랜잭션 시작


            try (PreparedStatement pstmt1 = con.prepareStatement(deleteLikeSql)) {
                pstmt1.setInt(1, reviewId);
                pstmt1.executeUpdate();
            }


            try (PreparedStatement pstmt2 = con.prepareStatement(deleteBookmarkSql)) {
                pstmt2.setInt(1, reviewId);
                pstmt2.executeUpdate();
            }


            int result = 0;
            try (PreparedStatement pstmt3 = con.prepareStatement(deleteReviewSql)) {
                pstmt3.setInt(1, reviewId);
                result = pstmt3.executeUpdate();
            }

            con.commit(); // 모두 성공 시 커밋
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback(); // 에러 발생 시 롤백
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
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

    public void increaseViewCount(int reviewId) {
        String sql = "update review set r_view_count = r_view_count + 1 where r_id = ?";
        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setInt(1, reviewId);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean toggleLike(int memberId, int reviewId) {
        String checkSql = "SELECT COUNT(*) FROM review_like WHERE member_id = ? AND review_id = ?";
        String insertSql = "INSERT INTO review_like (member_id, review_id) VALUES (?, ?)";
        String deleteSql = "DELETE FROM review_like WHERE member_id = ? AND review_id = ?";
        String plusSql = "UPDATE review SET r_like_count = r_like_count + 1 WHERE r_id = ?";
        String minusSql = "UPDATE review SET r_like_count = r_like_count - 1 WHERE r_id = ?";


        try (Connection con = DBManager_new.connect()) {
            con.setAutoCommit(false);

            boolean exists = false;
            try (PreparedStatement pstmt = con.prepareStatement(checkSql)) {
                pstmt.setInt(1, memberId);
                pstmt.setInt(2, reviewId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    rs.next();
                    exists = rs.getInt(1) > 0;

                    if (exists) {
                        try (PreparedStatement del = con.prepareStatement(deleteSql)) {
                            del.setInt(1, memberId);
                            del.setInt(2, reviewId);
                            del.executeUpdate();
                        }
                        try (PreparedStatement minus = con.prepareStatement(minusSql)) {
                            minus.setInt(1, reviewId);
                            minus.executeUpdate();
                        }
                        con.commit();
                        return false;
                    } else {
                        try (PreparedStatement ins = con.prepareStatement(insertSql)) {

                            ins.setInt(1, memberId);
                            ins.setInt(2, reviewId);
                            ins.executeUpdate();
                        }
                        try (PreparedStatement plus = con.prepareStatement(plusSql)) {
                            plus.setInt(1, reviewId);
                            plus.executeUpdate();

                        }
                        con.commit();
                        return true;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isLiked(int memberId, int reviewId) {
        String sql = "SELECT COUNT(*) FROM review_like WHERE member_id = ? AND review_id = ?";
        try (
                Connection con = DBManager_new.connect();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
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

    public boolean toggleBookmark(int memberId, int reviewId) {
        String checkSql = "SELECT COUNT(*) FROM review_bookmark WHERE member_id = ? AND review_id = ?";
        String insertSql = "INSERT INTO review_bookmark (member_id, review_id) VALUES (?, ?)";
        String deleteSql = "DELETE FROM review_bookmark WHERE member_id = ? AND review_id = ?";
        String plusSql = "UPDATE review SET r_bookmark_count = r_bookmark_count + 1 WHERE r_id = ?";
        String minusSql = "UPDATE review SET r_bookmark_count = r_bookmark_count - 1 WHERE r_id = ?";

        Connection con = null;
        try {
            con = DBManager_new.connect();
            // 자동 커밋 해제 (트랜잭션 시작)
            con.setAutoCommit(false);

            boolean exists = false;
            try (PreparedStatement pstmt = con.prepareStatement(checkSql)) {
                pstmt.setInt(1, memberId);
                pstmt.setInt(2, reviewId);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        exists = rs.getInt(1) > 0;
                    }
                }
            }

            if (exists) {
                // 이미 북마크가 존재하면 삭제 및 카운트 감소
                try (PreparedStatement del = con.prepareStatement(deleteSql)) {
                    del.setInt(1, memberId);
                    del.setInt(2, reviewId);
                    del.executeUpdate();
                }
                try (PreparedStatement minus = con.prepareStatement(minusSql)) {
                    minus.setInt(1, reviewId);
                    minus.executeUpdate();
                }
                con.commit(); // 두 작업 모두 성공 시 확정
                return false;
            } else {
                // 북마크가 없으면 추가 및 카운트 증가
                try (PreparedStatement ins = con.prepareStatement(insertSql)) {
                    ins.setInt(1, memberId);
                    ins.setInt(2, reviewId);
                    ins.executeUpdate();
                }
                try (PreparedStatement plus = con.prepareStatement(plusSql)) {
                    plus.setInt(1, reviewId);
                    plus.executeUpdate();
                }
                con.commit(); // 두 작업 모두 성공 시 확정
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (con != null) {
                try {
                    con.rollback(); // 중간에 오류 발생 시 모든 작업 취소
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true); // 커넥션 풀 반환 전 기본값으로 원상 복구
                    con.close(); // 자원 해제
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return false;
    }

    public boolean isBookmarked(int memberId, int reviewId) {
        String sql = "SELECT COUNT(*) FROM review_bookmark WHERE member_id = ? AND review_id = ?";
        try (
                Connection con = DBManager_new.connect();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
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


    public Map<String, Object> getFilteredReviews(String companyIds, String interviewType, String result, String sort, int page, int pageSize) {
        StringBuilder baseSql = new StringBuilder(
                "select r.*, c.company_name from review r " +
                        "join company c on r.r_company_id = c.company_id " +
                        "where c.is_verified = 'Y'"
        );
        List<Object> params = new ArrayList<>();

        if (companyIds != null && !companyIds.isBlank()) {
            String[] ids = companyIds.split(",");
            baseSql.append(" and r.r_company_id IN (");
            for (int i = 0; i < ids.length; i++) {
                baseSql.append(i > 0 ? ",?" : "?");
                params.add(Integer.parseInt(ids[i].trim()));
            }
            baseSql.append(")");
        }
        if (interviewType != null && !interviewType.isEmpty()) {
            baseSql.append(" and r.r_interview_type = ?");
            params.add(interviewType);
        }
        if (result != null && !result.isBlank()) {
            baseSql.append(" and r.r_result = ?");
            params.add(result);
        }

        // 2. ORDER BY 추가 전에 COUNT 쿼리 생성 (성능 향상)
        String countSql = "SELECT COUNT(*) FROM (" + baseSql.toString() + ")";

        // 3. 정렬 문자열 앞 공백 추가 및 baseSql에 병합
        String orderBy = " order by r.r_created_date desc"; // 컬럼명 확인 요망
        if ("difficulty_desc".equals(sort)) {
            orderBy = " order by r.r_difficulty desc";
        } else if ("difficulty_asc".equals(sort)) {
            orderBy = " order by r.r_difficulty asc";
        }
        baseSql.append(orderBy);

        // 페이징 쿼리 조립
        String pagedSql = "SELECT * FROM ("
                + "  SELECT ROWNUM rn, t.* FROM (" + baseSql.toString() + ") t"
                + ") WHERE rn BETWEEN ? AND ?";
        int start = (page - 1) * pageSize + 1;
        int end = page * pageSize;

        Map<String, Object> data = new HashMap<>();
        try (Connection con = DBManager_new.connect()) {
            // 전체 건수 조회
            try (PreparedStatement pstmt = con.prepareStatement(countSql)) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(i + 1, params.get(i));
                }
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) data.put("totalCount", rs.getInt(1));
                }
            }

            // 페이징 데이터 조회
            try (PreparedStatement pstmt = con.prepareStatement(pagedSql)) {
                for (int i = 0; i < params.size(); i++) {
                    pstmt.setObject(i + 1, params.get(i));
                }
                pstmt.setInt(params.size() + 1, start);
                pstmt.setInt(params.size() + 2, end);

                List<ReviewVO> reviews = new ArrayList<>();
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        reviews.add(ReviewVO.fromResultSet(rs));
                    }
                }
                data.put("reviews", reviews);
            }

            int totalCount = (int) data.getOrDefault("totalCount", 0);
            data.put("totalPages", (int) Math.ceil((double) totalCount / pageSize));
            data.put("currentPage", page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
