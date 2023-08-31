package com.github.hybusa.taskMSocial.listener;

import com.github.hybusa.taskMSocial.entity.User;
import com.github.hybusa.taskMSocial.scheduler.DailyDomainScheduler;
import com.github.hybusa.taskMSocial.service.MessageService;
import com.github.hybusa.taskMSocial.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.DefaultAbsSender;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MSocialBot extends DefaultAbsSender implements LongPollingBot {
    private final Logger logger = LoggerFactory.getLogger(MSocialBot.class);
    private final UserService userService;
    private final MessageService messageService;

    @Value("${telegram.bot.name}")
    String botName;

    public MSocialBot(@Value("${telegram.bot.token}") String botToken,
                      UserService userService,
                      MessageService messageService) {
        super(new DefaultBotOptions(),botToken);
        this.userService = userService;
        this.messageService = messageService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        logger.info("Update received" + update);
        if (update.hasMessage()) {
            long userChatId = update.getMessage().getFrom().getId();
            Optional<User> userOptional = userService.findByChatId(userChatId);
            if (userOptional.isEmpty()) {
                userService.createUser(userChatId);
            } else {
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
        logger.info("message: \"" + sendMessage + "\" sent and saved");
    }

    @Scheduled(cron = "${cron.value}")
    public void scheduled() {
        AtomicInteger count = new AtomicInteger();
        CompletableFuture.runAsync(() -> {
            try {
                count.set(DailyDomainScheduler.fetchAndUpdateData());
            } catch (IOException e) {
                logger.error(Arrays.toString(e.getStackTrace()));
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();
            String message = dtf.format(now) + ". Collected " + count + " domens";

            List<Long> chatIds = userService.getAllUsersChatIds();
            for (Long chatId : chatIds) {
                createAndSendMessage(chatId, message);
            }
        });
    }


    @Override
    public String getBotUsername() {
        return botName;
    }


    @Override
    public void clearWebhook() throws TelegramApiRequestException {

    }

}
