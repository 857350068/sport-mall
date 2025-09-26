package com.sportmall.repository;

import com.sportmall.entity.Coupon;
import com.sportmall.enums.CouponType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 优惠券Repository接口
 */
@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    /**
     * 根据优惠券编码查找
     */
    @Query("SELECT c FROM Coupon c WHERE c.code = :code AND c.deleted = false")
    Optional<Coupon> findByCode(@Param("code") String code);

    /**
     * 查找有效的优惠券
     */
    @Query("SELECT c FROM Coupon c WHERE c.enabled = true AND c.deleted = false " +
           "AND c.validFrom <= :now AND c.validTo >= :now AND c.usedCount < c.totalCount")
    List<Coupon> findValidCoupons(@Param("now") LocalDateTime now);

    /**
     * 根据类型查找优惠券
     */
    @Query("SELECT c FROM Coupon c WHERE c.type = :type AND c.enabled = true AND c.deleted = false")
    Page<Coupon> findByType(@Param("type") CouponType type, Pageable pageable);

    /**
     * 根据状态查找优惠券
     */
    @Query("SELECT c FROM Coupon c WHERE c.enabled = :enabled AND c.deleted = false")
    Page<Coupon> findByEnabled(@Param("enabled") Boolean enabled, Pageable pageable);

    /**
     * 查找即将过期的优惠券
     */
    @Query("SELECT c FROM Coupon c WHERE c.enabled = true AND c.deleted = false " +
           "AND c.validTo BETWEEN :startDate AND :endDate")
    List<Coupon> findExpiringSoon(@Param("startDate") LocalDateTime startDate, 
                                 @Param("endDate") LocalDateTime endDate);

    /**
     * 检查优惠券编码是否存在
     */
    @Query("SELECT COUNT(c) > 0 FROM Coupon c WHERE c.code = :code AND c.deleted = false")
    boolean existsByCode(@Param("code") String code);

    /**
     * 统计各类型优惠券数量
     */
    @Query("SELECT c.type, COUNT(c) FROM Coupon c WHERE c.deleted = false GROUP BY c.type")
    List<Object[]> countByType();

    /**
     * 统计各状态优惠券数量
     */
    @Query("SELECT CASE WHEN c.usedCount >= c.totalCount THEN 'EXPIRED' " +
           "WHEN c.validTo < :now THEN 'EXPIRED' " +
           "WHEN c.usedCount > 0 THEN 'USED' " +
           "ELSE 'UNUSED' END as status, COUNT(c) " +
           "FROM Coupon c WHERE c.deleted = false GROUP BY status")
    List<Object[]> countByUsageStatus(@Param("now") LocalDateTime now);
}