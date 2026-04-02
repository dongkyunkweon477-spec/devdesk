package com.devdesk.pj.companysearch;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompanySearchVO {
    private int companyId;
    private String companyName;
    private String companyIndustry;
    private String companyLocation;
    private double companyRating;
    private Date companyCreatedDate;
    private Date companyApplicationDate;
    private int companySize;

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }



}
