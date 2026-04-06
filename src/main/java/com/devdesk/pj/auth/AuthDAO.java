package com.devdesk.pj.auth;

import com.devdesk.pj.user.MemberDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class AuthDAO {

    public static final AuthDAO ADAO = new AuthDAO();

    // 2. 다른 곳에서 new AuthDAO()를 못 하게 막아둡니다.
    private AuthDAO() {
    }

    public boolean isLoggedIn(HttpServletRequest request) {
        HttpSession hs = request.getSession();
        MemberDTO user = (MemberDTO) hs.getAttribute("user");

        return user != null;
    }
}



