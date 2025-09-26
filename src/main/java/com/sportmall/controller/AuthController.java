package com.sportmall.controller;

import com.sportmall.entity.User;
import com.sportmall.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

/**
 * 认证控制器
 * 处理登录、注册、找回密码等认证相关功能
 */
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * 显示登录页面
     */
    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        if (error != null) {
            model.addAttribute("error", "用户名或密码错误");
        }
        if (logout != null) {
            model.addAttribute("message", "您已成功退出登录");
        }
        return "auth/login";
    }

    /**
     * 显示注册页面
     */
    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    /**
     * 处理用户注册
     */
    @PostMapping("/register")
    public String register(@RequestParam String username,
                          @RequestParam String password,
                          @RequestParam String confirmPassword,
                          @RequestParam String email,
                          RedirectAttributes redirectAttributes) {
        try {
            // 验证密码确认
            if (!password.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "两次输入的密码不一致");
                return "redirect:/register";
            }

            // 注册用户
            userService.register(username, password, email);
            redirectAttributes.addFlashAttribute("success", "注册成功，请登录");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    /**
     * 显示找回密码页面
     */
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "auth/forgot-password";
    }

    /**
     * 处理找回密码请求
     */
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email,
                                RedirectAttributes redirectAttributes) {
        try {
            userService.generateResetToken(email);
            redirectAttributes.addFlashAttribute("success", 
                "密码重置链接已发送到您的邮箱，请查收");
            return "redirect:/forgot-password";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/forgot-password";
        }
    }

    /**
     * 显示重置密码页面
     */
    @GetMapping("/reset-password")
    public String resetPasswordPage(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "auth/reset-password";
    }

    /**
     * 处理密码重置
     */
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               RedirectAttributes redirectAttributes) {
        try {
            // 验证密码确认
            if (!password.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "两次输入的密码不一致");
                return "redirect:/reset-password?token=" + token;
            }

            userService.resetPassword(token, password);
            redirectAttributes.addFlashAttribute("success", "密码重置成功，请使用新密码登录");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/reset-password?token=" + token;
        }
    }

    /**
     * 显示修改密码页面
     */
    @GetMapping("/change-password")
    public String changePasswordPage() {
        return "auth/change-password";
    }

    /**
     * 处理修改密码
     */
    @PostMapping("/change-password")
    public String changePassword(@RequestParam String oldPassword,
                                @RequestParam String newPassword,
                                @RequestParam String confirmPassword,
                                HttpServletRequest request,
                                RedirectAttributes redirectAttributes) {
        try {
            // 验证密码确认
            if (!newPassword.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("error", "两次输入的新密码不一致");
                return "redirect:/change-password";
            }

            // 获取当前用户ID（这里简化处理，实际应从Security上下文获取）
            HttpSession session = request.getSession();
            Long userId = (Long) session.getAttribute("userId");
            if (userId == null) {
                redirectAttributes.addFlashAttribute("error", "请先登录");
                return "redirect:/login";
            }

            userService.changePassword(userId, oldPassword, newPassword);
            redirectAttributes.addFlashAttribute("success", "密码修改成功");
            return "redirect:/user/profile";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/change-password";
        }
    }

    /**
     * 处理登录成功后的逻辑
     */
    @GetMapping("/login-success")
    public String loginSuccess(HttpServletRequest request) {
        try {
            // 获取当前认证的用户
            String username = request.getUserPrincipal().getName();
            
            // 更新最后登录时间
            userService.updateLastLoginTime(username);

            // 将用户信息存储到session中
            User user = userService.getUserByUsername(username).orElse(null);
            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("userId", user.getId());
                session.setAttribute("username", user.getUsername());
                session.setAttribute("role", user.getRole().name());
                
                // 根据用户角色重定向
                if (user.isAdmin()) {
                    return "redirect:/admin/dashboard";
                } else {
                    return "redirect:/";
                }
            } else {
                // 如果用户不存在，重定向到登录页
                return "redirect:/login?error=用户不存在";
            }
        } catch (Exception e) {
            // 发生异常时重定向到登录页
            return "redirect:/login?error=登录处理失败";
        }
    }

    /**
     * 注销处理
     */
    @GetMapping("/logout-success")
    public String logoutSuccess(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login?logout";
    }
}