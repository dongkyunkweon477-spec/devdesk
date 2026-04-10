package com.devdesk.pj.application;

import com.devdesk.pj.resumeblock.ResumeBlockDAO;
import com.devdesk.pj.resumeblock.ResumeBlockVO;
import com.devdesk.pj.user.MemberDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ApplicationStarC", value = "/application-star")
public class ApplicationStarC extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        MemberDTO loginUser = (MemberDTO) request.getSession().getAttribute("user");
        if (loginUser == null) {
            response.sendRedirect("login");
            return;
        }

        int memberId = loginUser.getMember_id();
        String filter = request.getParameter("filter"); // all, star, shimei, jikopr, ...

        ApplicationDAO.filterApplication(request);
        ApplicationDAO.selectAllCompanies(request);
        request.setAttribute("content", "/application/application_list.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}
