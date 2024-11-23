package com.annb.quizz.service;

import com.annb.quizz.dto.request.message.MessageRequest;
import com.annb.quizz.dto.response.gemini.GeminiResponse;
import jakarta.validation.Valid;

public interface AISearchService {

    GeminiResponse sendMessage(@Valid MessageRequest req);

}
