package com.annb.quizz.controller;

import com.annb.quizz.dto.request.TopicRequest;
import com.annb.quizz.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/topic")
@RequiredArgsConstructor
public class TopicController {
    private final TopicService topicService;

    @GetMapping
    public ResponseEntity<?> getAllTopics() {
        var res = topicService.getAll();
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getTopic(@PathVariable("code") String code) {
        var res = topicService.getByCode(code);
        return ResponseEntity.ok(res);
    }
    @PostMapping
    public ResponseEntity<?> updateTopic(@Valid @RequestBody TopicRequest req) {
        var res = topicService.updateByCode(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping
    public ResponseEntity<?> addTopic(@Valid @RequestBody TopicRequest topic) {
        var res = topicService.createTopic(topic);
        return ResponseEntity.ok(res);
    }
}
