package com.example.eightyage.domain.event.dto.response;

import com.example.eightyage.domain.event.entity.EventState;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class EventResponseDto {

    private final String name;
    private final String description;
    private final int quantity;
    private final LocalDateTime startDate;
    private final LocalDateTime endDate;
    private final EventState state;


    public EventResponseDto(String name, String description, int quantity, LocalDateTime startDate, LocalDateTime endDate, EventState state) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.startDate = startDate;
        this.endDate = endDate;
        this.state = state;
    }
}
