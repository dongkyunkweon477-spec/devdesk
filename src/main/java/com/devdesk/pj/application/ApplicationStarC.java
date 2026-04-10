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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        MemberDTO loginUser = (MemberDTO) request.getSession().getAttribute("user");
        if (loginUser == null) {
            response.sendRedirect("login");
            return;
        }


        ApplicationDAO.selectStarApplication(request);
        ApplicationDAO.selectAllCompanies(request);
        request.setAttribute("content", "/application/application_list.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        MemberDTO loginUser = (MemberDTO) request.getSession().getAttribute("user");
        if (loginUser == null) {
            response.sendRedirect("login");
            return;
        }

        ApplicationDAO.selectAllCompanies(request);
        ApplicationDAO.starApplication(request);
        response.sendRedirect("application-list");
    }


}
