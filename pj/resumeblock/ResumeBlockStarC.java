package com.devdesk.pj.resumeblock;

import com.devdesk.pj.user.MemberDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ResumeBlockStarC", value = "/resume-block-star")
public class ResumeBlockStarC extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");

        MemberDTO loginUser = (MemberDTO) request.getSession().getAttribute("user");
        if (loginUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        int blockId = Integer.parseInt(request.getParameter("blockId"));

        ResumeBlockDAO dao = new ResumeBlockDAO();
        dao.toggleStar(blockId);

        // AJAX 요청이면 JSON 응답
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"success\":true}");
    }
}
