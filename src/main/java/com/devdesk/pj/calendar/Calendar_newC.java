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

        MemberDTO user = (MemberDTO) request.getSession().getAttribute("user");
        int memberId =user.getMember_id(); // 기본값 (추후 삭제할게여)

        if (user != null) {
            memberId = user.getMember_id();
        }
        ArrayList<Schedule_newDTO> schList = Schedule_newDAO.SCAO.getCalendarEvents(memberId);
        request.setAttribute("list", schList);

        ArrayList<String> companyList = Schedule_newDAO.SCAO.getAllCompanyNames();
        request.setAttribute("companyList", companyList);

        request.setAttribute("content", "calendar/index_cal.jsp");
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}