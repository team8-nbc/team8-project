package com.example.eightyage.domain.review.controller;

import com.example.eightyage.domain.review.dto.request.ReviewSaveRequestDto;
import com.example.eightyage.domain.review.dto.request.ReviewUpdateRequestDto;
import com.example.eightyage.domain.review.dto.response.ReviewSaveResponseDto;
import com.example.eightyage.domain.review.dto.response.ReviewUpdateResponseDto;
import com.example.eightyage.domain.review.dto.response.ReviewsGetResponseDto;
import com.example.eightyage.domain.review.service.ReviewService;
import com.example.eightyage.global.dto.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 생성
    @Secured("ROLE_USER")
    @PostMapping("/v1/products/{productId}/reviews")
    public ResponseEntity<ReviewSaveResponseDto> saveReview(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long productId,
            @Valid @RequestBody ReviewSaveRequestDto requestDto
    ){
        ReviewSaveResponseDto responseDto = reviewService.saveReview(authUser.getUserId(), productId, requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 리뷰 수정
    @Secured("ROLE_USER")
    @PatchMapping("/v1/reviews/{reviewId}")
    public ResponseEntity<ReviewUpdateResponseDto> updateReview(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long reviewId,
            @RequestBody ReviewUpdateRequestDto requestDto
    ){
        ReviewUpdateResponseDto responseDto = reviewService.updateReview(authUser.getUserId(), reviewId, requestDto);

        return ResponseEntity.ok(responseDto);
    }

    // 리뷰 다건 조회
    @GetMapping("/v1/products/{productId}/reviews")
    public ResponseEntity<Page<ReviewsGetResponseDto>> getReviews(
            @PathVariable Long productId,
            @RequestParam(required = false, defaultValue = "score") String orderBy,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, orderBy));

        Page<ReviewsGetResponseDto> reviews = reviewService.findReviews(productId, pageRequest);

        return ResponseEntity.ok(reviews);
    }

    // 리뷰 삭제
    @Secured("ROLE_USER")
    @DeleteMapping("/v1/reviews/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long reviewId
    ){
        reviewService.deleteReview(authUser.getUserId(), reviewId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
