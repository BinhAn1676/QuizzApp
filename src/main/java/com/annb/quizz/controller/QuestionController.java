package com.annb.quizz.controller;

import com.annb.quizz.dto.request.QuestionRequest;
import com.annb.quizz.service.QuestionService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;

    @PostMapping("/update")
    public ResponseEntity<?> updateQuestion(@Valid @RequestBody QuestionRequest req){
        var result = questionService.updateQuestion(req);
        return ResponseEntity.ok(result);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteQuestion(@PathVariable String id){
        var result = questionService.deleteQuestion(id);
        if(result){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build();
    }
    @GetMapping("/detail/{id}")
    public ResponseEntity<?> detailQuestion(@PathVariable String id){
        var result = questionService.getQuestion(id);
        return ResponseEntity.ok(result);
    }
}
