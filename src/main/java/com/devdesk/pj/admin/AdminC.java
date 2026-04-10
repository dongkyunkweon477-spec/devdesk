package com.devdesk.pj.admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "AdminC", value = "/admin")
public class AdminC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // 🌟 1. DAO에서 회원 리스트 가져오기 (최신 회원 목록용)
        List<AdminVO> members = AdminDAO.ADAO.getAllMembers();
        request.setAttribute("members", members);

        // 🌟 2. [추가] 차트 및 요약 카드 데이터 조회 (나중에 DB에서 진짜 숫자를 가져와야 합니다)
        // [테스트] 일단 더미 데이터를 넣습니다!
        request.setAttribute("totalMembers", 1245);
        request.setAttribute("totalBoards", 340);
        request.setAttribute("todayNewMembers", 12);

        // [테스트] 가입자 트렌드 더미 데이터 (최근 7일)
        int[] memberTrend = {12, 18, 15, 20, 22, 19, 21};
        request.setAttribute("memberTrend", memberTrend);

        // [테스트] 직무 분포 더미 데이터 (백엔드, 프론트엔드, AI, 기타)
        int[] jobDistribution = {400, 300, 150, 145};
        request.setAttribute("jobDistribution", jobDistribution);

        // 3. index.jsp의 <jsp:include page="${content}"> 부분에 들어갈 jsp 경로 지정!
        request.setAttribute("content", "admin/admin.jsp");

        // 4. 최종적으로 index.jsp로 이동!
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }


    public void destroy() {
    }
}