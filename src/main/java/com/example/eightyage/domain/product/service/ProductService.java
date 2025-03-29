package com.example.eightyage.domain.product.service;

import com.example.eightyage.domain.product.dto.request.ProductSaveRequestDto;
import com.example.eightyage.domain.product.dto.request.ProductUpdateRequestDto;
import com.example.eightyage.domain.product.dto.response.*;
import com.example.eightyage.domain.product.category.Category;
import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.product.entity.ProductImage;
import com.example.eightyage.domain.product.repository.ProductImageRepository;
import com.example.eightyage.domain.product.salestate.SaleState;
import com.example.eightyage.domain.product.repository.ProductRepository;
import com.example.eightyage.domain.review.entity.Review;
import com.example.eightyage.domain.review.repository.ReviewRepository;
import com.example.eightyage.domain.search.service.v1.SearchServiceV1;
import com.example.eightyage.domain.search.service.v2.SearchServiceV2;
import com.example.eightyage.domain.search.service.v3.SearchServiceV3;
import com.example.eightyage.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ReviewRepository reviewRepository;
    private final ProductImageRepository productImageRepository;
    private final SearchServiceV1 searchServiceV1;
    private final SearchServiceV2 searchServiceV2;
    private final SearchServiceV3 searchServiceV3;

    // 제품 생성
    @Transactional
    public ProductSaveResponseDto saveProduct(ProductSaveRequestDto requestDto) {
        Product product = new Product(requestDto.getProductName(), requestDto.getCategory(), requestDto.getContent(), requestDto.getPrice(), SaleState.FOR_SALE);

        Product savedProduct = productRepository.save(product);

        return ProductSaveResponseDto.builder()
                .productName(savedProduct.getName())
                .category(savedProduct.getCategory())
                .price(savedProduct.getPrice())
                .content(savedProduct.getContent())
                .saleState(savedProduct.getSaleState())
                .createdAt(savedProduct.getCreatedAt())
                .modifiedAt(savedProduct.getModifiedAt())
                .build();
    }

    // 제품 수정
    @Transactional
    public ProductUpdateResponseDto updateProduct(Long productId, ProductUpdateRequestDto requestDto) {
        Product findProduct = findProductByIdOrElseThrow(productId);

        findProduct.updateNameIfNotNull(requestDto.getProductName());
        findProduct.updateCategoryIfNotNull(requestDto.getCategory());
        findProduct.updateContentIfNotNull(requestDto.getContent());
        findProduct.updateSaleStateIfNotNull(requestDto.getSaleState());
        findProduct.updatePriceIfNotNull(requestDto.getPrice());

        return ProductUpdateResponseDto.builder()
                .productName(findProduct.getName())
                .category(findProduct.getCategory())
                .price(findProduct.getPrice())
                .content(findProduct.getContent())
                .saleState(findProduct.getSaleState())
                .createdAt(findProduct.getCreatedAt())
                .modifiedAt(findProduct.getModifiedAt())
                .build();
    }

    // 제품 단건 조회
    @Transactional(readOnly = true)
    public ProductGetResponseDto getProductById(Long productId) {
        Product findProduct = findProductByIdOrElseThrow(productId);
        List<String> productImageList = productImageRepository.findProductImageByProductId(productId);

        return ProductGetResponseDto.builder()
                .productName(findProduct.getName())
                .content(findProduct.getContent())
                .category(findProduct.getCategory())
                .price(findProduct.getPrice())
                .saleState(findProduct.getSaleState())
                .productImageList(productImageList)
                .createdAt(findProduct.getCreatedAt())
                .modifiedAt(findProduct.getModifiedAt())
                .build();
    }

    // 제품 다건 조회 version 1
    @Transactional(readOnly = true)
    public Page<ProductSearchResponseDto> getProductsV1(String productName, Category category, int size, int page) {
        int adjustedPage = Math.max(0, page - 1);
        Pageable pageable = PageRequest.of(adjustedPage, size);
        Page<ProductSearchResponseDto> productsResponse = productRepository.findProductsOrderByReviewScore(productName, category, pageable);

        if (StringUtils.hasText(productName) && !productsResponse.isEmpty()) {
            searchServiceV1.saveSearchLog(productName); // 로그만 저장
        }
        return productsResponse;
    }

    // 제품 다건 조회 version 2
    @Transactional(readOnly = true)
    public Page<ProductSearchResponseDto> getProductsV2(String productName, Category category, int size, int page) {
        int adjustedPage = Math.max(0, page - 1);
        Pageable pageable = PageRequest.of(adjustedPage, size);
        Page<ProductSearchResponseDto> productsResponse = productRepository.findProductsOrderByReviewScore(productName, category, pageable);

        if (StringUtils.hasText(productName) && !productsResponse.isEmpty()) {
            searchServiceV2.saveSearchLog(productName); // 로그 저장
            searchServiceV2.increaseKeywordCount(productName); // 캐시 작업
        }

        return productsResponse;
    }

    // 제품 다건 조회 version 3
    @Transactional(readOnly = true)
    public Page<ProductSearchResponseDto> getProductsV3(String productName, Category category, int size, int page) {
        int adjustedPage = Math.max(0, page - 1);
        Pageable pageable = PageRequest.of(adjustedPage, size);
        Page<ProductSearchResponseDto> productsResponse = productRepository.findProductsOrderByReviewScore(productName, category, pageable);

        if(StringUtils.hasText(productName) && !productsResponse.isEmpty()){
            searchServiceV3.saveSearchLog(productName);
            searchServiceV3.increaseSortedKeywordRank(productName);
        }
        return productsResponse;
    }

    // 제품 삭제
    @Transactional
    public void deleteProduct(Long productId) {
        Product findProduct = findProductByIdOrElseThrow(productId);
        List<Review> findReviewList = reviewRepository.findReviewsByProductId(productId);

        reviewRepository.deleteAll(findReviewList);

        findProduct.deleteProduct();
    }

    public Product findProductByIdOrElseThrow(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new NotFoundException("해당 제품이 존재하지 않습니다.")
        );
    }


}
