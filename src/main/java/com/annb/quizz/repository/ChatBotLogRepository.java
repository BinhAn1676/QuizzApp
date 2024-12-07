package com.annb.quizz.repository;

import com.annb.quizz.entity.ChatBotLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatBotLogRepository  extends JpaRepository<ChatBotLog, String> {
    List<ChatBotLog> findAllByCreatedByOrderByCreatedAt(String username);
    Page<ChatBotLog> findAllByCreatedByOrderByCreatedAt(String username, Pageable pageable);
}
