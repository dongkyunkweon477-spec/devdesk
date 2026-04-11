package com.devdesk.pj.admin;

import lombok.Data;

@Data
public class AdminVO {
    private int member_id;
    private String email;
    private String nickname;
    private String job_category;
    private String login_type;
    private String role;
    private String status;
    private String created_date;
}