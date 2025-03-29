package com.example.eightyage.domain.product.repository;

import com.example.eightyage.domain.product.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    @Query("SELECT pi FROM ProductImage pi WHERE pi.id = :imageId")
    Optional<ProductImage> findById(Long imageId);

    @Query("SELECT pi.imageUrl FROM ProductImage pi WHERE pi.product.id = :productId")
    List<String> findProductImageByProductId(Long productId);
}
