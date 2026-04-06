package com.devdesk.pj.review;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewVO {
    private int reviewId;
    private int reviewCompanyId;
    private int reviewMemberId;
    private String reviewTitle;
    private String reviewJobPosition;
    private String reviewInterviewType;
    private int reviewDifficulty;
    private String reviewResult;
    private String reviewContent;
    private int reviewInterviewerCount;
    private int reviewStudentCount;
    private String reviewAtmosphere;
    private String reviewContactMethod;
    private int reviewContactDays;
    private int reviewViewCount;
    private int reviewBookmarkCount;
    private int reviewLikeCount;
    private Date reviewCreatedDate;
    private Date reviewUpdatedDate;

    private String companyName;    // JOIN용
    private String memberNickname; // JOIN용

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public static ReviewVO fromResultSet(ResultSet rs) throws SQLException {
        ReviewVO r = new ReviewVO();
        r.setReviewId(rs.getInt("r_id"));
        r.setReviewCompanyId(rs.getInt("r_company_id"));
        r.setReviewMemberId(rs.getInt("r_member_id"));
        r.setReviewTitle(rs.getString("r_title"));
        r.setReviewJobPosition(rs.getString("r_job_position"));
        r.setReviewInterviewType(rs.getString("r_interview_type"));
        r.setReviewDifficulty(rs.getInt("r_difficulty"));
        r.setReviewResult(rs.getString("r_result"));
        r.setReviewContent(rs.getString("r_content"));
        r.setReviewInterviewerCount(rs.getInt("r_interviewer_count"));
        r.setReviewStudentCount(rs.getInt("r_student_count"));
        r.setReviewAtmosphere(rs.getString("r_atmosphere"));
        r.setReviewContactMethod(rs.getString("r_contact_method"));
        r.setReviewContactDays(rs.getInt("r_contact_days"));
        r.setReviewViewCount(rs.getInt("r_view_count"));
        r.setReviewBookmarkCount(rs.getInt("r_bookmark_count"));
        r.setReviewLikeCount(rs.getInt("r_like_count"));
        r.setReviewCreatedDate(rs.getDate("r_created_date"));
        r.setReviewUpdatedDate(rs.getDate("r_updated_date"));
        try {
            r.setCompanyName(rs.getString("company_name"));
            r.setMemberNickname(rs.getString("member_nickname"));
        } catch (Exception e) {

        }

        return r;
    }


}
