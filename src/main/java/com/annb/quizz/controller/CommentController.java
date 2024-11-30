package com.annb.quizz.controller;

import com.annb.quizz.dto.request.CommentRequest;
import com.annb.quizz.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<?> addComment(@RequestBody CommentRequest commentRequest) {
        return ResponseEntity.ok(commentService.create(commentRequest));
    }
    @PostMapping("/add-reply")
    public ResponseEntity<?> addReply(@RequestBody CommentRequest commentRequest) {
        return ResponseEntity.ok(commentService.addReply(commentRequest));
    }
}
