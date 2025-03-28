package com.example.eightyage.domain.search.repository;

import com.example.eightyage.domain.search.dto.PopularKeywordDto;
import com.example.eightyage.domain.search.entity.SearchLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SearchLogRepository extends JpaRepository<SearchLog, Long> {

    @Query("SELECT new com.example.eightyage.domain.search.dto.PopularKeywordDto(s.keyword, COUNT(s))" +
            "FROM SearchLog s " +
            "WHERE s.searchedAt >= :since " +
            "GROUP BY s.keyword " +
            "ORDER BY COUNT(s) DESC ")
    List<PopularKeywordDto> findPopularKeywords(@Param("since") LocalDateTime since);
}
