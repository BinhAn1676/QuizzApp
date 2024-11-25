package com.annb.quizz.service.impl;

import com.annb.quizz.repository.ChatBotLogRepository;
import com.annb.quizz.service.ChatBotLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatBotLogServiceImpl implements ChatBotLogService {
    private final ChatBotLogRepository chatBotLogRepository;
}
