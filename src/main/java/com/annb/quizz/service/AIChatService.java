package com.annb.quizz.service;

import com.annb.quizz.dto.request.BaseFilter;
import com.annb.quizz.dto.request.message.BaseContent;
import com.annb.quizz.dto.request.message.MessageModel;
import com.annb.quizz.dto.response.gemini.GeminiResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface AIChatService {

    GeminiResponse sendMessage(@Valid MessageModel req);

    Page<BaseContent> getMessage(BaseFilter username);

    MessageModel saveMessage(@Valid MessageModel req);
}
