package com.annb.quizz.service.impl;

import com.annb.quizz.dto.request.QuestionRequest;
import com.annb.quizz.dto.response.AnswerResponse;
import com.annb.quizz.dto.response.QuestionResponse;
import com.annb.quizz.entity.Answer;
import com.annb.quizz.entity.Question;
import com.annb.quizz.exception.ResourceNotFoundException;
import com.annb.quizz.repository.QuestionRepository;
import com.annb.quizz.repository.RoomRepository;
import com.annb.quizz.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final RoomRepository roomRepository;
    @Override
    public QuestionResponse updateQuestion(QuestionRequest req) {
        var question = questionRepository.findById(req.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", req.getId()));
        question.setQuestionType(req.getQuestionType());
        question.setContent(req.getContent());
        question.setImageUrl(req.getImageUrl());
        // Get existing answers
        var existingAnswers = question.getAnswers();

        // Update or remove answers based on the request
        List<Answer> updatedAnswers = req.getAnswers().stream().map(answerReq -> {
            Answer answer = existingAnswers.stream()
                    .filter(a -> a.getId().equals(answerReq.getId()))
                    .findFirst()
                    .orElseGet(() -> {
                        Answer newAnswer = new Answer();
                        newAnswer.setId(UUID.randomUUID().toString().replace("-",""));
                        return newAnswer;
                    });

            answer.setContent(answerReq.getContent());
            answer.setIsCorrect(answerReq.getIsCorrect());
            answer.setQuestion(question); // Ensure the relationship is set
            return answer;
        }).collect(Collectors.toList());

        // Set the updated answers list
        question.setAnswers(updatedAnswers);

        // Save updated question and answers
        var savedQuestion = questionRepository.save(question);
        var response = new QuestionResponse();
        response.setId(savedQuestion.getId());
        response.setQuestionType(savedQuestion.getQuestionType());
        response.setContent(savedQuestion.getContent());
        response.setImageUrl(savedQuestion.getImageUrl());
        var answerResponse = savedQuestion.getAnswers()
                .stream().map(item -> {
                    AnswerResponse res = new AnswerResponse();
                    res.setId(item.getId());
                    res.setContent(item.getContent());
                    res.setIsCorrect(item.getIsCorrect());
                    return  res;
                }).toList();
        response.setAnswers(answerResponse);
        return response;
    }

    @Override
    public Boolean deleteQuestion(String id) {
        var isExist = questionRepository.existsById(id);
        if (isExist) {
            questionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public QuestionResponse getQuestion(String id) {
        var question = questionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Question", "id", id));
        var response = new QuestionResponse();
        response.setQuestionType(question.getQuestionType());
        response.setContent(question.getContent());
        response.setImageUrl(question.getImageUrl());
        response.setId(question.getId());
        var answerResponse = question.getAnswers()
                .stream().map(item -> {
                    AnswerResponse res = new AnswerResponse();
                    res.setId(item.getId());
                    res.setContent(item.getContent());
                    res.setIsCorrect(item.getIsCorrect());
                    return  res;
                }).toList();
        response.setAnswers(answerResponse);
        return response;
    }

    @Override
    public QuestionResponse getQuestionFromRoom(String roomId, String id) {
        var room = roomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room", "id", roomId));
        var questionOptional = room.getQuiz().getQuestions().stream().filter(q -> q.getId().equals(id)).findFirst();
        if(questionOptional.isEmpty()) {
            throw new ResourceNotFoundException("Question", "id", id);
        }
        Question question = questionOptional.get();
        var response = new QuestionResponse();
        response.setQuestionType(question.getQuestionType());
        response.setContent(question.getContent());
        response.setImageUrl(question.getImageUrl());
        response.setId(question.getId());
        var answerResponse = question.getAnswers()
                .stream().map(item -> {
                    AnswerResponse res = new AnswerResponse();
                    res.setId(item.getId());
                    res.setContent(item.getContent());
                    res.setIsCorrect(item.getIsCorrect());
                    return  res;
                }).toList();
        response.setAnswers(answerResponse);
        return response;
    }
}
