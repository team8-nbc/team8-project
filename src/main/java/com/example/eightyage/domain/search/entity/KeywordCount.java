package com.example.eightyage.domain.search.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class KeywordCount {

    @Id
    private String keyword;
    private Long count;

    public KeywordCount(String keyword, Long count) {
        this.keyword = keyword;
        this.count = count;
    }

    public void updateCount(Long count) {
        this.count = count;
    }
}
