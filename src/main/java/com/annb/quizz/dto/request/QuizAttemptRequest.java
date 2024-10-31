package com.annb.quizz.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizAttemptRequest {
    private String id;
    private Double score;
    private String quizzId;
    private Boolean isPass;

}
