package com.devdesk.pj.user;

import com.devdesk.pj.main.DBManager_new;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class MemberDAO {

    public static final MemberDAO MBAO = new MemberDAO();

    private  MemberDAO() {
    }

    public int idCheak(String email){
        int result = 0;
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from member where email = ?";

        try {

            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);

            pstmt.setString(1,email);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                result = rs.getInt(1);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            DBManager_new.close(con,pstmt,rs);
        }
        return result;
    }

    public void signUp(HttpServletRequest request) {




    }
}
