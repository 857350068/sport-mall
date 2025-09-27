package com.sportmall.service;

import com.sportmall.entity.Coupon;
import com.sportmall.enums.CouponType;
import com.sportmall.repository.CouponRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 优惠券服务类
 */
@Service
@Transactional
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    /**
     * 创建优惠券
     */
    public Coupon createCoupon(String name, String description, CouponType type, 
                             BigDecimal discountValue, BigDecimal minAmount, 
                             BigDecimal maxDiscount, LocalDateTime validFrom, 
                             LocalDateTime validTo, Integer totalCount, Integer perUserLimit) {
        // 检查优惠券编码是否已存在
        String code;
        do {
            code = generateCouponCode();
        } while (couponRepository.existsByCode(code));

        Coupon coupon = new Coupon();
        coupon.setName(name);
        coupon.setCode(code);
        coupon.setDescription(description);
        coupon.setType(type);
        coupon.setDiscountValue(discountValue);
        coupon.setMinAmount(minAmount);
        coupon.setMaxDiscount(maxDiscount);
        coupon.setValidFrom(validFrom);
        coupon.setValidTo(validTo);
        coupon.setTotalCount(totalCount);
        coupon.setPerUserLimit(perUserLimit);

        return couponRepository.save(coupon);
    }

    /**
     * 生成优惠券编码
     */
    private String generateCouponCode() {
        return "CPN" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * 根据ID获取优惠券
     */
    @Transactional(readOnly = true)
    public Coupon getCouponById(Long id) {
        return couponRepository.findById(id)
                .filter(c -> !c.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("优惠券不存在"));
    }

    /**
     * 根据编码获取优惠券
     */
    @Transactional(readOnly = true)
    public Coupon getCouponByCode(String code) {
        return couponRepository.findByCode(code)
                .filter(c -> !c.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("优惠券不存在"));
    }

    /**
     * 更新优惠券
     */
    public Coupon updateCoupon(Long id, String name, String description, CouponType type,
                             BigDecimal discountValue, BigDecimal minAmount,
                             BigDecimal maxDiscount, LocalDateTime validFrom,
                             LocalDateTime validTo, Integer totalCount, Integer perUserLimit) {
        Coupon coupon = getCouponById(id);

        coupon.setName(name);
        coupon.setDescription(description);
        coupon.setType(type);
        coupon.setDiscountValue(discountValue);
        coupon.setMinAmount(minAmount);
        coupon.setMaxDiscount(maxDiscount);
        coupon.setValidFrom(validFrom);
        coupon.setValidTo(validTo);
        coupon.setTotalCount(totalCount);
        coupon.setPerUserLimit(perUserLimit);

        return couponRepository.save(coupon);
    }

    /**
     * 删除优惠券（逻辑删除）
     */
    public void deleteCoupon(Long id) {
        Coupon coupon = getCouponById(id);
        coupon.delete();
        couponRepository.save(coupon);
    }

    /**
     * 获取所有有效优惠券
     */
    @Transactional(readOnly = true)
    public List<Coupon> getValidCoupons() {
        return couponRepository.findValidCoupons(LocalDateTime.now());
    }

    /**
     * 根据类型获取优惠券（分页）
     */
    @Transactional(readOnly = true)
    public Page<Coupon> getCouponsByType(CouponType type, Pageable pageable) {
        return couponRepository.findByType(type, pageable);
    }

    /**
     * 根据启用状态获取优惠券（分页）
     */
    @Transactional(readOnly = true)
    public Page<Coupon> getCouponsByEnabled(Boolean enabled, Pageable pageable) {
        return couponRepository.findByEnabled(enabled, pageable);
    }

    /**
     * 获取所有优惠券（分页）
     */
    @Transactional(readOnly = true)
    public Page<Coupon> getAllCoupons(Pageable pageable) {
        return couponRepository.findAll(pageable);
    }

    /**
     * 启用/禁用优惠券
     */
    public void toggleCouponStatus(Long id, Boolean enabled) {
        Coupon coupon = getCouponById(id);
        coupon.setEnabled(enabled);
        couponRepository.save(coupon);
    }

    /**
     * 计算优惠金额
     */
    public BigDecimal calculateDiscount(String couponCode, BigDecimal orderAmount) {
        Coupon coupon = getCouponByCode(couponCode);
        
        if (!coupon.canBeUsed(orderAmount)) {
            throw new IllegalArgumentException("优惠券不可用");
        }

        return coupon.calculateDiscount(orderAmount);
    }

    /**
     * 使用优惠券
     */
    public void useCoupon(String couponCode) {
        Coupon coupon = getCouponByCode(couponCode);
        coupon.use();
        couponRepository.save(coupon);
    }

    /**
     * 获取即将过期的优惠券
     */
    @Transactional(readOnly = true)
    public List<Coupon> getExpiringSoonCoupons() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusDays(7); // 7天内过期
        return couponRepository.findExpiringSoon(now, endDate);
    }
}