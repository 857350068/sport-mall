package com.sportmall.entity;

import com.sportmall.enums.CouponType;
import com.sportmall.enums.CouponStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 优惠券实体类
 */
@Entity
@Table(name = "coupons")
public class Coupon extends BaseEntity {

    /**
     * 优惠券名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 优惠券编码
     */
    @Column(name = "code", unique = true, nullable = false, length = 50)
    private String code;

    /**
     * 优惠券类型
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private CouponType type;

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
     * 有效开始时间
     */
    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFrom;

    /**
     * 有效结束时间
     */
    @Column(name = "valid_to", nullable = false)
    private LocalDateTime validTo;

    /**
     * 发放总数
     */
    @Column(name = "total_count", nullable = false)
    private Integer totalCount;

    /**
     * 已使用数量
     */
    @Column(name = "used_count", nullable = false)
    private Integer usedCount = 0;

    /**
     * 每人限领数量
     */
    @Column(name = "per_user_limit", nullable = false)
    private Integer perUserLimit = 1;

    /**
     * 优惠券描述
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 是否启用
     */
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    // 构造函数
    public Coupon() {}

    public Coupon(String name, String code, CouponType type, BigDecimal discountValue) {
        this.name = name;
        this.code = code;
        this.type = type;
        this.discountValue = discountValue;
    }

    // 业务方法
    public boolean isValid() {
        LocalDateTime now = LocalDateTime.now();
        return enabled && !isDeleted() && 
               now.isAfter(validFrom) && now.isBefore(validTo) &&
               usedCount < totalCount;
    }

    public boolean canBeUsed(BigDecimal orderAmount) {
        return isValid() && (minAmount == null || orderAmount.compareTo(minAmount) >= 0);
    }

    public BigDecimal calculateDiscount(BigDecimal orderAmount) {
        if (!canBeUsed(orderAmount)) {
            return BigDecimal.ZERO;
        }

        BigDecimal discount = BigDecimal.ZERO;
        switch (type) {
            case FIXED_AMOUNT:
                discount = discountValue;
                break;
            case PERCENTAGE:
                discount = orderAmount.multiply(discountValue.divide(BigDecimal.valueOf(100)));
                break;
            case FULL_REDUCTION:
                if (orderAmount.compareTo(minAmount) >= 0) {
                    discount = discountValue;
                }
                break;
        }

        // 检查最大折扣限制
        if (maxDiscount != null && discount.compareTo(maxDiscount) > 0) {
            discount = maxDiscount;
        }

        return discount;
    }

    public void use() {
        if (!isValid()) {
            throw new IllegalStateException("优惠券不可用");
        }
        this.usedCount++;
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(validTo);
    }

    public int getRemainingCount() {
        return totalCount - usedCount;
    }

    // Getter和Setter方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CouponType getType() {
        return type;
    }

    public void setType(CouponType type) {
        this.type = type;
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

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidTo() {
        return validTo;
    }

    public void setValidTo(LocalDateTime validTo) {
        this.validTo = validTo;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getUsedCount() {
        return usedCount;
    }

    public void setUsedCount(Integer usedCount) {
        this.usedCount = usedCount;
    }

    public Integer getPerUserLimit() {
        return perUserLimit;
    }

    public void setPerUserLimit(Integer perUserLimit) {
        this.perUserLimit = perUserLimit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Coupon{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", type=" + type +
                ", discountValue=" + discountValue +
                '}';
    }
}