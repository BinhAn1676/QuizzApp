package com.annb.quizz.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizGenerateRequest {
    @NotNull(message = "Prompt không được để trống")
    @NotEmpty(message = "Prompt không được để trống")
    private String prompt;

    @NotNull(message = "Topic code không được để trống")
    @NotEmpty(message = "Topic code không được để trống")
    private String topicCode;

    @NotNull(message = "Số lượng câu hỏi không được để trống")
    @Min(value = 1, message = "Số lượng câu hỏi phải lớn hơn 0")
    private Integer numberOfQuestions;
}
