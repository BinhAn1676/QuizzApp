package com.annb.quizz.dto.response;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizResponse {
    private String id;
    private String title;
    private String imageUrl;
    @NotNull(message = "topic code cant be null")
    @NotEmpty(message = "topic code cant be empty")
    private String topicCode;
    private String description;
    private String createdBy;
    private List<QuestionResponse> questions;
}
