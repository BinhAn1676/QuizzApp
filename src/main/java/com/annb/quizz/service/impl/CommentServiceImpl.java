package com.annb.quizz.service.impl;

import com.annb.quizz.dto.request.CommentRequest;
import com.annb.quizz.dto.response.CommentResponse;
import com.annb.quizz.entity.Comment;
import com.annb.quizz.entity.Quizz;
import com.annb.quizz.entity.Review;
import com.annb.quizz.exception.ResourceNotFoundException;
import com.annb.quizz.repository.CommentRepository;
import com.annb.quizz.repository.QuizzRepository;
import com.annb.quizz.repository.ReviewRepository;
import com.annb.quizz.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final QuizzRepository quizzRepository;
    private final ReviewRepository reviewRepository;

    @Override
    public CommentResponse create(CommentRequest commentRequest) {
        Review review = reviewRepository.findById(commentRequest.getReviewId())
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", commentRequest.getReviewId()));
        var quiz = quizzRepository.findById(commentRequest.getQuizzId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", commentRequest.getQuizzId()));

        Comment parentComment = null;
        if (commentRequest.getParentCommentId() != null) {
            parentComment = commentRepository.findById(commentRequest.getParentCommentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentRequest.getParentCommentId()));
        }

        Comment comment = new Comment();
        comment.setId(UUID.randomUUID().toString().replace("-",""));
        comment.setQuizz(quiz);
        comment.setReview(review);
        comment.setUsername(commentRequest.getUsername());
        comment.setContent(commentRequest.getContent());
        comment.setParentComment(parentComment);
        var savedComment = commentRepository.save(comment);
        CommentResponse response = new CommentResponse();
        response.setId(savedComment.getId());
        response.setUsername(savedComment.getUsername());
        response.setContent(savedComment.getContent());
        response.setParentCommentId(parentComment != null ? parentComment.getId() : null);
        response.setReviewId(review.getId());
        response.setQuizzId(quiz.getId());
        response.setCreatedAt(savedComment.getCreatedAt());
        return response;
    }

    @Override
    public CommentResponse addReply(CommentRequest commentRequest) {
        // Fetch parent comment
        Comment parentComment = commentRepository.findById(commentRequest.getParentCommentId())
                .orElseThrow(() -> new ResourceNotFoundException("Comment", "id", commentRequest.getParentCommentId()));

        // Fetch quiz and review (optional, for data integrity)
        Quizz quiz = quizzRepository.findById(commentRequest.getQuizzId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", commentRequest.getQuizzId()));

        Review review = reviewRepository.findById(commentRequest.getReviewId())
                .orElseThrow(() -> new ResourceNotFoundException("Review", "id", commentRequest.getReviewId()));

        // Create reply
        Comment reply = new Comment();
        reply.setId(UUID.randomUUID().toString().replace("-", ""));
        reply.setParentComment(parentComment);
        reply.setQuizz(quiz);
        reply.setReview(review);
        reply.setUsername(commentRequest.getUsername());
        reply.setContent(commentRequest.getContent());

        // Save reply
        var savedReply = commentRepository.save(reply);

        // Build response
        CommentResponse response = new CommentResponse();
        response.setId(savedReply.getId());
        response.setUsername(savedReply.getUsername());
        response.setContent(savedReply.getContent());
        response.setParentCommentId(parentComment.getId());
        response.setReviewId(review.getId());
        response.setQuizzId(quiz.getId());
        response.setCreatedAt(savedReply.getCreatedAt());
        return response;
    }
}
