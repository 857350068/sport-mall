package com.sportmall.config;

import com.sportmall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 订单过期处理定时任务
 */
@Component
public class OrderExpirationScheduler {

    @Autowired
    private OrderService orderService;

    /**
     * 每分钟检查一次超时未支付的订单并自动取消
     * cron表达式: 秒 分 时 日 月 周
     */
    @Scheduled(cron = "0 * * * * ?") // 每分钟执行一次
    public void processExpiredOrders() {
        try {
            orderService.processExpiredOrders();
        } catch (Exception e) {
            System.err.println("处理超时订单失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
}