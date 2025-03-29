package com.example.eightyage.domain.product.dto.request;

import com.example.eightyage.domain.product.category.Category;
import com.example.eightyage.global.dto.ValidationMessage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductSaveRequestDto {

    @NotBlank(message= ValidationMessage.NOT_BLANK_PRODUCT_NAME)
    private String productName;

    @NotNull(message=ValidationMessage.NOT_NULL_CATEGORY)
    private Category category;

    @NotBlank(message=ValidationMessage.NOT_BLANK_CONTENT)
    private String content;

    @NotNull(message=ValidationMessage.NOT_NULL_PRICE)
    private Integer price;
}
