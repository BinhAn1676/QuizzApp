package com.annb.quizz.service;

import com.annb.quizz.dto.QuizDto;
import com.annb.quizz.dto.request.QuizRequest;
import com.annb.quizz.dto.request.BaseFilter;
import com.annb.quizz.dto.response.QuizResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import java.util.concurrent.ExecutionException;

public interface QuizzService {
    Boolean createQuiz(QuizRequest quizDto) throws ExecutionException, InterruptedException;

    QuizResponse getQuiz(String id);

    Page<QuizDto> filter(@Valid BaseFilter req);
}
