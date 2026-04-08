package com.devdesk.pj.resumeblock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeBlockVO {
    private int blockId;
    private int memberId;
    private String categoryId;
    private String title;
    private String content;
    private String tags;        // 쉼표 구분 문자열
    private int isStar;         // 0 or 1
    private int charLimit;
    private String createdDate;
    private String updatedDate;
    private int latestVersion;  // 최신 버전 번호 (조회 시 계산)
}
