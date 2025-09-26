package com.sportmall.enums;

/**
 * 用户状态枚举
 */
public enum UserStatus {
    /**
     * 正常
     */
    ACTIVE("正常"),
    
    /**
     * 禁用
     */
    DISABLED("禁用"),
    
    /**
     * 待激活
     */
    PENDING("待激活");

    private final String description;

    UserStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}