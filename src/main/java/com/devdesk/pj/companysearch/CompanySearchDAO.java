package com.devdesk.pj.companysearch;

import com.devdesk.pj.main.DBManager_new;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class CompanySearchDAO {
    public static final CompanySearchDAO COMPANY_SEARCH_DAO = new CompanySearchDAO();

    private CompanySearchDAO() {

    }


    public List<String> companySearch(Map<String, String> conditions) {
        Set<String> allowedText = Set.of("company_name", "company_industry", "company_location");
        Set<String> allowedRange = Set.of("company_rating", "company_size");
        StringBuilder sql = new StringBuilder("select * from company where 1=1");
        List<Object> params = new ArrayList<>();

        for (String col : allowedText) {
            if (conditions.containsKey(col)) {
                sql.append(" and ").append(col).append(" like ?");
                params.add("%" + conditions.get(col) + "%");
            }

        }
        for (String col : allowedRange) {
            String minVal = conditions.get("min_" + col);
            String maxVal = conditions.get("max_" + col);
            if (minVal != null && !minVal.isBlank()) {
                sql.append(" and ").append(col).append(">= ?");
                params.add(Double.parseDouble(minVal));
            }
            if (maxVal != null && !maxVal.isBlank()) {
                sql.append(" and ").append(col).append("<= ?");
                params.add(Double.parseDouble(maxVal));
            }
        }

        List<String> companies = new ArrayList<>();
        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql.toString())
        ) {
            for (int i = 0; i < params.size(); i++) {
                pstmt.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = pstmt.executeQuery()) {
                CompanySearchVO company = new CompanySearchVO();
                while (rs.next()) {
                    company = CompanySearchVO.fromRS(rs);
                    companies.add(company.toJson());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return companies;
    }

    public CompanySearchVO getCompanyById(int companyId) {
        String sql = "select * from company where company_id = ? ";
        try (
                Connection con = DBManager_new.connect();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setInt(1, companyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return CompanySearchVO.fromRS(rs);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
