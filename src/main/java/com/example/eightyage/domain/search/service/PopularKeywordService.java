package com.example.eightyage.domain.search.service;

import com.example.eightyage.domain.search.dto.PopularKeywordDto;

import java.util.List;

public interface PopularKeywordService {
    List<PopularKeywordDto> searchPopularKeywords(int days);
}
