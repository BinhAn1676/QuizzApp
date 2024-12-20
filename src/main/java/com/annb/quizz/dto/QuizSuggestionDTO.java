package com.annb.quizz.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuizSuggestionDTO {
    private String id;
    private String title;
    private String description;
    private Integer status;
    private String topicId;
    private double averageRating;
    private int totalComments;
    private int totalQuestions;

}