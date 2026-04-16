package com.devdesk.pj.report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;

@NoArgsConstructor
@Setter
@AllArgsConstructor
@Getter
public class ReportVO {
    private int reportId;
    private int repoReviewId;   // REVIEW_ID (리뷰 신고 시)
    private int repoBoardId;    // BOARD_ID  (게시글 신고 시)
    private int repoMemberId;   // MEMBER_ID (신고자)
    private String repoReason;
    private String repoContent;
    private String repoStatus;
    private Date repoCreated;   // REPO_CREATED_DATE
}
