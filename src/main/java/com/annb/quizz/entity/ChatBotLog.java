package com.annb.quizz.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_bot_log")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatBotLog extends BaseEntity{
    @Id
    private String id;
    private String role;
    @Column(name = "message",columnDefinition = "LONGTEXT")
    private String message;
}
