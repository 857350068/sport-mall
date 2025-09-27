package com.sportmall.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品分类实体类
 */
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    /**
     * 分类名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 分类描述
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 分类图标URL
     */
    @Column(name = "icon_url", length = 500)
    private String iconUrl;

    /**
     * 父分类ID
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 分类层级
     */
    @Column(name = "level", nullable = false)
    private Integer level = 1;

    /**
     * 排序权重
     */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    /**
     * 是否启用
     */
    @Column(name = "enabled", nullable = false)
    private Boolean enabled = true;

    /**
     * 商品列表
     */
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    // 构造函数
    public Category() {}

    public Category(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // 业务方法
    public boolean isRootCategory() {
        return parentId == null;
    }

    public boolean hasProducts() {
        return products != null && !products.isEmpty();
    }

    // Getter和Setter方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", level=" + level +
                ", enabled=" + enabled +
                '}';
    }
}