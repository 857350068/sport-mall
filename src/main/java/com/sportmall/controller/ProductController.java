package com.sportmall.controller;

import com.sportmall.entity.Product;
import com.sportmall.entity.Category;
import com.sportmall.entity.Brand;
import com.sportmall.enums.ProductStatus;
import com.sportmall.service.ProductService;
import com.sportmall.service.CategoryService;
import com.sportmall.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

/**
 * 商品控制器
 */
@Controller
@RequestMapping("/admin/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    /**
     * 商品列表页面
     */
    @GetMapping
    public String listProducts(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size,
                             @RequestParam(required = false) String keyword,
                             @RequestParam(required = false) Long categoryId,
                             @RequestParam(required = false) Long brandId,
                             @RequestParam(required = false) ProductStatus status,
                             Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Product> productPage;
        
        // 多条件查询
        productPage = productService.findProductsByConditions(keyword, categoryId, brandId, 
                                                           null, null, status, pageable);
        
        model.addAttribute("products", productPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalElements", productPage.getTotalElements());
        
        // 传递查询条件
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("brandId", brandId);
        model.addAttribute("status", status);
        
        // 传递分类和品牌列表
        model.addAttribute("categories", categoryService.getAllEnabledCategories());
        model.addAttribute("brands", brandService.getAllEnabledBrands());
        model.addAttribute("statuses", ProductStatus.values());
        
        return "admin/product/list";
    }

    /**
     * 创建商品页面
     */
    @GetMapping("/create")
    public String createProductForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", categoryService.getAllEnabledCategories());
        model.addAttribute("brands", brandService.getAllEnabledBrands());
        return "admin/product/form";
    }

    /**
     * 保存商品
     */
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product,
                            @RequestParam(required = false) Long categoryId,
                            @RequestParam(required = false) Long brandId,
                            RedirectAttributes redirectAttributes) {
        try {
            if (product.getId() == null) {
                // 创建新商品
                productService.createProduct(product.getName(), product.getPrice(), 
                                           product.getStock(), categoryId, brandId);
            } else {
                // 更新商品
                productService.updateProduct(product.getId(), product.getName(), 
                                           product.getTitle(), product.getDescription(),
                                           product.getPrice(), product.getStock(),
                                           categoryId, brandId,
                                           product.getMainImage(), product.getDetailImages(),
                                           product.getSpecifications());
            }
            redirectAttributes.addFlashAttribute("success", "保存成功");
            return "redirect:/admin/products";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/products";
        }
    }

    /**
     * 编辑商品页面
     */
    @GetMapping("/edit/{id}")
    public String editProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.getAllEnabledCategories());
        model.addAttribute("brands", brandService.getAllEnabledBrands());
        return "admin/product/form";
    }

    /**
     * 删除商品
     */
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("success", "删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/products";
    }

    /**
     * 上架商品
     */
    @PostMapping("/activate/{id}")
    public String activateProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.activateProduct(id);
            redirectAttributes.addFlashAttribute("success", "上架成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/products";
    }

    /**
     * 下架商品
     */
    @PostMapping("/deactivate/{id}")
    public String deactivateProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deactivateProduct(id);
            redirectAttributes.addFlashAttribute("success", "下架成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/products";
    }

    /**
     * 批量上架商品
     */
    @PostMapping("/batch/activate")
    public String batchActivateProducts(@RequestParam("ids") List<Long> ids, 
                                      RedirectAttributes redirectAttributes) {
        try {
            productService.activateProducts(ids);
            redirectAttributes.addFlashAttribute("success", "批量上架成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/products";
    }

    /**
     * 批量下架商品
     */
    @PostMapping("/batch/deactivate")
    public String batchDeactivateProducts(@RequestParam("ids") List<Long> ids, 
                                        RedirectAttributes redirectAttributes) {
        try {
            productService.deactivateProducts(ids);
            redirectAttributes.addFlashAttribute("success", "批量下架成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/products";
    }

    /**
     * 商品详情页面
     */
    @GetMapping("/{id}")
    public String productDetail(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "admin/product/detail";
    }
}