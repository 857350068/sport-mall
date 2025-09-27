package com.sportmall.entity;

import com.sportmall.enums.OrderStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单实体类
 */
@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

    /**
     * 订单号
     */
    @Column(name = "order_no", unique = true, nullable = false, length = 50)
    private String orderNo;

    /**
     * 用户
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 订单状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private OrderStatus status = OrderStatus.PENDING_PAYMENT;

    /**
     * 商品总金额
     */
    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * 优惠金额
     */
    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    /**
     * 运费
     */
    @Column(name = "shipping_fee", precision = 10, scale = 2)
    private BigDecimal shippingFee = BigDecimal.ZERO;

    /**
     * 实付金额
     */
    @Column(name = "actual_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal actualAmount;

    /**
     * 收货人姓名
     */
    @Column(name = "receiver_name", nullable = false, length = 50)
    private String receiverName;

    /**
     * 收货人电话
     */
    @Column(name = "receiver_phone", nullable = false, length = 20)
    private String receiverPhone;

    /**
     * 收货地址
     */
    @Column(name = "receiver_address", nullable = false, length = 500)
    private String receiverAddress;

    /**
     * 订单备注
     */
    @Column(name = "remark", length = 500)
    private String remark;

    /**
     * 支付时间
     */
    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    /**
     * 发货时间
     */
    @Column(name = "ship_time")
    private LocalDateTime shipTime;

    /**
     * 完成时间
     */
    @Column(name = "complete_time")
    private LocalDateTime completeTime;

    /**
     * 取消时间
     */
    @Column(name = "cancel_time")
    private LocalDateTime cancelTime;

    /**
     * 取消原因
     */
    @Column(name = "cancel_reason", length = 200)
    private String cancelReason;

    /**
     * 订单项列表
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * 使用的优惠券ID
     */
    @Column(name = "coupon_id")
    private Long couponId;

    // 构造函数
    public Order() {}

    public Order(String orderNo, User user, BigDecimal totalAmount) {
        this.orderNo = orderNo;
        this.user = user;
        this.totalAmount = totalAmount;
        this.actualAmount = totalAmount;
    }

    // 业务方法
    public boolean canBePaid() {
        return status == OrderStatus.PENDING_PAYMENT && !isDeleted();
    }

    public boolean canBeCancelled() {
        return status == OrderStatus.PENDING_PAYMENT || status == OrderStatus.PAID;
    }

    public boolean canBeShipped() {
        return status == OrderStatus.PAID;
    }

    public boolean canBeCompleted() {
        return status == OrderStatus.DELIVERED;
    }

    public boolean canBeRefunded() {
        return status == OrderStatus.PAID || status == OrderStatus.SHIPPED || status == OrderStatus.DELIVERED;
    }

    public void pay() {
        if (canBePaid()) {
            this.status = OrderStatus.PAID;
            this.paymentTime = LocalDateTime.now();
        } else {
            throw new IllegalStateException("订单状态不允许支付");
        }
    }

    public void ship() {
        if (canBeShipped()) {
            this.status = OrderStatus.SHIPPED;
            this.shipTime = LocalDateTime.now();
        } else {
            throw new IllegalStateException("订单状态不允许发货");
        }
    }

    public void deliver() {
        if (status == OrderStatus.SHIPPED) {
            this.status = OrderStatus.DELIVERED;
        } else {
            throw new IllegalStateException("订单状态不允许确认收货");
        }
    }

    public void complete() {
        if (canBeCompleted()) {
            this.status = OrderStatus.COMPLETED;
            this.completeTime = LocalDateTime.now();
        } else {
            throw new IllegalStateException("订单状态不允许完成");
        }
    }

    public void cancel(String reason) {
        if (canBeCancelled()) {
            this.status = OrderStatus.CANCELLED;
            this.cancelTime = LocalDateTime.now();
            this.cancelReason = reason;
        } else {
            throw new IllegalStateException("订单状态不允许取消");
        }
    }

    public void refund() {
        if (canBeRefunded()) {
            this.status = OrderStatus.REFUNDING;
        } else {
            throw new IllegalStateException("订单状态不允许退款");
        }
    }

    public boolean isExpired() {
        // 检查订单是否超过1小时未支付
        if (status == OrderStatus.PENDING_PAYMENT && getCreatedAt() != null) {
            return LocalDateTime.now().isAfter(getCreatedAt().plusHours(1));
        }
        return false;
    }

    public void autoCancel() {
        if (isExpired()) {
            cancel("超时未支付，系统自动取消");
        }
    }

    public void calculateActualAmount() {
        this.actualAmount = totalAmount.subtract(discountAmount).add(shippingFee);
    }

    // Getter和Setter方法
    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public BigDecimal getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(BigDecimal shippingFee) {
        this.shippingFee = shippingFee;
    }

    public BigDecimal getActualAmount() {
        return actualAmount;
    }

    public void setActualAmount(BigDecimal actualAmount) {
        this.actualAmount = actualAmount;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public void setReceiverAddress(String receiverAddress) {
        this.receiverAddress = receiverAddress;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(LocalDateTime paymentTime) {
        this.paymentTime = paymentTime;
    }

    public LocalDateTime getShipTime() {
        return shipTime;
    }

    public void setShipTime(LocalDateTime shipTime) {
        this.shipTime = shipTime;
    }

    public LocalDateTime getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(LocalDateTime completeTime) {
        this.completeTime = completeTime;
    }

    public LocalDateTime getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(LocalDateTime cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + getId() +
                ", orderNo='" + orderNo + '\'' +
                ", status=" + status +
                ", actualAmount=" + actualAmount +
                '}';
    }
}