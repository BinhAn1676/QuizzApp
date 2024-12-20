package com.annb.quizz.dto;

import com.annb.quizz.dto.response.QuestionResponse;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private String createdBy;
    private String imageUrl;
    private Long reviewCount;    // Number of reviews
    private Long questionCount;  // Number of questions
    private Double averageRating; // Average rating
    private LocalDateTime createdAt;
    private String updatedBy;
    private LocalDateTime updatedAt;
    private List<QuestionResponse> questions;
}
