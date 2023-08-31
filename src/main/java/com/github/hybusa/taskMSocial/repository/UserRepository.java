package com.github.hybusa.taskMSocial.repository;

import com.github.hybusa.taskMSocial.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByChatId(Long chatId);
    boolean existsByChatId(Long chatId);
}
