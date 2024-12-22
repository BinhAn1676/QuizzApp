package com.annb.quizz.service;

import com.annb.quizz.dto.AnswerSubmission;
import com.annb.quizz.dto.UserScore;
import com.annb.quizz.dto.request.QuestionRequest;
import com.annb.quizz.dto.response.QuestionResponse;
import jakarta.validation.Valid;

public interface QuestionService {
    QuestionResponse updateQuestion(@Valid QuestionRequest req);

    Boolean deleteQuestion(String id);

    QuestionResponse getQuestion(String id);

    QuestionResponse getQuestionFromRoom(String roomId, String id);

    UserScore updateScore(String roomId, AnswerSubmission submission);
}
