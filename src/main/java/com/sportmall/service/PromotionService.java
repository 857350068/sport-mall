package com.sportmall.service;

import com.sportmall.entity.Promotion;
import com.sportmall.enums.PromotionType;
import com.sportmall.enums.PromotionStatus;
import com.sportmall.repository.PromotionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 促销活动服务类
 */
@Service
@Transactional
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    /**
     * 创建促销活动
     */
    public Promotion createPromotion(String name, String description, PromotionType type,
                                   BigDecimal discountValue, BigDecimal minAmount,
                                   BigDecimal maxDiscount, LocalDateTime startTime,
                                   LocalDateTime endTime) {
        Promotion promotion = new Promotion();
        promotion.setName(name);
        promotion.setDescription(description);
        promotion.setType(type);
        promotion.setDiscountValue(discountValue);
        promotion.setMinAmount(minAmount);
        promotion.setMaxDiscount(maxDiscount);
        promotion.setStartTime(startTime);
        promotion.setEndTime(endTime);
        promotion.setStatus(PromotionStatus.PENDING);

        return promotionRepository.save(promotion);
    }

    /**
     * 根据ID获取促销活动
     */
    @Transactional(readOnly = true)
    public Promotion getPromotionById(Long id) {
        return promotionRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("促销活动不存在"));
    }

    /**
     * 更新促销活动
     */
    public Promotion updatePromotion(Long id, String name, String description, PromotionType type,
                                   BigDecimal discountValue, BigDecimal minAmount,
                                   BigDecimal maxDiscount, LocalDateTime startTime,
                                   LocalDateTime endTime, Boolean enabled) {
        Promotion promotion = getPromotionById(id);

        promotion.setName(name);
        promotion.setDescription(description);
        promotion.setType(type);
        promotion.setDiscountValue(discountValue);
        promotion.setMinAmount(minAmount);
        promotion.setMaxDiscount(maxDiscount);
        promotion.setStartTime(startTime);
        promotion.setEndTime(endTime);
        if (enabled != null) {
            promotion.setEnabled(enabled);
        }

        return promotionRepository.save(promotion);
    }

    /**
     * 删除促销活动（逻辑删除）
     */
    public void deletePromotion(Long id) {
        Promotion promotion = getPromotionById(id);
        promotion.delete();
        promotionRepository.save(promotion);
    }

    /**
     * 获取进行中的促销活动
     */
    @Transactional(readOnly = true)
    public List<Promotion> getActivePromotions() {
        return promotionRepository.findActivePromotions(LocalDateTime.now());
    }

    /**
     * 根据类型获取促销活动（分页）
     */
    @Transactional(readOnly = true)
    public Page<Promotion> getPromotionsByType(PromotionType type, Pageable pageable) {
        return promotionRepository.findByType(type, pageable);
    }

    /**
     * 根据状态获取促销活动（分页）
     */
    @Transactional(readOnly = true)
    public Page<Promotion> getPromotionsByStatus(PromotionStatus status, Pageable pageable) {
        return promotionRepository.findByStatus(status, pageable);
    }

    /**
     * 获取所有促销活动（分页）
     */
    @Transactional(readOnly = true)
    public Page<Promotion> getAllPromotions(Pageable pageable) {
        return promotionRepository.findAll(pageable);
    }

    /**
     * 启动促销活动
     */
    public void startPromotion(Long id) {
        Promotion promotion = getPromotionById(id);
        
        if (!promotion.isPending()) {
            throw new IllegalArgumentException("只有待开始的活动才能启动");
        }

        if (promotion.getStartTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("活动开始时间未到");
        }

        promotion.start();
        promotionRepository.save(promotion);
    }

    /**
     * 结束促销活动
     */
    public void endPromotion(Long id) {
        Promotion promotion = getPromotionById(id);
        
        if (!promotion.isActive()) {
            throw new IllegalArgumentException("只有进行中的活动才能结束");
        }

        promotion.end();
        promotionRepository.save(promotion);
    }

    /**
     * 取消促销活动
     */
    public void cancelPromotion(Long id) {
        Promotion promotion = getPromotionById(id);
        promotion.cancel();
        promotionRepository.save(promotion);
    }

    /**
     * 获取即将开始的促销活动
     */
    @Transactional(readOnly = true)
    public List<Promotion> getUpcomingPromotions() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endDate = now.plusDays(7); // 7天内开始
        return promotionRepository.findUpcomingPromotions(now, endDate);
    }

    /**
     * 获取已结束的促销活动
     */
    @Transactional(readOnly = true)
    public List<Promotion> getEndedPromotions() {
        return promotionRepository.findEndedPromotions(LocalDateTime.now());
    }

    /**
     * 根据商品ID获取相关促销活动
     */
    @Transactional(readOnly = true)
    public List<Promotion> getPromotionsByProductId(Long productId) {
        return promotionRepository.findByProductId(productId, LocalDateTime.now());
    }

    /**
     * 计算促销折扣金额
     */
    public BigDecimal calculateDiscount(Long promotionId, BigDecimal orderAmount) {
        Promotion promotion = getPromotionById(promotionId);
        
        if (!promotion.canBeUsed(orderAmount)) {
            throw new IllegalArgumentException("促销活动不可用");
        }

        return promotion.calculateDiscount(orderAmount);
    }
}