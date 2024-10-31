package com.annb.quizz.service;

import com.annb.quizz.dto.request.AttemptLogRequest;
import com.annb.quizz.dto.request.BaseFilter;
import com.annb.quizz.dto.request.QuizAttemptRequest;
import com.annb.quizz.dto.response.QuizAttemptResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

public interface QuizzAttemptService {
    QuizAttemptResponse createQuizLog(@Valid QuizAttemptRequest req);

    Page<QuizAttemptResponse> fitlerQuizLog(@Valid AttemptLogRequest req);

}
