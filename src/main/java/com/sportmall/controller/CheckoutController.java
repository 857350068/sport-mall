package com.sportmall.controller;

import com.sportmall.entity.User;
import com.sportmall.service.CartService;
import com.sportmall.service.OrderService;
import com.sportmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

/**
 * 结账控制器
 */
@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    /**
     * 显示结账页面
     */
    @GetMapping
    public String checkoutPage(Model model, HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            if (userId == null) {
                return "redirect:/login";
            }

            // 获取用户购物车商品
            model.addAttribute("cartItems", cartService.getUserSelectedCartItems(userId));
            model.addAttribute("cartTotal", cartService.calculateCartTotal(userId));
            
            // 获取用户信息
            User user = userService.getUserById(userId);
            model.addAttribute("user", user);
            
            return "checkout/index";
        } catch (Exception e) {
            model.addAttribute("error", "加载结账页面失败: " + e.getMessage());
            return "checkout/index";
        }
    }

    /**
     * 提交订单
     */
    @PostMapping("/place-order")
    @ResponseBody
    public String placeOrder(@RequestParam(required = false) String remark,
                            HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            if (userId == null) {
                return "请先登录";
            }

            // 创建订单
            orderService.createOrderFromCart(userId, remark);
            return "订单提交成功";
        } catch (IllegalArgumentException e) {
            return "提交失败: " + e.getMessage();
        } catch (Exception e) {
            return "提交失败: 系统错误";
        }
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return userId;
    }
}