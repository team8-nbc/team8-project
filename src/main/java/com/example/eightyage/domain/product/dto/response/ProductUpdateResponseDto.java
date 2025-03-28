package com.example.eightyage.domain.product.dto.response;

import com.example.eightyage.domain.product.entity.Category;
import com.example.eightyage.domain.product.entity.SaleState;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ProductUpdateResponseDto {

    private final String productName;

    private final Integer price;

    private final String content;

    private final Category category;

    private final SaleState saleState;

    private final LocalDateTime createdAt;

    private final LocalDateTime modifiedAt;
}
