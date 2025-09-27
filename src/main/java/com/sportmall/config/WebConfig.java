package com.sportmall.config;

import java.io.IOException;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Web配置类
 * 
 * @author SportMall
 * @since 1.0.0
 */
@Configuration
public class WebConfig {
    
    /**
     * 安全响应头过滤器
     */
    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> securityHeadersFilter() {
        FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>();
        
        registrationBean.setFilter(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
                    throws ServletException, IOException {
                // 添加X-Content-Type-Options响应头
                response.setHeader("X-Content-Type-Options", "nosniff");
                
                // 移除不必要的安全响应头
                response.setHeader("X-XSS-Protection", "");
                
                // 设置内容类型字符集
                response.setCharacterEncoding("UTF-8");
                
                filterChain.doFilter(request, response);
            }
        });
        
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        
        return registrationBean;
    }
    
    /**
     * 静态资源缓存控制过滤器
     */
    @Bean
    public FilterRegistrationBean<OncePerRequestFilter> staticResourceCacheFilter() {
        FilterRegistrationBean<OncePerRequestFilter> registrationBean = new FilterRegistrationBean<>();
        
        registrationBean.setFilter(new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
                    throws ServletException, IOException {
                String requestURI = request.getRequestURI();
                
                // 为静态资源添加缓存控制头
                if (requestURI.startsWith("/css/") || 
                    requestURI.startsWith("/js/") || 
                    requestURI.startsWith("/images/") || 
                    requestURI.startsWith("/fonts/")) {
                    // 添加缓存控制头，使用immutable指令
                    response.setHeader("Cache-Control", "public, max-age=31536000, immutable");
                }
                
                filterChain.doFilter(request, response);
            }
        });
        
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(2);
        
        return registrationBean;
    }
}