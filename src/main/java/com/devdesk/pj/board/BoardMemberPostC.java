package com.devdesk.pj.board;

import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet(name = "BoardMemberPostC", value = "/member-posts")
public class BoardMemberPostC extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            System.out.println("=== BoardMemberPostC 호출됨 ===");
            
            String memberIdStr = request.getParameter("memberId");
            System.out.println("전달된 memberId: " + memberIdStr);
            
            if (memberIdStr == null || memberIdStr.trim().isEmpty()) {
                System.out.println("memberId가 null이거나 비어있음");
                response.getWriter().print("[]");
                return;
            }
            
            int memberId = Integer.parseInt(memberIdStr);
            System.out.println("변환된 memberId: " + memberId);

            List<BoardVO> list = BoardDAO.getPostsByMember(memberId);
            System.out.println("조회된 게시글 수: " + (list != null ? list.size() : 0));
            
            // 각 게시글 정보 상세 출력
            if (list != null) {
                for (BoardVO post : list) {
                    System.out.println("게시글 정보 - ID: " + post.getBoard_id() + ", 제목: '" + post.getTitle() + "'");
                }
            }

            // 🔥 여기!!!
            response.setContentType("application/json;charset=UTF-8");

            PrintWriter out = response.getWriter();
            String json = new Gson().toJson(list);
            System.out.println("JSON 응답: " + json);
            out.print(json);

        } catch (Exception e) {
            System.out.println("=== BoardMemberPostC 오류 발생 ===");
            e.printStackTrace();
        }
    }
}
