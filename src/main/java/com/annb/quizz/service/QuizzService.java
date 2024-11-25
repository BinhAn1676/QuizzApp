package com.annb.quizz.service;

import com.annb.quizz.dto.QuizDto;
import com.annb.quizz.dto.request.QuizRequest;
import com.annb.quizz.dto.request.BaseFilter;
import com.annb.quizz.dto.request.QuizUpdateRequest;
import com.annb.quizz.dto.response.QuizResponse;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface QuizzService {
    QuizResponse createQuiz(QuizRequest quizDto) throws ExecutionException, InterruptedException;

    QuizResponse getQuiz(String id);

    Page<QuizDto> filter(@Valid BaseFilter req);

    QuizResponse updateQuiz(@Valid QuizUpdateRequest quizDto);

    List<String> getQuestionIds(String id);

    QuizRequest parseExcelFile(MultipartFile file) throws IOException;

    Boolean deleteById(String id);
}
