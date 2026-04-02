create table member(
                       member_id number(4) primary key,
                       email varchar2(100) unique not null,
                       password varchar2(200),
                       nickname varchar2(50) not null,
                       job_category varchar2(50),
                       login_type varchar2(10) default 'local' not null,
                       social_id varchar2(200),
                       role varchar2(10) default 'user' not null,
                       status varchar2(10) default 'active' not null,
                       created_date date default sysdate not null,
                       update_date date
);

create sequence member_id_seq
    start with 1
    increment by 1
    nocache
    nocycle ;

-- 테스트 1: 일반 회원가입(LOCAL) 정상 작동 테스트
INSERT INTO MEMBER (MEMBER_ID, EMAIL, PASSWORD, NICKNAME, JOB_CATEGORY)
VALUES (member_id_seq.NEXTVAL, 'test1@gmail.com', 'hashed_password_123', '개발자지망생', '백엔드');

-- 테스트 2: 구글 소셜로그인(GOOGLE) 정상 작동 테스트 (비밀번호 NULL)
INSERT INTO MEMBER (MEMBER_ID, EMAIL, NICKNAME, LOGIN_TYPE, SOCIAL_ID)
VALUES (member_id_seq.NEXTVAL, 'google_user@gmail.com', '구글유저', 'GOOGLE', 'google_unique_id_777');

-- 데이터가 잘 들어갔는지 확인
SELECT * FROM MEMBER;

-- 테스트가 끝났으면 데이터 비우기 (실제 개발 시작 전 깔끔하게 정리)
TRUNCATE TABLE MEMBER;


