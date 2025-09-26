package com.sportmall.enums;

/**
 * 订单状态枚举
 */
public enum OrderStatus {
    /**
     * 待付款
     */
    PENDING_PAYMENT("待付款"),
    
    /**
     * 已付款
     */
    PAID("已付款"),
    
    /**
     * 已发货
     */
    SHIPPED("已发货"),
    
    /**
     * 已收货
     */
    DELIVERED("已收货"),
    
    /**
     * 已完成
     */
    COMPLETED("已完成"),
    
    /**
     * 已取消
     */
    CANCELLED("已取消"),
    
    /**
     * 退款中
     */
    REFUNDING("退款中"),
    
    /**
     * 已退款
     */
    REFUNDED("已退款");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}