package com.devdesk.pj.user;

<<<<<<< HEAD
//import com.devdesk.pj.main.RecaptchaUtil;
=======
// import com.devdesk.pj.main.RecaptchaUtil;

import com.devdesk.pj.main.RecaptchaUtil;
>>>>>>> 47913c593aa4770f5f941787c5393a39c66befad

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "loginC", value = "/login")
public class loginC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        request.setAttribute("content", "user/login.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

<<<<<<< HEAD
//        String token = request.getParameter("g-recaptcha-response");
=======
        String token = request.getParameter("g-recaptcha-response");
>>>>>>> 47913c593aa4770f5f941787c5393a39c66befad
//        System.out.println("[reCAPTCHA] token: [" + token + "]");
//        if (!RecaptchaUtil.verify(token)) {
//            request.setAttribute("msg", "보안 인증에 실패했습니다. 다시 시도해주세요.");
//            request.setAttribute("content", "user/login.jsp");
//            request.getRequestDispatcher("/index.jsp").forward(request, response);
//            return;
//        }

        MemberDAO.MBAO.login(request);

        HttpSession session = request.getSession();

        if (session.getAttribute("user") != null) {

            String dest = (String) session.getAttribute("dest");

            if (dest != null) {
                session.removeAttribute("dest");
                response.sendRedirect(dest);
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }

        } else {
            request.setAttribute("content", "user/login.jsp");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    public void destroy() {
    }
}