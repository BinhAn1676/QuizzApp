package com.annb.quizz.service;

import com.annb.quizz.dto.request.QuizDto;

import java.util.concurrent.ExecutionException;

public interface QuizzService {
    Boolean createQuiz(QuizDto quizDto) throws ExecutionException, InterruptedException;
}
