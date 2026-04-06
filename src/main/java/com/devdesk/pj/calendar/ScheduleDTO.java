package com.devdesk.pj.calendar;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.servlet.annotation.WebServlet;
import java.util.Date;

@Data
@AllArgsConstructor

public class ScheduleDTO {
    private String scheduleID;
    private String member_ID;
    private String company_name;
    private java.sql.Date schedule_date;
    private String schedule_time;
    private String interview_type;
    private String memo;
    private java.sql.Date created_date;
    private String google_event_id;
}
