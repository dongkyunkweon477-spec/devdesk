package com.devdesk.pj.admin;

import lombok.Data;

@Data
public class AdminVO {
    private int admin_id;
    private String admin_name;
    private String admin_email;
    private String admin_password;
    private String admin_role;
    private char active_yn;
    private String created_date;
    private String updated_date;
}
