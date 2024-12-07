package com.annb.quizz.controller;

import com.annb.quizz.dto.request.BaseFilter;
import com.annb.quizz.dto.request.message.MessageModel;
import com.annb.quizz.service.AIChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class AIChatController {
    private final AIChatService aiChatService;

    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessage(@Valid @RequestBody MessageModel msg) {
        var result = aiChatService.sendMessage(msg);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/get-message")
    public ResponseEntity<?> getMessage(@RequestBody BaseFilter req) {
        return ResponseEntity.ok(aiChatService.getMessage(req));
    }

    @PostMapping("/save-message")
    public ResponseEntity<?> saveMessage(@Valid @RequestBody MessageModel req) {
        return ResponseEntity.ok(aiChatService.saveMessage(req));
    }
}
