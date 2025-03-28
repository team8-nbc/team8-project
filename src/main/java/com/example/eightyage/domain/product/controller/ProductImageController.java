package com.example.eightyage.domain.product.controller;

import com.example.eightyage.domain.product.service.ProductImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductImageController {

    private final ProductImageService productImageService;

    // 제품 이미지 업로드
    @Secured("ROLE_ADMIN")
    @PostMapping("/v1/products/{productId}/images")
    public ResponseEntity<String> uploadImage(
            @PathVariable Long productId,
            @RequestParam("file") MultipartFile file) {

        String imageUrl = productImageService.uploadImage(productId, file);
        return ResponseEntity.ok(imageUrl);
    }

    // 제품 이미지 삭제
    @Secured("ROLE_ADMIN")
    @DeleteMapping("/v1/products/images/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long imageId) {
        productImageService.deleteImage(imageId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
