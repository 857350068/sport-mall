package com.sportmall.enums;

/**
 * 优惠券类型枚举
 */
public enum CouponType {
    /**
     * 固定金额折扣
     */
    FIXED_AMOUNT("固定金额"),
    
    /**
     * 百分比折扣
     */
    PERCENTAGE("百分比折扣"),
    
    /**
     * 满减
     */
    FULL_REDUCTION("满减");

    private final String description;

    CouponType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}