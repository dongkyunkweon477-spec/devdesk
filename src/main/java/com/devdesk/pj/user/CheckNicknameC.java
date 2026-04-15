package com.devdesk.pj.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 닉네임 중복 확인 컨트롤러
 * POST /user-checkNickname
 * → 응답: "1" (이미 사용 중) | "0" (사용 가능)
 * <p>
 * 기존 이메일 중복확인(CheckIdC) 패턴과 동일한 구조
 */
@WebServlet(name = "CheckNicknameC", value = "/user-checkNickname")
public class CheckNicknameC extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String nickname = request.getParameter("nickname");
        int result = MemberDAO.MBAO.nicknameCheck(nickname); // 0 = 없음, 1 = 중복

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println(result);
    }
}