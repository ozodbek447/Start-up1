package com.example.testwebb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
@EnableScheduling
public class TestWEbbApplication {

    public static void main(String[] args) {

        ApplicationContext context = SpringApplication.run(TestWEbbApplication.class, args);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);

            //! ❗ Spring konteyner orqali botni olish
            MyTelegramBot bot = context.getBean(MyTelegramBot.class);

            botsApi.registerBot(bot);
            System.out.println("✅ Bot ishga tushdi!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
