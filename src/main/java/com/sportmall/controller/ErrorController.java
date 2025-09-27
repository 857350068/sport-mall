package com.sportmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 错误控制器
 */
@Controller
@RequestMapping("/error")
public class ErrorController {

    /**
     * 显示错误页面
     */
    @GetMapping
    public String showErrorPage() {
        return "error/500";
    }
}