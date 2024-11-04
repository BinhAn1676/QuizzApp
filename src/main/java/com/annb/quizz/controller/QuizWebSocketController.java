package com.annb.quizz.controller;

import com.annb.quizz.dto.response.QuestionResponse;
import com.annb.quizz.service.QuestionService;
import com.annb.quizz.service.QuizzService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
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
        var nextQuestion = questionService.getQuestionFromRoom(roomId,id);
        if (nextQuestion == null) {
            // Handle the case when there are no more questions
            throw new RuntimeException("No more questions available");
        }
        return nextQuestion; // This sends the question to the specified topic
    }

}