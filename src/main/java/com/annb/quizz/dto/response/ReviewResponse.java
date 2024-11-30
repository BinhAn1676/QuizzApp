package com.annb.quizz.dto.response;

import lombok.Data;

@Data
public class ReviewResponse {
    private String id;
    private String quizzId;
    private String username;
    private String comment;
    private Double rating;
}
