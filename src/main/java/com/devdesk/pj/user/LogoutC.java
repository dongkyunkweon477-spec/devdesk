package com.devdesk.pj.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "LogoutC", value = "/logout")
public class LogoutC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        // 1. 세션 사물함에서 'user'라는 이름표를 단 로그인 정보만 쏙 빼서 버립니다.
        request.getSession().removeAttribute("user");
        System.out.println("👋 로그아웃 완료");

        // 2. 로그아웃이 끝났으니, 메인 홈("/")으로 튕겨냅니다.
        response.sendRedirect(request.getContextPath() + "/");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

    }

    public void destroy() {

    }
}