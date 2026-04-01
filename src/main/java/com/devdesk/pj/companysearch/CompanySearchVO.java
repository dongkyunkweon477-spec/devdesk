package com.devdesk.pj.companysearch;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanySearchVO {
    private int companyId;
    private String companyName;
    private String companyIndustry;
    private String companyLocation;
    private String companyWebsite;
    private String companyLogo;
    private String companyDescription;
    private double companyRating;

}
