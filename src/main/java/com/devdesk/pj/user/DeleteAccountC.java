package com.devdesk.pj.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(name = "DeleteAccountC", value = "/delete-account")
public class DeleteAccountC extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        MemberDTO user = (MemberDTO) session.getAttribute("user");

        if (user != null) {
            // 🌟 사용자가 모달창에 입력한 비밀번호 가져오기
            String inputPassword = request.getParameter("confirm_password");

            // DAO 실행 (1: 성공, 0: 비번틀림, -1: 에러)
            int result = MemberDAO.MBAO.deleteAccount(user.getMember_id(), inputPassword);

            if (result == 1) {
                // 성공: 세션 파기 후 메인으로 이동
                session.invalidate();
                response.sendRedirect("index.jsp?msg=account_deleted");
            } else if (result == 0) {
                // 비번 틀림: 에러 메시지를 담아서 마이페이지로 다시 포워딩
                request.setAttribute("errorMsg", "비밀번호가 일치하지 않습니다.");
                request.setAttribute("content", "user/mypage.jsp");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            } else {
                // 기타 에러
                request.setAttribute("errorMsg", "탈퇴 처리에 실패했습니다. 관리자에게 문의하세요.");
                request.setAttribute("content", "user/mypage.jsp");
                request.getRequestDispatcher("/index.jsp").forward(request, response);
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }
}