create table comments (
  c_comments_id number primary key,
  b_board_id number not null,
  member_id number not null,
  parent_id number,
  c_content varchar2(1000) not null,
  c_created_date date default sysdate,

  constraint fk_comment_member
      foreign key(member_id)
          references member(member_id),

  constraint fk_comment_board
      foreign key(b_board_id)
          references board(b_board_id),

  constraint fk_comment_parent
      foreign key(parent_id)
          references comments(c_comments_id)
);

create sequence comment_seq;

-- 부모 댓글
INSERT INTO comments
    (c_comments_id, b_board_id, member_id, parent_id, c_content)
VALUES (comment_seq.NEXTVAL, 5, 3, NULL, '부모 댓글');

SELECT MAX(c_comments_id) FROM comments;

-- 대댓글
INSERT INTO comments
(c_comments_id, b_board_id, member_id, parent_id, c_content)
VALUES (comment_seq.NEXTVAL, 5, 3, 3, '대댓글');

-- 대대댓글
INSERT INTO comments
(c_comments_id, b_board_id, member_id, parent_id, c_content)
VALUES (comment_seq.NEXTVAL, 5, 3, 4, '대대댓글');

select * from comments;
