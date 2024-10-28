package com.annb.quizz.service.impl;

import com.annb.quizz.constant.CommonConstant;
import com.annb.quizz.dto.QuizDto;
import com.annb.quizz.dto.request.AnswerRequest;
import com.annb.quizz.dto.request.QuestionRequest;
import com.annb.quizz.dto.request.QuizRequest;
import com.annb.quizz.dto.request.BaseFilter;
import com.annb.quizz.dto.response.AnswerResponse;
import com.annb.quizz.dto.response.QuestionResponse;
import com.annb.quizz.dto.response.QuizResponse;
import com.annb.quizz.entity.Answer;
import com.annb.quizz.entity.Question;
import com.annb.quizz.entity.Quizz;
import com.annb.quizz.exception.ResourceNotFoundException;
import com.annb.quizz.repository.AnswerRepository;
import com.annb.quizz.repository.QuestionRepository;
import com.annb.quizz.repository.QuizzRepository;
import com.annb.quizz.repository.TopicRepository;
import com.annb.quizz.service.QuizzService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizzServiceImpl implements QuizzService {
    private final QuizzRepository quizzRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final TopicRepository topicRepository;

    @Override
    @Transactional
    public Boolean createQuiz(QuizRequest quizDto) {
        // Create and save the quiz
        var topic = topicRepository.findByCode(quizDto.getTopicCode())
                .orElseThrow(() -> new ResourceNotFoundException("Topic", "code", quizDto.getTopicCode()));
        Quizz quiz = new Quizz();
        quiz.setId(UUID.randomUUID().toString().replace("-", ""));
        quiz.setTitle(quizDto.getTitle());
        quiz.setDescription(quizDto.getDescription());
        quiz.setStatus(CommonConstant.Status.ACTIVE);
        quiz.setTopic(topic);
        var quizSaved = quizzRepository.saveAndFlush(quiz); // Ensure quiz is persisted

        // Loop through each question and save it
        for (QuestionRequest questionDto : quizDto.getQuestions()) {
            Question question = new Question();
            question.setId(UUID.randomUUID().toString().replace("-", ""));
            question.setContent(questionDto.getContent());
            question.setQuizz(quizSaved); // Associate quiz with question
            question.setImageUrl(questionDto.getImageUrl());
            question.setQuestionType(questionDto.getQuestionType() == 1 ? CommonConstant.QuestionType.MULTIPLE_CHOICE : CommonConstant.QuestionType.WRITE_CHOICE);
            var questionSaved = questionRepository.save(question);

            // Save answers corresponding to the question
            for (AnswerRequest answerDto : questionDto.getAnswers()) {
                Answer answer = new Answer();
                answer.setId(UUID.randomUUID().toString().replace("-", ""));
                answer.setContent(answerDto.getContent());
                answer.setIsCorrect(answerDto.getIsCorrect());
                answer.setQuestion(questionSaved); // Associate answer with question
                answerRepository.save(answer);
            }
        }
        return true;
    }

    @Override
    public QuizResponse getQuiz(String id) {
        var quiz = quizzRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", id));
        QuizResponse response = new QuizResponse();
        response.setTitle(quiz.getTitle());
        response.setId(quiz.getId());
        response.setDescription(quiz.getDescription());
        response.setTopicCode(quiz.getTopic().getCode());
        response.setQuestions(
                quiz.getQuestions().stream().map(question -> {
                    var questionDto = new QuestionResponse();
                    questionDto.setId(question.getId());
                    questionDto.setContent(question.getContent());
                    questionDto.setQuestionType(question.getQuestionType());
                    questionDto.setImageUrl(question.getImageUrl());
                    questionDto.setAnswers(
                            question.getAnswers().stream().map(answer -> {
                                var answerDto = new AnswerResponse();
                                answerDto.setId(answer.getId());
                                answerDto.setContent(answer.getContent());
                                answerDto.setIsCorrect(answer.getIsCorrect());
                                return answerDto;
                            }).collect(Collectors.toList())
                    );
                    return questionDto;
                }).collect(Collectors.toList())
        );

        return response;
    }

    @Override
    public Page<QuizDto> filter(BaseFilter req) {
        Pageable pageable = PageRequest.of(req.getPageNo(), req.getPageSize());
        var quizzes =  quizzRepository.findFiltered(req.getTextSearch(), req.getFrom(), req.getTo(),req.getStatus(), pageable);
        return quizzes.map(quiz -> {
            QuizDto dto = new QuizDto();
            dto.setId(quiz.getId());
            dto.setTitle(quiz.getTitle());
            dto.setTopicCode(quiz.getTopic().getCode());
            dto.setDescription(quiz.getDescription());
            dto.setStatus(quiz.getStatus());
            return dto;
        });
    }
}
