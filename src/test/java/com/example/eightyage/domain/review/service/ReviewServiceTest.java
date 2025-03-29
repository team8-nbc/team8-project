package com.example.eightyage.domain.review.service;

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
import com.example.eightyage.domain.user.userrole.UserRole;
import com.example.eightyage.domain.user.repository.UserRepository;
import com.example.eightyage.domain.user.service.UserService;
import com.example.eightyage.global.exception.UnauthorizedException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.data.domain.*;
import org.springframework.data.querydsl.QPageRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    ReviewRepository reviewRepository;

    @Mock
    UserService userService;

    @Mock
    ProductService productService;

    @InjectMocks
    ReviewService reviewService;

    @Mock
    User user;

    @Mock
    Product product;

    @Mock
    Review review;

    @Test
    void 리뷰_생성_성공(){
        // given
        Long userId = 1L;
        Long productId = 1L;
        Long reviewId = 1L;

        Review review = new Review(reviewId, user, product, 5.0, "8자 주름을 다리미처럼 펴줘요 짱짱");
        given(reviewRepository.save(any())).willReturn(review);

        ReviewSaveRequestDto requestDto = new ReviewSaveRequestDto(5.0, "8자 주름을 다리미처럼 펴줘요 짱짱");

        // when
        ReviewSaveResponseDto responseDto = reviewService.saveReview(userId, productId, requestDto);

        // then
        assertEquals(requestDto.getContent(), responseDto.getContent());
    }

    @Test
    void 리뷰_수정_작성한_본인이_아닐_경우_실패(){
        // given
        Long userId = 2L;
        Long reviewId = 1L;
        ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto(1.0, "쓰다보니 8자 주름이 깊어졌어요. 대실망");

        User user1 = new User(1L, "ijieun@gmail.com", "이지은B", "password123", UserRole.ROLE_USER);
        User user2 = new User(userId, "ijieun@gmail.com", "이지은B", "password123", UserRole.ROLE_USER);
        Review review = new Review(reviewId, user1, product, 5.0, "8자 주름을 펴줘요");

        given(userService.findUserByIdOrElseThrow(any())).willReturn(user2);
        given(reviewRepository.findById(any())).willReturn(Optional.of(review));

        // when
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            reviewService.updateReview(userId, reviewId, requestDto);
        });

        // then
        assertEquals("리뷰를 수정할 권한이 없습니다.", exception.getMessage());
    }

    @Test
    void 리뷰_수정_성공(){
        // given
        Long userId = 1L;
        Long reviewId = 1L;
        ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto(1.0, "쓰다보니 8자 주름이 깊어졌어요. 대실망");

        User user = new User(userId, "ijieun@gmail.com", "이지은B", "password123", UserRole.ROLE_USER);
        Review review = new Review(reviewId, user, product, 5.0, "8자 주름을 펴줘요");

        given(userService.findUserByIdOrElseThrow(any())).willReturn(user);
        given(reviewRepository.findById(any())).willReturn(Optional.of(review));

        // when
        ReviewUpdateResponseDto responseDto = reviewService.updateReview(userId, reviewId, requestDto);

        // then
        assertEquals(requestDto.getContent(), responseDto.getContent());
    }

    @Test
    void 리뷰_다건_조회_성공(){
        // given
        Long productId = 1L;
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "score"));

        Review review1 = new Review(1L, user, product, 5.0, "8자 주름을 펴줘요");
        Review review2 = new Review(1L, user, product, 5.0, "8자 주름을 펴줘요");

        List<Review> reviewList = new ArrayList<>();
        reviewList.add(review1);
        reviewList.add(review2);

        Page<Review> reviewPage = new PageImpl<>(reviewList, pageRequest, reviewList.size());

        when(reviewRepository.findByProductIdAndProductDeletedAtIsNull(any(Long.class), eq(pageRequest))).thenReturn(reviewPage);

        // when
        Page<ReviewsGetResponseDto> result = reviewService.getReviews(productId, pageRequest);

        // then
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(reviewRepository, times(1)).findByProductIdAndProductDeletedAtIsNull(any(Long.class), eq(pageRequest));
    }

    @Test
    void 리뷰_삭제_작성한_본인이_아닐_경우_실패(){
        // given
        Long userId = 2L;
        Long reviewId = 1L;

        User user1 = new User(1L, "ijieun@gmail.com", "이지은B", "password123", UserRole.ROLE_USER);
        User user2 = new User(userId, "ijieun@gmail.com", "이지은B", "password123", UserRole.ROLE_USER);
        Review review = new Review(reviewId, user1, product, 5.0, "8자 주름을 펴줘요");

        given(userService.findUserByIdOrElseThrow(any())).willReturn(user2);
        given(reviewRepository.findById(any())).willReturn(Optional.of(review));

        // when
        UnauthorizedException exception = assertThrows(UnauthorizedException.class, () -> {
            reviewService.deleteReview(userId, reviewId);
        });

        // then
        assertEquals("리뷰를 삭제할 권한이 없습니다.", exception.getMessage());
    }

    @Test
    void 리뷰_삭제_성공(){
        // given
        Long userId = 1L;
        Long reviewId = 1L;

        User user = new User(userId, "ijieun@gmail.com", "이지은B", "password123", UserRole.ROLE_USER);

        given(userService.findUserByIdOrElseThrow(any())).willReturn(user);
        given(reviewRepository.findById(any())).willReturn(Optional.of(review));
        given(review.getUser()).willReturn(user);

        // when
        reviewService.deleteReview(userId, reviewId);

        // then
        verify(reviewRepository, times(1)).delete(review);
    }
}