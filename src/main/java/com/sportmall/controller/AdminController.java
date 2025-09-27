package com.sportmall.controller;

import com.sportmall.entity.User;
import com.sportmall.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 管理后台控制器
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private PromotionService promotionService;

    /**
     * 管理后台仪表板页面
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);
        
        model.addAttribute("currentUser", currentUser);
        
        // TODO: 添加实际的统计数据
        // 这里可以添加实际的业务统计数据，如订单数量、用户数量、商品数量等
        
        return "admin/dashboard";
    }

    /**
     * 商品分类管理页面 - 修改路径以避免冲突
     */
    @GetMapping("/categories/list")
    public String categories(Model model) {
        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);
        
        // 使用正确的分页方法
        Pageable pageable = PageRequest.of(0, 10);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("categories", categoryService.getAllCategories(pageable));
        
        return "admin/category/list";
    }

    /**
     * 品牌管理页面 - 修改路径以避免冲突
     */
    @GetMapping("/brands/list")
    public String brands(Model model) {
        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);
        
        // 使用正确的分页方法
        Pageable pageable = PageRequest.of(0, 10);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("brands", brandService.getAllBrands(pageable));
        
        return "admin/brand/list";
    }

    /**
     * 商品管理页面 - 修改路径以避免冲突
     */
    @GetMapping("/products/list")
    public String products(Model model) {
        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);
        
        // 使用正确的分页方法
        Pageable pageable = PageRequest.of(0, 10);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("products", productService.getAllProducts(pageable));
        model.addAttribute("categories", categoryService.getAllCategories(pageable));
        model.addAttribute("brands", brandService.getAllBrands(pageable));
        
        return "admin/product/list";
    }

    /**
     * 订单管理页面 - 修改路径以避免冲突
     */
    @GetMapping("/orders/list")
    public String orders(Model model) {
        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);
        
        // 使用正确的分页方法
        Pageable pageable = PageRequest.of(0, 10);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("orders", orderService.getAllOrders(pageable));
        
        return "admin/order/list";
    }

    /**
     * 用户管理页面 - 修改路径以避免冲突
     */
    @GetMapping("/users/list")
    public String users(Model model) {
        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);
        
        // 使用正确的分页方法
        Pageable pageable = PageRequest.of(0, 5);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("users", userService.getAllUsers(pageable));
        
        return "admin/user/list";
    }

    /**
     * 优惠券管理页面 - 修改路径以避免冲突
     */
    @GetMapping("/coupons/list")
    public String coupons(Model model) {
        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);
        
        // 使用正确的分页方法
        Pageable pageable = PageRequest.of(0, 10);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("coupons", couponService.getAllCoupons(pageable));
        
        return "admin/coupon/list";
    }

    /**
     * 促销活动管理页面 - 修改路径以避免冲突
     */
    @GetMapping("/promotions/list")
    public String promotions(Model model) {
        // 获取当前登录用户信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User currentUser = userService.findByUsername(username);
        
        // 使用正确的分页方法
        Pageable pageable = PageRequest.of(0, 10);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("promotions", promotionService.getAllPromotions(pageable));
        
        return "admin/promotion/list";
    }
}