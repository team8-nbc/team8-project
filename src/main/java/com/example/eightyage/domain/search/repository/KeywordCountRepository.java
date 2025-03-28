package com.example.eightyage.domain.search.repository;


import com.example.eightyage.domain.search.entity.KeywordCount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface KeywordCountRepository extends JpaRepository<KeywordCount, String> {
}
