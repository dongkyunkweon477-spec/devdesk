package com.devdesk.pj.review;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReviewVO {
    private int reviewId;
    private int reviewCompanyId;
    private int reviewMemberId;
    private String reviewJobPosition;
    private String reviewInterviewType;
    private int reviewDifficulty;
    private String reviewResult;
    private String reviewContent;
    private int reviewViewCount;
    private Date reviewCreatedDate;
    private Date reviewUpdatedDate;
}
