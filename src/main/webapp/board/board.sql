CREATE TABLE board (
b_board_id      number(5) PRIMARY KEY,          -- 게시글 번호
member_id     NUMBER(4) NOT NULL,             -- 작성자 (FK)
b_category      VARCHAR2(50) not null,                -- 카테고리
b_title         VARCHAR2(200) NOT NULL,      -- 제목
b_content       varchar2(1500) not null,                        -- 내용
b_view_count    NUMBER DEFAULT 0,            -- 조회수
b_like_count    NUMBER DEFAULT 0,            -- 좋아요
b_hidden_yn     CHAR(1) DEFAULT 'N',         -- 숨김 여부 (Y/N)
b_created_date  DATE DEFAULT SYSDATE,        -- 작성일
b_updated_date  DATE,                         -- 수정일

    constraint fk_board_member
    foreign key(member_id)
    references member(member_id)
);

create sequence board_seq;
