package com.annb.quizz.dto;

import java.time.LocalDateTime;

public interface QuizProjection {
    String getId();
    String getTitle();
    String getDescription();
    Integer getStatus();
    String getImageUrl();
    String getTopicCode();
    Long getReviewCount();
    Long getQuestionCount();
    Double getAverageRating();
    LocalDateTime getCreatedAt();
    String getCreatedBy();
    LocalDateTime getUpdatedAt();
    String getUpdatedBy();
}

