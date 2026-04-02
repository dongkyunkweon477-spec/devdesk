package com.devdesk.pj.calendar;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.servlet.annotation.WebServlet;
import java.util.Date;

@Data
@AllArgsConstructor
@WebServlet

public class ScheduleDTO {
    private int schedule_ID;
    private int member_ID;
    private String company_name;
    private Date schedule_date;
    private String schedule_time;
    private String interview_type;
    private String memo;
    private Date created_date;

}
