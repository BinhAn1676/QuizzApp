package com.annb.quizz.dto.request;

import com.annb.quizz.dto.AttemptDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizAttemptRequest {
    private String id;
    private Double score;
    private String quizzId;
    private Boolean isPass;
    private List<AttemptDTO> questions;
}
