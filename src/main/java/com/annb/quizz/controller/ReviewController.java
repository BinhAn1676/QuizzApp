package com.annb.quizz.controller;

import com.annb.quizz.dto.request.BaseFilter;
import com.annb.quizz.dto.request.ReviewRequest;
import com.annb.quizz.dto.response.ReviewWithCommentsResponse;
import com.annb.quizz.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/review")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping("/create")
    public ResponseEntity<?> addReview(@RequestBody ReviewRequest reviewRequest) {
        return new ResponseEntity<>(reviewService.addReview(reviewRequest), HttpStatus.CREATED);
    }

    /*@PostMapping("/reviews")
    public ResponseEntity<Page<ReviewWithCommentsResponse>> getReviewsWithComments(
            @RequestBody BaseFilter request) {
        Page<ReviewWithCommentsResponse> reviews = reviewService.getReviewsWithComments(request);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }*/

    @GetMapping("/reviews")
    public ResponseEntity<Page<ReviewWithCommentsResponse>> getReviewsWithComments(@RequestBody BaseFilter req) {
        Page<ReviewWithCommentsResponse> reviews = reviewService.getReviewsWithComments(req);
        return ResponseEntity.ok(reviews);
    }
}
