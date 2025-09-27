package com.sportmall.entity;

import com.sportmall.enums.ProductStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品实体类
 */
@Entity
@Table(name = "products")
public class Product extends BaseEntity {

    /**
     * 商品名称
     */
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    /**
     * 商品标题
     */
    @Column(name = "title", length = 300)
    private String title;

    /**
     * 商品描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 商品主图
     */
    @Column(name = "main_image", length = 500)
    private String mainImage;

    /**
     * 商品详情图片（JSON格式存储）
     */
    @Column(name = "detail_images", columnDefinition = "TEXT")
    private String detailImages;

    /**
     * 商品价格
     */
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    /**
     * 原价
     */
    @Column(name = "original_price", precision = 10, scale = 2)
    private BigDecimal originalPrice;

    /**
     * 库存数量
     */
    @Column(name = "stock", nullable = false)
    private Integer stock = 0;

    /**
     * 销量
     */
    @Column(name = "sales", nullable = false)
    private Integer sales = 0;

    /**
     * 商品状态
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProductStatus status = ProductStatus.DRAFT;

    /**
     * 商品规格（JSON格式存储）
     */
    @Column(name = "specifications", columnDefinition = "TEXT")
    private String specifications;

    /**
     * 商品重量（克）
     */
    @Column(name = "weight")
    private Integer weight;

    /**
     * 排序权重
     */
    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

    /**
     * 是否推荐
     */
    @Column(name = "is_featured", nullable = false)
    private Boolean isFeatured = false;

    /**
     * 商品分类
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    /**
     * 商品品牌
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id")
    private Brand brand;

    // 构造函数
    public Product() {}

    public Product(String name, BigDecimal price, Integer stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // 业务方法
    public boolean isAvailable() {
        return status == ProductStatus.ACTIVE && stock > 0 && !isDeleted();
    }

    public boolean canBePurchased(Integer quantity) {
        return isAvailable() && stock >= quantity;
    }

    public void reduceStock(Integer quantity) {
        if (stock >= quantity) {
            this.stock -= quantity;
            this.sales += quantity;
        } else {
            throw new IllegalStateException("库存不足");
        }
    }

    public void increaseStock(Integer quantity) {
        this.stock += quantity;
    }

    public void updateStatus(ProductStatus newStatus) {
        this.status = newStatus;
    }

    public boolean isOnSale() {
        return status == ProductStatus.ACTIVE;
    }

    public boolean hasDiscount() {
        return originalPrice != null && originalPrice.compareTo(price) > 0;
    }

    public BigDecimal getDiscountAmount() {
        if (hasDiscount()) {
            return originalPrice.subtract(price);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getDiscountRate() {
        if (hasDiscount()) {
            return getDiscountAmount().divide(originalPrice, 4, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    // Getter和Setter方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMainImage() {
        return mainImage;
    }

    public void setMainImage(String mainImage) {
        this.mainImage = mainImage;
    }

    public String getDetailImages() {
        return detailImages;
    }

    public void setDetailImages(String detailImages) {
        this.detailImages = detailImages;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(BigDecimal originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public String getSpecifications() {
        return specifications;
    }

    public void setSpecifications(String specifications) {
        this.specifications = specifications;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", status=" + status +
                '}';
    }
}