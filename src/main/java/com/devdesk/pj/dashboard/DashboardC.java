package com.devdesk.pj.dashboard;

import com.devdesk.pj.til.TilDAO;
import com.devdesk.pj.til.TilTagStatVO;
import com.devdesk.pj.til.TilV0;
import com.devdesk.pj.user.MemberDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet("/dashboard")
public class DashboardC extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        List<TilTagStatVO> tilTagStats = new ArrayList<>();
        TilTagStatVO s = new TilTagStatVO();
        s.setTag("SQL");
        s.setColor("#4ecdc4");
        s.setPct(40);
        tilTagStats.add(s);

        request.setAttribute("tilTagStats", tilTagStats);

        // TAG_CONFIG와 동일한 색상 매핑
        Map<String, String> tagColorMap = new HashMap<>();
        tagColorMap.put("Java", "#ff9f69");
        tagColorMap.put("Spring", "#56e39f");
        tagColorMap.put("SQL", "#4ecdc4");
        tagColorMap.put("JavaScript", "#ffd166");
        tagColorMap.put("Git", "#ff6b6b");
        tagColorMap.put("Python", "#5b7cf8");
        tagColorMap.put("CSS", "#8b6ef5");
        tagColorMap.put("React", "#4ecdc4");
        tagColorMap.put("기타", "#9da3b8");

        Map<String, String> tagBgMap = new HashMap<>();
        tagBgMap.put("Java", "rgba(255,159,105,0.12)");
        tagBgMap.put("Spring", "rgba(86,227,159,0.12)");
        tagBgMap.put("SQL", "rgba(78,205,196,0.12)");
        tagBgMap.put("JavaScript", "rgba(255,209,102,0.12)");
        tagBgMap.put("Git", "rgba(255,107,107,0.12)");
        tagBgMap.put("Python", "rgba(91,124,248,0.12)");
        tagBgMap.put("CSS", "rgba(139,110,245,0.12)");
        tagBgMap.put("React", "rgba(78,205,196,0.12)");
        tagBgMap.put("기타", "rgba(157,163,184,0.12)");

// recentTils 가공
        TilDAO tilDao = new TilDAO();
        MemberDTO loginUser = (MemberDTO) request.getSession().getAttribute("user");
        int memberId = loginUser.getMember_id();
        List<TilV0> rawTils = tilDao.getRecentTils(memberId, 5);

// TilVO에 tagColor, tagBg, timeAgo 필드 추가 필요
        for (TilV0 t : rawTils) {
            t.setTagColor(tagColorMap.getOrDefault(t.getTag(), "#9da3b8"));
            t.setTagBg(tagBgMap.getOrDefault(t.getTag(), "rgba(157,163,184,0.12)"));
            t.setTimeAgo(calcTimeAgo(t.getCreatedAt())); // 아래 메서드 참고
        }


        request.setAttribute("recentTils", rawTils);


        DashboardDAO.countGroupbystage(request);
        DashboardDAO.getFunnelData(DashboardDAO.countGroupbystage(request));
        List<Map<String, Object>> funnelData = DashboardDAO.getFunnelData(DashboardDAO.countGroupbystage(request));
        request.setAttribute("funnelData", funnelData);
        request.setAttribute("content", "/dashboard/dashboard.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);

    }

    private String calcTimeAgo(String createdAt) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date created = sdf.parse(createdAt);
            long diff = System.currentTimeMillis() - created.getTime();
            long days = diff / (1000 * 60 * 60 * 24);

            if (days == 0) return "오늘";
            if (days == 1) return "어제";
            if (days < 7) return days + "일 전";
            if (days < 30) return (days / 7) + "주 전";
            return (days / 30) + "개월 전";
        } catch (Exception e) {
            return createdAt;
        }
    }


    public void destroy() {
    }
}