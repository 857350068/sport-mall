package com.sportmall.enums;

/**
 * 商品状态枚举
 */
public enum ProductStatus {
    /**
     * 上架
     */
    ACTIVE("上架"),
    
    /**
     * 下架
     */
    INACTIVE("下架"),
    
    /**
     * 草稿
     */
    DRAFT("草稿"),
    
    /**
     * 售罄
     */
    SOLD_OUT("售罄");

    private final String description;

    ProductStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}