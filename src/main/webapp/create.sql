CREATE TABLE application (
                             app_id NUMBER PRIMARY KEY,
                             member_id NUMBER NOT NULL,
                             company_id NUMBER NOT NULL,
                             position VARCHAR2(100),
                             stage VARCHAR2(20),
                             apply_date DATE,
                             memo VARCHAR2(1000),
                             created_date DATE DEFAULT SYSDATE,

                             CONSTRAINT fk_app_member
                                 FOREIGN KEY (member_id)
                                     REFERENCES member(member_id),

                             CONSTRAINT fk_app_company
                                 FOREIGN KEY (company_id)
                                     REFERENCES company(company_id),

                             CONSTRAINT chk_stage
                                 CHECK (stage IN ('APPLIED', 'DOCUMENT', 'FIRST_INTERVIEW', 'SECOND_INTERVIEW','THIRD_INTERVIEW','PASS', 'FAIL')));








);
ALTER TABLE application ADD interview_date DATE;
ALTER TABLE application MODIFY interview_time VARCHAR2(20);
ALTER TABLE application MODIFY interview_type VARCHAR2(50);

CREATE TABLE til (
                     til_id NUMBER PRIMARY KEY,
                     member_id NUMBER NOT NULL,
                     title VARCHAR2(200) NOT NULL,
                     content CLOB,
                     created_date DATE DEFAULT SYSDATE,

                     CONSTRAINT fk_til_member
                         FOREIGN KEY (member_id)
                             REFERENCES member(member_id)
);
CREATE TABLE resume (
                        resume_id NUMBER PRIMARY KEY,
                        member_id NUMBER NOT NULL,
                        name VARCHAR2(100),
                        phone VARCHAR2(50),
                        email VARCHAR2(100),
                        education VARCHAR2(200),
                        experience CLOB,
                        created_date DATE DEFAULT SYSDATE,

                        CONSTRAINT fk_resume_member
                            FOREIGN KEY (member_id)
                                REFERENCES member(member_id)
);
CREATE TABLE resume_field (
                              field_id NUMBER PRIMARY KEY,
                              resume_id NUMBER NOT NULL,
                              field_name VARCHAR2(100),
                              field_value CLOB,
                              sort_order NUMBER,

                              CONSTRAINT fk_resume_field
                                  FOREIGN KEY (resume_id)
                                      REFERENCES resume(resume_id)
                                          ON DELETE CASCADE
);
CREATE TABLE tag (
                     tag_id NUMBER PRIMARY KEY,
                     tag_name VARCHAR2(100) UNIQUE
);