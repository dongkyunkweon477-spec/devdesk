package com.devdesk.pj.admin;

import lombok.Data;

@Data
public class AdminCompanyVO {
    private int company_id;
    private String company_name;
    private String company_industry;
    private String company_location;
    private double company_rating;
    private int company_size;
    private String is_verified;         // 'Y' = 승인, 'N' = 미승인(대기)
    private String company_created_date;
    private int review_count;           // 리뷰 수 (JOIN)
    private String registered_by;       // 등록 경로 구분용 (선택)
}