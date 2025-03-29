package com.example.eightyage.domain.product.service;

import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.product.entity.ProductImage;
import com.example.eightyage.domain.product.repository.ProductImageRepository;
import com.example.eightyage.global.exception.NotFoundException;
import com.example.eightyage.global.exception.ProductImageUploadException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final S3Client s3Client;
    private final ProductImageRepository productImageRepository;
    private final ProductService productService;

    private static final String BUCKET_NAME = "my-gom-bucket";
    private static final String REGION = "ap-northeast-2";

    // 제품 이미지 업로드
    @Transactional
    public String uploadImage(Long productId, MultipartFile file) {
        try{
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); // 파일명 중복 방지

            // S3에 업로드
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET_NAME)
                            .key(fileName)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            // S3 이미지 URL 생성
            String imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", BUCKET_NAME, REGION, fileName);

            // DB 저장
            Product product = productService.findProductByIdOrElseThrow(productId);
            ProductImage productImage = new ProductImage(product, imageUrl);
            productImageRepository.save(productImage);

            return imageUrl;
        } catch (IOException e) {
            throw new ProductImageUploadException("이미지 업로드를 실패하였습니다: " + e.getMessage(), e);
        }
    }

    // 제품 이미지 삭제
    @Transactional
    public void deleteImage(Long imageId) {
        ProductImage findProductImage = findProductImageByIdOrElseThrow(imageId);

        productImageRepository.delete(findProductImage);
    }

    public ProductImage findProductImageByIdOrElseThrow(Long imageId){
        return productImageRepository.findById(imageId).orElseThrow(
                () -> new NotFoundException("해당 이미지가 존재하지 않습니다.")
        );
    }
}

