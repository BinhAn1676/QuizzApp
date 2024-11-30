package com.annb.quizz.service;

import com.annb.quizz.dto.request.BaseFilter;
import com.annb.quizz.dto.request.ReviewRequest;
import com.annb.quizz.dto.response.ReviewResponse;
import com.annb.quizz.dto.response.ReviewWithCommentsResponse;
import org.springframework.data.domain.Page;

public interface ReviewService {
    ReviewResponse addReview(ReviewRequest reviewRequest);

    Page<ReviewWithCommentsResponse> getReviewsWithComments(BaseFilter request);
}
