package com.annb.quizz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizAttemptResponse {
    private String id;
    private Double score;
    private String quizzId;
    private Boolean isPass;
    private String username;
    private LocalDateTime time;
}
