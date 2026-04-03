package com.devdesk.pj.application;

import lombok.Data;

import java.util.Date;

@Data

public class ApplicationV0 {
    private String appId;
    private String companyName;
    private String companyId;
    private String position;
    private String status;
    private Date appDate;
    private String memo;
<<<<<<< HEAD
    private String statusName;
=======
    private Date interviewDate;
    private String interviewTime;
    private String interviewType;
>>>>>>> 8e3dec9e009eb56abe2dde18de6feee9e3e6b2e8
}
