package com.devdesk.pj.calendar;

import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {

    private static final Properties properties = new Properties();

    // 🌟 클래스가 로드될 때 딱 한 번만 conf.properties 파일을 읽어옵니다.
    static {
        try (InputStream in = ConfigManager.class.getClassLoader().getResourceAsStream("conf.properties")) {
            if (in != null) {
                properties.load(in);
                System.out.println("✅ conf.properties 로드 성공!");
            } else {
                System.out.println("❌ conf.properties 파일을 찾을 수 없습니다. (src/main/resources 폴더 안에 있는지 확인하세요)");
            }
        } catch (Exception e) {
            System.out.println("❌ conf.properties 읽기 오류 발생!");
            e.printStackTrace();
        }
    }

    // 🌟 필요한 키(예: "google.api.key")를 넣으면 값을 꺼내주는 메서드
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}