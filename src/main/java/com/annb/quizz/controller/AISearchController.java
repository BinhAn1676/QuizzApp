package com.annb.quizz.controller;

import com.annb.quizz.dto.request.message.MessageRequest;
import com.annb.quizz.service.AISearchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class AISearchController {
    private final AISearchService aiSearchService;

    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessage(@Valid @RequestBody MessageRequest msg) {
        var result = aiSearchService.sendMessage(msg);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/send-message-with-context")
    public ResponseEntity<?> sendMessageWithContext(@Valid @RequestBody MessageRequest msg) {
        var result = aiSearchService.sendMessageWithContext(msg);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
