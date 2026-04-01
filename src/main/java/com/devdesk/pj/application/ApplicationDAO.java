package com.devdesk.pj.application;

import com.devdesk.pj.main.DBManager_new;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class ApplicationDAO {



    public static void Movie(HttpServletRequest request) {
            Connection con=null;
            PreparedStatement pstmt = null;
            String sql= "insert into movie_test values (movie_test_seq.nextval,?,?,?,?)";
            String path = "C:/kd/dbws_intellij/upload/movieFile";

            try {
                con= DBManager_new.connect();
                pstmt=con.prepareStatement(sql);

                MultipartRequest mr =new MultipartRequest(request, path,1024*1024*20,"utf-8",new DefaultFileRenamePolicy());

                String title = mr.getParameter("title");
                String actor = mr.getParameter("actor");
                String story = mr.getParameter("story");
                String fileName = mr.getFilesystemName("file");

                System.out.println(title);
                System.out.println(actor);
                System.out.println(story);
                System.out.println(fileName);

                story = story.replaceAll("\r\n","<br>");




                pstmt.setString(1,title);
                pstmt.setString(2,actor);
                pstmt.setString(3,fileName);
                pstmt.setString(4,story);

                if (pstmt.executeUpdate()==1){
                    System.out.println("add success");
                }



            }catch (Exception e){
                e.printStackTrace();

            }finally {
                DBManager_new.close(con,pstmt,null);
            }
    }
}


