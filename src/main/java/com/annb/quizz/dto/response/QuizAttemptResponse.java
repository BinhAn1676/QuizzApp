package com.annb.quizz.dto.response;

import com.annb.quizz.dto.AttemptDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private List<AttemptDTO> questions;
}
