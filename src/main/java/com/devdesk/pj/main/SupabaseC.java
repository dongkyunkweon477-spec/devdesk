package com.devdesk.pj.main;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/supa-upload")
@MultipartConfig
public class SupabaseC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("--supa --");
        resp.setContentType("text/html;charset=UTF-8");
        resp.getWriter().println(SupabaseDAO.upload(req, resp));
<<<<<<< HEAD
        // resp.getWriter().println(SupabaseDAO.SUPADAO.upload(req, resp));
=======
>>>>>>> e60537aaa883d4736088d00fdd47701a2b844ce1
    }

    public void destroy() {
    }
}
