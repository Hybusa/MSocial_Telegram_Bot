package com.github.hybusa.taskMSocial.service;

import com.github.hybusa.taskMSocial.entity.User;
import com.github.hybusa.taskMSocial.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;

    public void createUser(Long chatId) {
        com.github.hybusa.taskMSocial.entity.User user =
                new com.github.hybusa.taskMSocial.entity.User(chatId, Instant.now());
        userRepository.save(user);
        log.info("New user created");
    }

    public void updateMessageTime(User user) {
        user.setLastMessageAt(Instant.now());
        userRepository.save(user);
        log.info("User last message updated");
    }
    public Optional<User> findByChatId(long chatId){
        return userRepository.findByChatId(chatId);
    }

    public List<Long> getAllUsersChatIds() {
        log.info("All chat called");
        return userRepository.getAllChatIds();

    }
}
