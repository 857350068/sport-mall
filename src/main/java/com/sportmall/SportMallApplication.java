package com.sportmall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * 运动装备商城 主启动类
 * 
 * @author SportMall
 * @since 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
public class SportMallApplication {

    public static void main(String[] args) {
        SpringApplication.run(SportMallApplication.class, args);
        System.out.println("\n" +
                "===================================\n" +
                "🏃‍♂️ 运动装备商城启动成功！\n" +
                "📱 访问地址: http://localhost:8080\n" +
                "🔧 健康检查: http://localhost:8080/api/test/health\n" +
                "===================================\n");
    }
}