package com.github.hybusa.taskMSocial.service;

import com.github.hybusa.taskMSocial.entity.User;
import com.github.hybusa.taskMSocial.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public void createUser(Long chatId) {
        com.github.hybusa.taskMSocial.entity.User user =
                new com.github.hybusa.taskMSocial.entity.User(chatId, Instant.now());
        userRepository.save(user);
    }

    public boolean userExistsByChatId(long userChatId) {
        return userRepository.existsByChatId(userChatId);
    }

    public void updateMessageTime(User user) {
        user.setLastMessageAt(Instant.now());
        userRepository.save(user);
    }
    public Optional<User> findByChatId(long chatId){
        return userRepository.findByChatId(chatId);
    }
}
