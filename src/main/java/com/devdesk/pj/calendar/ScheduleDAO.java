package com.devdesk.pj.calendar;

import com.devdesk.pj.main.DBManager_new;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;

public class ScheduleDAO {
    public static final ScheduleDAO SCAO = new ScheduleDAO();

    private ScheduleDAO() {}


    public ArrayList<ScheduleDTO> showAllSch(HttpServletRequest request) {

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from schedule";
        try {
            con = DBManager_new.connect();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();
            ScheduleDTO scheduleDTO = null;
            ArrayList<ScheduleDTO> schedules = new ArrayList<ScheduleDTO>();

            while (rs.next()) {
                String id = rs.getString("schedule_id");
                String member_id = rs.getString("member_id");
                String company_name = rs.getString("company_name");
                Date schedule_date = rs.getDate("schedule_date");
                String schedule_time = rs.getString("schedule_time");
                String memo = rs.getString("memo");
                Date created_date = rs.getDate("created_date");
                String google_event_id = rs.getString("google_event_id");
               

                schedules.add(scheduleDTO);
            }
            return schedules;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBManager_new.close(con, pstmt, rs);
        }
            return null;
    }
}
