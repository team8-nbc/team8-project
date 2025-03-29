package com.example.eightyage.domain.product.dto.response;

import com.example.eightyage.domain.product.category.Category;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
public class ProductSearchResponseDto {
    private String name;
    private Category category;
    private Integer price;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.0")
    private Double scoreAvg;

    public ProductSearchResponseDto(String name, Category category, Integer price, Double scoreAvg) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.scoreAvg = scoreAvg;
    }
}
