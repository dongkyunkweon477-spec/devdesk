package com.devdesk.pj.report;

import java.sql.Date;

public class ReportVO {
    private int reportId;
    private int repoReviewId;
    private int repoMemberId;
    private String repoReason;
    private String repoContent;
    private String repoStatus;
    private Date reopCreated;

    public ReportVO() {}

    public int getReportId() { return reportId; }
    public void setReportId(int reportId) { this.reportId = reportId; }
    public int getRepoReviewId() { return repoReviewId; }
    public void setRepoReviewId(int repoReviewId) { this.repoReviewId = repoReviewId; }
    public int getRepoMemberId() { return repoMemberId; }
    public void setRepoMemberId(int repoMemberId) { this.repoMemberId = repoMemberId; }
    public String getRepoReason() { return repoReason; }
    public void setRepoReason(String repoReason) { this.repoReason = repoReason; }
    public String getRepoContent() { return repoContent; }
    public void setRepoContent(String repoContent) { this.repoContent = repoContent; }
    public String getRepoStatus() { return repoStatus; }
    public void setRepoStatus(String repoStatus) { this.repoStatus = repoStatus; }
    public Date getReopCreated() { return reopCreated; }
    public void setReopCreated(Date reopCreated) { this.reopCreated = reopCreated; }
}
