INSERT INTO application (app_id, member_id, company_id, position, stage, apply_date, memo)
VALUES (1, 3, 1, '백엔드 개발자', 'APPLIED', SYSDATE, '첫 지원');

INSERT INTO application (app_id, member_id, company_id, position, stage, apply_date, memo)
VALUES (2, 3, 1, '프론트엔드 개발자', 'FIRST_INTERVIEW', SYSDATE, '면접 예정');
INSERT INTO application (app_id, member_id, company_id, position, stage, apply_date, memo)
VALUES (3, 3, 2, '프론트엔드 개발자', 'SECOND_INTERVIEW', SYSDATE, '면접 예정');
INSERT INTO til (til_id, member_id, title, content)
VALUES (1, 3, 'Oracle FK 정리', 'FK 제약조건과 CASCADE 학습');

INSERT INTO til (til_id, member_id, title, content)
VALUES (2, 3, 'Spring 구조 이해', 'Service와 Controller 역할 정리');
INSERT INTO resume (resume_id, member_id, name, phone, email, education, experience)
VALUES (1, 3, '홍길동', '010-1234-5678', 'test@test.com',
        'OO대학교 컴퓨터공학과',
        '프로젝트 경험 2회');
INSERT INTO resume_field (field_id, resume_id, field_name, field_value, sort_order)
VALUES (1, 1, '기술스택', 'Java, Spring, Oracle', 1);

INSERT INTO resume_field (field_id, resume_id, field_name, field_value, sort_order)
VALUES (2, 1, '프로젝트', '취업 관리 시스템 개발', 2);

INSERT INTO resume_field (field_id, resume_id, field_name, field_value, sort_order)
VALUES (3, 1, '자격증', '정보처리기사', 3);
INSERT INTO tag (tag_id, tag_name)
VALUES (1, '면접');

INSERT INTO tag (tag_id, tag_name)
VALUES (2, '서류');

INSERT INTO tag (tag_id, tag_name)
VALUES (3, '합격');

SELECT *
FROM application;
select *
from COMPANY;
select *
from APPLICATION;
select *
from TIL;
select *
from RESUME;
select *
from RESUME_FIELD;
select *
from TAG;

create sequence application_seq;
create sequence til_seq;
create sequence resume_seq;
create sequence resume_filed_seq;
create sequence tag_seq;

