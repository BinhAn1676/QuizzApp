package com.annb.quizz.controller;

import com.annb.quizz.dto.request.QuizRequest;
import com.annb.quizz.dto.request.BaseFilter;
import com.annb.quizz.dto.request.QuizUpdateRequest;
import com.annb.quizz.service.QuizzService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/v1/quiz")
@RequiredArgsConstructor
public class QuizzController {
    private final QuizzService quizzService;

    @PostMapping("/create")
    public ResponseEntity<?> createQuiz(@RequestBody QuizRequest quizDto) throws ExecutionException, InterruptedException {
        var result = quizzService.createQuiz(quizDto);
        if (result) {
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

    @PostMapping("/update")
    public ResponseEntity<?> updateQuiz(@Valid @RequestBody QuizUpdateRequest quizDto) throws ExecutionException, InterruptedException {
        var result = quizzService.updateQuiz(quizDto);
        if (result) {
            return new ResponseEntity<>("updated quiz successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("updated quiz Failed", HttpStatus.BAD_REQUEST);
    }
    @GetMapping("/question-ids")
    public ResponseEntity<?> getALlQuestionIdByQuizId(@RequestParam("id") String id) {
        var result = quizzService.getQuestionIds(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/import")
    public ResponseEntity<?> importQuiz(@RequestParam("file") MultipartFile file) {
        try {
            QuizRequest quizRequest = quizzService.parseExcelFile(file);
            boolean result = quizzService.createQuiz(quizRequest);
            if (result) {
                return new ResponseEntity<>("Quiz imported successfully", HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>("Quiz import failed", HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
