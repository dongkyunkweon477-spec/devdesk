package com.devdesk.pj.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "AccountC", value = "/account")
public class AccountC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        request.setAttribute("content", "/user/account.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        boolean isSuccess = MemberDAO.MBAO.signUp(req);

        if (isSuccess) {
            System.out.println("회원가입 성공!");

            // 💡 추가 1: 화면에 "ㅇㅇㅇ님"이라고 띄워주기 위해 방금 입력한 닉네임을 바구니에 담습니다.
            req.setAttribute("welcomeName", req.getParameter("nickname"));

            // 💡 추가 2: 성공 화면(accountSuccess.jsp) 알맹이를 세팅해서 껍데기(index.jsp)로 보냅니다!
            req.setAttribute("content", "/user/accountSuccess.jsp");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);

        } else {
            System.out.println("회원가입 실패");
            // ... (기존 실패 로직 그대로 유지)
        }


    }

    public void destroy() {

    }
}