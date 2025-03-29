package com.example.eightyage.domain.product.service;

import com.example.eightyage.domain.product.category.Category;
import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.product.entity.ProductImage;
import com.example.eightyage.domain.product.repository.ProductImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.util.Optional;
import java.util.function.Consumer;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductImageServiceTest {

    @Mock
    S3Client s3Client;

    @Mock
    ProductImageRepository productImageRepository;

    @Mock
    ProductService productService;

    @InjectMocks
    ProductImageService productImageService;

    @Mock
    ProductImage productImage;

    private MockMultipartFile mockFile;

    private Product mockProduct;

    @BeforeEach
    void setUp(){
        mockFile = new MockMultipartFile(
                "file",
                "test.jpg",
                "image/jpeg",
                "test image content".getBytes()
        );

        mockProduct = Product.builder()
                .name("Test Product")
                .category(Category.SKINCARE)
                .content("This is test product.")
                .price(10000)
                .build();

        ReflectionTestUtils.setField(mockProduct, "id", 1L);
    }

    @Test
    void 이미지_업로드_성공() {
        // given
        Long productId = 1L;
        given(productService.findProductByIdOrElseThrow(productId)).willReturn(mockProduct);

        ProductImage mockProductImage = mock(ProductImage.class);
        given(productImageRepository.save(any(ProductImage.class))).willReturn(mockProductImage);

        when(s3Client.putObject(any(PutObjectRequest.class), any(RequestBody.class)))
                .thenReturn(PutObjectResponse.builder().build());

        // when
        String imageUrl = productImageService.uploadImage(productId, mockFile);

        // then
        assertNotNull(imageUrl);
        assertTrue(imageUrl.startsWith("https://my-gom-bucket.s3.ap-northeast-2.amazonaws.com/"));
    }


    @Test
    void 이미지_삭제_성공(){
        // given
        Long imageId = 1L;
        given(productImageRepository.findById(imageId)).willReturn(Optional.of(productImage));

        // when
        productImageService.deleteImage(imageId);

        // then
        verify(productImageRepository, times(1)).delete(productImage);
    }
}