package com.devdesk.pj.calendar;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarRequestInitializer; // 🌟 요거 하나 새로 추가됐습니다!

public class GoogleCalendarHelper {

    private static final String APPLICATION_NAME = "DevDesk-Project";
    private static final String API_KEY = ConfigManager.getProperty("google.api.key");

    public static Calendar getCalendarService() throws Exception {
        // 구글 서버와 통신하기 위한 전송 통로와 JSON 변환기 설정
        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                null)
                .setApplicationName(APPLICATION_NAME)
                // 🚨 에러 나던 람다식(request -> ...) 대신 전용 세팅 객체로 변경! (빨간줄 완벽 해결)
                .setGoogleClientRequestInitializer(new CalendarRequestInitializer(API_KEY))
                .build();
    }
}