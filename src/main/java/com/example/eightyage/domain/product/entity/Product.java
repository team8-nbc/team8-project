package com.example.eightyage.domain.product.entity;

import com.example.eightyage.domain.product.category.Category;
import com.example.eightyage.domain.product.salestate.SaleState;
import com.example.eightyage.domain.review.entity.Review;
import com.example.eightyage.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product",
        indexes = @Index(name = "index_saleState_category_name", columnList = "saleState, category, name")
)
public class Product extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String content;

    private Integer price;

    @Enumerated(EnumType.STRING)
    private SaleState saleState;

    @OneToMany(mappedBy = "product")
    private List<Review> reviews = new ArrayList<>();

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = true)
    private LocalDateTime deletedAt;

    public Product(Long id) {
        this.id = id;
    }

    @Builder
    public Product(String name, Category category, String content, Integer price, SaleState saleState) {
        this.name = name;
        this.category = category;
        this.content = content;
        this.price = price;
        this.saleState = saleState;
    }

    public void updateNameIfNotNull(String newName){
        if(newName != null){
            this.name = newName;
        }
    }

    public void updateCategoryIfNotNull(Category newCategory) {
        if (newCategory != null) {
            this.category = newCategory;
        }
    }

    public void updateContentIfNotNull(String newContent) {
        if (newContent != null) {
            this.content = newContent;
        }
    }

    public void updatePriceIfNotNull(Integer newPrice) {
        if (newPrice != null) {
            this.price = newPrice;
        }
    }

    public void updateSaleStateIfNotNull(SaleState newSaleState) {
        if (newSaleState != null) {
            this.saleState = newSaleState;
        }
    }

    public void deleteProduct() {
        this.deletedAt = LocalDateTime.now();
    }
}
