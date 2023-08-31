package com.github.hybusa.taskMSocial.repository;

import com.github.hybusa.taskMSocial.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}