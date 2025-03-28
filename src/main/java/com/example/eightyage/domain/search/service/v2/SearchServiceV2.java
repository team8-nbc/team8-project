package com.example.eightyage.domain.search.service.v2;

import com.example.eightyage.domain.search.entity.SearchLog;
import com.example.eightyage.domain.search.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class SearchServiceV2 {

    private final SearchLogRepository searchLogRepository;
    private final CacheManager cacheManager;
    private static final String KEYWORD_COUNT_MAP = "keywordCountMap";
    private static final String KEYWORD_KEY_SET = "keywordKeySet";


    // 검색 키워드를 로그에 저장
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSearchLog(String keyword) {
        if (StringUtils.hasText(keyword)) {
            searchLogRepository.save(SearchLog.of(keyword));
        }
    }

    // 검색 시 키워드 카운트 증가
    @Transactional
    public void increaseKeywordCount(String keyword) {
        if (!StringUtils.hasText(keyword)) return;

        Cache countCache = cacheManager.getCache(KEYWORD_COUNT_MAP);

        if (countCache != null) {
            String countStr = countCache.get(keyword, String.class);
            long count = (countStr == null) ? 1L : Long.parseLong(countStr) + 1;
            countCache.put(keyword, Long.toString(count));
        }

        updateKeywordSet(keyword);
    }

    private void updateKeywordSet(String keyword) {
        Cache keySetCache = cacheManager.getCache(KEYWORD_KEY_SET);
        if (keySetCache != null) {
            Set<String> keywordSet = keySetCache.get("keywords", Set.class);
            if (keywordSet == null) {
                keywordSet = new HashSet<>();
            }
            keywordSet.add(keyword);
            keySetCache.put("keywords", keywordSet);
        }
    }
}
