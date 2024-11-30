package com.annb.quizz.controller;

import com.annb.quizz.dto.QuizSuggestionDTO;
import com.annb.quizz.dto.request.QuizRequest;
import com.annb.quizz.dto.request.BaseFilter;
import com.annb.quizz.dto.request.QuizUpdateRequest;
import com.annb.quizz.service.QuizzService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuizById(@PathVariable("id") String id) {
        var result = quizzService.getQuiz(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/filter")
    public ResponseEntity<?> filterQuiz(@Valid @RequestBody BaseFilter req) {
        var result = quizzService.filter(req);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateQuiz(@Valid @RequestBody QuizUpdateRequest quizDto) throws ExecutionException, InterruptedException {
        var result = quizzService.updateQuiz(quizDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> updateQuiz(@PathVariable("id") String id) throws ExecutionException, InterruptedException {
        var result = quizzService.deleteById(id);
        if(result){
            return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
        }
        return new ResponseEntity<>("Failed to delete quiz", HttpStatus.BAD_REQUEST);
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
            var result = quizzService.createQuiz(quizRequest);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error processing file: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/top-suggestions")
    public ResponseEntity<Page<QuizSuggestionDTO>> getTopQuizzes(@RequestBody BaseFilter request) {
        Page<QuizSuggestionDTO> suggestions = quizzService.getTopSuggestedQuizzes(request);
        return ResponseEntity.ok(suggestions);
    }
}
