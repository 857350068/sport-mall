-- =============================================
-- 运动装备商城数据库初始化脚本
-- 数据库：sport_mall
-- 字符集：utf8mb4
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
    parent_id BIGINT COMMENT '父分类ID',
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
    status BOOLEAN NOT NULL DEFAULT TRUE COMMENT '状态 0-下架 1-上架',
    is_recommended BOOLEAN NOT NULL DEFAULT FALSE COMMENT '是否推荐 0-否 1-是',
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
    INDEX idx_is_recommended (is_recommended),
    INDEX idx_is_new (is_new),
    INDEX idx_is_hot (is_hot),
    INDEX idx_deleted (deleted),
    FOREIGN KEY (category_id) REFERENCES product_category(id)
) COMMENT='商品表';

-- =============================================
-- 4. 购物车表
-- =============================================
CREATE TABLE cart_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '购物车项ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL DEFAULT 1 COMMENT '商品数量',
    selected BOOLEAN DEFAULT TRUE COMMENT '是否选中 0-未选中 1-选中',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    INDEX idx_deleted (deleted),
    FOREIGN KEY (user_id) REFERENCES sys_user(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
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
    status ENUM('PENDING', 'PAID', 'SHIPPED', 'DELIVERED', 'COMPLETED', 'CANCELLED', 'REFUNDED') DEFAULT 'PENDING' COMMENT '订单状态',
    payment_status ENUM('PENDING', 'SUCCESS', 'FAILED', 'CANCELLED', 'REFUNDED') DEFAULT 'PENDING' COMMENT '支付状态',
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
    INDEX idx_deleted (deleted),
    FOREIGN KEY (user_id) REFERENCES sys_user(id),
    FOREIGN KEY (product_id) REFERENCES product(id)
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
    INDEX idx_create_time (create_time),
    FOREIGN KEY (order_id) REFERENCES orders(id)
) COMMENT='订单日志表';

-- =============================================
-- 7. 优惠券表
-- =============================================
CREATE TABLE coupon (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '优惠券ID',
    name VARCHAR(100) NOT NULL COMMENT '优惠券名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '优惠券编码',
    type VARCHAR(20) NOT NULL COMMENT '优惠券类型',
    amount DECIMAL(10,2) NOT NULL COMMENT '优惠金额/折扣百分比',
    min_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '最低使用金额',
    max_amount DECIMAL(10,2) COMMENT '最大优惠金额',
    total_count INT NOT NULL COMMENT '发放总数',
    used_count INT NOT NULL DEFAULT 0 COMMENT '已使用数量',
    per_user_limit INT NOT NULL DEFAULT 1 COMMENT '每人限领数量',
    start_time DATETIME NOT NULL COMMENT '有效期开始',
    end_time DATETIME NOT NULL COMMENT '有效期结束',
    description TEXT COMMENT '优惠券描述',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_code (code),
    INDEX idx_type (type),
    INDEX idx_status (status),
    INDEX idx_start_end_time (start_time, end_time),
    INDEX idx_deleted (deleted)
) COMMENT='优惠券表';

-- =============================================
-- 8. 用户优惠券关联表
-- =============================================
CREATE TABLE user_coupon (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    coupon_id BIGINT NOT NULL COMMENT '优惠券ID',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '状态 0-未使用 1-已使用 2-已过期',
    used_time DATETIME COMMENT '使用时间',
    order_id BIGINT COMMENT '使用的订单ID',
    receive_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
    expire_time DATETIME NOT NULL COMMENT '过期时间',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_coupon_id (coupon_id),
    INDEX idx_status (status),
    INDEX idx_expire_time (expire_time),
    INDEX idx_deleted (deleted),
    FOREIGN KEY (user_id) REFERENCES sys_user(id),
    FOREIGN KEY (coupon_id) REFERENCES coupon(id)
) COMMENT='用户优惠券关联表';

-- =============================================
-- 9. 促销活动表
-- =============================================
CREATE TABLE promotion (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '活动ID',
    name VARCHAR(100) NOT NULL COMMENT '活动名称',
    type VARCHAR(20) NOT NULL COMMENT '活动类型',
    description TEXT COMMENT '活动描述',
    discount_value DECIMAL(10,2) COMMENT '折扣值/减免金额',
    min_amount DECIMAL(10,2) DEFAULT 0.00 COMMENT '最低消费金额',
    max_discount DECIMAL(10,2) COMMENT '最大折扣金额',
    start_time DATETIME NOT NULL COMMENT '活动开始时间',
    end_time DATETIME NOT NULL COMMENT '活动结束时间',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    deleted TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_type (type),
    INDEX idx_status (status),
    INDEX idx_start_end_time (start_time, end_time),
    INDEX idx_deleted (deleted)
) COMMENT='促销活动表';

-- =============================================
-- 初始化数据
-- =============================================

-- 插入管理员用户
INSERT INTO sys_user (username, password, nickname, email, role, status) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaYMviOw8WQUO', '系统管理员', 'admin@sportmall.com', 'ROLE_ADMIN', 1),
('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBaYMviOw8WQUO', '测试用户', 'user1@test.com', 'ROLE_USER', 1);

-- 插入商品分类
INSERT INTO product_category (name, description, icon, sort_order, status, show_in_home) VALUES
('跑步装备', '专业跑步相关装备', 'fas fa-running', 1, 1, TRUE),
('健身器材', '力量训练和健身器材', 'fas fa-dumbbell', 2, 1, TRUE),
('户外运动', '户外探险和运动装备', 'fas fa-mountain', 3, 1, TRUE),
('球类运动', '各类球类运动装备', 'fas fa-basketball-ball', 4, 1, TRUE);

-- 插入示例商品
INSERT INTO product (code, name, brand, category_id, main_image, description, price, original_price, stock, status, is_recommended, is_hot) VALUES
('NIKE001', 'Nike Air Max 270 跑步鞋', 'Nike', 1, '/images/nike-air-max-270.jpg', '舒适透气的跑步鞋，适合日常训练和休闲穿着', 699.00, 899.00, 50, TRUE, TRUE, TRUE),
('ADIDAS001', 'Adidas UltraBoost 22', 'Adidas', 1, '/images/adidas-ultraboost-22.jpg', '顶级缓震跑步鞋，专为长距离跑步设计', 1299.00, 1499.00, 30, TRUE, TRUE, FALSE),
('NIKE002', '可调节哑铃套装', 'Nike', 2, '/images/adjustable-dumbbells.jpg', '家用健身哑铃，重量可调节，节省空间', 899.00, 1299.00, 20, TRUE, FALSE, TRUE),
('NORTH001', 'The North Face 冲锋衣', 'The North Face', 3, '/images/north-face-jacket.jpg', '防风防水透气冲锋衣，适合各种户外活动', 1899.00, 2299.00, 15, TRUE, TRUE, FALSE),
('WILSON001', 'Wilson 网球拍', 'Wilson', 4, '/images/wilson-tennis-racket.jpg', '专业网球拍，适合中高级球员使用', 599.00, 799.00, 25, TRUE, FALSE, FALSE);

-- 插入优惠券
INSERT INTO coupon (name, code, type, amount, min_amount, total_count, per_user_limit, start_time, end_time, description, status) VALUES
('新用户专享', 'NEW50', 'FIXED', 50.00, 200.00, 1000, 1, '2024-01-01 00:00:00', '2024-12-31 23:59:59', '新用户注册专享50元优惠券', 1),
('满300减30', 'SAVE30', 'FIXED', 30.00, 300.00, 500, 3, '2024-01-01 00:00:00', '2024-12-31 23:59:59', '满300元立减30元', 1),
('9折优惠券', 'DISCOUNT10', 'PERCENT', 0.10, 100.00, 200, 2, '2024-01-01 00:00:00', '2024-12-31 23:59:59', '全场商品9折优惠', 1);

-- 插入促销活动
INSERT INTO promotion (name, type, description, discount_value, min_amount, start_time, end_time, status) VALUES
('春季大促', 'DISCOUNT', '春季运动装备大促销，全场8.5折', 0.15, 0.00, '2024-03-01 00:00:00', '2024-05-31 23:59:59', 1),
('满千减百', 'FULL_REDUCE', '满1000元立减100元', 100.00, 1000.00, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 1);

COMMIT;