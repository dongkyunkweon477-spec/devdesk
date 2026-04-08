package com.devdesk.pj.til;

import com.devdesk.pj.application.ApplicationDAO;
import com.devdesk.pj.user.MemberDTO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "TilC", value = "/til-list")
public class TilC extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        // ✅ null 체크 추가
        MemberDTO loginUser = (MemberDTO) request.getSession().getAttribute("user");

        if (loginUser == null) {
            // 로그인 안 된 상태면 로그인 페이지로 리다이렉트
            response.sendRedirect("login");
            return;
        }

        int memberId = loginUser.getMember_id();
        // ... 이하 동일


        // 2. 태그 필터 파라미터
        String tagFilter = request.getParameter("tag"); // null이면 전체

        // 3. DB에서 전체 목록 조회
        TilDAO dao = new TilDAO();
        List<TilV0> allList = dao.selectAllTils(memberId);
        System.out.println(allList);
        // ── 여기서부터 각 변수 계산 ──

        // totalCount
        int totalCount = allList.size();

        // totalHours
        double totalHours = 0;
        for (TilV0 t : allList) {
            totalHours += t.getStudyTime();
        }
        // 소수점 한 자리
        totalHours = Math.round(totalHours * 10) / 10.0;

        // avgHours
        double avgHours = totalCount > 0
                ? Math.round((totalHours / totalCount) * 10) / 10.0
                : 0;

        // tagStats (태그별 건수) — Map<String, Integer>
        Map<String, Integer> tagStats = new LinkedHashMap<>();
        for (TilV0 t : allList) {
            tagStats.put(t.getTag(), tagStats.getOrDefault(t.getTag(), 0) + 1);
        }

        // topTag (가장 많은 태그)
        String topTag = tagStats.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        // tagHours (태그별 학습 시간) — Map<String, Double>
        Map<String, Double> tagHours = new LinkedHashMap<>();
        for (TilV0 t : allList) {
            tagHours.put(t.getTag(),
                    Math.round((tagHours.getOrDefault(t.getTag(), 0.0) + t.getStudyTime()) * 10) / 10.0);
        }

        // maxHours (통계 바 최대값용)
        double maxHours = tagHours.values().stream()
                .mapToDouble(Double::doubleValue).max().orElse(1);


        // 5. setAttribute
        request.setAttribute("tilList", allList);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("totalHours", totalHours);
        request.setAttribute("avgHours", avgHours);
        request.setAttribute("topTag", topTag);
        request.setAttribute("tagStats", tagStats);
        request.setAttribute("tagHours", tagHours);
        request.setAttribute("maxHours", maxHours);

        request.setAttribute("content", "/til/til2.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }


    public void destroy() {
    }
}