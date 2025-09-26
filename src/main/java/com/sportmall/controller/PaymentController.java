package com.sportmall.controller;

import com.sportmall.entity.User;
import com.sportmall.service.OrderService;
import com.sportmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

/**
 * 支付控制器
 */
@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /**
     * 显示支付页面
     */
    @GetMapping
    public String paymentPage(Model model, HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            if (userId == null) {
                return "redirect:/login";
            }

            // 获取用户信息
            User user = userService.getUserById(userId);
            model.addAttribute("user", user);
            
            // 这里应该获取待支付的订单信息
            // 简化处理，实际项目中应该从session或数据库获取
            
            return "payment/index";
        } catch (Exception e) {
            model.addAttribute("error", "加载支付页面失败: " + e.getMessage());
            return "payment/index";
        }
    }

    /**
     * 模拟支付处理
     */
    @PostMapping("/process")
    @ResponseBody
    public String processPayment(@RequestParam String orderId,
                                @RequestParam String paymentMethod,
                                HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            if (userId == null) {
                return "请先登录";
            }

            // 模拟支付处理
            orderService.processPayment(orderId, paymentMethod);
            return "支付成功";
        } catch (IllegalArgumentException e) {
            return "支付失败: " + e.getMessage();
        } catch (Exception e) {
            return "支付失败: 系统错误";
        }
    }

    /**
     * 支付成功页面
     */
    @GetMapping("/success")
    public String paymentSuccess(Model model, @RequestParam String orderId) {
        model.addAttribute("orderId", orderId);
        return "payment/success";
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId(HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        return userId;
    }
}