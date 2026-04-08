package com.devdesk.pj.dashboard;

import lombok.Data;

@Data

public class DashboardScheduleVO {
    private String month;    // "APR"
    private String day;      // "08"
    private String company;  // "카카오"
    private String time;     // "14:00"
    private String type;     // "1차 면접"
    private String badgeBg;
    private String badgeColor;
    private boolean isToday;
}
