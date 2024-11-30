package com.annb.quizz.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewWithCommentsResponse {
    private String id;
    private String username;
    private Double rating;
    private String comment;
    private String createdBy;
    private LocalDateTime createdDate;
    private List<CommentResponse> comments;
}