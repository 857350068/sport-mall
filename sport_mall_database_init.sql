-- =============================================
-- 运动装备商城完整数据库初始化脚本
-- 数据库：sport_mall
-- 字符集：utf8mb4
-- 说明：包含完整的表结构、约束和初始化数据
-- =============================================

-- 创建数据库
DROP DATABASE IF EXISTS sport_mall;
CREATE DATABASE sport_mall CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sport_mall;

-- =============================================
-- 1. 用户表
-- =============================================
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码(加密)',
    nickname VARCHAR(50) COMMENT '昵称',
    real_name VARCHAR(50) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    gender TINYINT DEFAULT 0 COMMENT '性别 0-未知 1-男 2-女',
    birthday DATE COMMENT '生日',
    address VARCHAR(500) COMMENT '地址',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER' COMMENT '角色 ROLE_USER-用户 ROLE_ADMIN-管理员',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (phone),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) COMMENT='用户表';

-- =============================================
-- 2. 商品分类表
-- =============================================
CREATE TABLE product_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '分类ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    description VARCHAR(200) COMMENT '分类描述',
    icon VARCHAR(100) COMMENT '分类图标',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
    sort_order INT DEFAULT 0 COMMENT '排序序号',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    show_in_home BOOLEAN DEFAULT FALSE COMMENT '是否显示在首页',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) COMMENT='商品分类表';

-- =============================================
-- 3. 商品表
-- =============================================
CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '商品ID',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '商品编码',
    name VARCHAR(200) NOT NULL COMMENT '商品名称',
    brand VARCHAR(100) COMMENT '品牌',
    model VARCHAR(100) COMMENT '型号',
    category_id BIGINT COMMENT '分类ID',
    main_image VARCHAR(255) COMMENT '主图片',
    images TEXT COMMENT '图片列表，JSON格式',
    description TEXT COMMENT '商品描述',
    detail_description LONGTEXT COMMENT '商品详情',
    original_price DECIMAL(10,2) COMMENT '原价',
    price DECIMAL(10,2) NOT NULL COMMENT '现价',
    cost_price DECIMAL(10,2) COMMENT '成本价',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    sold_count INT NOT NULL DEFAULT 0 COMMENT '销售数量',
    view_count BIGINT NOT NULL DEFAULT 0 COMMENT '浏览次数',
    origin VARCHAR(100) COMMENT '产地',
    weight DECIMAL(8,2) COMMENT '重量(kg)',
    size VARCHAR(100) COMMENT '尺寸',
    color VARCHAR(50) COMMENT '颜色',
    material VARCHAR(100) COMMENT '材质',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0-下架 1-上架',
    is_recommend TINYINT NOT NULL DEFAULT 0 COMMENT '是否推荐 0-否 1-是',
    is_new TINYINT NOT NULL DEFAULT 0 COMMENT '是否新品 0-否 1-是',
    is_hot TINYINT NOT NULL DEFAULT 0 COMMENT '是否热销 0-否 1-是',
    meta_title VARCHAR(255) COMMENT 'SEO标题',
    meta_keywords VARCHAR(255) COMMENT 'SEO关键词',
    meta_description TEXT COMMENT 'SEO描述',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_code (code),
    INDEX idx_category_id (category_id),
    INDEX idx_brand (brand),
    INDEX idx_status (status),
    INDEX idx_price (price),
    INDEX idx_is_recommend (is_recommend),
    INDEX idx_is_new (is_new),
    INDEX idx_is_hot (is_hot),
    INDEX idx_deleted (deleted)
) COMMENT='商品表';

-- =============================================
-- 4. 购物车表
-- =============================================
CREATE TABLE cart_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '购物车项ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL DEFAULT 1 COMMENT '商品数量',
    selected TINYINT DEFAULT 1 COMMENT '是否选中 0-未选中 1-选中',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_product (user_id, product_id),
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    INDEX idx_deleted (deleted)
) COMMENT='购物车表';

-- =============================================
-- 5. 订单表
-- =============================================
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    product_image VARCHAR(255) COMMENT '商品图片',
    product_price DECIMAL(10,2) NOT NULL COMMENT '商品单价',
    quantity INT NOT NULL COMMENT '购买数量',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '总金额',
    discount_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '优惠金额',
    actual_amount DECIMAL(10,2) NOT NULL COMMENT '实付金额',
    coupon_id BIGINT COMMENT '使用的优惠券ID',
    promotion_id BIGINT COMMENT '参与的促销活动ID',
    receiver_name VARCHAR(50) NOT NULL COMMENT '收货人姓名',
    receiver_phone VARCHAR(20) NOT NULL COMMENT '收货人电话',
    receiver_address VARCHAR(500) NOT NULL COMMENT '收货地址',
    remark TEXT COMMENT '订单备注',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '订单状态',
    payment_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '支付状态',
    payment_time DATETIME COMMENT '支付时间',
    shipment_time DATETIME COMMENT '发货时间',
    delivery_time DATETIME COMMENT '送达时间',
    completion_time DATETIME COMMENT '完成时间',
    auto_cancel_time DATETIME COMMENT '自动取消时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    INDEX idx_status (status),
    INDEX idx_payment_status (payment_status),
    INDEX idx_create_time (create_time),
    INDEX idx_deleted (deleted)
) COMMENT='订单表';

-- =============================================
-- 6. 订单日志表
-- =============================================
CREATE TABLE order_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单号',
    action VARCHAR(50) NOT NULL COMMENT '操作动作',
    old_status VARCHAR(20) COMMENT '原状态',
    new_status VARCHAR(20) COMMENT '新状态',
    operator_id BIGINT COMMENT '操作人ID',
    operator_name VARCHAR(50) COMMENT '操作人姓名',
    remark TEXT COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order_id (order_id),
    INDEX idx_order_no (order_no),
    INDEX idx_create_time (create_time)
) COMMENT='订单日志表';

-- =============================================
-- 7. 优惠券表
-- =============================================
CREATE TABLE coupon (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '优惠券ID',
    name VARCHAR(100) NOT NULL COMMENT '优惠券名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '优惠券编码',
    type VARCHAR(20) NOT NULL COMMENT '优惠券类型 FIXED_AMOUNT-固定金额 PERCENTAGE-百分比折扣',
    amount DECIMAL(10,2) NOT NULL COMMENT '优惠金额/折扣百分比',
    min_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '最低使用金额',
    max_amount DECIMAL(10,2) COMMENT '最大优惠金额',
    total_count INT NOT NULL COMMENT '发放总数',
    used_count INT DEFAULT 0 COMMENT '已使用数量',
    per_user_limit INT DEFAULT 1 COMMENT '每人限领数量',
    start_time DATETIME NOT NULL COMMENT '有效期开始',
    end_time DATETIME NOT NULL COMMENT '有效期结束',
    description TEXT COMMENT '优惠券描述',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_code (code),
    INDEX idx_type (type),
    INDEX idx_status (status),
    INDEX idx_start_time (start_time),
    INDEX idx_end_time (end_time),
    INDEX idx_deleted (deleted)
) COMMENT='优惠券表';

-- =============================================
-- 8. 用户优惠券表
-- =============================================
CREATE TABLE user_coupon (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    coupon_id BIGINT NOT NULL COMMENT '优惠券ID',
    coupon_code VARCHAR(50) NOT NULL COMMENT '优惠券编码',
    order_id BIGINT COMMENT '使用的订单ID',
    status TINYINT DEFAULT 0 COMMENT '状态 0-未使用 1-已使用 2-已过期',
    receive_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
    use_time DATETIME COMMENT '使用时间',
    expire_time DATETIME COMMENT '过期时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_coupon_id (coupon_id),
    INDEX idx_coupon_code (coupon_code),
    INDEX idx_status (status),
    INDEX idx_expire_time (expire_time),
    INDEX idx_deleted (deleted)
) COMMENT='用户优惠券表';

-- =============================================
-- 9. 促销活动表
-- =============================================
CREATE TABLE promotion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '促销活动ID',
    name VARCHAR(100) NOT NULL COMMENT '活动名称',
    type VARCHAR(20) NOT NULL COMMENT '活动类型 DISCOUNT-折扣 FULL_REDUCTION-满减 FIXED_AMOUNT-固定减免',
    description TEXT COMMENT '活动描述',
    discount_value DECIMAL(10,2) COMMENT '折扣值/减免金额',
    min_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '最低消费金额',
    max_discount DECIMAL(10,2) COMMENT '最大折扣金额',
    start_time DATETIME NOT NULL COMMENT '活动开始时间',
    end_time DATETIME NOT NULL COMMENT '活动结束时间',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_type (type),
    INDEX idx_status (status),
    INDEX idx_start_time (start_time),
    INDEX idx_end_time (end_time),
    INDEX idx_deleted (deleted)
) COMMENT='促销活动表';

-- =============================================
-- 10. 商品促销关联表
-- =============================================
CREATE TABLE product_promotion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    promotion_id BIGINT NOT NULL COMMENT '促销活动ID',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_product_promotion (product_id, promotion_id),
    INDEX idx_product_id (product_id),
    INDEX idx_promotion_id (promotion_id)
) COMMENT='商品促销关联表';

-- =============================================
-- 11. 支付记录表
-- =============================================
CREATE TABLE payment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '支付ID',
    payment_no VARCHAR(50) NOT NULL UNIQUE COMMENT '支付单号',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    payment_method VARCHAR(20) DEFAULT 'ALIPAY' COMMENT '支付方式 ALIPAY-支付宝 WECHAT-微信 BANK-银行卡',
    amount DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '支付状态 PENDING-待支付 SUCCESS-成功 FAILED-失败 CANCELLED-取消',
    third_party_no VARCHAR(100) COMMENT '第三方支付单号',
    callback_data TEXT COMMENT '回调数据',
    payment_time DATETIME COMMENT '支付时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_payment_no (payment_no),
    INDEX idx_order_id (order_id),
    INDEX idx_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) COMMENT='支付记录表';

-- =============================================
-- 初始化数据
-- =============================================

-- 1. 插入默认管理员用户
INSERT INTO sys_user (username, password, nickname, role, status) VALUES 
('admin', '$2a$10$7JB720yubVSQLVM5c2uyOuWmiBZwM9L9rN4r9sK2E.k4hZ7W.W7W6', '系统管理员', 'ROLE_ADMIN', 1),
('test', '$2a$10$7JB720yubVSQLVM5c2uyOuWmiBZwM9L9rN4r9sK2E.k4hZ7W.W7W6', '测试用户', 'ROLE_USER', 1);
-- 默认密码都是: admin123

-- 2. 插入商品分类数据
INSERT INTO product_category (name, parent_id, sort_order, description, show_in_home) VALUES
('跑步装备', 0, 1, '专业跑步运动装备', true),
('健身器材', 0, 2, '健身房和家庭健身器材', true),
('户外运动', 0, 3, '户外探险运动装备', true),
('球类运动', 0, 4, '各种球类运动装备', true),
('游泳用品', 0, 5, '游泳和水上运动用品', true),
('运动服装', 0, 6, '专业运动服装系列', true),
('跑步鞋', 1, 1, '专业跑步鞋类', false),
('跑步服装', 1, 2, '跑步专用服装', false),
('跑步配件', 1, 3, '跑步配件用品', false),
('力量训练', 2, 1, '力量训练器材', false),
('有氧设备', 2, 2, '有氧运动设备', false),
('瑜伽用品', 2, 3, '瑜伽和普拉提用品', false);

-- 3. 插入示例商品数据
INSERT INTO product (code, name, brand, category_id, main_image, description, detail_description, price, original_price, stock, origin, status, is_recommend, is_new, is_hot) VALUES
('SPT2024001', 'Nike Air Zoom Pegasus 40 跑步鞋', 'Nike', 7, '/img/nike-pegasus-40.jpg', '经典跑步鞋，轻量透气，提供优秀的缓震性能', '详细描述内容...', 899.00, 1199.00, 50, '美国', 1, 1, 1, 1),
('SPT2024002', 'Adidas Ultraboost 23 跑步鞋', 'Adidas', 7, '/img/adidas-ultraboost-23.jpg', '采用Boost中底技术，能量回弹卓越', '详细描述内容...', 1299.00, 1699.00, 35, '德国', 1, 1, 1, 0),
('SPT2024003', 'Under Armour HeatGear 运动T恤', 'Under Armour', 8, '/img/ua-heatgear-tshirt.jpg', '速干排汗，保持运动时的干爽舒适', '详细描述内容...', 199.00, 299.00, 100, '美国', 1, 0, 1, 0),
('SPT2024004', 'Lululemon Align 瑜伽裤', 'Lululemon', 12, '/img/lululemon-align-pants.jpg', '裸感面料，4向弹性，瑜伽首选', '详细描述内容...', 688.00, 888.00, 60, '加拿大', 1, 1, 0, 1),
('SPT2024005', 'TRX 悬挂训练器', 'TRX', 10, '/img/trx-suspension-trainer.jpg', '全身功能性训练，随时随地健身', '详细描述内容...', 1299.00, 1599.00, 30, '美国', 1, 1, 0, 0),
('SPT2024006', 'Manduka PRO 瑜伽垫', 'Manduka', 12, '/img/manduka-pro-mat.jpg', '专业级瑜伽垫，防滑耐用，终身保修', '详细描述内容...', 799.00, 999.00, 80, '美国', 1, 0, 0, 1),
('SPT2024007', 'Garmin Forerunner 255 运动手表', 'Garmin', 9, '/img/garmin-forerunner-255.jpg', 'GPS运动手表，全天候健康监测', '详细描述内容...', 2399.00, 2799.00, 25, '美国', 1, 1, 1, 1),
('SPT2024008', 'Nike Dri-FIT 运动短裤', 'Nike', 8, '/img/nike-dri-fit-shorts.jpg', '轻量速干，运动自由无束缚', '详细描述内容...', 299.00, 399.00, 120, '美国', 1, 0, 0, 0);

-- 4. 插入优惠券数据
INSERT INTO coupon (name, code, type, amount, min_amount, max_amount, total_count, per_user_limit, start_time, end_time, description, status) VALUES
('新用户专享券', 'WELCOME100', 'FIXED_AMOUNT', 100.00, 500.00, NULL, 1000, 1, '2024-01-01 00:00:00', '2024-12-31 23:59:59', '新用户专享100元优惠券，满500可用', 1),
('运动装备8折券', 'SPORT20OFF', 'PERCENTAGE', 0.80, 200.00, 300.00, 500, 2, '2024-01-01 00:00:00', '2024-12-31 23:59:59', '运动装备专享8折优惠，最高优惠300元', 1),
('满1000减200', 'SAVE200', 'FIXED_AMOUNT', 200.00, 1000.00, NULL, 200, 1, '2024-01-01 00:00:00', '2024-12-31 23:59:59', '满1000元减200元优惠券', 1);

-- 5. 插入促销活动数据
INSERT INTO promotion (name, type, description, discount_value, min_amount, max_discount, start_time, end_time, status) VALUES
('跑步装备专场', 'DISCOUNT', '跑步装备85折优惠活动', 0.85, 200.00, 500.00, '2024-03-01 00:00:00', '2024-05-31 23:59:59', 1),
('健身器材满减', 'FULL_REDUCTION', '健身器材满2000减300', 300.00, 2000.00, NULL, '2024-04-01 00:00:00', '2024-06-30 23:59:59', 1),
('新品上市特惠', 'FIXED_AMOUNT', '新品上市限时特惠', 150.00, 800.00, NULL, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 1);

-- 6. 商品促销关联
INSERT INTO product_promotion (product_id, promotion_id) VALUES
(1, 1), (2, 1), (3, 1), -- 跑步装备参与跑步专场活动
(5, 2), (6, 2), -- 健身器材参与满减活动
(1, 3), (2, 3), (7, 3); -- 新品参与新品特惠

-- =============================================
-- 完成初始化
-- =============================================
SELECT 'Database initialization completed successfully!' AS message;