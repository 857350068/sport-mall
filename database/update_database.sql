-- 运动装备商城数据库更新脚本
-- 执行顺序：先执行表结构修改，再插入示例数据

USE `sport_mall`;

-- 1. 添加用户优惠券关联表
CREATE TABLE IF NOT EXISTS `user_coupon` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `create_time` datetime(6) NOT NULL,
  `deleted` int NOT NULL,
  `update_time` datetime(6) NOT NULL,
  `user_id` bigint NOT NULL,
  `coupon_id` bigint NOT NULL,
  `coupon_code` varchar(50) COLLATE utf8mb4_unicode_ci NOT NULL,
  `order_id` bigint DEFAULT NULL,
  `status` int NOT NULL DEFAULT 0 COMMENT '0-未使用 1-已使用 2-已过期',
  `receive_time` datetime(6) NOT NULL,
  `use_time` datetime(6) DEFAULT NULL,
  `expire_time` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_coupon_id` (`coupon_id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_user_coupon_user` FOREIGN KEY (`user_id`) REFERENCES `sys_user` (`id`),
  CONSTRAINT `fk_user_coupon_coupon` FOREIGN KEY (`coupon_id`) REFERENCES `coupon` (`id`),
  CONSTRAINT `fk_user_coupon_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户优惠券关联表';

-- 2. 修改 product 表，添加 image 字段
ALTER TABLE `product` ADD COLUMN IF NOT EXISTS `image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品主图片' AFTER `main_image`;

-- 3. 插入示例商品数据（如果不存在）
INSERT IGNORE INTO `product` (`create_time`, `deleted`, `update_time`, `brand`, `category_id`, `code`, `color`, `cost_price`, `description`, `detail_description`, `images`, `is_hot`, `is_new`, `is_recommended`, `main_image`, `image`, `material`, `meta_description`, `meta_keywords`, `meta_title`, `model`, `name`, `origin`, `original_price`, `price`, `sales_count`, `size`, `status`, `stock`, `view_count`, `weight`) VALUES
(NOW(), 0, NOW(), 'Nike', 1, 'NK001', '黑色', 300.00, '经典跑步鞋，舒适透气', '采用最新科技，提供出色的缓震和支撑效果，适合各种跑步训练。透气网面设计，让双脚保持干爽舒适。', '/images/products/nike_running_1.jpg,/images/products/nike_running_2.jpg', 1, 1, 1, '/images/products/nike_running_main.jpg', '/images/products/nike_running_main.jpg', '网面+橡胶', 'Nike经典跑步鞋', '跑步鞋,Nike,运动鞋', 'Nike经典跑步鞋', 'Air Max', 'Nike Air Max 跑步鞋', '美国', 599.00, 499.00, 156, '40,41,42,43,44', 1, 100, 2340, 0.8),
(NOW(), 0, NOW(), 'Adidas', 1, 'AD001', '白色', 280.00, '轻便运动鞋，适合日常穿着', '经典三条纹设计，采用BOOST科技中底，提供持久的能量反馈。适合跑步、健身和日常穿着。', '/images/products/adidas_sport_1.jpg,/images/products/adidas_sport_2.jpg', 0, 1, 1, '/images/products/adidas_sport_main.jpg', '/images/products/adidas_sport_main.jpg', '合成材料', 'Adidas运动鞋', '运动鞋,Adidas,休闲鞋', 'Adidas休闲运动鞋', 'Ultraboost', 'Adidas Ultraboost 运动鞋', '德国', 799.00, 699.00, 89, '39,40,41,42,43', 1, 75, 1567, 0.7),
(NOW(), 0, NOW(), 'Puma', 2, 'PM001', '蓝色', 80.00, '舒适运动T恤，吸湿排汗', '采用DryCell技术，快速吸湿排汗，保持运动时的舒适感。经典PUMA标志设计，时尚运动风格。', '/images/products/puma_tshirt_1.jpg,/images/products/puma_tshirt_2.jpg', 1, 0, 0, '/images/products/puma_tshirt_main.jpg', '/images/products/puma_tshirt_main.jpg', '聚酯纤维', 'Puma运动T恤', '运动服,T恤,Puma', 'Puma运动T恤', 'DryCell', 'Puma DryCell 运动T恤', '德国', 159.00, 129.00, 234, 'S,M,L,XL,XXL', 1, 200, 890, 0.2),
(NOW(), 0, NOW(), 'Nike', 3, 'NK002', '黑色', 1200.00, '家用多功能哑铃套装', '可调节重量哑铃，适合家庭健身使用。包含多种重量片，满足不同训练需求。', '/images/products/nike_dumbbell_1.jpg,/images/products/nike_dumbbell_2.jpg', 0, 1, 1, '/images/products/nike_dumbbell_main.jpg', '/images/products/nike_dumbbell_main.jpg', '铸铁+橡胶', 'Nike哑铃套装', '哑铃,健身器材,Nike', 'Nike哑铃套装', 'Pro Series', 'Nike Pro 可调节哑铃套装', '美国', 1999.00, 1599.00, 45, '20KG,30KG,40KG', 1, 30, 567, 15.0),
(NOW(), 0, NOW(), 'Under Armour', 4, 'UA001', '灰色', 150.00, '防水运动背包，大容量', '防水耐用材质，多个分层设计，适合户外运动和日常使用。人体工学背带设计，减轻负重感。', '/images/products/ua_backpack_1.jpg,/images/products/ua_backpack_2.jpg', 1, 1, 0, '/images/products/ua_backpack_main.jpg', '/images/products/ua_backpack_main.jpg', '防水尼龙', 'Under Armour运动背包', '背包,运动包,Under Armour', 'Under Armour运动背包', 'Storm Series', 'Under Armour Storm 防水背包', '美国', 399.00, 299.00, 78, '30L,40L', 1, 120, 432, 1.2);

-- 4. 插入示例优惠券数据
INSERT IGNORE INTO `coupon` (`create_time`, `deleted`, `update_time`, `amount`, `code`, `description`, `end_time`, `max_amount`, `min_amount`, `name`, `per_user_limit`, `start_time`, `status`, `total_count`, `type`, `used_count`) VALUES
(NOW(), 0, NOW(), 50.00, 'WELCOME50', '新用户专享50元优惠券，购买任意商品满200元可用', DATE_ADD(NOW(), INTERVAL 30 DAY), NULL, 200.00, '新用户50元优惠券', 1, NOW(), 1, 1000, 'FIXED', 0),
(NOW(), 0, NOW(), 10.00, 'DISCOUNT10', '全场通用10%折扣券，最高优惠100元', DATE_ADD(NOW(), INTERVAL 15 DAY), 100.00, 100.00, '全场10%折扣券', 3, NOW(), 1, 500, 'PERCENT', 0),
(NOW(), 0, NOW(), 100.00, 'VIP100', 'VIP用户专享100元优惠券，购买满500元可用', DATE_ADD(NOW(), INTERVAL 60 DAY), NULL, 500.00, 'VIP专享100元券', 2, NOW(), 1, 200, 'FIXED', 0),
(NOW(), 0, NOW(), 20.00, 'SPORTS20', '运动装备专享20%折扣，最高优惠200元', DATE_ADD(NOW(), INTERVAL 10 DAY), 200.00, 300.00, '运动装备20%折扣券', 2, NOW(), 1, 300, 'PERCENT', 0);

-- 5. 插入示例促销活动数据
INSERT IGNORE INTO `promotion` (`create_time`, `deleted`, `update_time`, `description`, `discount_value`, `end_time`, `max_discount`, `min_amount`, `name`, `start_time`, `status`, `type`) VALUES
(NOW(), 0, NOW(), '全场运动鞋8.5折优惠活动，适用于所有运动鞋品类', 15.00, DATE_ADD(NOW(), INTERVAL 7 DAY), 200.00, 200.00, '运动鞋专场85折', NOW(), 1, 'PERCENTAGE'),
(NOW(), 0, NOW(), '购物满299元立减50元，可与优惠券叠加使用', 50.00, DATE_ADD(NOW(), INTERVAL 14 DAY), NULL, 299.00, '满299减50', NOW(), 1, 'FIXED_AMOUNT'),
(NOW(), 0, NOW(), '新品限时9折优惠，最高优惠100元', 10.00, DATE_ADD(NOW(), INTERVAL 3 DAY), 100.00, 100.00, '新品9折特惠', NOW(), 1, 'PERCENTAGE'),
(NOW(), 0, NOW(), '健身器材专场8折优惠，助力您的健身计划', 20.00, DATE_ADD(NOW(), INTERVAL 20 DAY), 500.00, 500.00, '健身器材8折特惠', NOW(), 1, 'PERCENTAGE');

-- 执行完成提示
SELECT '数据库更新完成！已添加用户优惠券表和示例数据。' AS message;