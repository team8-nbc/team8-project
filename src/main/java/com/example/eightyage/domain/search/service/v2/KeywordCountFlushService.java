package com.example.eightyage.domain.search.service.v2;

import com.example.eightyage.domain.search.entity.KeywordCount;
import com.example.eightyage.domain.search.repository.KeywordCountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class KeywordCountFlushService {

    private final CacheManager cacheManager;
    private final KeywordCountRepository keywordCountRepository;
    private static final String KEYWORD_COUNT_MAP = "keywordCountMap";
    private static final String KEYWORD_KEY_SET = "keywordKeySet";

    @Transactional
    @Scheduled(fixedRate = 5 * 60 * 1000) // 5분마다 실행
    public void flushKeywordCounts() {
        Cache countCache = cacheManager.getCache(KEYWORD_COUNT_MAP);
        Cache keySetCache = cacheManager.getCache(KEYWORD_KEY_SET);

        if (countCache == null || keySetCache == null) {
            log.warn("캐시를 찾을 수 없습니다.");
            return;
        }

        try {
            // 키 목록 가져오기
            Set<String> keywordSet = keySetCache.get("keywords", Set.class);
            if (keywordSet == null || keywordSet.isEmpty()) {
                log.info("flush 할 키워드가 없습니다.");
                return;
            }

            int flushed = 0;

            // 반복문을 이용하여 저장하기
            for (String keyword : keywordSet) {
                String countStr = countCache.get(keyword, String.class);
                if (countStr == null) continue;

                Long count = Long.parseLong(countStr);
                if (count == 0L) continue;

                keywordCountRepository.findById(keyword)
                        .ifPresentOrElse(
                                exist -> exist.updateCount(exist.getCount() + count),
                                () -> keywordCountRepository.save(new KeywordCount(keyword, count))
                        );
                flushed++;
                countCache.evict(keyword);
            }

            keySetCache.put("keywords", new java.util.HashSet<>());

            log.info("{}개의 키워드 플러시 성공", flushed);

        } catch (Exception e) {
            log.error("플러시 실패", e);
        }

    }

}
