package com.sportmall.controller;

import com.sportmall.entity.CartItem;
import com.sportmall.entity.User;
import com.sportmall.service.CartService;
import com.sportmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;

/**
 * 购物车控制器
 */
@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    /**
     * 显示购物车页面
     */
    @GetMapping
    public String cartPage(Model model, HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            if (userId != null) {
                List<CartItem> cartItems = cartService.getUserCartItems(userId);
                model.addAttribute("cartItems", cartItems);
                model.addAttribute("cartTotal", cartService.calculateCartTotal(userId));
                model.addAttribute("itemCount", cartItems.size());
            }
        } catch (Exception e) {
            model.addAttribute("error", "加载购物车失败: " + e.getMessage());
        }
        return "cart";
    }

    /**
     * 添加商品到购物车
     */
    @PostMapping("/add")
    @ResponseBody
    public String addToCart(@RequestParam Long productId,
                           @RequestParam(defaultValue = "1") Integer quantity,
                           HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            if (userId == null) {
                return "请先登录";
            }

            cartService.addToCart(userId, productId, quantity);
            return "商品已添加到购物车";
        } catch (IllegalArgumentException e) {
            return "添加失败: " + e.getMessage();
        } catch (Exception e) {
            return "添加失败: 系统错误";
        }
    }

    /**
     * 更新购物车商品数量
     */
    @PostMapping("/update")
    @ResponseBody
    public String updateQuantity(@RequestParam Long cartItemId,
                                @RequestParam Integer quantity,
                                HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            if (userId == null) {
                return "请先登录";
            }

            cartService.updateCartItemQuantity(cartItemId, quantity);
            return "数量已更新";
        } catch (IllegalArgumentException e) {
            return "更新失败: " + e.getMessage();
        } catch (Exception e) {
            return "更新失败: 系统错误";
        }
    }

    /**
     * 从购物车中移除商品
     */
    @PostMapping("/remove")
    @ResponseBody
    public String removeFromCart(@RequestParam Long cartItemId,
                                HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            if (userId == null) {
                return "请先登录";
            }

            cartService.removeFromCart(cartItemId);
            return "商品已从购物车中移除";
        } catch (IllegalArgumentException e) {
            return "移除失败: " + e.getMessage();
        } catch (Exception e) {
            return "移除失败: 系统错误";
        }
    }

    /**
     * 选择/取消选择购物车商品
     */
    @PostMapping("/select")
    @ResponseBody
    public String selectCartItem(@RequestParam Long cartItemId,
                                @RequestParam Boolean selected,
                                HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            if (userId == null) {
                return "请先登录";
            }

            cartService.selectCartItem(cartItemId, selected);
            return selected ? "商品已选中" : "商品已取消选中";
        } catch (IllegalArgumentException e) {
            return "操作失败: " + e.getMessage();
        } catch (Exception e) {
            return "操作失败: 系统错误";
        }
    }

    /**
     * 全选/取消全选购物车商品
     */
    @PostMapping("/select-all")
    @ResponseBody
    public String selectAllCartItems(@RequestParam Boolean selected,
                                    HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            if (userId == null) {
                return "请先登录";
            }

            cartService.selectAllCartItems(userId, selected);
            return selected ? "已全选" : "已取消全选";
        } catch (IllegalArgumentException e) {
            return "操作失败: " + e.getMessage();
        } catch (Exception e) {
            return "操作失败: 系统错误";
        }
    }

    /**
     * 清空购物车
     */
    @PostMapping("/clear")
    @ResponseBody
    public String clearCart(HttpSession session) {
        try {
            Long userId = getCurrentUserId(session);
            if (userId == null) {
                return "请先登录";
            }

            cartService.clearCart(userId);
            return "购物车已清空";
        } catch (IllegalArgumentException e) {
            return "清空失败: " + e.getMessage();
        } catch (Exception e) {
            return "清空失败: 系统错误";
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