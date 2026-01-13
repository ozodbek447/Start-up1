package com.example.testwebb.server;

import com.example.testwebb.MyTelegramBot;
import com.example.testwebb.data.DataBase;
import com.example.testwebb.entity.UserWord;
import com.example.testwebb.entity.Users;
import com.example.testwebb.repository.UserRepository;
import com.example.testwebb.repository.UserWordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor

public class UserService {


    private final UserRepository userRepository;
    private final UserWordRepository userWordRepository;
    private final WordService wordService;

    public Users getUserId(Long userId) {

        Optional<Users> first =userRepository.findById(userId);

        return first.orElse(null);

    }
    public int getUser(Users user) {

        List<Users> list = userRepository.findAll().stream().sorted(Comparator.comparingInt(Users::getScore)).toList();

        return list.indexOf(user)+1;


    }
    @Transactional
    public UserWord getUserWord(Long userId) {
        Optional<UserWord> byUserId = userWordRepository.findByUserId(userId);

        return byUserId.orElseGet(() -> {
            UserWord uw = new UserWord(userId, wordService.getWords());
            return userWordRepository.save(uw);
        });

    }

    public void saveUserWord(UserWord userWord) {
        userWordRepository.save(userWord);
    }
}
