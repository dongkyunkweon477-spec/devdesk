package com.devdesk.pj.calendar;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class GoogleCalendarSyncListener implements ServletContextListener {

    private ScheduledExecutorService scheduler;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("🚀 [DevDesk] 서버 시작: 구글 캘린더 자동 동기화 스케줄러 가동 준비...");

        scheduler = Executors.newSingleThreadScheduledExecutor();

        // 🌟 10분 -> 12시간 간격으로 변경 완료!
        // (서버 켜지고 1분 뒤에 첫 실행, 그 이후부터는 12시간마다 실행)
        scheduler.scheduleAtFixedRate(() -> {
            try {
                System.out.println("🔄 [DevDesk 스케줄러] 구글 캘린더 일정 12시간 주기 조회 시작...");

                Schedule_newDAO.SCAO.syncGoogleCalendarToDB();

            } catch (Exception e) {
                System.out.println("❌ [DevDesk 스케줄러] 동기화 중 에러 발생: " + e.getMessage());
            }
        }, 1, 12, TimeUnit.HOURS); // <- 여기가 MINUTES 에서 HOURS 로 변경되었습니다.(나중에 수정하기~~)
        // 1, 10, TimeUnit.MINUTES : 10분으로 돌아가기
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            System.out.println("🛑 [DevDesk] 서버 종료: 구글 캘린더 자동 동기화 스케줄러 중지됨.");
        }
    }
}
