package com.example.eightyage.domain.review.dto.request;

import com.example.eightyage.global.dto.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReviewSaveRequestDto {

    @NotNull(message = ValidationMessage.NOT_NULL_SCORE)
    private Double score;

    @NotBlank(message = ValidationMessage.NOT_BLANK_CONTENT)
    private String content;
}
