package com.example.testwebb;

import com.example.testwebb.data.DataBase;
import com.example.testwebb.entity.UserWord;
import com.example.testwebb.entity.Users;
import com.example.testwebb.entity.Word;
import com.example.testwebb.repository.UserRepository;
import com.example.testwebb.repository.UserWordRepository;
import com.example.testwebb.server.Server;
import com.example.testwebb.server.UserService;
import com.example.testwebb.server.WordService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.*;

@Component
@RequiredArgsConstructor
public class MyTelegramBot extends TelegramLongPollingBot {

    private final UserService userService;
    private final WordService wordService;
    private final UserRepository userRepository;
    private final UserWordRepository userWordRepository;
    @Value("${telegram.bot.username}")
    String username;

    @Value("${telegram.bot.token}")
    String token;

//    private Long adminId=1817365313L;
//    private Map<Long, List<Word>> usersWords = new HashMap<>();




    @Override
    public void onUpdateReceived(Update update) {


        if (!update.hasMessage()) return;
       if (update.hasMessage()){


           Message message = update.getMessage();
           Long chatId = message.getChat().getId();
           String text = message.getText();
           Users user = userService.getUserId(chatId);

           //! yangi foydalanuvchi ruyhatdan tishi
           if (user == null){
               sendMessage("Salom bottimizga xush kelibsiz\n Hayotingizdagi yana bir tug'ri tanlovni" +
                       " amalga oshirish uchun iltimos ruyhatdan o'ting.Username kiriting ",chatId);

               Users users=new Users();
               users.setStep("ism");
               users.setId(chatId);
               userRepository.save(users);
               return;
           }


           //! foydalanubvchi ismini olish
           if (user.getStep().equals("ism")){
               user.setName(text);
               user.setStep("menu");
               userRepository.save(user);
               boshlash(user.getId(),"Qoyil siz ruyhatdan utishni tugatdingiz .\n" +
                       "Buni 50% insonlar qila olmaydi .menu dagi battinlardan birini tanla");

               return;
           }
           if(!userWordRepository.existsByUserId(user.getId())){
               UserWord userWord=new UserWord();
               userWord.setUserId(user.getId());
               userWord.setWords(wordService.getWords());
               userWordRepository.save(userWord);


           }
           if(user.getStep() == null){
               user.setStep("menu");
               userRepository.save(user);

           }

           if ( text.startsWith("AI bilan jang")) {
               user.setStep("0");
               user.setAnswers(0);
               user.setFight(user.getFight()+1);
               wordBatting(userService.getUserWord(user.getId()).getWords().get(0),user.getId());
               userRepository.save(user);
               return;
           }

           if (user.getStep().matches("\\d+")) {
               int index = Integer.parseInt(user.getStep());
               Word currentWord = userService.getUserWord(user.getId()).getWords().get(index);

               if (currentWord.getTranslation().equalsIgnoreCase(text)) {
                   user.setAnswers(user.getAnswers() + 1);
                   user.getAnswer().add(text);
                   userRepository.save(user);
               }else {
                   user.getAnswer().add(text);
                   userRepository.save(user);
               }

               index++;

               if (index < 10) {
                   user.setStep(String.valueOf(index));
                   wordBatting(userService.getUserWord(user.getId()).getWords().get(index),user.getId());
                   userRepository.save(user);
               } else {

                   user.setStep("menu");
                   user.setScore(user.getScore() + user.getAnswers());


                   sendMessage(
                           toStringg(userService.getUserWord(user.getId()).getWords(),user),
                           chatId);
                   userService.getUserWord(user.getId()).getWords().clear();
                   userService.saveUserWord(userService.getUserWord(user.getId()));
                   userRepository.save(user);
                   return;
               }
           }
           if (text.equals("orqaga1")) {
               boshlash(user.getId(),"Tanla aql👇");

               userRepository.save(user);
               return;

           }
           if (text.equals("mening malumotlarim1")) {
               sendMessage("👨‍🎓Foydalanuvchi: "+user.getName()+
                       "\n🌟Umumiy ball: "+user.getScore()+
                       "\n⚔Umumiy janglar soni: "+user.getFight()+
                       "\n📋Dataja:"+userService.getUser(user),user.getId());
               user.setStep("menu");
               userRepository.save(user);
               return;
           }
           //! jangni boshlash
           if (user.getStep().equals("menu")){
               boshlash(user.getId(),"Tanla aql👇");

               userRepository.save(user);

               return;

           }




       }



    }
    private void  sendMessage(String text ,Long chatId){
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        try {
            execute(sendMessage);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void boshlash(Long chatId,String text) {

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();


        KeyboardRow row = new KeyboardRow();
        row.add("AI bilan jang");
        keyboard.add(row);

        KeyboardRow row12 = new KeyboardRow();
        row12.add("mening malumotlarim1");
        keyboard.add(row12);

        KeyboardRow row11 = new KeyboardRow();
        row11.add("orqaga1");
        keyboard.add(row11);

        replyKeyboardMarkup.setKeyboard(keyboard);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        message.setReplyMarkup(replyKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
    private void wordBatting(Word word, Long chatId) {

        List<Word> variants = wordService.getWord(word);

        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow row = new KeyboardRow();
        for (Word w : variants) {
            row.add(w.getTranslation());
        }
        keyboard.add(row);

        replyKeyboardMarkup.setKeyboard(keyboard);

        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText("So‘z tarjimasini tanlang: \n\n👉 " + word.getText());
        message.setReplyMarkup(replyKeyboardMarkup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public String toStringg(List<Word> words,Users users) {
        String text = "";

        text="👨‍🎓Foydalanuvchi: "+users.getName()+
           "\n🌟Umumiy ball: "+users.getScore()+
           "\n⚔Umumiy janglar soni: "+users.getFight()+
           "\n📋Dataja:"+userService.getUser(users)+"/"+"\n"+"\n"+"Suzlaringiz\n\n";
        int i=0;
        for (Word w : words) {
            if (users.getAnswer().get(i).equals(w.getTranslation())){
                text=text+w.getText()+" = "+users.getAnswer().get(i)+" ✅\n";

            }else {
                text=text+w.getText()+" = "+users.getAnswer().get(i)+" ❌\n";
            }
            i++;
        }
        users.getAnswer().clear();
        userRepository.save(users);
        userWordRepository.delete(userService.getUserWord(users.getId()));



        return text+"\nXohlagan harfni bossang menuga tushasan";


    }




    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }
}
