package com.annb.quizz.controller;

import com.annb.quizz.dto.request.BaseFilter;
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

    @PostMapping("filter")
    public ResponseEntity<?> getTopics(@RequestBody @Valid BaseFilter request) {
        var res = topicService.getTopics(request);
        return ResponseEntity.ok(res);
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getTopic(@PathVariable("code") String code) {
        var res = topicService.getByCode(code);
        return ResponseEntity.ok(res);
    }
    @PostMapping("/update")
    public ResponseEntity<?> updateTopic(@Valid @RequestBody TopicRequest req) {
        var res = topicService.updateByCode(req);
        return ResponseEntity.ok(res);
    }

    @PostMapping("/create")
    public ResponseEntity<?> addTopic(@Valid @RequestBody TopicRequest topic) {
        var res = topicService.createTopic(topic);
        return ResponseEntity.ok(res);
    }
}
