# 🖥️ DevDesk

> **개인 워크스페이스 기반 IT 취업 준비 커뮤니티 플랫폼**
> 나만의 이력서·캘린더·포트폴리오를 올려두는 개발자(Dev)의 책상(Desk)

---

## 📌 목차

1. [프로젝트 소개](#1-프로젝트-소개)
2. [팀원 소개](#2-팀원-소개)
3. [기술 스택](#3-기술-스택)
4. [주요 기능](#4-주요-기능)
5. [DB 설계](#5-db-설계)
6. [보안 설계](#6-보안-설계)
7. [설치 및 실행](#7-설치-및-실행)
8. [개발 일지](#8-개발-일지)

---

## 1. 프로젝트 소개

기존 취업 준비 서비스는 **개인 관리 툴**과 **커뮤니티 서비스**가 따로 존재했습니다.
DevDesk는 채용 공고 저장 → 지원 상태 관리 → 면접 일정 기록 → 후기 작성 및 공유까지
취업 준비의 모든 흐름을 **하나의 공간**에서 해결합니다.

| 문제                                  | DevDesk 해결책                       |
| ------------------------------------- | ------------------------------------ |
| 면접 후기가 여러 사이트에 흩어져 있다 | 기업별 면접 후기 아카이브            |
| 지원 현황을 스프레드시트로 관리한다   | 지원 현황 CRUD + 단계별 퍼널 차트    |
| 면접 일정을 별도 앱에 따로 관리한다   | 캘린더 + Google Calendar 양방향 연동 |
| 자소서 버전 관리가 어렵다             | 이력서 블록 시스템 (버전 관리)       |
| 공부 기록을 남길 공간이 없다          | TIL 대시보드 내장                    |

---

## 2. 팀원 소개

| 이름                | 역할       | 담당 기능                                                                                                                            |
| ------------------- | ---------- | ------------------------------------------------------------------------------------------------------------------------------------ |
| **김선민** (팀리더) | Full-Stack | 레이아웃(index.jsp/css), 자유게시판 CRUD, 댓글·대댓글·좋아요, 검색·정렬·페이징, 게시글 사진 첨부, 작성자 프로필 모달, 주간/야간 토글 |
| **권동균**          | Full-Stack | 지원현황 CRUD, 대시보드, TIL, 이력서(resume), 즐겨찾기, 사이드바, 퍼널 차트                                                          |
| **김지영**          | Full-Stack | 캘린더 CRUD, Google Calendar API 양방향 연동, 미니 캘린더, 캘린더 사이드바                                                           |
| **이상준**          | Full-Stack | 기업 검색 CRUD, 면접 후기 CRUD, 기업 상세페이지, 비동기 필터링, 태그, XSS 보안, reCAPTCHA                                            |
| **최영은**          | Full-Stack | 회원가입·로그인·로그아웃, 마이페이지, 프로필 수정, 비밀번호 변경·찾기, 회원탈퇴, 구글 소셜 로그인, 관리자 기능                       |

---

## 3. 기술 스택

### Backend

| 항목     | 기술                                             |
| -------- | ------------------------------------------------ |
| 언어     | Java 8                                           |
| 서버     | Apache Tomcat / Servlet 4.0                      |
| 빌드     | Gradle 8.8                                       |
| DB       | Oracle (JDBC, Apache Commons DBCP2)              |
| JSON     | Gson 2.11                                        |
| 보안     | Google reCAPTCHA v2, JSTL `<c:out>` XSS 차단     |
| 외부 API | Google Calendar API, Google OAuth2 (소셜 로그인) |

### Frontend

| 항목      | 기술                        |
| --------- | --------------------------- |
| 뷰 템플릿 | JSP + JSTL                  |
| 비동기    | jQuery 3.6 Ajax / Fetch API |
| 캘린더 UI | FullCalendar 5.11 / 6.1     |
| 폰트      | Pretendard, JetBrains Mono  |

---

## 4. 주요 기능

### 4-1. 자유게시판 — 김선민

- 카테고리(자유토크 / 이력서 / 면접후기 / 취업 TIP) 분류
- 댓글·대댓글 CRUD (Ajax 비동기)
- 게시글 좋아요 토글
- 정렬: 최신순 / 인기순(좋아요) / 조회순
- 검색: 제목·내용·작성자 키워드, 태그별 검색

```java
// BoardDAO — 검색 타입별 동적 쿼리 분기
switch (searchType) {
    case "title":   sql += "WHERE b.b_title LIKE ? ";   break;
    case "content": sql += "WHERE b.b_content LIKE ? "; break;
    case "author":  sql += "WHERE m.nickname LIKE ? ";  break;
}
```

- 페이징 처리
- 게시글 작성 시 이미지 첨부 (API 연동)
- 작성자 클릭 → 해당 작성자의 게시글 모달 표시
- 주간 / 야간 모드 토글

---

### 4-2. 개인 워크스페이스 — 권동균

**지원현황 (application)**

- 지원 등록·조회·수정·삭제, 즐겨찾기
- 단계: `APPLIED → DOCUMENT_PASS → FIRST/SECOND/THIRD_INTERVIEW → CODING_TEST → PASS / FAIL`
- 단계별 집계 + 전환율 퍼널 차트 자동 계산

```java
// DashboardDAO — 퍼널 전환율 계산
int pct = (from == 0) ? 0 : (int) Math.round((double) to / from * 100);
```

**TIL (Today I Learned)**

- 제목·내용·태그·학습 시간 기록
- 대시보드에서 최근 TIL 목록 확인

**이력서 블록 (resume-block)**

- 지원동기 / 자기PR 등 카테고리별 블록 등록·편집
- 버전 관리 (v1, v2…)
- "이력서 조합" 탭에서 블록 선택 후 바로 복사 → 외부 지원 사이트에 붙여넣기

---

### 4-3. 면접 일정 캘린더 — 김지영

- FullCalendar 기반 월별 뷰, 날짜 클릭 / `+` 버튼으로 일정 등록
- 캘린더 CRUD (회사명 DB 목록 또는 직접 입력, 시간 10분 단위, 전형 드롭다운)
- **Google Calendar API 양방향 연동**
  - DevDesk에서 등록 → Google Calendar에 자동 반영
  - Google Calendar에서 입력 → DevDesk 캘린더에 자동 반영
- 사이드바: 이번 주 일정 요약, To-Do List, 미니 캘린더
- 대시보드·워크스페이스 미니 캘린더 위젯 제공

---

### 4-4. 기업 검색 & 면접 후기 — 이상준

**기업 검색**

- 회사명·업종·지역(LIKE) + 평점·규모 범위 다중 조건 동적 검색
- `StringBuilder`로 WHERE 절 동적 조립 + `PreparedStatement` 파라미터 바인딩 (SQL Injection 방지)
- 평점 필터는 집계 후 HAVING 절 처리

```java
// 평점 필터는 WHERE 대신 HAVING 사용 (AVG는 집계 후에만 필터 가능)
if (minRating != null) {
    baseSql.append(" HAVING NVL(ROUND(AVG(r.r_rating), 1), 0) >= ?");
}
```

- Ajax 비동기 필터링 — 검색 결과 즉시 렌더링

**기업 검색 모달**

- 리뷰 작성 도중 팝업에서 회사 선택 가능
- 검색 결과 없으면 '직접 등록' 버튼 → `is_verified='N'`으로 DB 저장 → 관리자 승인 후 공개

**면접 후기 (Review)**

- 목록(페이징) / 상세 / 작성 / 수정 / 삭제
- 좋아요 · 북마크 · 조회수 (Ajax 토글)
- 복합 PK로 중복 좋아요 DB 레벨 차단, 카운트는 review 테이블에 비정규화
- 단일 트랜잭션으로 카운트 불일치 방지

```java
// toggleLike — 두 SQL을 하나의 트랜잭션으로
if (isLiked(memberId, reviewId)) {
    // DELETE + r_like_count - 1
    return false;
} else {
    // INSERT + r_like_count + 1
    return true;
}
```

- NEW 뱃지: 로그인 후 읽으면 자동 소멸
- 기업 평점 ↔ 유저 평점 연계: 리뷰 작성·삭제 시 company 평점 자동 갱신
- 비동기 필터링, 태그 기능

---

### 4-5. 회원 관리 & 관리자 — 최영은

**회원**

- 회원가입 (이메일 중복 확인 Ajax, 닉네임 중복 확인, 비밀번호 규칙 검증)
- 로그인 / 로그아웃, 로그인 체크 필터
- **Google OAuth2 소셜 로그인**
- 마이페이지: 프로필 수정, 비밀번호 변경·찾기
- 내가 쓴 게시글·댓글 보기, 페이징
- 회원 탈퇴

**관리자**

- 회원 목록 조회 / 강제 탈퇴
- 게시글 숨김·삭제, 페이징
- 신고 관리 (비동기 처리)
- 기업 정보 수정·승인 (`is_verified` 관리)
- 관리자 대시보드 (전체 통계 요약)

---

## 5. DB 설계

### 테이블 관계

```
member (1) ──< application    (N)
member (1) ──< schedule       (N)
member (1) ──< board          (N)
member (1) ──< comments       (N)
member (1) ──< til            (N)
member (1) ──< resume         (N)
company(1) ──< application    (N)
company(1) ──< review         (N)
review (1) ──< review_like     (N)  ← 복합 PK (member_id, review_id)
```

### 핵심 테이블 정의

#### `member`

| 컬럼         | 설명                    |
| ------------ | ----------------------- |
| member_id PK | 회원 고유 ID (시퀀스)   |
| email UNIQUE | 로그인 이메일           |
| password     | 비밀번호                |
| nickname     | 닉네임                  |
| job_category | 관심 직무               |
| login_type   | `'local'` / `'GOOGLE'`  |
| role         | `'user'` / `'admin'`    |
| status       | `'active'` / `'banned'` |

#### `company`

| 컬럼                | 설명                          |
| ------------------- | ----------------------------- |
| company_id PK       | 회사 ID                       |
| company_name UNIQUE | 회사명                        |
| company_industry    | 업종                          |
| company_location    | 지역                          |
| company_size        | 직원 수                       |
| company_rating      | 평점                          |
| is_verified CHAR(1) | 관리자 승인 여부 (기본 `'N'`) |

#### `review`

| 컬럼             | 설명                                         |
| ---------------- | -------------------------------------------- |
| r_id PK          | 리뷰 ID (시퀀스)                             |
| r_company_id FK  | 대상 회사                                    |
| r_member_id FK   | 작성 회원                                    |
| r_title          | 제목                                         |
| r_job_position   | 지원 직무                                    |
| r_interview_type | CODING / TECH / PERSONAL / EXEC / GROUP / PT |
| r_difficulty     | 난이도 1~5                                   |
| r_result         | PASS / FAIL / PENDING                        |
| r_content CLOB   | 상세 후기 (장문)                             |
| r_rating         | 기업 평점 1~5                                |
| r_view_count     | 조회수                                       |
| r_like_count     | 좋아요 수 (비정규화)                         |
| r_bookmark_count | 북마크 수 (비정규화)                         |

#### `application`

| 컬럼           | 설명                                                                                                       |
| -------------- | ---------------------------------------------------------------------------------------------------------- |
| app_id PK      | 지원 ID (시퀀스)                                                                                           |
| member_id FK   | 회원                                                                                                       |
| company_id FK  | 지원 회사                                                                                                  |
| position       | 지원 직무                                                                                                  |
| stage          | APPLIED / DOCUMENT_PASS / FIRST_INTERVIEW / SECOND_INTERVIEW / THIRD_INTERVIEW / CODING_TEST / PASS / FAIL |
| apply_date     | 지원일                                                                                                     |
| interview_date | 면접일                                                                                                     |
| interview_time | 면접 시간                                                                                                  |
| memo           | 메모                                                                                                       |

#### `schedule`

| 컬럼            | 설명                    |
| --------------- | ----------------------- |
| schedule_id PK  | 일정 ID                 |
| member_id FK    | 회원                    |
| company_name    | 회사명                  |
| schedule_date   | 면접 날짜               |
| schedule_time   | 면접 시간               |
| interview_type  | 면접 유형               |
| google_event_id | Google Calendar 연동 ID |

#### `til`

| 컬럼         | 설명             |
| ------------ | ---------------- |
| til_id PK    | TIL ID (시퀀스)  |
| member_id FK | 회원             |
| title        | 제목             |
| content      | 내용             |
| tag          | 태그             |
| study_time   | 학습 시간 (시간) |
| created_date | 작성일           |

#### `resume` / `resume_field`

| 테이블       | 설명                                                                |
| ------------ | ------------------------------------------------------------------- |
| resume       | 이력서 기본 정보 (이름, 전화, 학력, 경험)                           |
| resume_field | 커스텀 항목 (기술스택, 프로젝트, 자격증 등, sort_order로 순서 관리) |

#### `board` / `comments`

| 테이블   | 설명                                                     |
| -------- | -------------------------------------------------------- |
| board    | 게시글 (카테고리, 제목, 내용, 좋아요, 조회수, hidden_yn) |
| comments | 댓글·대댓글 (parent_id로 대댓글 구분)                    |

---

## 6. 보안 설계

### XSS (Cross-Site Scripting) 차단

사용자 입력값 출력 시 `${...}` → `<c:out>`으로 HTML 이스케이프 처리

```jsp
<%-- 취약한 코드 --%>
<div>${r.reviewContent}</div>

<%-- 안전한 코드 --%>
<div><c:out value="${r.reviewContent}"/></div>
```

적용 파일: `reviewDetail.jsp`, `boardDetail.jsp`, `companyDetail.jsp`

---

### SQL Injection 방지

모든 DB 조회에 `PreparedStatement` + 파라미터 바인딩, 동적 쿼리 컬럼명은 화이트리스트로 검증

```java
Set<String> allowedText = Set.of("company_name", "company_industry", "company_location");
```

---

### Google reCAPTCHA v2

```
[login.jsp] 체크박스 완료 → g-recaptcha-response 토큰 전송
    ↓
[LoginC.doPost()] 토큰 수신
    ↓
[RecaptchaUtil.verify(token)]
    POST https://www.google.com/recaptcha/api/siteverify
    ├─ success:true  → 로그인 진행
    └─ success:false → 로그인 차단
```

`secretKey`는 `conf.properties`에 외부화 → 코드 직접 노출 방지 / 회원가입·로그인 폼 모두 적용

---

## 7. 설치 및 실행

### 사전 요구사항

- JDK 8+
- Apache Tomcat 9+
- Oracle DB

### 1단계 — 소스 클론

```bash
git clone https://github.com/your-org/devdesk.git
cd devdesk
```

### 2단계 — DB 연결 설정

`DBManager_new.java`를 프로젝트 루트에 생성 (`.gitignore` 처리됨)

```java
public static Connection connect() throws Exception {
    Class.forName("oracle.jdbc.driver.OracleDriver");
    return DriverManager.getConnection(
        "jdbc:oracle:thin:@localhost:1521:xe", "user", "password");
}
```

### 3단계 — DB 테이블 생성 (순서 중요)

```
1. src/main/webapp/user/member.sql         → member 테이블 + 시퀀스
2. src/main/webapp/create.sql              → application, til, resume 테이블
3. src/main/webapp/board/board.sql         → board, comments 테이블
4. src/main/webapp/calendar/cal_db.sql     → schedule 테이블
5. company / review 관련 SQL 실행
```

### 4단계 — 빌드 및 배포

```bash
# Windows
gradlew.bat build

# macOS / Linux
./gradlew build
```

`build/libs/pj-1.0-SNAPSHOT.war` → Tomcat `webapps/`에 배포

### 5단계 — 접속

```
http://localhost:8080/pj/main
```

---

## 8. 개발 일지

팀 업무 기록 채널을 팀원별로 정리한 내역입니다.

### 김선민 (팀리더)

| 날짜 | 작업 내용                                                                 |
| ---- | ------------------------------------------------------------------------- |
| 4/1  | index.jsp / index.css 80% 완성                                            |
| 4/2  | index 레이아웃 완성, 자유게시판 페이지·글쓰기 페이지 완성                 |
| 4/3  | 자유게시판 CRUD 완성                                                      |
| 4/6  | 댓글 작성·삭제 완성                                                       |
| 4/7  | 댓글 수정, 대댓글 CRUD, 게시글 좋아요 토글                                |
| 4/8  | 인기순·최신순·조회순 정렬, 제목·내용·작성자 검색, 태그별 검색, home.jsp   |
| 4/9  | 페이징 처리, 주간/야간 토글, 로그인 체크 후 탭 이동, board-all.css 정리   |
| 4/10 | 게시글 사진 첨부, 작성자 클릭 시 게시글 모달 (80%)                        |
| 4/13 | 사진 업로드·작성자 모달 완성, 팀 회의                                     |
| 4/14 | css 파일 정리, 대시보드 css 보조, 로그인 시 대시보드 출력 수정, 버그 수정 |
| 4/15 | 버그 수정, finaltest 마무리, 조원 협력                                    |
| 4/16 | finaltest 최종 마무리, 발표 회의 준비                                     |

### 권동균

| 날짜 | 작업 내용                                          |
| ---- | -------------------------------------------------- |
| 4/1  | 담당 DB 생성·테스트, 기업 지원 등록 JSP 완성       |
| 4/2  | 대시보드 설계, DB 수정                             |
| 4/3  | DB 롤백 후 CRUD 재수정, 지원 현황 목록→카드형 전환 |
| 4/6  | 지원 단계별 숫자 집계 오류 수정                    |
| 4/7  | TIL 완성, 대시보드 작성                            |
| 4/8  | resume 작성, 태그 등 완성                          |
| 4/9  | 대시보드·TIL css 테마, 지원 현황 집계 오류 수정    |
| 4/10 | 지원 현황 오류 수정, 즐겨찾기 기능 추가 완료       |
| 4/13 | 즐겨찾기 완성, 사이드바 추가, 오류 수정            |
| 4/14 | TIL 오류 수정                                      |
| 4/15 | 버그 수정, finaltest 머지, 지원현황 UI·기능 정비   |
| 4/16 | finaltest 최종 마무리, 발표 회의 준비              |

### 김지영

| 날짜 | 작업 내용                                                                 |
| ---- | ------------------------------------------------------------------------- |
| 4/1  | DB 설계·입력 완료                                                         |
| 4/2  | 캘린더 UI 완성                                                            |
| 4/3  | 클릭 시 팝업 상세 일정, DB 수정, CRUD 완성                                |
| 4/6  | 캘린더 CRUD 완성, 시간 10분 단위, 회사명 DB·직접 입력, 전형 드롭다운      |
| 4/7  | 포지션 → application DB 연동, update·delete 오류 수정, 메인→캘린더 이동   |
| 4/8  | 캘린더 CRUD 완벽 구동, 로그인 전 접근 시 로그인창 이동                    |
| 4/9  | 대시보드·TIL 미니 캘린더 추가, Google API 키 발급                         |
| 4/10 | 미니 캘린더 완성, Google Calendar API 연동 완료 (DevDesk→Google 반영)     |
| 4/13 | 캘린더 + 버튼, 사이드바 (이번 주 일정·To-Do·미니 캘린더)                  |
| 4/14 | 캘린더 사이드바 완성                                                      |
| 4/15 | 사이드바 css 통일, Google Calendar 양방향 연동 완성 (Google→DevDesk 반영) |
| 4/16 | finaltest 데이터 통합, 디버깅                                             |

### 이상준

| 날짜 | 작업 내용                                                                |
| ---- | ------------------------------------------------------------------------ |
| 4/1  | DB 생성·테스트, 회사 찾기 패키지, 리뷰 VO·DAO·가페이지, css              |
| 4/2  | 회사 찾기 구현 완료, 리뷰 페이지 연동                                    |
| 4/3  | 면접 후기 작성·조회 기능 완성                                            |
| 4/6  | 기업 검색 CRUD, 후기 CRUD 완성, 기업 상세페이지 메서드·JSP 틀            |
| 4/7  | 기업 상세페이지 완성, 기업 검색 JSP 조정                                 |
| 4/8  | 리뷰 좋아요·북마크·조회수, NEW 뱃지, 삭제·수정, 기업 평점↔유저 평점 연계 |
| 4/9  | 기업 카드 페이징, 회사 정보 로직 변경, 비동기 필터링                     |
| 4/10 | 리뷰↔회사 검색 비동기 연결, 태그 기능, 버그 수정                         |
| 4/13 | 관리자 신고 기능, reCAPTCHA 연동, 디버깅, 보안 강화                      |
| 4/14 | 파일 정리, 다크모드 적용, 디버깅, 버그 수정                              |
| 4/15 | 버그 수정, finaltest 머지, 리뷰 추천·북마크 기능 정비                    |
| 4/16 | finaltest 데이터 통합 및 작동 확인, 디버깅                               |

### 최영은

| 날짜  | 작업 내용                                                          |
| ----- | ------------------------------------------------------------------ |
| 4/1   | member 테이블·DTO·DAO 생성                                         |
| 4/2   | 회원가입 로직 완성, 아이디 중복확인, 로그인 페이지 연결            |
| 4/3   | 회원가입 완료 화면, 로그인 기능, 로그인 시 상단 UI 변경            |
| 4/4~5 | 로그아웃, 로그인 체크 필터, 마이페이지 CRUD, 프로필 수정, css      |
| 4/7   | 로그인 체크 필터 변경, 프로필 수정 완성, 비밀번호 변경 페이지 연동 |
| 4/8   | 비밀번호 변경 로직·모달 완성                                       |
| 4/9   | 마이페이지 내 댓글 보기·페이징, 회원탈퇴, 구글 로그인 버튼 추가    |
| 4/10  | 구글 소셜 로그인 오류 수정, 관리자 DB 연결, 관리자 대시보드 완성   |
| 4/13  | css 오류 수정, 관리자 기업 수정 테스트 완료                        |
| 4/14  | 신고 관리 비동기 변경, css 전반 수정, 게시글 관리 페이징·삭제      |
| 4/15  | 닉네임 중복 확인, 비밀번호 찾기, css 전반 수정                     |
| 4/16  | finaltest 데이터 통합 및 작동 확인, 디버깅                         |

---

_© 2026 Team 5조사마 — DevDesk_
README.md
