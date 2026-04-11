package com.devdesk.pj.auth;

import com.devdesk.pj.user.MemberDTO;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

// 🌟 포인트: urlPatterns에 "/admin/*"을 주면, 앞으로 만들 /admin/member 등
// admin으로 시작하는 모든 주소를 일일이 적지 않아도 알아서 철통 방어합니다!
@WebFilter(urlPatterns = {"/admin", "/admin/*"})
public class AdminCheckFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession();
        MemberDTO user = (MemberDTO) session.getAttribute("user");

        // 🛑 1차 관문: 아예 로그인을 안 한 사람
        if (user == null) {
            System.out.println("어드민 필터: 비로그인 사용자 접근 차단");
            res.sendRedirect(req.getContextPath() + "/login");
            return; // 🌟 핵심: 여기서 return을 안 쓰면 아래 코드로 질주해버려서 에러가 납니다! 흐름을 딱 끊어줍니다.
        }

        // 🛑 2차 관문: 로그인은 했는데 'admin'이 아닌 일반 'user'인 사람
        if (!"admin".equals(user.getRole())) {
            System.out.println("어드민 필터: 권한 없는 일반 유저 접근 차단 (" + user.getEmail() + ")");

            // 자바스크립트로 경고창(alert)을 띄우고 메인 화면으로 강제 이동시킵니다.
            res.setContentType("text/html; charset=UTF-8");
            PrintWriter out = res.getWriter();
            out.println("<script>");
            out.println("alert('관리자만 접근할 수 있는 페이지입니다. 👮‍♂️');");
            out.println("location.href='" + req.getContextPath() + "/';");
            out.println("</script>");
            out.flush();
            return; // 🌟 흐름 차단
        }

        // ✅ 무사 통과: 진짜 관리자 확인 완료! 페이지를 보여줍니다.
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}