package com.example.eightyage.domain.search.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class PopularKeywordDto {

    private String keyword;
    private Long count;

    public static PopularKeywordDto keywordOf(String keyword, Long score) {
        return new PopularKeywordDto(keyword, score);
    }
}
