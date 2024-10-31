package com.annb.quizz.service.impl;

import com.annb.quizz.controller.QuizzAttemptController;
import com.annb.quizz.dto.request.AttemptLogRequest;
import com.annb.quizz.dto.request.QuizAttemptRequest;
import com.annb.quizz.dto.response.QuizAttemptResponse;
import com.annb.quizz.entity.Quizz;
import com.annb.quizz.entity.QuizzAttempt;
import com.annb.quizz.exception.ResourceNotFoundException;
import com.annb.quizz.repository.QuestionRepository;
import com.annb.quizz.repository.QuizzAttemptRepository;
import com.annb.quizz.repository.QuizzRepository;
import com.annb.quizz.service.QuizzAttemptService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class QuizzAttemptServiceImpl implements QuizzAttemptService {
    private final QuizzAttemptRepository quizzAttemptRepository;
    private final QuizzRepository quizzRepository;
    private final QuestionRepository questionRepository;

    @Override
    @Transactional
    public QuizAttemptResponse createQuizLog(QuizAttemptRequest req) {
        Quizz quiz = quizzRepository.findById(req.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", req.getId()));

// Tạo bản ghi QuizAttempt
        QuizzAttempt quizAttempt = new QuizzAttempt();
        quizAttempt.setQuizz(quiz);
        quizAttempt.setScore(req.getScore());
        quizAttempt.setId(UUID.randomUUID().toString().replace("-", ""));
        quizAttempt.setIsPass(req.getIsPass());
        // Lưu thông tin vào database
        var saved =  quizzAttemptRepository.save(quizAttempt);
        var res = new QuizAttemptResponse();
        res.setId(saved.getId());
        res.setIsPass(saved.getIsPass());
        res.setScore(saved.getScore());
        res.setTime(saved.getCreatedAt());
        res.setUsername(saved.getCreatedBy());
        return res;
    }

    @Override
    public Page<QuizAttemptResponse> fitlerQuizLog(AttemptLogRequest req) {
        Pageable pageable = PageRequest.of(req.getPageNo(), req.getPageSize());
        var quizzAttempts =  quizzAttemptRepository.findFiltered(req.getTextSearch(), req.getFrom(), req.getTo(),req.getIsPass(), pageable);
        return quizzAttempts.map(quiz -> {
            QuizAttemptResponse dto = new QuizAttemptResponse();
            dto.setId(quiz.getId());
            dto.setUsername(quiz.getCreatedBy());
            dto.setScore(quiz.getScore());
            dto.setIsPass(quiz.getIsPass());
            dto.setTime(quiz.getCreatedAt());
            return dto;
        });
    }


}
