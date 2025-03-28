package com.example.eightyage.domain.review.entity;

import com.example.eightyage.domain.product.entity.Product;
import com.example.eightyage.domain.user.entity.User;
import com.example.eightyage.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "review")
public class Review extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private Double score;

    private String content;

    public Review(User user, Product product, Double score, String content) {
        this.user = user;
        this.product = product;
        this.score = score;
        this.content = content;
    }

    public void updateScore(Double newScore){
        if(newScore != null){
            this.score = newScore;
        }
    }

    public void updateContent(String newContent){
        if(newContent != null){
            this.content = newContent;
        }
    }
}
