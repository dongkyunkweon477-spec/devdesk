package com.devdesk.pj.admin;

import lombok.Data;

@Data
public class AdminBoardVO {
    private int board_id;       // 게시글 번호
    private String category;    // 카테고리 (질문, 팁 등)
    private String title;       // 게시글 제목
    private String nickname;    // 작성자 닉네임 (member 테이블 조인)
    private String email;       // 작성자 이메일
    private String created_date;// 작성일
    private int view_count;     // 조회수
}