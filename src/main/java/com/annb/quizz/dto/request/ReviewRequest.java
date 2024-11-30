package com.annb.quizz.dto.request;

import lombok.Data;

@Data
public class ReviewRequest {
    private String quizzId;
    private String username;
    private String comment;
    private Double rating;
}
