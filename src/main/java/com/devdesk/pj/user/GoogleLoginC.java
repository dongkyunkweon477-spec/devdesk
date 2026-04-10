package com.devdesk.pj.user;

import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.net.*;
import java.util.Properties;

@WebServlet("/google-login")
public class GoogleLoginC extends HttpServlet {

    private String CLIENT_ID;
    private String CLIENT_SECRET;
    private String REDIRECT_URI;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            Properties prop = new Properties();
            InputStream input = getClass().getClassLoader().getResourceAsStream("conf.properties");

<<<<<<< HEAD
            InputStream input = request
                    .getServletContext()
                    .getResourceAsStream("/WEB-INF/conf.properties");

            prop.load(input);

            CLIENT_ID = prop.getProperty("google.client.id");
            CLIENT_SECRET = prop.getProperty("google.client.secret");
            REDIRECT_URI = prop.getProperty("google.redirect.uri");

            System.out.println(CLIENT_ID);
            System.out.println(CLIENT_SECRET);
            System.out.println(REDIRECT_URI);


=======
            if (input != null) {
                prop.load(input);
                CLIENT_ID = prop.getProperty("google.client.id");
                CLIENT_SECRET = prop.getProperty("google.client.secret");
                REDIRECT_URI = prop.getProperty("google.redirect.uri");
            } else {
                System.out.println("❌ 경고: conf.properties 파일을 찾을 수 없어 읽지 못했습니다!");
            }
>>>>>>> 6dfa457655735a314753b902b498e35ce6260c83
        } catch (Exception e) {
            System.out.println("❌ 경고: conf.properties 파일을 읽는 중 에러 발생!");
            e.printStackTrace();
        }


        String code = request.getParameter("code");

        if (code == null) {
            System.out.println("❌ 코드를 받지 못했습니다.");
            response.sendRedirect("login");
            return;
        }

        // STEP 1. 인가 코드 → Access Token 교환
        String tokenJson = requestAccessToken(code);
        JSONObject tokenObj = new JSONObject(tokenJson);
        String accessToken = tokenObj.getString("access_token");

        // 🌟 [추가된 부분] 캘린더 동기화를 위한 Refresh Token 추출 🌟
        String refreshToken = null;
        if (tokenObj.has("refresh_token")) {
            refreshToken = tokenObj.getString("refresh_token");
            System.out.println("✅ 구글 Refresh Token 획득 성공 (캘린더 동기화용): " + refreshToken);
        } else {
            System.out.println("⚠️ Refresh Token이 없습니다. (이미 발급받았거나 권한 동의를 건너뜀)");
        }

        // STEP 2. Access Token → 유저 정보 조회
        String userJson = requestUserInfo(accessToken);
        JSONObject userObj = new JSONObject(userJson);

        String email = userObj.getString("email");
        String name = userObj.optString("name", "구글유저");
        String socialId = userObj.optString("sub", "");    // 구글 고유 ID

        System.out.println("✅ 구글 유저 로그인 성공: " + email);

        // STEP 3. DB 처리
        MemberDAO dao = MemberDAO.MBAO;
        MemberDTO member = dao.getMemberByEmail(email);

        if (member == null) {
            // 최초 로그인 → 자동 가입
            // 💡 나중에 팀원과 상의해서 여기에 refreshToken도 DB에 함께 저장하도록 수정해야 합니다!
            dao.insertGoogleMember(email, name, socialId);
            member = dao.getMemberByEmail(email);
        } else if (refreshToken != null) {
            // 💡 기존 유저인데 새로운 refreshToken을 받았다면 DB를 업데이트 해줘야 합니다.
            // dao.updateGoogleRefreshToken(email, refreshToken);
        }

        // STEP 4. 세션 저장 → 메인 이동
        HttpSession session = request.getSession();
        session.setAttribute("user", member);  // 기존 코드와 동일한 키 "user"
        session.setMaxInactiveInterval(30 * 60);

        response.sendRedirect(request.getContextPath() + "/main");
    }

    private String requestAccessToken(String code) throws IOException {
        URL url = new URL("https://oauth2.googleapis.com/token");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        // 이 부분에서 REDIRECT_URI가 null이면 500 에러가 났었습니다. (위에서 방어 완료)
        String params = "code=" + URLEncoder.encode(code, "UTF-8")
                + "&client_id=" + CLIENT_ID
                + "&client_secret=" + CLIENT_SECRET
                + "&redirect_uri=" + URLEncoder.encode(REDIRECT_URI, "UTF-8")
                + "&grant_type=authorization_code";

        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes("UTF-8"));
        }

        return readResponse(conn);
    }

    private String requestUserInfo(String accessToken) throws IOException {
        URL url = new URL("https://www.googleapis.com/oauth2/v3/userinfo");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        return readResponse(conn);
    }

    private String readResponse(HttpURLConnection conn) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
        }
        return sb.toString();
    }
}