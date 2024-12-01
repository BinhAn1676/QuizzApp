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


    @PostMapping("/update")
    public ResponseEntity<?> updateReview(@RequestBody ReviewRequest reviewRequest) {
        return new ResponseEntity<>(reviewService.updateReview(reviewRequest), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable String id) {
        var result = reviewService.deleteReview(id);
        if(result){
            return ResponseEntity.ok("Deleted Successfullu");
        }
        return ResponseEntity.ok("Failed to delete review");
    }

    @PostMapping("/filter")
    public ResponseEntity<Page<ReviewWithCommentsResponse>> getReviewsWithComments(@RequestBody BaseFilter req) {
        Page<ReviewWithCommentsResponse> reviews = reviewService.getReviewsWithComments(req);
        return ResponseEntity.ok(reviews);
    }
}
