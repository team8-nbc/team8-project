package com.example.eightyage.domain.product.service;

import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.product.entity.ProductImage;
import com.example.eightyage.domain.product.repository.ProductImageRepository;
import com.example.eightyage.domain.product.repository.ProductRepository;
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
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductImageService {

    private final S3Client s3Client;
    private final ProductImageRepository productImageRepository;
    private final ProductService productService;

    @Value("${aws.s3.bucket}")
    private String bucket;

    @Value("${aws.region}")
    private String region;

    // 제품 이미지 업로드
    @Transactional
    public String uploadImage(Long productId, MultipartFile file) {
        try{
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename(); // 파일명 중복 방지

            // S3에 업로드
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(fileName)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize())
            );

            // S3 이미지 URL 생성
            String imageUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucket, region, fileName);

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

        findProductImage.delete();
    }

    public ProductImage findProductImageByIdOrElseThrow(Long imageId){
        return productImageRepository.findById(imageId).orElseThrow(
                () -> new NotFoundException("해당 이미지가 존재하지 않습니다.")
        );
    }
}

