package com.sportmall.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sportmall.entity.Brand;

/**
 * 品牌Repository接口
 */
@Repository
public interface BrandRepository extends JpaRepository<Brand, Long> {

    /**
     * 查找所有启用的品牌（未删除）
     */
    @Query("SELECT b FROM Brand b WHERE b.enabled = true AND b.deleted = false ORDER BY b.sortOrder ASC")
    List<Brand> findAllEnabled();

    /**
     * 根据名称模糊查询
     */
    @Query("SELECT b FROM Brand b WHERE b.name LIKE %:name% AND b.enabled = true AND b.deleted = false")
    List<Brand> findByNameContaining(@Param("name") String name);

    /**
     * 分页查询所有品牌（包括禁用的）
     */
    @Query("SELECT b FROM Brand b WHERE b.deleted = false")
    Page<Brand> findAllNotDeleted(Pageable pageable);

    /**
     * 检查品牌名称是否存在
     */
    @Query("SELECT COUNT(b) > 0 FROM Brand b WHERE b.name = :name AND b.deleted = false")
    boolean existsByName(@Param("name") String name);
}