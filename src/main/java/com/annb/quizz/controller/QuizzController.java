package com.annb.quizz.controller;

import com.annb.quizz.dto.request.QuizDto;
import com.annb.quizz.service.QuizzService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/quizz")
@RequiredArgsConstructor
public class QuizzController {
    private final QuizzService quizzService;

    @PostMapping
    public ResponseEntity<?> createQuiz(@RequestBody QuizDto quizDto) throws ExecutionException, InterruptedException {
        var result = quizzService.createQuiz(quizDto);
        if(result){
            return new ResponseEntity<>("createdQuiz", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("createdQuiz Failed", HttpStatus.BAD_REQUEST);

    }
}
