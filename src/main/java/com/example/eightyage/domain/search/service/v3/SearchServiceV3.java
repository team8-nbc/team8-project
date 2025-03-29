package com.example.eightyage.domain.search.service.v3;

import com.example.eightyage.domain.search.entity.SearchLog;
import com.example.eightyage.domain.search.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.Duration;


@Service
@RequiredArgsConstructor
public class SearchServiceV3 {

    private final SearchLogRepository searchLogRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private static final String RANKING_KEY = "rankingPopularKeywords";

    // 검색 키워드를 로그에 저장
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSearchLog(String keyword) {
        if (StringUtils.hasText(keyword)) {
            searchLogRepository.save(SearchLog.of(keyword));
        }
    }

    // 검색어 점수 증가
    public void increaseSortedKeywordRank(String productName) {
        redisTemplate.opsForZSet().incrementScore(RANKING_KEY, productName, 1);
        redisTemplate.expire(RANKING_KEY, Duration.ofMinutes(1));
    }
}
