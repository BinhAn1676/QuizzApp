package com.annb.quizz.service.impl;

import com.annb.quizz.dto.request.BaseFilter;
import com.annb.quizz.dto.request.ReviewRequest;
import com.annb.quizz.dto.response.CommentResponse;
import com.annb.quizz.dto.response.ReviewResponse;
import com.annb.quizz.dto.response.ReviewWithCommentsResponse;
import com.annb.quizz.entity.Comment;
import com.annb.quizz.entity.Quizz;
import com.annb.quizz.entity.Review;
import com.annb.quizz.exception.ResourceNotFoundException;
import com.annb.quizz.repository.QuizzRepository;
import com.annb.quizz.repository.ReviewRepository;
import com.annb.quizz.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final QuizzRepository quizzRepository;
    @Override
    public ReviewResponse addReview(ReviewRequest reviewRequest) {
        Quizz quiz = quizzRepository.findById(reviewRequest.getQuizzId())
                .orElseThrow(() -> new ResourceNotFoundException("Quiz", "id", reviewRequest.getQuizzId()));

        Review review = new Review();
        review.setId(UUID.randomUUID().toString().replace("-", ""));
        review.setQuizz(quiz);
        review.setUsername(reviewRequest.getUsername());
        review.setComment(reviewRequest.getComment());
        review.setRating(reviewRequest.getRating());

        var saved =  reviewRepository.save(review);
        var response = new ReviewResponse();
        response.setId(saved.getId());
        response.setUsername(saved.getUsername());
        response.setComment(saved.getComment());
        response.setQuizzId(saved.getQuizz().getId());
        response.setRating(saved.getRating());
        return response;
    }

    /*@Override
    public Page<ReviewWithCommentsResponse> getReviewsWithComments(BaseFilter request) {
        Pageable pageable = PageRequest.of(request.getPageNo(), request.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        // Fetch reviews by quiz ID
        Page<Review> reviews = reviewRepository.findByQuizzId(request.getTextSearch(), pageable);

        // Map each review into a ReviewWithCommentsResponse
        return reviews.map(review -> {
            // Fetch comments for this review
            List<CommentResponse> commentResponses = review.getComments().stream().map(this::mapCommentToResponse).toList();

            // Create and return the response for each review
            return new ReviewWithCommentsResponse(
                    review.getId(),
                    review.getUsername(),
                    review.getRating(),
                    review.getComment(),
                    review.getCreatedBy(),
                    review.getCreatedAt(),
                    commentResponses
            );
        });
    }

    // Helper method to map comments, including replies
    private CommentResponse mapCommentToResponse(Comment comment) {
        List<CommentResponse> replyResponses = comment.getReplies().stream()
                .map(this::mapCommentToResponse) // Recursive call for nested replies
                .toList();

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUsername(),
                comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                comment.getReview().getId(),
                comment.getQuizz().getId(),
                comment.getCreatedAt(),
                replyResponses
        );
    }*/
    @Override
    public Page<ReviewWithCommentsResponse> getReviewsWithComments(BaseFilter request) {
        Pageable pageable = PageRequest.of(request.getPageNo(), request.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Review> reviews = reviewRepository.findByQuizzId(request.getTextSearch(), pageable);

        // Map each review into a ReviewWithCommentsResponse
        return reviews.map(review -> {
            // Fetch only top-level comments for this review
            List<CommentResponse> commentResponses = review.getComments().stream()
                    .filter(comment -> comment.getParentComment() == null) // Only include top-level comments
                    .map(this::mapCommentToResponse)
                    .toList();

            // Create and return the response for each review
            return new ReviewWithCommentsResponse(
                    review.getId(),
                    review.getUsername(),
                    review.getRating(),
                    review.getComment(),
                    review.getCreatedBy(),
                    review.getCreatedAt(),
                    commentResponses
            );
        });
    }

    // Helper method to map comments, including replies
    private CommentResponse mapCommentToResponse(Comment comment) {
        // Fetch replies for this comment
        List<CommentResponse> replyResponses = comment.getReplies().stream()
                .map(this::mapCommentToResponse)
                .toList();

        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getUsername(),
                comment.getParentComment() != null ? comment.getParentComment().getId() : null,
                comment.getReview().getId(),
                comment.getQuizz().getId(),
                comment.getCreatedAt(),
                replyResponses
        );
    }

}
