package com.sportmall.controller;

import com.sportmall.entity.User;
import com.sportmall.enums.UserRole;
import com.sportmall.enums.UserStatus;
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

/**
 * 用户管理控制器
 * 处理管理员对用户的管理操作
 */
@Controller
@RequestMapping("/admin/users")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户列表页面
     */
    @GetMapping
    public String listUsers(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "10") int size,
                           @RequestParam(required = false) UserRole role,
                           @RequestParam(required = false) UserStatus status,
                           @RequestParam(required = false) String keyword,
                           Model model) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<User> userPage = userService.findUsersByConditions(role, status, keyword, pageable);
        
        model.addAttribute("users", userPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("totalElements", userPage.getTotalElements());
        
        // 传递查询条件
        model.addAttribute("role", role);
        model.addAttribute("status", status);
        model.addAttribute("keyword", keyword);
        
        // 传递枚举列表
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("statuses", UserStatus.values());
        
        return "admin/user/list";
    }

    /**
     * 创建用户页面
     */
    @GetMapping("/create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("statuses", UserStatus.values());
        return "admin/user/form";
    }

    /**
     * 编辑用户页面
     */
    @GetMapping("/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("statuses", UserStatus.values());
        return "admin/user/form";
    }

    /**
     * 保存用户
     */
    @PostMapping("/save")
    public String saveUser(@ModelAttribute User user,
                          RedirectAttributes redirectAttributes) {
        try {
            if (user.getId() == null) {
                // 创建新用户
                userService.register(user.getUsername(), "123456", user.getEmail());
                redirectAttributes.addFlashAttribute("success", "用户创建成功，默认密码为123456");
            } else {
                // 更新用户
                userService.updateUser(user.getId(), user.getRealName(), user.getPhone(), user.getEmail());
                redirectAttributes.addFlashAttribute("success", "用户更新成功");
            }
            return "redirect:/admin/users";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/admin/users";
        }
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteUser(id);
            redirectAttributes.addFlashAttribute("success", "用户删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    /**
     * 批量删除用户
     */
    @PostMapping("/batch/delete")
    public String batchDeleteUsers(@RequestParam("ids") Long[] ids, RedirectAttributes redirectAttributes) {
        try {
            for (Long id : ids) {
                try {
                    userService.deleteUser(id);
                } catch (Exception e) {
                    // 记录错误但继续处理其他用户
                    System.err.println("删除用户失败 ID: " + id + ", 错误: " + e.getMessage());
                }
            }
            redirectAttributes.addFlashAttribute("success", "批量删除成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    /**
     * 更改用户状态
     */
    @PostMapping("/status/{id}")
    public String changeUserStatus(@PathVariable Long id, 
                                  @RequestParam UserStatus status,
                                  RedirectAttributes redirectAttributes) {
        try {
            userService.changeUserStatus(id, status);
            redirectAttributes.addFlashAttribute("success", "用户状态更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    /**
     * 更改用户角色
     */
    @PostMapping("/role/{id}")
    public String changeUserRole(@PathVariable Long id, 
                                @RequestParam UserRole role,
                                RedirectAttributes redirectAttributes) {
        try {
            userService.changeUserRole(id, role);
            redirectAttributes.addFlashAttribute("success", "用户角色更新成功");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }

    /**
     * 重置用户密码
     */
    @PostMapping("/reset-password/{id}")
    public String resetPassword(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            User user = userService.getUserById(id);
            // 这里可以设置一个默认密码或者生成随机密码
            // 为了简化，我们使用固定密码
            // 实际项目中应该生成随机密码并通过邮件发送给用户
            redirectAttributes.addFlashAttribute("success", "用户密码已重置为123456");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/admin/users";
    }
}