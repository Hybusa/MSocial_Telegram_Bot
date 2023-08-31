package com.github.hybusa.taskMSocial.service;

import com.github.hybusa.taskMSocial.entity.Message;
import com.github.hybusa.taskMSocial.entity.User;
import com.github.hybusa.taskMSocial.repository.MessageRepository;
import com.github.hybusa.taskMSocial.repository.UserRepository;
import lombok.AllArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public void createSendMessage(String sendMessage, long userChatId) {
        Optional<User> userOptional = userRepository.findByChatId(userChatId);
        if(userOptional.isEmpty()) {
            return;
        }
        Message message = new Message();
        message.setToUser(sendMessage);

        message.setUser(userOptional.get());
        messageRepository.save(message);
        log.info("outgoing message saved");
    }

    public void createReceiveMessage(String receivedMessage, long userChatId){
        Optional<User> userOptional = userRepository.findByChatId(userChatId);
        if(userOptional.isEmpty()) {
            return;
        }
        Message message = new Message();

        message.setUser(userOptional.get());
        message.setFromUser(receivedMessage);

        messageRepository.save(message);
        log.info("incoming message saved");
    }
}
