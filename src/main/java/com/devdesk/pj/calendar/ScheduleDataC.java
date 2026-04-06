package com.devdesk.pj.calendar;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/getScheduleData") // AJAX가 호출할 주소
public class ScheduleDataC extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // 1. DB에서 데이터 가져오기 (기존 DAO 사용)
        ArrayList<ScheduleDTO> list = ScheduleDAO.SCAO.showAllSch();

        // 2. 응답 형식을 JSON으로 설정
        response.setContentType("application/json; charset=UTF-8");

        // 3. 자바 리스트를 JSON 문자열로 변환 (Gson 사용)
        Gson gson = new Gson();
        String jsonResponse = gson.toJson(list);

        // 4. 데이터 쏴주기!
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
}