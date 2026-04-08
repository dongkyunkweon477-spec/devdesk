package com.devdesk.pj.resumeblock;

import com.devdesk.pj.user.MemberDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ResumeBlockAddC", value = "/resume-block-add")
public class ResumeBlockAddC extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");

        MemberDTO loginUser = (MemberDTO) request.getSession().getAttribute("user");
        if (loginUser == null) {
            response.sendRedirect("login");
            return;
        }

        int memberId = loginUser.getMember_id();
        String categoryId = request.getParameter("categoryId");
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String tags = request.getParameter("tags");
        int charLimit = 400;
        try {
            charLimit = Integer.parseInt(request.getParameter("charLimit"));
        } catch (Exception ignored) {}

        ResumeBlockDAO dao = new ResumeBlockDAO();
        dao.insertBlock(memberId, categoryId, title, content, tags, charLimit);

        response.sendRedirect("resume-block");
    }
}
