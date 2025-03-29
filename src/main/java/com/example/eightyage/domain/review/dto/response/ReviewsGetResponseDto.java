package com.example.eightyage.domain.review.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ReviewsGetResponseDto {
    private final Long id;
    private final Long userId;
    private final String nickname;
    private final Double score;
    private final String content;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
}
