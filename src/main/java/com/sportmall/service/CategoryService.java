package com.sportmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sportmall.entity.Category;
import com.sportmall.repository.CategoryRepository;

/**
 * 商品分类服务类
 */
@Service
@Transactional
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * 创建分类
     */
    public Category createCategory(String name, String description, Long parentId) {
        // 检查分类名称是否已存在
        if (categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("分类名称已存在");
        }

        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        category.setParentId(parentId);
        
        // 设置层级
        if (parentId == null) {
            category.setLevel(1);
        } else {
            Category parent = getCategoryById(parentId);
            category.setLevel(parent.getLevel() + 1);
        }

        return categoryRepository.save(category);
    }

    /**
     * 更新分类
     */
    public Category updateCategory(Long id, String name, String description, Boolean enabled) {
        Category category = getCategoryById(id);
        
        // 检查名称是否被其他分类使用
        if (!category.getName().equals(name) && categoryRepository.existsByName(name)) {
            throw new IllegalArgumentException("分类名称已存在");
        }

        category.setName(name);
        category.setDescription(description);
        if (enabled != null) {
            category.setEnabled(enabled);
        }

        return categoryRepository.save(category);
    }

    /**
     * 根据ID获取分类
     */
    @Transactional(readOnly = true)
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .filter(c -> !c.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("分类不存在"));
    }

    /**
     * 删除分类（逻辑删除）
     */
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        
        // 检查是否有子分类
        List<Category> children = categoryRepository.findByParentId(id);
        if (!children.isEmpty()) {
            throw new IllegalArgumentException("该分类下有子分类，不能删除");
        }
        
        category.delete();
        categoryRepository.save(category);
    }

    /**
     * 获取所有启用的分类
     */
    @Transactional(readOnly = true)
    public List<Category> getAllEnabledCategories() {
        return categoryRepository.findAllEnabled();
    }

    /**
     * 获取根分类
     */
    @Transactional(readOnly = true)
    public List<Category> getRootCategories() {
        return categoryRepository.findRootCategories();
    }

    /**
     * 根据父分类ID获取子分类
     */
    @Transactional(readOnly = true)
    public List<Category> getChildrenByParentId(Long parentId) {
        return categoryRepository.findByParentId(parentId);
    }

    /**
     * 分页查询所有分类
     */
    @Transactional(readOnly = true)
    public Page<Category> getAllCategories(Pageable pageable) {
        return categoryRepository.findAllNotDeleted(pageable);
    }

    /**
     * 搜索分类
     */
    @Transactional(readOnly = true)
    public List<Category> searchCategories(String keyword) {
        return categoryRepository.findByNameContaining(keyword);
    }

    /**
     * 启用/禁用分类
     */
    public void toggleCategoryStatus(Long id, Boolean enabled) {
        Category category = getCategoryById(id);
        category.setEnabled(enabled);
        categoryRepository.save(category);
    }
}