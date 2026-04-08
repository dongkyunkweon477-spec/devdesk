package com.devdesk.pj.auth; // 본인의 패키지 경로에 맞게 수정하세요!

import com.devdesk.pj.user.MemberDTO; // DTO 임포트

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(urlPatterns = {
        "/mypage",
        "/profile-update",
        "/review/write",
        "/review/edit",
        "/review/delete",
        "/company/insert",
        "/company/edit",
        "/company/delete"

        //⬇️ 이런 식으로 본인이 맡은 주소를 계속 추가하면 됩니다!
        //"/board-write",      게시글 쓰기 (예시)
        //"/board-update",     게시글 수정 (예시)
        //"/comment-write"     댓글 달기 (예시)
})
public class LoginCheckFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        HttpSession session = req.getSession();
        MemberDTO user = (MemberDTO) session.getAttribute("user");

        if (user != null) {
            chain.doFilter(request, response);
        } else {
            System.out.println("필터 작동: 비로그인 사용자 접근 차단");
            res.sendRedirect(req.getContextPath() + "/login");
        }
    }

    @Override
    public void destroy() {
    }
}