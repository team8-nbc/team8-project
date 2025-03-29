package com.example.eightyage.domain.product.category;

public enum Category {
    SKINCARE("스킨케어"),
    MAKEUP("메이크업"),
    HAIRCARE("헤어케어"),
    BODYCARE("바디케어"),
    FRAGRANCE("향수"),
    SUNCARE("선케어"),
    CLEANSING("클렌징"),
    MASK_PACK("마스크팩"),
    MEN_CARE("남성용"),
    TOOL("뷰티 도구");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
