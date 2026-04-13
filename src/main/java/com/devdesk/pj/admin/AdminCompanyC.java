package com.devdesk.pj.admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/admin/company")
public class AdminCompanyC extends HttpServlet {

    private static final int PAGE_SIZE = 10; // 한 페이지에 보여줄 기업 수

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // 필터: "Y"=승인됨, "N"=승인대기, ""=전체
        String filter = request.getParameter("filter");
        if (filter == null) filter = "";

        // 기업명 검색 키워드
        String keyword = request.getParameter("keyword");
        if (keyword == null) keyword = "";

        // 현재 페이지 (기본 1페이지)
        int currentPage = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null && !pageParam.isBlank()) {
            try {
                currentPage = Integer.parseInt(pageParam);
            } catch (Exception e) {
                currentPage = 1;
            }
        }
        if (currentPage < 1) currentPage = 1;

        // 페이징 포함 기업 목록 조회
        java.util.Map<String, Object> result = AdminDAO.ADAO.getCompaniesPaged(
                filter.isBlank() ? null : filter,
                keyword.isBlank() ? null : keyword,
                currentPage,
                PAGE_SIZE
        );

        int totalCount = (int) result.getOrDefault("totalCount", 0);
        int totalPages = (int) result.getOrDefault("totalPages", 1);
        List<AdminCompanyVO> companies = (List<AdminCompanyVO>) result.get("companies");

        // 승인 대기 건수 (상단 뱃지용)
        int pendingCount = AdminDAO.ADAO.getPendingCompanyCount();

        request.setAttribute("companies", companies);
        request.setAttribute("pendingCount", pendingCount);
        request.setAttribute("totalCount", totalCount);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("currentFilter", filter);
        request.setAttribute("keyword", keyword);
        request.setAttribute("pageSize", PAGE_SIZE);

        request.setAttribute("content", "admin/admin_company.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }
}