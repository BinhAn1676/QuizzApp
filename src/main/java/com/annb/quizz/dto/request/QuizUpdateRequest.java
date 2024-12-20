package com.annb.quizz.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizUpdateRequest {
    private String id;;
    private String title;
    private String imageUrl;
    @NotNull(message = "topic code cant be null")
    @NotEmpty(message = "topic code cant be empty")
    private String topicCode;
    private Integer status;
    private String description;
    private List<QuestionRequest> questions;
}
