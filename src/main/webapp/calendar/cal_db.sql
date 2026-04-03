-- 1. 면접 일정 테이블 생성
CREATE TABLE SCHEDULE (
                          SCHEDULE_ID NUMBER PRIMARY KEY,
                          MEMBER_ID NUMBER NOT NULL,
                          COMPANY_NAME VARCHAR2(100) NOT NULL,
                          SCHEDULE_DATE DATE NOT NULL,
                          SCHEDULE_TIME VARCHAR2(10),
                          INTERVIEW_TYPE VARCHAR2(20),
                          MEMO VARCHAR2(500),
                          CREATED_DATE DATE,

    CONSTRAINT FK_SCHEDULE_MEMBER
    FOREIGN KEY (MEMBER_ID)
    REFERENCES MEMBER(MEMBER_ID)
);

SELECT * FROM MEMBER;

-- 3. 일정 고유 ID(PK) 자동 증가를 위한 시퀀스 생성
CREATE SEQUENCE SEQ_SCHEDULE
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

TRUNCATE TABLE SCHEDULE;

INSERT INTO SCHEDULE VALUES (SEQ_SCHEDULE.nextval, 3, '네이버', TO_DATE('2026-04-05','YYYY-MM-DD'), '14:00', '기술면접', '코딩 테스트 이후 1차 면접', SYSDATE);
INSERT INTO SCHEDULE VALUES (SEQ_SCHEDULE.nextval, 4, '카카오', TO_DATE('2026-04-10','YYYY-MM-DD'), '10:30', '인성면접', '비대면 화상 면접', SYSDATE);

SELECT * FROM SCHEDULE;

rollback ;

CREATE TABLE APPLICATION(
                            APP_ID NUMBER PRIMARY KEY,
                            MEMBER_ID NUMBER NOT NULL,
                            COMPANY_ID NUMBER NOT NULL,
                            POSITION VARCHAR2(100),
                            STAGE VARCHAR2(5) DEFAULT '01' NOT NULL,
                            APPLY_DATE DATE NOT NULL,
                            MEMO VARCHAR2(500),
                            CREATED_DATE DATE DEFAULT SYSDATE NOT NULL,
                            CONSTRAINT FK_APP_MEMBER FOREIGN KEY (MEMBER_ID) REFERENCES MEMBER(MEMBER_ID),
                            CONSTRAINT FK_APP_COMPANY FOREIGN KEY (COMPANY_ID) REFERENCES COMPANY(COMPANY_ID)
                        );

CREATE SEQUENCE SEQ_APPLICATION
    START WITH 1
    INCREMENT BY 1
    NOCACHE
    NOCYCLE;

select * from COMPANY;

select * from APPLICATION;

-- 1. 나중에 구글 달력이랑 연결할 때 쓸 '이름표 자리(빈칸)' 하나 만들기
ALTER TABLE SCHEDULE ADD (GOOGLE_EVENT_ID VARCHAR2(255));

-- 2. 정보 찾는 속도를 엄청 빠르게 해주는 '책갈피(인덱스)' 3개 꽂아두기
CREATE INDEX IDX_SCHEDULE_MEMBER ON SCHEDULE(MEMBER_ID);
CREATE INDEX IDX_SCHEDULE_DATE ON SCHEDULE(SCHEDULE_DATE);
CREATE INDEX IDX_APPLICATION_MEMBER ON APPLICATION(MEMBER_ID);
