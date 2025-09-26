package com.sportmall.controller;

import com.sportmall.entity.Promotion;
import com.sportmall.enums.PromotionType;
import com.sportmall.enums.PromotionStatus;
import com.sportmall.service.PromotionService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 促销活动控制器
 */
@Controller
@RequestMapping("/admin/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    /**
     * 促销活动列表页面
     */
    @GetMapping
    public String listPromotions(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               @RequestParam(required = false) PromotionType type,
                               @RequestParam(required = false) PromotionStatus status,
                               Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Promotion> promotionPage;
        
        if (type != null) {
            promotionPage = promotionService.getPromotionsByType(type, pageable);
            model.addAttribute("type", type);
        } else if (status != null) {
            promotionPage = promotionService.getPromotionsByStatus(status, pageable);
            model.addAttribute("status", status);
        } else {
            promotionPage = promotionService.getAllPromotions(pageable);
        }
        
        model.addAttribute("promotions", promotionPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", promotionPage.getTotalPages());
        model.addAttribute("totalElements", promotionPage.getTotalElements());
        
        // 传递类型和状态列表
        model.addAttribute("types", PromotionType.values());
        model.addAttribute("statuses", PromotionStatus.values());
        
        return "admin/promotion/list";
    }

    /**
     * 创建促销活动页面
     */
    @GetMapping("/create")
    public String createPromotionForm(Model model) {
        model.addAttribute("promotion", new Promotion());
        model.addAttribute("types", PromotionType.values());
        return "admin/promotion/form";
    }

    /**
     * 保存促销活动
     */
    @PostMapping("/save")
    public String savePromotion(@ModelAttribute Promotion promotion,
                              @RequestParam String startTimeStr,
                              @RequestParam String endTimeStr,
                              RedirectAttributes redirectAttributes) {
        try {
            // 解析日期时间
            LocalDateTime startTime = LocalDateTime.parse(startTimeStr, 
                                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime endTime = LocalDateTime.parse(endTimeStr, 
                                                      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            
            if (promotion.getId() == null) {
                // 创建新促销活动
                promotionService.createPromotion(promotion.getName(), promotion.getDescription(),
                                               promotion.getType(), promotion.getDiscountValue(),
                                               promotion.getMinAmount(), promotion.getMaxDiscount(),
                                               startTime, endTime);
            } else {
                // 更新促销活动
                promotionService.updatePromotion(promotion.getId(), promotion.getName(), 
                                               promotion.getDescription(), promotion.getType(),
                                               promotion.getDiscountValue(), promotion.getMinAmount(),
                                               promotion.getMaxDiscount(), startTime, endTime,
                                               promotion.getEnabled());
            }
            redirectAttributes.addFlashAttribute("success", "保存成功");
            return "redirect:/admin/promotions";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/promotions";
        }
    }

    /**
     * 编辑促销活动页面
     */
    @GetMapping("/edit/{id}")
    public String editPromotionForm(@PathVariable Long id, Model model) {
        Promotion promotion = promotionService.getPromotionById(id);
        model.addAttribute("promotion", promotion);
        model.addAttribute("types", PromotionType.values());
        return "admin/promotion/form";
    }

    /**
     * 删除促销活动
     */
    @PostMapping("/delete/{id}")
    public String deletePromotion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            promotionService.deletePromotion(id);
            redirectAttributes.addFlashAttribute("success", "删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/promotions";
    }

    /**
     * 启动促销活动
     */
    @PostMapping("/start/{id}")
    public String startPromotion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            promotionService.startPromotion(id);
            redirectAttributes.addFlashAttribute("success", "活动已启动");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/promotions";
    }

    /**
     * 结束促销活动
     */
    @PostMapping("/end/{id}")
    public String endPromotion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            promotionService.endPromotion(id);
            redirectAttributes.addFlashAttribute("success", "活动已结束");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/promotions";
    }

    /**
     * 取消促销活动
     */
    @PostMapping("/cancel/{id}")
    public String cancelPromotion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            promotionService.cancelPromotion(id);
            redirectAttributes.addFlashAttribute("success", "活动已取消");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/promotions";
    }

    /**
     * 促销活动详情页面
     */
    @GetMapping("/{id}")
    public String promotionDetail(@PathVariable Long id, Model model) {
        Promotion promotion = promotionService.getPromotionById(id);
        model.addAttribute("promotion", promotion);
        return "admin/promotion/detail";
    }
}