package com.annb.quizz.controller;

import com.annb.quizz.dto.AnswerSubmission;
import com.annb.quizz.dto.UserScore;
import com.annb.quizz.dto.response.QuestionResponse;
import com.annb.quizz.exception.ResourceNotFoundException;
import com.annb.quizz.service.QuestionService;
import com.annb.quizz.service.QuizzService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class QuizWebSocketController {

    private final QuestionService questionService;

    // This method is triggered to send the next question when requested
    @MessageMapping("/room/{roomId}/next-question")
    @SendTo("/topic/room/{roomId}/questions") // This is the topic participants subscribe to
    public QuestionResponse sendNextQuestion(@DestinationVariable String roomId,String id) {
        return questionService.getQuestionFromRoom(roomId,id); // This sends the question to the specified topic
    }
    @MessageMapping("/room/{roomId}/update-score")
    @SendTo("/topic/room/{roomId}/user-score") // Broadcast updated user data to all participants
    public UserScore updateUserScore(@DestinationVariable String roomId, @Payload UserScore userScore) {
        var response = questionService.updateScore(roomId,userScore);
        return response;
    }
}