package com.devdesk.pj.calendar;

import com.devdesk.pj.main.DBManager_new;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ScheduleDAO {
    public static final ScheduleDAO SCAO = new ScheduleDAO();

    private ScheduleDAO() {}


    public ArrayList<ScheduleDTO> showAllSch() {

        String sql = "select * from schedule ";
                            // 작업끝나면 sql에 추가할게요=> where id = ?

        try (Connection con = DBManager_new.connect();
             PreparedStatement pstmt = con.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            ArrayList<ScheduleDTO> schedules = new ArrayList<>();

            while (rs.next()) {
                String id = rs.getString("schedule_id");
                String member_id = rs.getString("member_id");
                String company_name = rs.getString("company_name");
                java.sql.Date schedule_date = rs.getDate("schedule_date");
                String schedule_time = rs.getString("schedule_time");
                String interview_type = rs.getString("interview_type");
                String memo = rs.getString("memo");
                java.sql.Date created_date = rs.getDate("created_date");
                String google_event_id = rs.getString("google_event_id");

                ScheduleDTO schedule = new ScheduleDTO(
                        id, member_id, company_name, schedule_date, schedule_time,
                        interview_type, memo, created_date, google_event_id
                );

                schedules.add(schedule);
            }
            return schedules;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
