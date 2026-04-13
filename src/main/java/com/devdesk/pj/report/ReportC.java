package com.devdesk.pj.report;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "ReportC", value = "/report")
public class ReportC extends HttpServlet {

    private final ReportDAO reportDAO = ReportDAO.REPORT_DAO;

    // GET /report → 신고 목록 (관리자)
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<ReportVO> reports = reportDAO.getAllReports();
        request.setAttribute("reports", reports);

        request.setAttribute("content", "report/report.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    // POST /report → 신고 접수 (report_form.jsp에서 submit)
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        // 세션에서 로그인 회원 ID 가져오기
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // MEMBER_ID — JSP hidden 필드로 넘어온 값 사용
        // (UserVO 캐스팅으로 직접 꺼낼 경우 session.getAttribute("user") 캐스팅 필요)
        String memberIdStr = request.getParameter("memberId");
        int memberId = (memberIdStr != null && !memberIdStr.isEmpty())
                ? Integer.parseInt(memberIdStr) : 0;

        // REVIEW_ID / BOARD_ID (둘 중 하나만 값 있음)
        String reviewIdStr = request.getParameter("reviewId");
        // TODO: VO/DAO에 boardId 컬럼 추가 후 활성화
        // String boardIdStr = request.getParameter("boardId");
        // int boardId = (boardIdStr != null && !boardIdStr.isEmpty()) ? Integer.parseInt(boardIdStr) : 0;

        int reviewId = (reviewIdStr != null && !reviewIdStr.isEmpty())
                ? Integer.parseInt(reviewIdStr) : 0;

        // REPO_REASON, REPO_CONTENT
        String repoReason  = request.getParameter("repoReason");
        String repoContent = request.getParameter("repoContent");

        // 중복 신고 체크 (리뷰 신고인 경우만 — boardId 지원 시 분기 추가)
        if (reviewId > 0 && reportDAO.checkDuplicate(memberId, reviewId)) {
            // 이미 신고한 경우 이전 페이지로
            response.sendRedirect(request.getHeader("Referer"));
            return;
        }

        // VO 세팅 후 INSERT
        ReportVO vo = new ReportVO();
        vo.setRepoReviewId(reviewId);
        vo.setRepoMemberId(memberId);
        vo.setRepoReason(repoReason);
        vo.setRepoContent(repoContent);
        // REPO_STATUS 기본값은 DB DEFAULT 'PENDING' 처리

        reportDAO.insertReport(vo);

        // 신고 완료 후 이전 페이지로 복귀
        String referer = request.getHeader("Referer");
        response.sendRedirect(referer != null ? referer : request.getContextPath() + "/main");
    }

}