package com.example.eightyage.domain.product.repository;

import com.example.eightyage.domain.product.entity.ProductImage;
import com.example.eightyage.global.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.*;
import java.util.Optional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    @Query("SELECT pi FROM ProductImage pi WHERE pi.id = :imageId AND pi.deletedAt IS NULL")
    Optional<ProductImage> findById(Long imageId);
}
