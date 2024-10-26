package com.annb.quizz.controller;

import com.annb.quizz.service.QuizzService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/quizz")
@RequiredArgsConstructor
public class QuizzController {
    private final QuizzService quizzService;
}
