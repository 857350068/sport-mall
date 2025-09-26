package com.sportmall.enums;

/**
 * 促销活动类型枚举
 */
public enum PromotionType {
    /**
     * 固定折扣
     */
    FIXED_DISCOUNT("固定折扣"),
    
    /**
     * 满减
     */
    FULL_REDUCTION("满减"),
    
    /**
     * 固定金额抵扣
     */
    FIXED_AMOUNT("固定金额抵扣");

    private final String description;

    PromotionType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}