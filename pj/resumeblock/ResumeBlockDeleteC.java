package com.devdesk.pj.resumeblock;

import com.devdesk.pj.user.MemberDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ResumeBlockDeleteC", value = "/resume-block-delete")
public class ResumeBlockDeleteC extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");

        MemberDTO loginUser = (MemberDTO) request.getSession().getAttribute("user");
        if (loginUser == null) {
            response.sendRedirect("login");
            return;
        }

        int blockId = Integer.parseInt(request.getParameter("blockId"));

        ResumeBlockDAO dao = new ResumeBlockDAO();
        dao.deleteBlock(blockId);

        response.sendRedirect("resume-block");
    }
}
