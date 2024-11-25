package com.annb.quizz.repository;

import com.annb.quizz.entity.ChatBotLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatBotLogRepository  extends JpaRepository<ChatBotLog, String> {
}
