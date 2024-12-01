package com.annb.quizz.controller;

import com.annb.quizz.dto.request.CommentRequest;
import com.annb.quizz.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    @PostMapping("/update")
    public ResponseEntity<?> updateComment(@RequestBody CommentRequest commentRequest) {
        return ResponseEntity.ok(commentService.update(commentRequest));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable String id) {
        var result = commentService.deleteComment(id);
        if(result){
            return ResponseEntity.ok("Deleted Successfullu");
        }
        return ResponseEntity.ok("Failed to delete comment");
    }
}
