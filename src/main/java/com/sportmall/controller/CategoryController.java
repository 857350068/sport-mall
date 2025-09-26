package com.sportmall.controller;

import com.sportmall.entity.Category;
import com.sportmall.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 商品分类控制器
 */
@Controller
@RequestMapping("/admin/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 分类列表页面
     */
    @GetMapping
    public String listCategories(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryService.getAllCategories(pageable);
        
        model.addAttribute("categories", categoryPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", categoryPage.getTotalPages());
        model.addAttribute("totalElements", categoryPage.getTotalElements());
        
        return "admin/category/list";
    }

    /**
     * 创建分类页面
     */
    @GetMapping("/create")
    public String createCategoryForm(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("parentCategories", categoryService.getRootCategories());
        return "admin/category/form";
    }

    /**
     * 保存分类
     */
    @PostMapping("/save")
    public String saveCategory(@ModelAttribute Category category,
                             @RequestParam(required = false) Long parentId,
                             Model model) {
        try {
            if (category.getId() == null) {
                // 创建新分类
                categoryService.createCategory(category.getName(), category.getDescription(), parentId);
            } else {
                // 更新分类
                categoryService.updateCategory(category.getId(), category.getName(), 
                                             category.getDescription(), category.getEnabled());
            }
            return "redirect:/admin/categories?success=保存成功";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("category", category);
            model.addAttribute("parentCategories", categoryService.getRootCategories());
            return "admin/category/form";
        }
    }

    /**
     * 编辑分类页面
     */
    @GetMapping("/edit/{id}")
    public String editCategoryForm(@PathVariable Long id, Model model) {
        Category category = categoryService.getCategoryById(id);
        model.addAttribute("category", category);
        model.addAttribute("parentCategories", categoryService.getRootCategories());
        return "admin/category/form";
    }

    /**
     * 删除分类
     */
    @PostMapping("/delete/{id}")
    public String deleteCategory(@PathVariable Long id,
                               @RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size) {
        try {
            categoryService.deleteCategory(id);
            return "redirect:/admin/categories?page=" + page + "&size=" + size + "&success=删除成功";
        } catch (Exception e) {
            return "redirect:/admin/categories?page=" + page + "&size=" + size + "&error=" + e.getMessage();
        }
    }

    /**
     * 启用/禁用分类
     */
    @PostMapping("/toggle/{id}")
    public String toggleCategoryStatus(@PathVariable Long id,
                                     @RequestParam Boolean enabled,
                                     @RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        categoryService.toggleCategoryStatus(id, enabled);
        return "redirect:/admin/categories?page=" + page + "&size=" + size;
    }

    /**
     * 获取子分类（AJAX）
     */
    @GetMapping("/children/{parentId}")
    @ResponseBody
    public List<Category> getChildren(@PathVariable Long parentId) {
        return categoryService.getChildrenByParentId(parentId);
    }
}