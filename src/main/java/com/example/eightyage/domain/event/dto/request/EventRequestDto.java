package com.example.eightyage.domain.event.dto.request;

import com.example.eightyage.global.dto.ValidationMessage;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {

    @NotBlank(message = ValidationMessage.NOT_BLANK_EVENT_NAME)
    private String name;

    @NotBlank(message = ValidationMessage.NOT_BLANK_EVENT_DESCRIPTION)
    private String description;

    @Min(value = 1, message = ValidationMessage.INVALID_EVENT_QUANTITY)
    private int quantity;

    @NotNull(message = ValidationMessage.NOT_NULL_START_DATE)
    private LocalDateTime startDate;

    @NotNull(message = ValidationMessage.NOT_NULL_END_DATE)
    private LocalDateTime endDate;
}
