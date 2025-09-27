package com.sportmall.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 主页控制器
 * 
 * @author SportMall
 * @since 1.0.0
 */
@Controller
public class HomeController {
    
    private static final Logger log = LoggerFactory.getLogger(HomeController.class);
    
    /**
     * 首页
     */
    @GetMapping("/")
    public String index(Model model) {
        log.info("访问运动装备商城首页");
        return "index";
    }
    
    /**
     * 商品中心页面
     */
    @GetMapping("/products")
    public String products() {
        return "products";
    }
    
    /**
     * 分类浏览页面
     */
    @GetMapping("/categories")
    public String categories() {
        return "categories";
    }
    
    // 已删除与CartController冲突的/cart路由
}