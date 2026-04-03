create table board_like (
       member_id number,
       b_board_id number,
       created_date date default sysdate,

       constraint pk_board_like primary key (member_id, b_board_id),

       constraint fk_like_member
       foreign key (member_id) -- 얘는 회원탈퇴 시 좋아요 삭제
       references member(member_id)
       on delete cascade,

       constraint fk_like_board -- 얘는 게시글 삭제 시 좋아요 삭제
       foreign key (b_board_id)
       references board(b_board_id)
       on delete cascade
);

INSERT INTO board_like (member_id, b_board_id)
VALUES (3, 21);

INSERT INTO board_like (member_id, b_board_id)
VALUES (4, 5);

INSERT INTO board_like (member_id, b_board_id)
VALUES (3, 5);

select * from board_like;

SELECT * FROM board_like -- 토글테스트, 이미 눌렀는지 확인
WHERE member_id = 3 AND b_board_id = 21;

DELETE FROM board_like -- 취소
WHERE member_id = 3 AND b_board_id = 21;

INSERT INTO board_like (member_id, b_board_id) -- 다시 추천
VALUES (3, 21);



