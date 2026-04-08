package com.devdesk.pj.companysearch;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    public static CompanySearchVO fromRS(ResultSet rs) throws SQLException {
    CompanySearchVO company = new CompanySearchVO();
        company.setCompanyId(rs.getInt("company_id"));
        company.setCompanyName(rs.getString("company_name"));
        company.setCompanyIndustry(rs.getString("company_industry"));
        company.setCompanyLocation(rs.getString("company_location"));
        company.setCompanyRating(rs.getDouble("company_rating"));
        company.setCompanySize(rs.getInt("company_size"));
        company.setCompanyCreatedDate(rs.getDate("company_created_date"));
        company.setCompanyApplicationDate(rs.getDate("company_application_date"));

        return company;
    }



}
