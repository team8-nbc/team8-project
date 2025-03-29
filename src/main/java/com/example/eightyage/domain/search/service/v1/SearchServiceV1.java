package com.example.eightyage.domain.search.service.v1;

import com.example.eightyage.domain.search.entity.SearchLog;
import com.example.eightyage.domain.search.repository.SearchLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class SearchServiceV1 {

    private final SearchLogRepository searchLogRepository;

    // 검색 키워드를 로그에 저장
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveSearchLog(String keyword){
        if(StringUtils.hasText(keyword)){
            searchLogRepository.save(SearchLog.keywordOf(keyword));
        }
    }
}
