package com.annb.quizz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private String id;
    private String content;
    private String username;
    private String parentCommentId; // Nullable for top-level comments
    private String reviewId;
    private String quizzId;
    private LocalDateTime createdAt;
    private List<CommentResponse> replies; // Include nested replies
}
