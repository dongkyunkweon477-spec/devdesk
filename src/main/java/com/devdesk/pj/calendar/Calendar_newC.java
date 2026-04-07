package com.devdesk.pj.calendar;
import com.devdesk.pj.user.MemberDTO;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "calendarC", value = "/calendar")
public class Calendar_newC extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        // 1. 필터가 검증해 줬으니 안심하고 상자에서 바로 번호를 꺼냅니다! (보험 6번 삭제, null 검사 삭제!)
        MemberDTO user = (MemberDTO) request.getSession().getAttribute("user");
        int memberId = user.getMember_id();

        // 2. 일정과 회사 목록 가져오기
        ArrayList<Schedule_newDTO> schList = Schedule_newDAO.SCAO.getCalendarEvents(memberId);
        request.setAttribute("list", schList);

        ArrayList<String> companyList = Schedule_newDAO.SCAO.getAllCompanyNames();
        request.setAttribute("companyList", companyList);

        // 3. 화면 띄우기
        request.setAttribute("content", "calendar/index_cal.jsp");
        request.getRequestDispatcher("index.jsp").forward(request, response);
}
}