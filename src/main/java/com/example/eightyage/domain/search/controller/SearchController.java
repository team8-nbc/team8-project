package com.example.eightyage.domain.search.controller;

import com.example.eightyage.domain.search.dto.PopularKeywordDto;
import com.example.eightyage.domain.search.service.v1.PopularKeywordServiceV1;
import com.example.eightyage.domain.search.service.v2.PopularKeywordServiceV2;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SearchController {

    private final PopularKeywordServiceV1 popularKeywordServiceV1;
    private final PopularKeywordServiceV2 popularKeywordServiceV2;

    // 인기 검색어 조회 (캐시 X)
    @GetMapping("/api/v1/search/popular")
    public ResponseEntity<List<PopularKeywordDto>> searchPopularKeywords(
            @RequestParam(defaultValue = "7") int days
    ) {
        return ResponseEntity.ok(popularKeywordServiceV1.searchPopularKeywords(days));
    }

    // 인기 검색어 조회 (캐시 O)
    @GetMapping("/api/v2/search/popular")
    public ResponseEntity<List<PopularKeywordDto>> searchPopularKeywordsV2(
            @RequestParam(defaultValue = "7") int days
    ) {
        return ResponseEntity.ok(popularKeywordServiceV2.searchPopularKeywords(days));
    }
}
