package com.annb.quizz.service.impl;

import com.annb.quizz.controller.QuizzAttemptController;
import com.annb.quizz.dto.AttemptDTO;
import com.annb.quizz.dto.request.AttemptLogRequest;
import com.annb.quizz.dto.request.QuizAttemptRequest;
import com.annb.quizz.dto.response.QuizAttemptResponse;
import com.annb.quizz.entity.AttemptAnswer;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
        Quizz quiz = quizzRepository.findById(req.getQuizzId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", req.getId()));

// Tạo bản ghi QuizAttempt
        QuizzAttempt quizAttempt = new QuizzAttempt();
        quizAttempt.setQuizz(quiz);
        quizAttempt.setScore(req.getScore());
        quizAttempt.setId(UUID.randomUUID().toString().replace("-", ""));
        quizAttempt.setIsPass(req.getIsPass());

        // Convert questions from request to AttemptAnswer entities
        List<AttemptAnswer> attemptAnswers = req.getQuestions().stream().map(question -> {
            AttemptAnswer attemptAnswer = new AttemptAnswer();
            attemptAnswer.setId(UUID.randomUUID().toString().replace("-", ""));
            attemptAnswer.setQuizzAttempt(quizAttempt); // Set the parent entity
            attemptAnswer.setQuestionContent(question.getQuestionContent());
            attemptAnswer.setSelectedAnswerContents(String.join("~", question.getSelectedAnswerContents()));
            attemptAnswer.setCorrectAnswerContents(String.join("~", question.getCorrectAnswerContents()));
            return attemptAnswer;
        }).toList();

        // Associate the answers with the quiz attempt
        quizAttempt.setAttemptAnswers(attemptAnswers);

        // Lưu thông tin vào database
        var saved =  quizzAttemptRepository.save(quizAttempt);
        var res = new QuizAttemptResponse();
        res.setId(saved.getId());
        res.setIsPass(saved.getIsPass());
        res.setScore(saved.getScore());
        res.setTime(saved.getCreatedAt());
        res.setUsername(saved.getCreatedBy());
        res.setQuestions(saved.getAttemptAnswers().stream().map(item -> {
            var attempt = new AttemptDTO();
            attempt.setQuestionContent(item.getQuestionContent());
            attempt.setSelectedAnswerContents(item.getSelectedAnswerContents().isEmpty() ?
                    new ArrayList<>() : Arrays.asList(item.getSelectedAnswerContents().split("~")));
            attempt.setCorrectAnswerContents(item.getCorrectAnswerContents().isEmpty() ?
                    new ArrayList<>() : Arrays.asList(item.getCorrectAnswerContents().split("~")));
            return attempt;
        }).toList());
        return res;
    }

    @Override
    public Page<QuizAttemptResponse> fitlerQuizLog(AttemptLogRequest req) {
        Pageable pageable = PageRequest.of(req.getPageNo(), req.getPageSize());
        var quizzAttempts =  quizzAttemptRepository.findFilteredV2(req.getTextSearch(), req.getFrom(), req.getTo(),req.getIsPass(), pageable);
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

    @Override
    public QuizAttemptResponse getQuizAttempt(String id) {
        QuizzAttempt quizAttempt = quizzAttemptRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("QuizAttempt", "id", id));
        var res = new QuizAttemptResponse();
        res.setId(quizAttempt.getId());
        res.setIsPass(quizAttempt.getIsPass());
        res.setScore(quizAttempt.getScore());
        res.setTime(quizAttempt.getCreatedAt());
        res.setUsername(quizAttempt.getCreatedBy());
        res.setQuestions(quizAttempt.getAttemptAnswers().stream().map(item -> {
            var attempt = new AttemptDTO();
            attempt.setQuestionContent(item.getQuestionContent());
            attempt.setSelectedAnswerContents(item.getSelectedAnswerContents().isEmpty() ?
                    new ArrayList<>() : Arrays.asList(item.getSelectedAnswerContents().split("~")));
            attempt.setCorrectAnswerContents(item.getCorrectAnswerContents().isEmpty() ?
                    new ArrayList<>() : Arrays.asList(item.getCorrectAnswerContents().split("~")));
            return attempt;
        }).toList());
        return res;
    }


}
