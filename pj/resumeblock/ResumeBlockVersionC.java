package com.devdesk.pj.resumeblock;

import com.devdesk.pj.user.MemberDTO;
import com.google.gson.Gson;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ResumeBlockVersionC", value = "/resume-block-versions")
public class ResumeBlockVersionC extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        request.setCharacterEncoding("UTF-8");

        MemberDTO loginUser = (MemberDTO) request.getSession().getAttribute("user");
        if (loginUser == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        int blockId = Integer.parseInt(request.getParameter("blockId"));

        ResumeBlockDAO dao = new ResumeBlockDAO();
        ResumeBlockVO block = dao.selectBlock(blockId);
        List<ResumeBlockVersionVO> versions = dao.selectVersions(blockId);

        Map<String, Object> result = new HashMap<>();
        result.put("block", block);
        result.put("versions", versions);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new Gson().toJson(result));
    }
}
