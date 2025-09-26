package com.sportmall.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sportmall.entity.CartItem;
import com.sportmall.entity.Product;
import com.sportmall.entity.User;

/**
 * 购物车Repository接口
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    /**
     * 根据用户查找购物车项
     */
    @Query("SELECT c FROM CartItem c WHERE c.user = :user AND c.deleted = false ORDER BY c.createdAt DESC")
    List<CartItem> findByUser(@Param("user") User user);

    /**
     * 根据用户和商品查找购物车项
     */
    @Query("SELECT c FROM CartItem c WHERE c.user = :user AND c.product = :product AND c.deleted = false")
    Optional<CartItem> findByUserAndProduct(@Param("user") User user, @Param("product") Product product);

    /**
     * 查找用户选中的购物车项
     */
    @Query("SELECT c FROM CartItem c WHERE c.user = :user AND c.selected = true AND c.deleted = false ORDER BY c.createdAt DESC")
    List<CartItem> findSelectedByUser(@Param("user") User user);

    /**
     * 统计用户购物车商品数量
     */
    @Query("SELECT COUNT(c) FROM CartItem c WHERE c.user = :user AND c.deleted = false")
    Long countByUser(@Param("user") User user);

    /**
     * 删除用户的指定商品购物车项
     */
    @Query("DELETE FROM CartItem c WHERE c.user = :user AND c.product = :product")
    void deleteByUserAndProduct(@Param("user") User user, @Param("product") Product product);

    /**
     * 清空用户购物车
     */
    @Query("UPDATE CartItem c SET c.deleted = true WHERE c.user = :user")
    void clearByUser(@Param("user") User user);
}