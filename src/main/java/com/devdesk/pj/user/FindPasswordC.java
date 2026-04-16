package com.devdesk.pj.user;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 비밀번호 찾기 컨트롤러
 * GET  /find-password  → find_password.jsp 보여주기
 * POST /find-password  → 닉네임+이메일 검증 후 세션에 이메일 저장 → 비밀번호 재설정 페이지로 이동
 * <p>
 * POST /find-password?step=reset → 새 비밀번호 저장 후 성공 모달(포워딩) -> 로그인 페이지 이동
 */
@WebServlet(name = "FindPasswordC", value = "/find-password")
public class FindPasswordC extends HttpServlet {

    // ── STEP 1 : 비밀번호 찾기 폼 보여주기 ──────────────────────────────────
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String step = request.getParameter("step");

        if ("reset".equals(step)) {
            // 세션에 인증된 이메일이 없으면 1단계로 돌려보냄
            String verifiedEmail = (String) request.getSession().getAttribute("pwResetEmail");
            if (verifiedEmail == null) {
                response.sendRedirect(request.getContextPath() + "/find-password");
                return;
            }
            // 비밀번호 재설정 폼 보여주기
            request.setAttribute("step", "reset");
        }

        request.setAttribute("content", "/user/find_password.jsp");
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }

    // ── STEP 2 : 닉네임 + 이메일 검증 ───────────────────────────────────────
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        String step = request.getParameter("step");

        if ("reset".equals(step)) {
            // ── STEP 3 : 새 비밀번호 저장 ──────────────────────────────────
            handleReset(request, response);
        } else {
            // ── STEP 2 : 닉네임 + 이메일 검증 ──────────────────────────────
            handleVerify(request, response);
        }
    }

    /**
     * 닉네임 + 이메일로 회원 확인 → 맞으면 세션에 이메일 저장 후 재설정 폼으로 이동
     */
    private void handleVerify(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nickname = request.getParameter("nickname");
        String email = request.getParameter("email");

        // 정상적으로 인자 2개 전달
        boolean exists = MemberDAO.MBAO.findMemberByNicknameAndEmail(nickname, email);

        if (exists) {
            // 인증 성공 → 세션에 이메일 저장 (재설정 폼에서만 사용)
            request.getSession().setAttribute("pwResetEmail", email);
            response.sendRedirect(request.getContextPath() + "/find-password?step=reset");
        } else {
            // 인증 실패 → 에러 메시지(모달용)와 함께 폼 다시 보여주기
            request.setAttribute("showErrorModal", "가입된 회원이 아닙니다.");
            request.setAttribute("content", "/user/find_password.jsp");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }

    /**
     * 세션의 이메일로 새 비밀번호 업데이트
     */
    private void handleReset(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String verifiedEmail = (String) request.getSession().getAttribute("pwResetEmail");

        if (verifiedEmail == null) {
            response.sendRedirect(request.getContextPath() + "/find-password");
            return;
        }

        String newPassword = request.getParameter("new_password");
        String confirmPassword = request.getParameter("confirm_password");

        // 1. 비밀번호 일치 체크
        if (newPassword == null || !newPassword.equals(confirmPassword)) {
            request.setAttribute("step", "reset");
            request.setAttribute("showErrorModal", "비밀번호가 일치하지 않습니다.");
            request.setAttribute("content", "/user/find_password.jsp");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        // ⭐ 2. 기존 비밀번호와 동일한지 체크 (여기 추가!)
        boolean isSame = MemberDAO.MBAO.isSameAsOldPassword(verifiedEmail, newPassword);

        if (isSame) {
            request.setAttribute("step", "reset");
            request.setAttribute("showErrorModal", "이미 사용중인 비밀번호입니다. 다시 입력해주세요.");
            request.setAttribute("content", "/user/find_password.jsp");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
            return;
        }

        // 3. 비밀번호 변경
        boolean success = MemberDAO.MBAO.resetPassword(verifiedEmail, newPassword);

        if (success) {
            request.getSession().removeAttribute("pwResetEmail");

            request.setAttribute("step", "reset");
            request.setAttribute("showSuccessModal", "true");
            request.setAttribute("content", "/user/find_password.jsp");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        } else {
            request.setAttribute("step", "reset");
            request.setAttribute("showErrorModal", "비밀번호 재설정 중 오류가 발생했습니다. 다시 시도해주세요.");
            request.setAttribute("content", "/user/find_password.jsp");
            request.getRequestDispatcher("/index.jsp").forward(request, response);
        }
    }
}