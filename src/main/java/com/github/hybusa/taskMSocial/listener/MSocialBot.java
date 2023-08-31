package com.github.hybusa.taskMSocial.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.hybusa.taskMSocial.configuration.BotConfig;

import com.github.hybusa.taskMSocial.entity.User;
import com.github.hybusa.taskMSocial.scheduler.DailyDomainScheduler;
import com.github.hybusa.taskMSocial.service.DailyDomainService;
import com.github.hybusa.taskMSocial.service.MessageService;
import com.github.hybusa.taskMSocial.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

import java.io.IOException;
import java.util.Optional;

@Component
public class MSocialBot extends TelegramLongPollingBot {
    private final Logger logger = LoggerFactory.getLogger(MSocialBot.class);

    private final BotConfig bot;
    private final UserService userService;

    private final MessageService messageService;
    private final DailyDomainService dailyDomainService;

    public MSocialBot(BotConfig bot,
                      UserService userService,
                      MessageService messageService,
                      DailyDomainService dailyDomainService) {
        this.bot = bot;
        this.userService = userService;
        this.messageService = messageService;
        this.dailyDomainService = dailyDomainService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        logger.info("Update received" + update);
        if (update.hasMessage()) {
            long userChatId = update.getMessage().getFrom().getId();
            Optional<User> userOptional= userService.findByChatId(userChatId);
            if (userOptional.isEmpty()) {
                userService.createUser(userChatId);
            }else{
                userService.updateMessageTime(userOptional.get());
            }
            String receivedMessage = update.getMessage().getText();
            messageService.createReceiveMessage(receivedMessage, userChatId);
            String sendMessage;
            switch (receivedMessage) {
                case "/start":

                    sendMessage = "Hello, " + update.getMessage().getFrom().getFirstName();
                    break;
                default:
                    sendMessage = "Echo: " + update.getMessage().getText();
            }
            createAndSendMessage(userChatId, sendMessage);

         Thread thread = new Thread(() -> {
             int count = 0;
             try {
                 count = DailyDomainScheduler.fetchAndUpdateData();
             }catch (IOException e){
                 logger.error(e.getMessage());
                 e.printStackTrace();
             }
             createAndSendMessage(userChatId, String.valueOf(count));
         });
            thread.start();
        }


    }

    private void createAndSendMessage(long userChatId, String sendMessage) {
        try {
            execute(SendMessage.builder()
                    .chatId(userChatId)
                    .text(sendMessage).build());
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
        messageService.createSendMessage(sendMessage, userChatId);
    }

    @Override
    public void clearWebhook() throws TelegramApiRequestException {

    }

    @Override
    public String getBotUsername() {
        return bot.getBotName();
    }

    @Override
    public String getBotToken() {
        return bot.getToken();
    }
}
