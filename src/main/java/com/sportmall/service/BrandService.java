package com.sportmall.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sportmall.entity.Brand;
import com.sportmall.repository.BrandRepository;

/**
 * 品牌服务类
 */
@Service
@Transactional
public class BrandService {

    @Autowired
    private BrandRepository brandRepository;

    /**
     * 创建品牌
     */
    public Brand createBrand(String name, String description, String logoUrl, String website) {
        // 检查品牌名称是否已存在
        if (brandRepository.existsByName(name)) {
            throw new IllegalArgumentException("品牌名称已存在");
        }

        Brand brand = new Brand();
        brand.setName(name);
        brand.setDescription(description);
        brand.setLogoUrl(logoUrl);
        brand.setWebsite(website);

        return brandRepository.save(brand);
    }

    /**
     * 更新品牌
     */
    public Brand updateBrand(Long id, String name, String description, String logoUrl, String website, Boolean enabled) {
        Brand brand = getBrandById(id);
        
        // 检查名称是否被其他品牌使用
        if (!brand.getName().equals(name) && brandRepository.existsByName(name)) {
            throw new IllegalArgumentException("品牌名称已存在");
        }

        brand.setName(name);
        brand.setDescription(description);
        brand.setLogoUrl(logoUrl);
        brand.setWebsite(website);
        if (enabled != null) {
            brand.setEnabled(enabled);
        }

        return brandRepository.save(brand);
    }

    /**
     * 根据ID获取品牌
     */
    @Transactional(readOnly = true)
    public Brand getBrandById(Long id) {
        return brandRepository.findById(id)
                .filter(b -> !b.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("品牌不存在"));
    }

    /**
     * 删除品牌（逻辑删除）
     */
    public void deleteBrand(Long id) {
        Brand brand = getBrandById(id);
        brand.delete();
        brandRepository.save(brand);
    }

    /**
     * 获取所有启用的品牌
     */
    @Transactional(readOnly = true)
    public List<Brand> getAllEnabledBrands() {
        return brandRepository.findAllEnabled();
    }

    /**
     * 分页查询所有品牌
     */
    @Transactional(readOnly = true)
    public Page<Brand> getAllBrands(Pageable pageable) {
        return brandRepository.findAllNotDeleted(pageable);
    }

    /**
     * 搜索品牌
     */
    @Transactional(readOnly = true)
    public List<Brand> searchBrands(String keyword) {
        return brandRepository.findByNameContaining(keyword);
    }

    /**
     * 启用/禁用品牌
     */
    public void toggleBrandStatus(Long id, Boolean enabled) {
        Brand brand = getBrandById(id);
        brand.setEnabled(enabled);
        brandRepository.save(brand);
    }
}