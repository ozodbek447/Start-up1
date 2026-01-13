package com.example.testwebb.data;

import com.example.testwebb.MyTelegramBot;
import com.example.testwebb.entity.Users;
import com.example.testwebb.entity.Word;
import com.example.testwebb.repository.WordRepository;
import com.example.testwebb.server.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Run implements CommandLineRunner {

    private final WordRepository wordRepository;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    @Override
    public void run(String... args) throws Exception {

        if (ddl.equals("create")) {
            List<Word> words = Server.words();
            wordRepository.saveAll(words);

        }


    }
}
