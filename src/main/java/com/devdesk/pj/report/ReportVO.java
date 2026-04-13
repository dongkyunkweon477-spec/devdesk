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
    private int repoReviewId;
    private int repoMemberId;
    private String repoReason;
    private String repoContent;
    private String repoStatus;
    private Date reopCreated;

}
