package com.devdesk.pj.board;

import com.devdesk.pj.main.DBManager_new;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class BoardDAO {
    public static void addBoard(HttpServletRequest request) {
        Connection con = null;
        PreparedStatement ps = null;
        String sql = "insert into board values ()";

        try {
            con = DBManager_new.connect();
            ps = con.prepareStatement(sql);


        }catch (Exception e){
            e.printStackTrace();
        }finally {
            DBManager_new.close(con,ps,null);
        }
    }
}
