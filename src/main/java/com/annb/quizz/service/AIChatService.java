package com.annb.quizz.service;

import com.annb.quizz.dto.request.message.MessageModel;
import com.annb.quizz.dto.response.gemini.GeminiResponse;
import jakarta.validation.Valid;

public interface AIChatService {

    GeminiResponse sendMessage(@Valid MessageModel req);

    MessageModel getMessage(String username);

    MessageModel saveMessage(@Valid MessageModel req);
}
