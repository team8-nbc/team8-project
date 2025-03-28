package com.example.eightyage.domain.search.service.v1;

import com.example.eightyage.domain.search.dto.PopularKeywordDto;
import com.example.eightyage.domain.search.repository.SearchLogRepository;
import com.example.eightyage.domain.search.service.PopularKeywordService;
import com.example.eightyage.global.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PopularKeywordServiceV1 implements PopularKeywordService {

    private final SearchLogRepository searchLogRepository;
    private static final int MIN_DAYS = 1;
    private static final int MAX_DAYS = 365;

    // 캐시X 인기 검색어 조회
    @Transactional(readOnly = true)
    public List<PopularKeywordDto> searchPopularKeywords(int days) {
        if (days < MIN_DAYS || days > MAX_DAYS) {
            throw new BadRequestException("조회 기간은 1 ~ 365일 사이여야 합니다.");
        }
        LocalDateTime since = LocalDateTime.now().minusDays(days);
        return searchLogRepository.findPopularKeywords(since);
    }

}