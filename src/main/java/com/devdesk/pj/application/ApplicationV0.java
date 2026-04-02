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
}
