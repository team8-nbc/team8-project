package com.example.eightyage.domain.review.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
@Getter
public class ReviewUpdateRequestDto {

    private Double score;

    private String content;
}