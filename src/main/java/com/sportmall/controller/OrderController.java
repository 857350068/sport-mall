package com.sportmall.controller;

import com.sportmall.entity.Order;
import com.sportmall.entity.User;
import com.sportmall.enums.OrderStatus;
import com.sportmall.service.OrderService;
import com.sportmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 订单控制器
 */
@Controller
@RequestMapping("/admin/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    /**
     * 订单列表页面
     */
    @GetMapping
    public String listOrders(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(required = false) String orderNo,
                           @RequestParam(required = false) OrderStatus status,
                           @RequestParam(required = false) Long userId,
                           @RequestParam(required = false) String startDate,
                           @RequestParam(required = false) String endDate,
                           Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Order> orderPage;
        
        // 解析日期
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;
        
        if (startDate != null && !startDate.isEmpty()) {
            startDateTime = LocalDateTime.parse(startDate + " 00:00:00", 
                                              DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        
        if (endDate != null && !endDate.isEmpty()) {
            endDateTime = LocalDateTime.parse(endDate + " 23:59:59", 
                                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        }
        
        // 多条件查询
        orderPage = orderService.findOrdersByConditions(orderNo, status, userId, 
                                                      startDateTime, endDateTime, pageable);
        
        model.addAttribute("orders", orderPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", orderPage.getTotalPages());
        model.addAttribute("totalElements", orderPage.getTotalElements());
        
        // 传递查询条件
        model.addAttribute("orderNo", orderNo);
        model.addAttribute("status", status);
        model.addAttribute("userId", userId);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        
        // 传递状态列表
        model.addAttribute("statuses", OrderStatus.values());
        
        return "admin/order/list";
    }

    /**
     * 订单详情页面
     */
    @GetMapping("/{id}")
    public String orderDetail(@PathVariable Long id, Model model) {
        Order order = orderService.getOrderById(id);
        model.addAttribute("order", order);
        return "admin/order/detail";
    }

    /**
     * 订单发货
     */
    @PostMapping("/ship/{id}")
    public String shipOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.shipOrder(id);
            redirectAttributes.addFlashAttribute("success", "发货成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    /**
     * 订单完成
     */
    @PostMapping("/complete/{id}")
    public String completeOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.completeOrder(id);
            redirectAttributes.addFlashAttribute("success", "订单完成");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    /**
     * 取消订单
     */
    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable Long id, 
                            @RequestParam String reason,
                            RedirectAttributes redirectAttributes) {
        try {
            orderService.cancelOrder(id, reason);
            redirectAttributes.addFlashAttribute("success", "订单已取消");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    /**
     * 退款处理
     */
    @PostMapping("/refund/{id}")
    public String refundOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.refundOrder(id);
            redirectAttributes.addFlashAttribute("success", "退款申请已处理");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/orders/" + id;
    }

    /**
     * 删除订单
     */
    @PostMapping("/delete/{id}")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            orderService.deleteOrder(id);
            redirectAttributes.addFlashAttribute("success", "删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/orders";
    }

    /**
     * 批量操作订单
     */
    @PostMapping("/batch/{action}")
    public String batchProcessOrders(@RequestParam("ids") Long[] ids,
                                   @PathVariable String action,
                                   @RequestParam(required = false) String reason,
                                   RedirectAttributes redirectAttributes) {
        try {
            for (Long id : ids) {
                try {
                    switch (action) {
                        case "ship":
                            orderService.shipOrder(id);
                            break;
                        case "complete":
                            orderService.completeOrder(id);
                            break;
                        case "cancel":
                            orderService.cancelOrder(id, reason != null ? reason : "批量取消");
                            break;
                    }
                } catch (Exception e) {
                    // 记录错误但继续处理其他订单
                    System.err.println("处理订单失败 ID: " + id + ", 错误: " + e.getMessage());
                }
            }
            redirectAttributes.addFlashAttribute("success", "批量操作完成");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/orders";
    }
}