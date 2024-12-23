package com.annb.quizz.controller;

import com.annb.quizz.dto.request.AttemptLogRequest;
import com.annb.quizz.dto.request.BaseFilter;
import com.annb.quizz.dto.request.QuizAttemptRequest;
import com.annb.quizz.service.QuizzAttemptService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/quiz-attemp")
@RequiredArgsConstructor
public class QuizzAttemptController {
    private final QuizzAttemptService quizzAttemptService;

    @PostMapping("/create")
    public ResponseEntity<?> createQuizLog(@Valid @RequestBody QuizAttemptRequest req) {
        var res = quizzAttemptService.createQuizLog(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterQuizLog(@Valid @RequestBody AttemptLogRequest req) {
        var res = quizzAttemptService.fitlerQuizLog(req);
        return ResponseEntity.ok(res);
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizAttempt(@PathVariable("id") String id) {
        var res = quizzAttemptService.getQuizAttempt(id);
        return ResponseEntity.ok(res);
    }

}
