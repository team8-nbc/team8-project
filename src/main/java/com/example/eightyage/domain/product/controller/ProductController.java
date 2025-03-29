package com.example.eightyage.domain.product.controller;

import com.example.eightyage.domain.product.dto.request.ProductSaveRequestDto;
import com.example.eightyage.domain.product.dto.request.ProductUpdateRequestDto;
import com.example.eightyage.domain.product.dto.response.ProductGetResponseDto;
import com.example.eightyage.domain.product.dto.response.ProductSaveResponseDto;
import com.example.eightyage.domain.product.dto.response.ProductSearchResponseDto;
import com.example.eightyage.domain.product.dto.response.ProductUpdateResponseDto;
import com.example.eightyage.domain.product.category.Category;
import com.example.eightyage.domain.product.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 제품 생성
    @Secured("ROLE_ADMIN")
    @PostMapping("/v1/products")
    public ResponseEntity<ProductSaveResponseDto> saveProduct(@Valid @RequestBody ProductSaveRequestDto requestDto){
        ProductSaveResponseDto responseDto = productService.saveProduct(requestDto);

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 제품 수정
    @Secured("ROLE_ADMIN")
    @PatchMapping("/v1/products/{productId}")
    public ResponseEntity<ProductUpdateResponseDto> updateProduct(
            @PathVariable Long productId,
            @RequestBody ProductUpdateRequestDto requestDto
    ){
        ProductUpdateResponseDto responseDto = productService.updateProduct(productId, requestDto);

        return ResponseEntity.ok(responseDto);
    }

    // 제품 단건 조회
    @GetMapping("/v1/products/{productId}")
    public ResponseEntity<ProductGetResponseDto> findProduct(@PathVariable Long productId){
        ProductGetResponseDto responseDto = productService.getProductById(productId);

        return ResponseEntity.ok(responseDto);
    }

    // 제품 다건 조회 version 1
    @GetMapping("/v1/products")
    public ResponseEntity<Page<ProductSearchResponseDto>> searchProductV1(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Category category,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(productService.getProductsV1(name, category, size, page));
    }

    // 제품 다건 조회 version 2
    @GetMapping("/v2/products")
    public ResponseEntity<Page<ProductSearchResponseDto>> searchProductV2(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Category category,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(productService.getProductsV2(name, category, size, page));
    }

    // 제품 다건 조회 version 3
    @GetMapping("/v3/products")
    public ResponseEntity<Page<ProductSearchResponseDto>> searchProductV3(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Category category,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "1") int page
    ) {
        return ResponseEntity.ok(productService.getProductsV3(name, category, size, page));
    }

    // 제품 삭제
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/v1/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){
        productService.deleteProduct(productId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
