package com.example.eightyage.domain.event.entity;

import com.example.eightyage.domain.event.dto.request.EventRequestDto;
import com.example.eightyage.domain.event.dto.response.EventResponseDto;
import com.example.eightyage.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Event extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private int quantity;

    @Column(name="start_at")
    private LocalDateTime startDate;
    @Column(name = "end_at")
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private EventState state;

    public Event(String name, String description, int quantity, LocalDateTime startDate, LocalDateTime endDate) {
        this.name = name;
        this.description = description;
        this.quantity = quantity;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public void setState(EventState state) {
        this.state = state;
    }

    public EventResponseDto toDto() {
        return new EventResponseDto(
                this.getName(),
                this.getDescription(),
                this.getQuantity(),
                this.getStartDate(),
                this.getEndDate(),
                this.getState()
        );
    }

    public void update(EventRequestDto eventRequestDto) {
        this.name = eventRequestDto.getName();
        this.description = eventRequestDto.getDescription();
        this.quantity = eventRequestDto.getQuantity();
        this.startDate = eventRequestDto.getStartDate();
        this.endDate = eventRequestDto.getEndDate();
    }
}
