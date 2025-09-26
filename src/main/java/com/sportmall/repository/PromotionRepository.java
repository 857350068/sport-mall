package com.sportmall.repository;

import com.sportmall.entity.Promotion;
import com.sportmall.enums.PromotionStatus;
import com.sportmall.enums.PromotionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 促销活动Repository接口
 */
@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    /**
     * 查找进行中的促销活动
     */
    @Query("SELECT p FROM Promotion p WHERE p.status = com.sportmall.enums.PromotionStatus.ACTIVE AND p.enabled = true " +
           "AND p.startTime <= :now AND p.endTime >= :now AND p.deleted = false")
    List<Promotion> findActivePromotions(@Param("now") LocalDateTime now);

    /**
     * 根据类型查找促销活动
     */
    @Query("SELECT p FROM Promotion p WHERE p.type = :type AND p.deleted = false")
    Page<Promotion> findByType(@Param("type") PromotionType type, Pageable pageable);

    /**
     * 根据状态查找促销活动
     */
    @Query("SELECT p FROM Promotion p WHERE p.status = :status AND p.deleted = false")
    Page<Promotion> findByStatus(@Param("status") PromotionStatus status, Pageable pageable);

    /**
     * 查找即将开始的促销活动
     */
    @Query("SELECT p FROM Promotion p WHERE p.status = com.sportmall.enums.PromotionStatus.PENDING AND p.startTime > :now " +
           "AND p.startTime <= :endDate AND p.deleted = false ORDER BY p.startTime ASC")
    List<Promotion> findUpcomingPromotions(@Param("now") LocalDateTime now, 
                                          @Param("endDate") LocalDateTime endDate);

    /**
     * 查找已结束的促销活动
     */
    @Query("SELECT p FROM Promotion p WHERE (p.status = com.sportmall.enums.PromotionStatus.ENDED OR p.endTime < :now) AND p.deleted = false")
    List<Promotion> findEndedPromotions(@Param("now") LocalDateTime now);

    /**
     * 根据商品ID查找相关促销活动
     */
    @Query("SELECT p FROM Promotion p WHERE p.productIds LIKE %:productId% AND p.status = com.sportmall.enums.PromotionStatus.ACTIVE " +
           "AND p.enabled = true AND p.startTime <= :now AND p.endTime >= :now AND p.deleted = false")
    List<Promotion> findByProductId(@Param("productId") Long productId, @Param("now") LocalDateTime now);

    /**
     * 统计各类型促销活动数量
     */
    @Query("SELECT p.type, COUNT(p) FROM Promotion p WHERE p.deleted = false GROUP BY p.type")
    List<Object[]> countByType();

    /**
     * 统计各状态促销活动数量
     */
    @Query("SELECT p.status, COUNT(p) FROM Promotion p WHERE p.deleted = false GROUP BY p.status")
    List<Object[]> countByStatus();
}