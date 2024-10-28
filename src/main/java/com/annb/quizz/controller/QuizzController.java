package com.annb.quizz.controller;

import com.annb.quizz.dto.request.QuizRequest;
import com.annb.quizz.dto.request.BaseFilter;
import com.annb.quizz.service.QuizzService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/quiz")
@RequiredArgsConstructor
public class QuizzController {
    private final QuizzService quizzService;

    @PostMapping("/create")
    public ResponseEntity<?> createQuiz(@RequestBody QuizRequest quizDto) throws ExecutionException, InterruptedException {
        var result = quizzService.createQuiz(quizDto);
        if(result){
            return new ResponseEntity<>("createdQuiz successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("createdQuiz Failed", HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizById(@PathVariable("id") String id) {
        var result = quizzService.getQuiz(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    @GetMapping("/filter")
    public ResponseEntity<?> filterQuiz(@Valid @RequestBody BaseFilter req) {
        var result = quizzService.filter(req);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
