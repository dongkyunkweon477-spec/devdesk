package com.devdesk.pj.companysearch;

import com.devdesk.pj.main.DBManager_new;

import java.sql.Connection;
import java.sql.Date;
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

    public List<String> getAllIndustries() {
        String sql = "select distinct company_industry from company order by company_industry";
        List<String> list = new ArrayList<>();
        try (
                Connection con = DBManager_new.connect();
                PreparedStatement pstmt = con.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
        ) {
            while (rs.next()) {
                list.add(rs.getString("company_industry"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<String> getAllLocation() {
        String sql = "select distinct SUBSTR(company_location, 1, INSTR(company_location, ' ') - 1) AS region " +
                "from company where company_location is not null " +
                "order by region";
        List<String> list = new ArrayList<>();
        try (
                Connection con = DBManager_new.connect();
                PreparedStatement pstmt = con.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery();
        ) {
            while (rs.next()) {
                String region = rs.getString("region");
                if (region != null && !region.isBlank()) {
                    list.add(region);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int insertCompany(CompanySearchVO vo) {
        String sql = "insert into company(company_id, company_name, company_industry, " +
                "company_location, company_rating, company_size, company_created_date, company_application_date) " +
                "values(company_seq.nextval, ?, ?, ?, ?, ?, ?, ?)";
        try (
                Connection con = DBManager_new.connect();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setString(1, vo.getCompanyName());
            pstmt.setString(2, vo.getCompanyIndustry());
            pstmt.setString(3, vo.getCompanyLocation());
            pstmt.setDouble(4, vo.getCompanyRating());
            pstmt.setInt(5, vo.getCompanySize());
            pstmt.setDate(6, (Date) vo.getCompanyCreatedDate());
            pstmt.setDate(7, (Date) vo.getCompanyApplicationDate());

            if (pstmt.executeUpdate() > 0) {
                System.out.println("Company insert success");
                return 1;
            } else {
                System.out.println("Company insert fail");
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    public void deleteCompany(int companyId) {
        String sqlDelRev = "delete from review where r_company_id = ?";
        String sqlDelComp = "delete from company where company_id = ?";
        try (
                Connection con = DBManager_new.connect();
        ) {
            try (PreparedStatement pstmt = con.prepareStatement(sqlDelRev)) {
                pstmt.setInt(1, companyId);
                pstmt.executeUpdate();
            }
            try (PreparedStatement pstmt = con.prepareStatement(sqlDelComp)) {
                pstmt.setInt(1, companyId);
                pstmt.executeUpdate();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCompany(CompanySearchVO vo) {
        String sql = "update company set company_name=?, company_industry=?, " +
                "company_location=?, company_rating=?, company_size=? , company_application_date=? " +
                "where company_id=?";
        try (
                Connection con = DBManager_new.connect();
                PreparedStatement pstmt = con.prepareStatement(sql);
        ) {
            pstmt.setString(1, vo.getCompanyName());
            pstmt.setString(2, vo.getCompanyIndustry());
            pstmt.setString(3, vo.getCompanyLocation());
            pstmt.setDouble(4, vo.getCompanyRating());
            pstmt.setInt(5, vo.getCompanySize());
            pstmt.setDate(6, (Date) vo.getCompanyApplicationDate());
            pstmt.setInt(7, vo.getCompanyId());

            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, Object> getCompanyStats(int companyId) {
        String sql = "select count(*) as total_count," +
                " round(avg(r_difficulty),1) as avg_difficulty," +
                " round(count(case when r_result = 'PASS' then 1 end) * 100.0 / count(*), 1) as pass_rate" +
                " from review where r_company_id = ?";
        Map<String, Object> stats = new HashMap<>();
        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, companyId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalCount", rs.getInt("total_count"));
                    stats.put("avgDifficulty", rs.getDouble("avg_difficulty"));
                    stats.put("passRate", rs.getDouble("pass_rate"));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return stats;

    }
}
