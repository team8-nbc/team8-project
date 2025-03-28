package com.example.eightyage.domain.review.service;

import com.example.eightyage.domain.product.dto.response.ProductUpdateResponseDto;
import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.product.repository.ProductRepository;
import com.example.eightyage.domain.product.service.ProductService;
import com.example.eightyage.domain.review.dto.request.ReviewSaveRequestDto;
import com.example.eightyage.domain.review.dto.request.ReviewUpdateRequestDto;
import com.example.eightyage.domain.review.dto.response.ReviewSaveResponseDto;
import com.example.eightyage.domain.review.dto.response.ReviewUpdateResponseDto;
import com.example.eightyage.domain.review.dto.response.ReviewsGetResponseDto;
import com.example.eightyage.domain.review.entity.Review;
import com.example.eightyage.domain.review.repository.ReviewRepository;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.domain.user.service.UserService;
import com.example.eightyage.global.exception.NotFoundException;
import com.example.eightyage.global.exception.UnauthorizedException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserService userService;
    private final ProductService productService;

    // 리뷰 생성
    @Transactional
    public ReviewSaveResponseDto saveReview(Long userId, Long productId, ReviewSaveRequestDto requestDto) {
        User findUser = userService.findUserByIdOrElseThrow(userId);
        Product findProduct = productService.findProductByIdOrElseThrow(productId);

        Review review = new Review(findUser, findProduct, requestDto.getScore(), requestDto.getContent());
        Review savedReview = reviewRepository.save(review);

        return ReviewSaveResponseDto.builder()
                .id(savedReview.getId())
                .userId(savedReview.getUser().getId())
                .productId(savedReview.getProduct().getId())
                .nickname(savedReview.getUser().getNickname())
                .score(savedReview.getScore())
                .content(savedReview.getContent())
                .createdAt(savedReview.getCreatedAt())
                .modifiedAt(savedReview.getModifiedAt())
                .build();
    }

    // 리뷰 수정
    @Transactional
    public ReviewUpdateResponseDto updateReview(Long userId, Long reviewId, ReviewUpdateRequestDto requestDto) {
        User findUser = userService.findUserByIdOrElseThrow(userId);
        Review findReview = findReviewByIdOrElseThrow(reviewId);

        if(findUser.getId().equals(findReview.getUser().getId())){
            findReview.updateScore(requestDto.getScore());
            findReview.updateContent(requestDto.getContent());
        } else {
            throw new UnauthorizedException("리뷰를 수정할 권한이 없습니다.");
        }

        return ReviewUpdateResponseDto.builder()
                .id(findReview.getId())
                .userId(findUser.getId())
                .nickname(findUser.getNickname())
                .score(findReview.getScore())
                .content(findReview.getContent())
                .createdAt(findReview.getCreatedAt())
                .modifiedAt(findReview.getModifiedAt())
                .build();
    }

    // 리뷰 다건 조회
    @Transactional(readOnly = true)
    public Page<ReviewsGetResponseDto> findReviews(Long productId, PageRequest pageRequest) {
        Page<Review> reviewPage = reviewRepository.findByProductIdAndProductDeletedAtIsNull(productId, pageRequest);

        return reviewPage.map(review -> ReviewsGetResponseDto.builder()
                .id(review.getId())
                .userId(review.getUser().getId())
                .nickname(review.getUser().getNickname())
                .score(review.getScore())
                .content(review.getContent())
                .createdAt(review.getCreatedAt())
                .modifiedAt(review.getModifiedAt())
                .build());
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long userId, Long reviewId) {
        User findUser = userService.findUserByIdOrElseThrow(userId);
        Review findReview = findReviewByIdOrElseThrow(reviewId);

        if(findUser.getId().equals(findReview.getUser().getId())){
            findReview.delete();
        } else {
            throw new UnauthorizedException("리뷰를 삭제할 권한이 없습니다.");
        }
    }

    public Review findReviewByIdOrElseThrow(Long reviewId){
        return reviewRepository.findById(reviewId).orElseThrow(
                () -> new NotFoundException("해당 리뷰가 존재하지 않습니다.")
        );
    }
}
