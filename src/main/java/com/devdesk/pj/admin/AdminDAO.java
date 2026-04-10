package com.devdesk.pj.admin;

import com.devdesk.pj.main.DBManager_new;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class AdminDAO {

    // CREATE - 관리자 추가
    public static void addAdmin(HttpServletRequest request) {

        String sql = "INSERT INTO admin (admin_id, admin_name, admin_email, admin_password, admin_role, active_yn, created_date) " +
                    " VALUES (admin_seq.NEXTVAL, ?, ?, ?, ?, 'Y', SYSDATE)";

        try (Connection con = DBManager_new.connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            request.setCharacterEncoding("UTF-8");

            ps.setString(1, request.getParameter("admin_name"));
            ps.setString(2, request.getParameter("admin_email"));
            ps.setString(3, request.getParameter("admin_password"));
            ps.setString(4, request.getParameter("admin_role"));

            int result = ps.executeUpdate();

            if (result == 1) {
                System.out.println("Admin added successfully");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // READ - 전체 관리자 목록 조회
    public static ArrayList<AdminVO> getAllAdmins(HttpServletRequest request) {
        ArrayList<AdminVO> admins = new ArrayList<>();
        String sql = "SELECT * FROM admin ORDER BY admin_id DESC";

        try (Connection con = DBManager_new.connect();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                AdminVO admin = new AdminVO();
                admin.setAdmin_id(rs.getInt("admin_id"));
                admin.setAdmin_name(rs.getString("admin_name"));
                admin.setAdmin_email(rs.getString("admin_email"));
                admin.setAdmin_role(rs.getString("admin_role"));
                admin.setActive_yn(rs.getString("active_yn").charAt(0));
                admin.setCreated_date(rs.getString("created_date"));
                admin.setUpdated_date(rs.getString("updated_date"));
                admins.add(admin);
            }
            request.setAttribute("admins", admins);
            return admins;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // READ - 특정 관리자 조회
    public static AdminVO getAdminById(HttpServletRequest request) {
        AdminVO admin = null;
        String sql = "SELECT * FROM admin WHERE admin_id = ?";

        try (Connection con = DBManager_new.connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(request.getParameter("admin_id")));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    admin = new AdminVO();
                    admin.setAdmin_id(rs.getInt("admin_id"));
                    admin.setAdmin_name(rs.getString("admin_name"));
                    admin.setAdmin_email(rs.getString("admin_email"));
                    admin.setAdmin_role(rs.getString("admin_role"));
                    admin.setActive_yn(rs.getString("active_yn").charAt(0));
                    admin.setCreated_date(rs.getString("created_date"));
                    admin.setUpdated_date(rs.getString("updated_date"));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return admin;
    }

    // UPDATE - 관리자 정보 수정
    public static void updateAdmin(HttpServletRequest request) {

        String sql = "UPDATE admin SET admin_name = ?, admin_email = ?, admin_role = ?, active_yn = ?, updated_date = SYSDATE " +
                    " WHERE admin_id = ?";

        try (Connection con = DBManager_new.connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            request.setCharacterEncoding("UTF-8");

            ps.setString(1, request.getParameter("admin_name"));
            ps.setString(2, request.getParameter("admin_email"));
            ps.setString(3, request.getParameter("admin_role"));
            ps.setString(4, request.getParameter("active_yn"));
            ps.setInt(5, Integer.parseInt(request.getParameter("admin_id")));

            int result = ps.executeUpdate();

            if (result == 1) {
                System.out.println("Admin updated successfully");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DELETE - 관리자 삭제
    public static void deleteAdmin(HttpServletRequest request) {

        String sql = "DELETE FROM admin WHERE admin_id = ?";

        try (Connection con = DBManager_new.connect();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, Integer.parseInt(request.getParameter("admin_id")));

            int result = ps.executeUpdate();

            if (result == 1) {
                System.out.println("Admin deleted successfully");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
