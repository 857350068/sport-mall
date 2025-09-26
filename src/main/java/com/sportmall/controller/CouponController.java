package com.sportmall.controller;

import com.sportmall.entity.Coupon;
import com.sportmall.enums.CouponType;
import com.sportmall.service.CouponService;
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
 * 优惠券控制器
 */
@Controller
@RequestMapping("/admin/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;

    /**
     * 优惠券列表页面
     */
    @GetMapping
    public String listCoupons(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "10") int size,
                            @RequestParam(required = false) CouponType type,
                            @RequestParam(required = false) Boolean enabled,
                            Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Coupon> couponPage;
        
        if (type != null) {
            couponPage = couponService.getCouponsByType(type, pageable);
            model.addAttribute("type", type);
        } else if (enabled != null) {
            couponPage = couponService.getCouponsByEnabled(enabled, pageable);
            model.addAttribute("enabled", enabled);
        } else {
            couponPage = couponService.getAllCoupons(pageable);
        }
        
        model.addAttribute("coupons", couponPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", couponPage.getTotalPages());
        model.addAttribute("totalElements", couponPage.getTotalElements());
        
        // 传递类型列表
        model.addAttribute("types", CouponType.values());
        
        return "admin/coupon/list";
    }

    /**
     * 创建优惠券页面
     */
    @GetMapping("/create")
    public String createCouponForm(Model model) {
        model.addAttribute("coupon", new Coupon());
        model.addAttribute("types", CouponType.values());
        return "admin/coupon/form";
    }

    /**
     * 保存优惠券
     */
    @PostMapping("/save")
    public String saveCoupon(@ModelAttribute Coupon coupon,
                           @RequestParam String validFromStr,
                           @RequestParam String validToStr,
                           RedirectAttributes redirectAttributes) {
        try {
            // 解析日期时间
            LocalDateTime validFrom = LocalDateTime.parse(validFromStr, 
                                                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            LocalDateTime validTo = LocalDateTime.parse(validToStr, 
                                                      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            
            if (coupon.getId() == null) {
                // 创建新优惠券
                couponService.createCoupon(coupon.getName(), coupon.getDescription(),
                                         coupon.getType(), coupon.getDiscountValue(),
                                         coupon.getMinAmount(), coupon.getMaxDiscount(),
                                         validFrom, validTo, coupon.getTotalCount(),
                                         coupon.getPerUserLimit());
            } else {
                // 更新优惠券
                couponService.updateCoupon(coupon.getId(), coupon.getName(), coupon.getDescription(),
                                         coupon.getType(), coupon.getDiscountValue(),
                                         coupon.getMinAmount(), coupon.getMaxDiscount(),
                                         validFrom, validTo, coupon.getTotalCount(),
                                         coupon.getPerUserLimit());
            }
            redirectAttributes.addFlashAttribute("success", "保存成功");
            return "redirect:/admin/coupons";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/coupons";
        }
    }

    /**
     * 编辑优惠券页面
     */
    @GetMapping("/edit/{id}")
    public String editCouponForm(@PathVariable Long id, Model model) {
        Coupon coupon = couponService.getCouponById(id);
        model.addAttribute("coupon", coupon);
        model.addAttribute("types", CouponType.values());
        return "admin/coupon/form";
    }

    /**
     * 删除优惠券
     */
    @PostMapping("/delete/{id}")
    public String deleteCoupon(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            couponService.deleteCoupon(id);
            redirectAttributes.addFlashAttribute("success", "删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/coupons";
    }

    /**
     * 启用/禁用优惠券
     */
    @PostMapping("/toggle/{id}")
    public String toggleCouponStatus(@PathVariable Long id,
                                   @RequestParam Boolean enabled,
                                   RedirectAttributes redirectAttributes) {
        try {
            couponService.toggleCouponStatus(id, enabled);
            redirectAttributes.addFlashAttribute("success", 
                                               enabled ? "优惠券已启用" : "优惠券已禁用");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/coupons";
    }

    /**
     * 优惠券详情页面
     */
    @GetMapping("/{id}")
    public String couponDetail(@PathVariable Long id, Model model) {
        Coupon coupon = couponService.getCouponById(id);
        model.addAttribute("coupon", coupon);
        return "admin/coupon/detail";
    }
}