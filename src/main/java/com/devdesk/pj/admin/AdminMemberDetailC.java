package com.devdesk.pj.admin;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/admin/memberDetail")
public class AdminMemberDetailC extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int memberId = Integer.parseInt(request.getParameter("member_id"));

        // DAO에서 맵 형태로 데이터 가져오기
        Map<String, Object> detail = AdminDAO.ADAO.getMemberDetail(memberId);

        // JSON으로 변환해서 응답 (Gson 라이브러리 사용 중이시죠?)
        response.setContentType("application/json;charset=UTF-8");
        new Gson().toJson(detail, response.getWriter());
    }
}