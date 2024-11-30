package com.annb.quizz.dto.response;

public interface QuizSuggestionInterface {
    String getQuizId();
    String getQuizTitle();
    String getQuizDescription();
    Double getAverageRating();
    Long getTotalReviews();
}
