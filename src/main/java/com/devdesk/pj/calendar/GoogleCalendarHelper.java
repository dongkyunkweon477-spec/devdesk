package com.devdesk.pj.calendar;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;

public class GoogleCalendarHelper {

    private static final String APPLICATION_NAME = "DevDesk-Project";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    // 🌟 사용자별 캘린더 접근을 위해 매개변수로 'refreshToken'을 받습니다!
    public static Calendar getCalendarService(String refreshToken) throws Exception {
        HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();

        // 1. ConfigManager에서 클라이언트 ID와 시크릿 키를 가져옵니다.
        String clientId = ConfigManager.getProperty("google.client.id");
        String clientSecret = ConfigManager.getProperty("google.client.secret");

        // 2. DB에서 가져온 refreshToken과 앱 정보(ID, Secret)를 조합해 인증 객체(Credential)를 만듭니다.
        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(httpTransport)
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setRefreshToken(refreshToken);

        // 3. 만들어진 인증 객체를 사용해서 구글 캘린더 서비스 객체를 생성합니다. (API Key 삭제됨!)
        return new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }
}