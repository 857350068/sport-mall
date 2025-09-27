-- 修改 product 表，添加缺失字段
ALTER TABLE `product` ADD COLUMN `image` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '商品主图片' AFTER `main_image`;