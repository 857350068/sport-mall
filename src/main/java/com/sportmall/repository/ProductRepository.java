package com.sportmall.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sportmall.entity.Product;
import com.sportmall.enums.ProductStatus;

/**
 * 商品Repository接口
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    /**
     * 查找所有上架的商品（未删除）
     */
    @Query("SELECT p FROM Product p WHERE p.status = com.sportmall.enums.ProductStatus.ACTIVE AND p.deleted = false ORDER BY p.sortOrder ASC")
    List<Product> findAllActive();

    /**
     * 根据分类ID查找商品
     */
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId AND p.status = com.sportmall.enums.ProductStatus.ACTIVE AND p.deleted = false")
    Page<Product> findByCategoryId(@Param("categoryId") Long categoryId, Pageable pageable);

    /**
     * 根据品牌ID查找商品
     */
    @Query("SELECT p FROM Product p WHERE p.brand.id = :brandId AND p.status = com.sportmall.enums.ProductStatus.ACTIVE AND p.deleted = false")
    Page<Product> findByBrandId(@Param("brandId") Long brandId, Pageable pageable);

    /**
     * 根据商品名称模糊查询
     */
    @Query("SELECT p FROM Product p WHERE p.name LIKE %:keyword% AND p.status = com.sportmall.enums.ProductStatus.ACTIVE AND p.deleted = false")
    Page<Product> findByNameContaining(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 多条件查询商品
     */
    @Query("SELECT p FROM Product p WHERE " +
           "(:keyword IS NULL OR p.name LIKE %:keyword% OR p.description LIKE %:keyword%) AND " +
           "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
           "(:brandId IS NULL OR p.brand.id = :brandId) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice) AND " +
           "(:status IS NULL OR p.status = :status) AND " +
           "p.deleted = false")
    Page<Product> findByConditions(@Param("keyword") String keyword,
                                  @Param("categoryId") Long categoryId,
                                  @Param("brandId") Long brandId,
                                  @Param("minPrice") BigDecimal minPrice,
                                  @Param("maxPrice") BigDecimal maxPrice,
                                  @Param("status") ProductStatus status,
                                  Pageable pageable);

    /**
     * 查找推荐商品
     */
    @Query("SELECT p FROM Product p WHERE p.isFeatured = true AND p.status = com.sportmall.enums.ProductStatus.ACTIVE AND p.deleted = false ORDER BY p.sortOrder ASC")
    List<Product> findFeaturedProducts();

    /**
     * 查找热销商品（按销量排序）
     */
    @Query("SELECT p FROM Product p WHERE p.status = com.sportmall.enums.ProductStatus.ACTIVE AND p.deleted = false ORDER BY p.sales DESC")
    List<Product> findBestSellingProducts(Pageable pageable);

    /**
     * 查找新品（按创建时间排序）
     */
    @Query("SELECT p FROM Product p WHERE p.status = com.sportmall.enums.ProductStatus.ACTIVE AND p.deleted = false ORDER BY p.createdAt DESC")
    List<Product> findNewProducts(Pageable pageable);

    /**
     * 根据商品状态查找
     */
    @Query("SELECT p FROM Product p WHERE p.status = :status AND p.deleted = false")
    Page<Product> findByStatus(@Param("status") ProductStatus status, Pageable pageable);

    /**
     * 查找库存不足的商品
     */
    @Query("SELECT p FROM Product p WHERE p.stock < :threshold AND p.deleted = false")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);

    /**
     * 统计各状态商品数量
     */
    @Query("SELECT p.status, COUNT(p) FROM Product p WHERE p.deleted = false GROUP BY p.status")
    List<Object[]> countByStatus();
}