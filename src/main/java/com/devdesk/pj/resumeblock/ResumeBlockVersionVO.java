package com.devdesk.pj.resumeblock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResumeBlockVersionVO {
    private int versionId;
    private int blockId;
    private int versionNum;
    private String content;
    private String createdDate;
}
