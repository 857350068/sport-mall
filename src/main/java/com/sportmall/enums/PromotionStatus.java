package com.sportmall.enums;

/**
 * 促销活动状态枚举
 */
public enum PromotionStatus {
    /**
     * 待开始
     */
    PENDING("待开始"),
    
    /**
     * 进行中
     */
    ACTIVE("进行中"),
    
    /**
     * 已结束
     */
    ENDED("已结束"),
    
    /**
     * 已取消
     */
    CANCELLED("已取消");

    private final String description;

    PromotionStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}