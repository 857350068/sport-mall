package com.sportmall.config;

import com.sportmall.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security配置类
 * 
 * @author SportMall
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    /**
     * 密码编码器
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Security过滤器链配置
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserService userService) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                // 静态资源和公共页面
                .requestMatchers("/css/**", "/js/**", "/images/**", "/static/**", "/favicon.ico").permitAll()
                .requestMatchers("/", "/index").permitAll()
                
                // 认证相关页面
                .requestMatchers("/login", "/register", "/forgot-password", "/reset-password").permitAll()
                
                // API接口
                .requestMatchers("/api/test/**").permitAll()
                .requestMatchers("/api/products/**").permitAll()  // 商品接口允许匿名访问
                
                // 管理员页面
                .requestMatchers("/admin/**").hasRole("ADMIN")
                
                // 用户页面
                .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")
                
                // 其他请求需要认证
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/login-success", true)
                .failureUrl("/login?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/logout-success")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .userDetailsService(userService)
            .csrf(csrf -> csrf.disable())  // 开发阶段禁用CSRF
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            )
            .headers(headers -> headers
                // 添加X-Content-Type-Options响应头
                .contentTypeOptions(config -> config.disable())
                // 添加其他安全头
                .frameOptions(frame -> frame.sameOrigin())
                // 禁用缓存控制
                .cacheControl(cache -> cache.disable())
            );
            
        return http.build();
    }
}