-- 插入示例商品数据
INSERT INTO `product` (`create_time`, `deleted`, `update_time`, `brand`, `category_id`, `code`, `color`, `cost_price`, `description`, `detail_description`, `images`, `is_hot`, `is_new`, `is_recommended`, `main_image`, `image`, `material`, `meta_description`, `meta_keywords`, `meta_title`, `model`, `name`, `origin`, `original_price`, `price`, `sales_count`, `size`, `status`, `stock`, `view_count`, `weight`) VALUES
(NOW(), 0, NOW(), 'Nike', 1, 'NK001', '黑色', 300.00, '经典跑步鞋，舒适透气', '详细描述...', '/images/products/nike_running_1.jpg,/images/products/nike_running_2.jpg', 1, 1, 1, '/images/products/nike_running_main.jpg', '/images/products/nike_running_main.jpg', '网面+橡胶', 'Nike经典跑步鞋', '跑步鞋,Nike,运动鞋', 'Nike经典跑步鞋', 'Air Max', 'Nike Air Max 跑步鞋', '美国', 599.00, 499.00, 156, '40,41,42,43,44', 1, 100, 2340, 0.8),
(NOW(), 0, NOW(), 'Adidas', 1, 'AD001', '白色', 280.00, '轻便运动鞋，适合日常穿着', '详细描述...', '/images/products/adidas_sport_1.jpg,/images/products/adidas_sport_2.jpg', 0, 1, 1, '/images/products/adidas_sport_main.jpg', '/images/products/adidas_sport_main.jpg', '合成材料', 'Adidas运动鞋', '运动鞋,Adidas,休闲鞋', 'Adidas休闲运动鞋', 'Ultraboost', 'Adidas Ultraboost 运动鞋', '德国', 799.00, 699.00, 89, '39,40,41,42,43', 1, 75, 1567, 0.7),
(NOW(), 0, NOW(), 'Puma', 2, 'PM001', '蓝色', 80.00, '舒适运动T恤，吸湿排汗', '详细描述...', '/images/products/puma_tshirt_1.jpg,/images/products/puma_tshirt_2.jpg', 1, 0, 0, '/images/products/puma_tshirt_main.jpg', '/images/products/puma_tshirt_main.jpg', '聚酯纤维', 'Puma运动T恤', '运动服,T恤,Puma', 'Puma运动T恤', 'DryCell', 'Puma DryCell 运动T恤', '德国', 159.00, 129.00, 234, 'S,M,L,XL,XXL', 1, 200, 890, 0.2);

-- 插入示例优惠券数据
INSERT INTO `coupon` (`create_time`, `deleted`, `update_time`, `amount`, `code`, `description`, `end_time`, `max_amount`, `min_amount`, `name`, `per_user_limit`, `start_time`, `status`, `total_count`, `type`, `used_count`) VALUES
(NOW(), 0, NOW(), 50.00, 'WELCOME50', '新用户专享50元优惠券', DATE_ADD(NOW(), INTERVAL 30 DAY), NULL, 200.00, '新用户50元优惠券', 1, NOW(), 1, 1000, 'FIXED', 0),
(NOW(), 0, NOW(), 10.00, 'DISCOUNT10', '全场通用10%折扣券', DATE_ADD(NOW(), INTERVAL 15 DAY), 100.00, 100.00, '全场10%折扣券', 3, NOW(), 1, 500, 'PERCENT', 0),
(NOW(), 0, NOW(), 100.00, 'VIP100', 'VIP用户专享100元优惠券', DATE_ADD(NOW(), INTERVAL 60 DAY), NULL, 500.00, 'VIP专享100元券', 2, NOW(), 1, 200, 'FIXED', 0);

-- 插入示例促销活动数据
INSERT INTO `promotion` (`create_time`, `deleted`, `update_time`, `description`, `discount_value`, `end_time`, `max_discount`, `min_amount`, `name`, `start_time`, `status`, `type`) VALUES
(NOW(), 0, NOW(), '全场运动鞋8.5折优惠活动', 15.00, DATE_ADD(NOW(), INTERVAL 7 DAY), 200.00, 200.00, '运动鞋专场85折', NOW(), 1, 'PERCENTAGE'),
(NOW(), 0, NOW(), '满299减50优惠活动', 50.00, DATE_ADD(NOW(), INTERVAL 14 DAY), NULL, 299.00, '满299减50', NOW(), 1, 'FIXED_AMOUNT'),
(NOW(), 0, NOW(), '新品限时9折优惠', 10.00, DATE_ADD(NOW(), INTERVAL 3 DAY), 100.00, 100.00, '新品9折特惠', NOW(), 1, 'PERCENTAGE');