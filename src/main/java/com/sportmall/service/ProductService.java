package com.sportmall.service;

import com.sportmall.entity.Product;
import com.sportmall.entity.Category;
import com.sportmall.entity.Brand;
import com.sportmall.enums.ProductStatus;
import com.sportmall.repository.ProductRepository;
import com.sportmall.repository.CategoryRepository;
import com.sportmall.repository.BrandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品服务类
 */
@Service
@Transactional
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    /**
     * 创建商品
     */
    public Product createProduct(String name, BigDecimal price, Integer stock, Long categoryId, Long brandId) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setStock(stock);
        product.setStatus(ProductStatus.DRAFT);

        // 设置分类
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .filter(c -> !c.isDeleted())
                    .orElseThrow(() -> new IllegalArgumentException("分类不存在"));
            product.setCategory(category);
        }

        // 设置品牌
        if (brandId != null) {
            Brand brand = brandRepository.findById(brandId)
                    .filter(b -> !b.isDeleted())
                    .orElseThrow(() -> new IllegalArgumentException("品牌不存在"));
            product.setBrand(brand);
        }

        return productRepository.save(product);
    }

    /**
     * 更新商品
     */
    public Product updateProduct(Long id, String name, String title, String description, 
                               BigDecimal price, Integer stock, Long categoryId, Long brandId,
                               String mainImage, String detailImages, String specifications) {
        Product product = getProductById(id);

        product.setName(name);
        product.setTitle(title);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setMainImage(mainImage);
        product.setDetailImages(detailImages);
        product.setSpecifications(specifications);

        // 设置分类
        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId)
                    .filter(c -> !c.isDeleted())
                    .orElseThrow(() -> new IllegalArgumentException("分类不存在"));
            product.setCategory(category);
        }

        // 设置品牌
        if (brandId != null) {
            Brand brand = brandRepository.findById(brandId)
                    .filter(b -> !b.isDeleted())
                    .orElseThrow(() -> new IllegalArgumentException("品牌不存在"));
            product.setBrand(brand);
        }

        return productRepository.save(product);
    }

    /**
     * 根据ID获取商品
     */
    @Transactional(readOnly = true)
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("商品不存在"));
    }

    /**
     * 删除商品（逻辑删除）
     */
    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        product.delete();
        productRepository.save(product);
    }

    /**
     * 上架商品
     */
    public void activateProduct(Long id) {
        Product product = getProductById(id);
        if (product.getStock() <= 0) {
            throw new IllegalArgumentException("库存不足，无法上架");
        }
        product.updateStatus(ProductStatus.ACTIVE);
        productRepository.save(product);
    }

    /**
     * 下架商品
     */
    public void deactivateProduct(Long id) {
        Product product = getProductById(id);
        product.updateStatus(ProductStatus.INACTIVE);
        productRepository.save(product);
    }

    /**
     * 批量上架商品
     */
    public void activateProducts(List<Long> productIds) {
        for (Long id : productIds) {
            try {
                activateProduct(id);
            } catch (Exception e) {
                // 记录错误但继续处理其他商品
                System.err.println("上架商品失败 ID: " + id + ", 错误: " + e.getMessage());
            }
        }
    }

    /**
     * 批量下架商品
     */
    public void deactivateProducts(List<Long> productIds) {
        for (Long id : productIds) {
            try {
                deactivateProduct(id);
            } catch (Exception e) {
                // 记录错误但继续处理其他商品
                System.err.println("下架商品失败 ID: " + id + ", 错误: " + e.getMessage());
            }
        }
    }

    /**
     * 获取所有上架商品
     */
    @Transactional(readOnly = true)
    public List<Product> getAllActiveProducts() {
        return productRepository.findAllActive();
    }

    /**
     * 根据分类获取商品（分页）
     */
    @Transactional(readOnly = true)
    public Page<Product> getProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryId(categoryId, pageable);
    }

    /**
     * 根据品牌获取商品（分页）
     */
    @Transactional(readOnly = true)
    public Page<Product> getProductsByBrand(Long brandId, Pageable pageable) {
        return productRepository.findByBrandId(brandId, pageable);
    }

    /**
     * 搜索商品
     */
    @Transactional(readOnly = true)
    public Page<Product> searchProducts(String keyword, Pageable pageable) {
        return productRepository.findByNameContaining(keyword, pageable);
    }

    /**
     * 多条件查询商品
     */
    @Transactional(readOnly = true)
    public Page<Product> findProductsByConditions(String keyword, Long categoryId, Long brandId,
                                                BigDecimal minPrice, BigDecimal maxPrice, 
                                                ProductStatus status, Pageable pageable) {
        return productRepository.findByConditions(keyword, categoryId, brandId, 
                                                 minPrice, maxPrice, status, pageable);
    }

    /**
     * 获取推荐商品
     */
    @Transactional(readOnly = true)
    public List<Product> getFeaturedProducts() {
        return productRepository.findFeaturedProducts();
    }

    /**
     * 获取热销商品
     */
    @Transactional(readOnly = true)
    public List<Product> getBestSellingProducts(Pageable pageable) {
        return productRepository.findBestSellingProducts(pageable);
    }

    /**
     * 获取新品
     */
    @Transactional(readOnly = true)
    public List<Product> getNewProducts(Pageable pageable) {
        return productRepository.findNewProducts(pageable);
    }

    /**
     * 根据状态获取商品（分页）
     */
    @Transactional(readOnly = true)
    public Page<Product> getProductsByStatus(ProductStatus status, Pageable pageable) {
        return productRepository.findByStatus(status, pageable);
    }

    /**
     * 减少商品库存
     */
    public void reduceStock(Long productId, Integer quantity) {
        Product product = getProductById(productId);
        product.reduceStock(quantity);
        productRepository.save(product);
    }

    /**
     * 增加商品库存
     */
    public void increaseStock(Long productId, Integer quantity) {
        Product product = getProductById(productId);
        product.increaseStock(quantity);
        productRepository.save(product);
    }
}