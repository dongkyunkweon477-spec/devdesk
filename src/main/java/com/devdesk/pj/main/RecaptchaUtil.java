package com.devdesk.pj.main;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class RecaptchaUtil {

    private static final String VERIFY_URL = "https://www.google.com/recaptcha/api/siteverify";

    private static String secretKey;
    private static String siteKey;

    static {
        try (InputStream input = RecaptchaUtil.class.getClassLoader().getResourceAsStream("conf.properties")) {
            Properties prop = new Properties();
            prop.load(input);
            secretKey = prop.getProperty("recaptcha.secret.key");
            siteKey = prop.getProperty("recaptcha.site.key");
            System.out.println("[reCAPTCHA] siteKey loaded: " + siteKey);
            System.out.println("[reCAPTCHA] secretKey loaded: " + secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getSiteKey() {
        return siteKey;
    }

    /**
     * g-recaptcha-response 토큰을 Google Apps 검증
     *
     * @return true면 사람, false면 봇 or 실패
     */
    public static boolean verify(String token) {
        if (token == null || token.isEmpty()) return false;

        try {
            String params = "secret=" + secretKey + "&response=" + token;
            byte[] body = params.getBytes(StandardCharsets.UTF_8);

            URL url = new URL(VERIFY_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(body);
            }

            try (InputStream is = conn.getInputStream()) {
                String response = new String(is.readAllBytes(), StandardCharsets.UTF_8);
                System.out.println("[reCAPTCHA] response: " + response);
                // 공백 유무 무관하게 체크
                return response.contains("\"success\": true") || response.contains("\"success\":true");
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
