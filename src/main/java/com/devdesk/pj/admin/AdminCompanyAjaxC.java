package com.devdesk.pj.admin;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 기업 정보 관리 AJAX 처리 컨트롤러
 * URL: /admin/company/ajax?action=XXX
 * <p>
 * action 목록:
 * approve  - 기업 승인 (N → Y)
 * reject   - 기업 반려 (Y → N)
 * delete   - 기업 삭제
 * update   - 기업 정보 수정
 * merge    - 중복 기업 병합
 */
@WebServlet("/admin/company/ajax")
public class AdminCompanyAjaxC extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String action = request.getParameter("action");
        if (action == null || action.isBlank()) {
            out.print("{\"success\":false,\"msg\":\"action 파라미터가 없습니다.\"}");
            return;
        }

        switch (action) {

            // ✅ 승인 처리
            case "approve": {
                int companyId = Integer.parseInt(request.getParameter("companyId"));
                boolean ok = AdminDAO.ADAO.approveCompany(companyId);
                out.print("{\"success\":" + ok + "}");
                break;
            }

            // ❌ 반려 처리
            case "reject": {
                int companyId = Integer.parseInt(request.getParameter("companyId"));
                boolean ok = AdminDAO.ADAO.rejectCompany(companyId);
                out.print("{\"success\":" + ok + "}");
                break;
            }

            // 🗑️ 삭제
            case "delete": {
                int companyId = Integer.parseInt(request.getParameter("companyId"));
                boolean ok = AdminDAO.ADAO.deleteCompanyAdmin(companyId);
                out.print("{\"success\":" + ok + "}");
                break;
            }

            // ✏️ 기업 정보 수정
            case "update": {
                int companyId = Integer.parseInt(request.getParameter("companyId"));
                String name = request.getParameter("companyName");
                String industry = request.getParameter("companyIndustry");
                String location = request.getParameter("companyLocation");

                String ratingStr = request.getParameter("companyRating");
                String sizeStr = request.getParameter("companySize");
                double rating = (ratingStr != null && !ratingStr.isBlank()) ? Double.parseDouble(ratingStr) : 0.0;
                int size = (sizeStr != null && !sizeStr.isBlank()) ? Integer.parseInt(sizeStr) : 0;

                boolean ok = AdminDAO.ADAO.updateCompanyAdmin(companyId, name, industry, location, rating, size);
                out.print("{\"success\":" + ok + "}");
                break;
            }

            // 🔀 중복 기업 병합
            case "merge": {
                int keepId = Integer.parseInt(request.getParameter("keepId"));
                int deleteId = Integer.parseInt(request.getParameter("deleteId"));
                if (keepId == deleteId) {
                    out.print("{\"success\":false,\"msg\":\"같은 기업은 병합할 수 없습니다.\"}");
                    break;
                }
                boolean ok = AdminDAO.ADAO.mergeCompanies(keepId, deleteId);
                out.print("{\"success\":" + ok + "}");
                break;
            }

            default:
                out.print("{\"success\":false,\"msg\":\"알 수 없는 action입니다.\"}");
        }
    }
}