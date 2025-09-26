package com.sportmall.enums;

/**
 * 用户角色枚举
 */
public enum UserRole {
    /**
     * 管理员
     */
    ADMIN("管理员", "ROLE_ADMIN"),
    
    /**
     * 普通用户
     */
    USER("普通用户", "ROLE_USER");

    private final String description;
    private final String authority;

    UserRole(String description, String authority) {
        this.description = description;
        this.authority = authority;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthority() {
        return authority;
    }
}