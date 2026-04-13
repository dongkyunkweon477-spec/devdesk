package com.devdesk.pj.user;

import com.devdesk.pj.main.RecaptchaUtil;

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

        String token = req.getParameter("g-recaptcha-response");
        if (!RecaptchaUtil.verify(token)) {
            req.setAttribute("msg", "보안 인증에 실패했습니다. 다시 시도해주세요.");
            req.setAttribute("content", "/user/account.jsp");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        boolean isSuccess = MemberDAO.MBAO.signUp(req);

        if (isSuccess) {
            System.out.println("✅ 회원가입 성공!");

            // 💡 중요: sendRedirect로 튕겨내면 request 바구니가 증발합니다!
            // 그래서 닉네임을 URL 뒤에 파라미터로 몰래 달아서 보냅니다. (?name=홍길동)
            String encodedName = java.net.URLEncoder.encode(req.getParameter("nickname"), "UTF-8");

            // 영은 님 아이디어대로 새로운 주소(/account-done)로 튕겨냅니다!
            resp.sendRedirect(req.getContextPath() + "/account-done?name=" + encodedName);

        } else {
            System.out.println("회원가입 실패");
            // ... (기존 실패 로직 그대로 유지)
        }


    }

    public void destroy() {

    }
}