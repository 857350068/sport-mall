package com.sportmall.repository;

import com.sportmall.entity.Order;
import com.sportmall.entity.User;
import com.sportmall.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 订单Repository接口
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * 根据用户查找订单
     */
    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.deleted = false ORDER BY o.createdAt DESC")
    Page<Order> findByUser(@Param("user") User user, Pageable pageable);

    /**
     * 根据订单号查找订单
     */
    @Query("SELECT o FROM Order o WHERE o.orderNo = :orderNo AND o.deleted = false")
    Order findByOrderNo(@Param("orderNo") String orderNo);

    /**
     * 根据订单状态查找订单
     */
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.deleted = false ORDER BY o.createdAt DESC")
    Page<Order> findByStatus(@Param("status") OrderStatus status, Pageable pageable);

    /**
     * 查找用户指定状态的订单
     */
    @Query("SELECT o FROM Order o WHERE o.user = :user AND o.status = :status AND o.deleted = false ORDER BY o.createdAt DESC")
    Page<Order> findByUserAndStatus(@Param("user") User user, @Param("status") OrderStatus status, Pageable pageable);

    /**
     * 查找超时未支付的订单
     */
    @Query("SELECT o FROM Order o WHERE o.status = com.sportmall.enums.OrderStatus.PENDING_PAYMENT AND o.createdAt < :expireTime AND o.deleted = false")
    List<Order> findExpiredOrders(@Param("expireTime") LocalDateTime expireTime);

    /**
     * 多条件查询订单
     */
    @Query("SELECT o FROM Order o WHERE " +
           "(:orderNo IS NULL OR o.orderNo LIKE %:orderNo%) AND " +
           "(:status IS NULL OR o.status = :status) AND " +
           "(:userId IS NULL OR o.user.id = :userId) AND " +
           "(:startDate IS NULL OR o.createdAt >= :startDate) AND " +
           "(:endDate IS NULL OR o.createdAt <= :endDate) AND " +
           "o.deleted = false ORDER BY o.createdAt DESC")
    Page<Order> findByConditions(@Param("orderNo") String orderNo,
                                @Param("status") OrderStatus status,
                                @Param("userId") Long userId,
                                @Param("startDate") LocalDateTime startDate,
                                @Param("endDate") LocalDateTime endDate,
                                Pageable pageable);

    /**
     * 统计各状态订单数量
     */
    @Query("SELECT o.status, COUNT(o) FROM Order o WHERE o.deleted = false GROUP BY o.status")
    List<Object[]> countByStatus();

    /**
     * 统计用户订单数量
     */
    @Query("SELECT COUNT(o) FROM Order o WHERE o.user = :user AND o.deleted = false")
    Long countByUser(@Param("user") User user);

    /**
     * 查找所有订单（分页）
     */
    @Query("SELECT o FROM Order o WHERE o.deleted = false ORDER BY o.createdAt DESC")
    Page<Order> findAllNotDeleted(Pageable pageable);
}