package com.example.eightyage.domain.product.dto.response;

import com.example.eightyage.domain.product.category.Category;
import com.example.eightyage.domain.product.salestate.SaleState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ProductSaveResponseDto {
    private final String productName;
    private final Category category;
    private final Integer price;
    private final String content;
    private final SaleState saleState;
    private final LocalDateTime createdAt;
    private final LocalDateTime modifiedAt;
}
