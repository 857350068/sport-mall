package com.sportmall.controller;

import com.sportmall.entity.Brand;
import com.sportmall.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 品牌控制器
 */
@Controller
@RequestMapping("/admin/brands")
public class BrandController {

    @Autowired
    private BrandService brandService;

    /**
     * 品牌列表页面
     */
    @GetMapping
    public String listBrands(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(required = false) String keyword,
                           Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Brand> brandPage;
        
        if (keyword != null && !keyword.isEmpty()) {
            // 搜索品牌
            model.addAttribute("keyword", keyword);
            brandPage = brandService.getAllBrands(pageable);
        } else {
            // 获取所有品牌
            brandPage = brandService.getAllBrands(pageable);
        }
        
        model.addAttribute("brands", brandPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", brandPage.getTotalPages());
        model.addAttribute("totalElements", brandPage.getTotalElements());
        
        return "admin/brand/list";
    }

    /**
     * 创建品牌页面
     */
    @GetMapping("/create")
    public String createBrandForm(Model model) {
        model.addAttribute("brand", new Brand());
        return "admin/brand/form";
    }

    /**
     * 保存品牌
     */
    @PostMapping("/save")
    public String saveBrand(@ModelAttribute Brand brand, Model model) {
        try {
            if (brand.getId() == null) {
                // 创建新品牌
                brandService.createBrand(brand.getName(), brand.getDescription(), 
                                       brand.getLogoUrl(), brand.getWebsite());
            } else {
                // 更新品牌
                brandService.updateBrand(brand.getId(), brand.getName(), brand.getDescription(),
                                       brand.getLogoUrl(), brand.getWebsite(), brand.getEnabled());
            }
            return "redirect:/admin/brands?success=保存成功";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("brand", brand);
            return "admin/brand/form";
        }
    }

    /**
     * 编辑品牌页面
     */
    @GetMapping("/edit/{id}")
    public String editBrandForm(@PathVariable Long id, Model model) {
        Brand brand = brandService.getBrandById(id);
        model.addAttribute("brand", brand);
        return "admin/brand/form";
    }

    /**
     * 删除品牌
     */
    @PostMapping("/delete/{id}")
    public String deleteBrand(@PathVariable Long id) {
        try {
            brandService.deleteBrand(id);
            return "redirect:/admin/brands?success=删除成功";
        } catch (Exception e) {
            return "redirect:/admin/brands?error=" + e.getMessage();
        }
    }

    /**
     * 启用/禁用品牌
     */
    @PostMapping("/toggle/{id}")
    public String toggleBrandStatus(@PathVariable Long id,
                                  @RequestParam Boolean enabled,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        brandService.toggleBrandStatus(id, enabled);
        return "redirect:/admin/brands?page=" + page + "&size=" + size;
    }
}