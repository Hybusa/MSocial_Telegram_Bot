package com.github.hybusa.taskMSocial.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long chatId;

    private Instant lastMessageAt;

    public User(Long id, Instant now) {
        this.chatId = id;
        this.lastMessageAt = now;
    }
}