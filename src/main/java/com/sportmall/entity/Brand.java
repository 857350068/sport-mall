package com.sportmall.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

/**
 * 品牌实体类
 */
@Entity
@Table(name = "brands")
public class Brand extends BaseEntity {

    /**
     * 品牌名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 品牌描述
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 品牌logo URL
     */
    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    /**
     * 品牌官网
     */
    @Column(name = "website", length = 200)
    private String website;

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

    // 构造函数
    public Brand() {}

    public Brand(String name, String description) {
        this.name = name;
        this.description = description;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}