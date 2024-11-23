package com.annb.quizz.service;

import com.annb.quizz.dto.request.message.MessageRequest;
import jakarta.validation.Valid;

public interface AISearchService {

    String sendMessage(@Valid MessageRequest req);

    String sendMessageWithContext(@Valid MessageRequest req);
}
