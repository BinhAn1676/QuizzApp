package com.annb.quizz.service;

import com.annb.quizz.dto.request.CommentRequest;
import com.annb.quizz.dto.response.CommentResponse;

public interface CommentService {
    CommentResponse create(CommentRequest commentRequest);

    CommentResponse addReply(CommentRequest commentRequest);
}
