package com.devdesk.pj.board;


import lombok.Data;

@Data
public class BoardVO {
    private int board_id;
    private int member_id;
    private String category;
    private String title;
    private String content;
    private int view_count;
    private int like_count;
    private char hidden_yn;
    private String created_date;
    private String updated_date;

}
