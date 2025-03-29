package com.example.eightyage.domain.search.service.v3;

import com.example.eightyage.domain.search.dto.PopularKeywordDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopularKeywordServiceV3 {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String RANKING_KEY = "rankingPopularKeywords";

    // 인기 검색어 상위 N개 조회
    @Transactional(readOnly = true)
    public List<PopularKeywordDto> searchPopularKeywords(int limit) {
        Set<ZSetOperations.TypedTuple<String>> keywordSet =
                redisTemplate.opsForZSet().reverseRangeWithScores(RANKING_KEY, 0, limit - 1);

        if (keywordSet == null) {
            return List.of();
        }
        return keywordSet.stream().map(tuple -> PopularKeywordDto.keywordOf(tuple.getValue(), Objects.requireNonNull(tuple.getScore()).longValue()))
                .collect(Collectors.toList());
    }
}
