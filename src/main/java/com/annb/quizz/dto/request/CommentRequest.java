package com.annb.quizz.dto.request;

import lombok.Data;

@Data
public class CommentRequest {
    private String reviewId;
    private String quizzId;
    private String content;
    private String username;
    private String parentCommentId;
}
