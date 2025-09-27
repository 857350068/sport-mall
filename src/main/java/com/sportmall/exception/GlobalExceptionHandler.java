package com.sportmall.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 全局异常处理器
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ModelAndView handleBusinessException(BusinessException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", e.getMessage());
        return new ModelAndView("redirect:/error");
    }

    /**
     * 处理参数非法异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ModelAndView handleIllegalArgumentException(IllegalArgumentException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "参数错误: " + e.getMessage());
        return new ModelAndView("redirect:/error");
    }

    /**
     * 处理所有其他异常
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        e.printStackTrace(); // 记录错误日志
        
        // 根据请求类型返回不同的响应
        String requestType = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equals(requestType)) {
            // AJAX请求返回JSON
            redirectAttributes.addFlashAttribute("error", "系统错误，请稍后重试");
            return new ModelAndView("redirect:/error");
        } else {
            // 普通请求返回错误页面
            ModelAndView mav = new ModelAndView("error/500");
            mav.addObject("error", "系统错误，请稍后重试");
            mav.addObject("message", e.getMessage());
            return mav;
        }
    }
}