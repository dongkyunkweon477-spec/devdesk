package com.devdesk.pj.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MemberDTO {

    private int member_id;
    private String email;
    private String password;
    private String nickname;
    private String job_category;
    private String login_type;
    private String social_id;
    private String role;
    private String status;
    private Date created_date;
    private Date updated_date;

}
