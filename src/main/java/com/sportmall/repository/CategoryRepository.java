package com.sportmall.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sportmall.entity.Category;

/**
 * 商品分类Repository接口
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 查找所有启用的分类（未删除）
     */
    @Query("SELECT c FROM Category c WHERE c.enabled = true AND c.deleted = false ORDER BY c.sortOrder ASC")
    List<Category> findAllEnabled();

    /**
     * 根据父分类ID查找子分类
     */
    @Query("SELECT c FROM Category c WHERE c.parentId = :parentId AND c.enabled = true AND c.deleted = false ORDER BY c.sortOrder ASC")
    List<Category> findByParentId(@Param("parentId") Long parentId);

    /**
     * 查找根分类（父分类为空）
     */
    @Query("SELECT c FROM Category c WHERE c.parentId IS NULL AND c.enabled = true AND c.deleted = false ORDER BY c.sortOrder ASC")
    List<Category> findRootCategories();

    /**
     * 根据分类层级查找
     */
    @Query("SELECT c FROM Category c WHERE c.level = :level AND c.enabled = true AND c.deleted = false ORDER BY c.sortOrder ASC")
    List<Category> findByLevel(@Param("level") Integer level);

    /**
     * 根据名称模糊查询
     */
    @Query("SELECT c FROM Category c WHERE c.name LIKE %:name% AND c.enabled = true AND c.deleted = false")
    List<Category> findByNameContaining(@Param("name") String name);

    /**
     * 分页查询所有分类（包括禁用的）
     */
    @Query("SELECT c FROM Category c WHERE c.deleted = false")
    Page<Category> findAllNotDeleted(Pageable pageable);

    /**
     * 检查分类名称是否存在
     */
    @Query("SELECT COUNT(c) > 0 FROM Category c WHERE c.name = :name AND c.deleted = false")
    boolean existsByName(@Param("name") String name);
}