package com.example.testwebb.server;

import com.example.testwebb.data.DataBase;
import com.example.testwebb.entity.Word;
import com.example.testwebb.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class WordService {


    private final WordRepository wordRepository;

    public List<Word> getWords() {
        Random random = new Random();
        List<Word> result = new ArrayList<>();
        List<Word> words = wordRepository.findAll();

        for (int i = 0; i < 10; i++) {
            result.add(words.get(random.nextInt(words.size())));
        }
        return result;

    }

    public List<Word> getWord(Word correctWord) {
        Random random = new Random();
        List<Word> allWords = Server.words();

        Word wrongWord;
        do {
            wrongWord = allWords.get(random.nextInt(allWords.size()));
        } while (wrongWord.getId() == correctWord.getId());

        List<Word> result = new ArrayList<>();
        result.add(correctWord);
        result.add(wrongWord);

        Collections.shuffle(result); // ARALASHTIRISH 🔀
        return result;
    }

}
