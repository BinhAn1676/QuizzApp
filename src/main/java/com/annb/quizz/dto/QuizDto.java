package com.annb.quizz.dto;

import com.annb.quizz.dto.response.QuestionResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizDto {
    private String id;
    private String title;
    private String topicCode;
    private String description;
    private Integer status;
    private List<QuestionResponse> questions;
}
