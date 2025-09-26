package com.sportmall.config;

import com.sportmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时任务配置类
 */
@Configuration
@EnableScheduling
public class ScheduledTasksConfig {

    @Autowired
    private OrderService orderService;

    /**
     * 每分钟检查一次超时未支付的订单并自动取消
     * cron表达式: 秒 分 时 日 月 周
     * 这里设置为每分钟执行一次
     */
    @Scheduled(cron = "0 * * * * ?")
    public void processExpiredOrders() {
        orderService.processExpiredOrders();
    }
}