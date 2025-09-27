package com.sportmall.entity;

import com.sportmall.enums.PromotionType;
import com.sportmall.enums.PromotionStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 促销活动实体类
 */
@Entity
@Table(name = "promotions")
public class Promotion extends BaseEntity {

    /**
     * 活动名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 活动描述
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 活动类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private PromotionType type;

    /**
     * 活动状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PromotionStatus status = PromotionStatus.PENDING;

    /**
     * 折扣值
     */
    @Column(name = "discount_value", nullable = false, precision = 10, scale = 2)
    private BigDecimal discountValue;

    /**
     * 最低消费金额
     */
    @Column(name = "min_amount", precision = 10, scale = 2)
    private BigDecimal minAmount;

    /**
     * 最大折扣金额
     */
    @Column(name = "max_discount", precision = 10, scale = 2)
    private BigDecimal maxDiscount;

    /**
     * 活动开始时间
     */
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    /**
     * 活动结束时间
     */
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    /**
     * 是否启用
     */
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    /**
     * 排序权重
     */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    /**
     * 关联的商品IDs（JSON格式存储）
     */
    @Column(name = "product_ids", columnDefinition = "TEXT")
    private String productIds;

    // 构造函数
    public Promotion() {}

    public Promotion(String name, PromotionType type, BigDecimal discountValue) {
        this.name = name;
        this.type = type;
        this.discountValue = discountValue;
    }

    // 业务方法
    public boolean isActive() {
        LocalDateTime now = LocalDateTime.now();
        return status == PromotionStatus.ACTIVE && enabled && !isDeleted() &&
               now.isAfter(startTime) && now.isBefore(endTime);
    }

    public boolean canBeUsed(BigDecimal orderAmount) {
        return isActive() && (minAmount == null || orderAmount.compareTo(minAmount) >= 0);
    }

    public BigDecimal calculateDiscount(BigDecimal orderAmount) {
        if (!canBeUsed(orderAmount)) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount = BigDecimal.ZERO;
        switch (type) {
            case FIXED_DISCOUNT:
                discount = orderAmount.multiply(discountValue.divide(BigDecimal.valueOf(100)));
                break;
            case FULL_REDUCTION:
                if (orderAmount.compareTo(minAmount) >= 0) {
                    discount = discountValue;
                }
                break;
            case FIXED_AMOUNT:
                discount = discountValue;
                break;
        }

        // 检查最大折扣限制
        if (maxDiscount != null && discount.compareTo(maxDiscount) > 0) {
            discount = maxDiscount;
        }

        return discount;
    }

    public void start() {
        if (status == PromotionStatus.PENDING) {
            this.status = PromotionStatus.ACTIVE;
        }
    }

    public void end() {
        if (status == PromotionStatus.ACTIVE) {
            this.status = PromotionStatus.ENDED;
        }
    }

    public void cancel() {
        this.status = PromotionStatus.CANCELLED;
    }

    public boolean isPending() {
        return status == PromotionStatus.PENDING;
    }

    public boolean isEnded() {
        return status == PromotionStatus.ENDED;
    }

    public boolean isCancelled() {
        return status == PromotionStatus.CANCELLED;
    }

    // Getter和Setter方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PromotionType getType() {
        return type;
    }

    public void setType(PromotionType type) {
        this.type = type;
    }

    public PromotionStatus getStatus() {
        return status;
    }

    public void setStatus(PromotionStatus status) {
        this.status = status;
    }

    public BigDecimal getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(BigDecimal discountValue) {
        this.discountValue = discountValue;
    }

    public BigDecimal getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigDecimal minAmount) {
        this.minAmount = minAmount;
    }

    public BigDecimal getMaxDiscount() {
        return maxDiscount;
    }

    public void setMaxDiscount(BigDecimal maxDiscount) {
        this.maxDiscount = maxDiscount;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public String getProductIds() {
        return productIds;
    }

    public void setProductIds(String productIds) {
        this.productIds = productIds;
    }

    @Override
    public String toString() {
        return "Promotion{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", discountValue=" + discountValue +
                '}';
    }
}