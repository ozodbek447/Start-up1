package com.example.testwebb.server;

import com.example.testwebb.entity.Users;
import com.example.testwebb.entity.Word;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.FileWriter;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class Server {



    public static List<Word> words() {
        try (FileReader reader = new FileReader("data/word.json")) {
            Type type = new TypeToken<List<Word>>(){}.getType();
            return new Gson().fromJson(reader, type);
        } catch (Exception e) {
            return new ArrayList<>(); // fayl bo‘lmasa bo‘sh list
        }

    }
}
