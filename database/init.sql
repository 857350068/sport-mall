-- 运动装备商城数据库初始化脚本
-- 创建数据库
CREATE DATABASE IF NOT EXISTS sport_mall 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE sport_mall;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码(加密)',
    nickname VARCHAR(50) COMMENT '昵称',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    avatar VARCHAR(255) COMMENT '头像URL',
    gender TINYINT COMMENT '性别 0-未知 1-男 2-女',
    birthday DATE COMMENT '生日',
    address VARCHAR(500) COMMENT '地址',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    role VARCHAR(20) DEFAULT 'ROLE_USER' COMMENT '角色 ROLE_USER-用户 ROLE_ADMIN-管理员',
    last_login_time DATETIME COMMENT '最后登录时间',
    last_login_ip VARCHAR(50) COMMENT '最后登录IP',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (phone),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 商品分类表
CREATE TABLE IF NOT EXISTS product_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID，0表示顶级分类',
    sort_order INT DEFAULT 0 COMMENT '排序',
    icon VARCHAR(255) COMMENT '分类图标',
    description TEXT COMMENT '分类描述',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品表
CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '商品ID',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '商品编码',
    name VARCHAR(200) NOT NULL COMMENT '商品名称',
    brand VARCHAR(100) COMMENT '品牌',
    category_id BIGINT COMMENT '分类ID',
    main_image VARCHAR(255) COMMENT '主图片',
    images TEXT COMMENT '图片列表，JSON格式',
    description TEXT COMMENT '商品描述',
    detail_description LONGTEXT COMMENT '商品详情',
    original_price DECIMAL(10,2) COMMENT '原价',
    price DECIMAL(10,2) NOT NULL COMMENT '现价',
    cost_price DECIMAL(10,2) COMMENT '成本价',
    stock INT DEFAULT 0 COMMENT '库存数量',
    sold_count INT DEFAULT 0 COMMENT '销售数量',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    origin VARCHAR(100) COMMENT '产地',
    weight DECIMAL(8,2) COMMENT '重量(kg)',
    size VARCHAR(100) COMMENT '尺寸',
    color VARCHAR(50) COMMENT '颜色',
    material VARCHAR(100) COMMENT '材质',
    status TINYINT DEFAULT 1 COMMENT '状态 0-下架 1-上架',
    is_recommend TINYINT DEFAULT 0 COMMENT '是否推荐 0-否 1-是',
    is_new TINYINT DEFAULT 0 COMMENT '是否新品 0-否 1-是',
    is_hot TINYINT DEFAULT 0 COMMENT '是否热销 0-否 1-是',
    meta_title VARCHAR(255) COMMENT 'SEO标题',
    meta_keywords VARCHAR(255) COMMENT 'SEO关键词',
    meta_description TEXT COMMENT 'SEO描述',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_code (code),
    INDEX idx_name (name),
    INDEX idx_category_id (category_id),
    INDEX idx_brand (brand),
    INDEX idx_price (price),
    INDEX idx_status (status),
    INDEX idx_is_recommend (is_recommend),
    INDEX idx_is_new (is_new),
    INDEX idx_is_hot (is_hot),
    INDEX idx_deleted (deleted),
    FULLTEXT idx_fulltext (name, description, detail_description)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL UNIQUE COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(200) NOT NULL COMMENT '商品名称',
    product_image VARCHAR(255) COMMENT '商品图片',
    product_price DECIMAL(10,2) NOT NULL COMMENT '商品单价',
    quantity INT NOT NULL COMMENT '购买数量',
    total_amount DECIMAL(10,2) NOT NULL COMMENT '总金额',
    discount_amount DECIMAL(10,2) DEFAULT 0 COMMENT '优惠金额',
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
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    INDEX idx_status (status),
    INDEX idx_payment_status (payment_status),
    INDEX idx_create_time (create_time),
    INDEX idx_auto_cancel_time (auto_cancel_time),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单日志表
CREATE TABLE IF NOT EXISTS order_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单号',
    action VARCHAR(50) NOT NULL COMMENT '操作',
    old_status VARCHAR(20) COMMENT '原状态',
    new_status VARCHAR(20) COMMENT '新状态',
    operator_id BIGINT COMMENT '操作人ID',
    operator_name VARCHAR(50) COMMENT '操作人姓名',
    remark TEXT COMMENT '备注',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order_id (order_id),
    INDEX idx_order_no (order_no),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单日志表';

-- 支付记录表
CREATE TABLE IF NOT EXISTS payment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '支付ID',
    payment_no VARCHAR(50) NOT NULL UNIQUE COMMENT '支付单号',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    payment_method VARCHAR(20) DEFAULT 'ALIPAY' COMMENT '支付方式',
    amount DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '支付状态',
    third_party_no VARCHAR(100) COMMENT '第三方支付单号',
    callback_data TEXT COMMENT '回调数据',
    payment_time DATETIME COMMENT '支付时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_payment_no (payment_no),
    INDEX idx_order_id (order_id),
    INDEX idx_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付记录表';

-- 优惠券表
CREATE TABLE IF NOT EXISTS coupon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '优惠券ID',
    name VARCHAR(100) NOT NULL COMMENT '优惠券名称',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '优惠券编码',
    type VARCHAR(20) NOT NULL COMMENT '优惠券类型',
    amount DECIMAL(10,2) NOT NULL COMMENT '优惠金额/折扣百分比',
    min_amount DECIMAL(10,2) DEFAULT 0 COMMENT '最低使用金额',
    max_amount DECIMAL(10,2) COMMENT '最大优惠金额',
    total_count INT NOT NULL COMMENT '发放总数',
    used_count INT DEFAULT 0 COMMENT '已使用数量',
    per_user_limit INT DEFAULT 1 COMMENT '每人限领数量',
    start_time DATETIME NOT NULL COMMENT '有效期开始',
    end_time DATETIME NOT NULL COMMENT '有效期结束',
    description TEXT COMMENT '优惠券描述',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_code (code),
    INDEX idx_type (type),
    INDEX idx_status (status),
    INDEX idx_start_time (start_time),
    INDEX idx_end_time (end_time),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='优惠券表';

-- 用户优惠券表
CREATE TABLE IF NOT EXISTS user_coupon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    coupon_id BIGINT NOT NULL COMMENT '优惠券ID',
    coupon_code VARCHAR(50) NOT NULL COMMENT '优惠券编码',
    order_id BIGINT COMMENT '使用的订单ID',
    status TINYINT DEFAULT 0 COMMENT '状态 0-未使用 1-已使用 2-已过期',
    receive_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '领取时间',
    use_time DATETIME COMMENT '使用时间',
    expire_time DATETIME COMMENT '过期时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (user_id),
    INDEX idx_coupon_id (coupon_id),
    INDEX idx_coupon_code (coupon_code),
    INDEX idx_status (status),
    INDEX idx_expire_time (expire_time),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户优惠券表';

-- 促销活动表
CREATE TABLE IF NOT EXISTS promotion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '促销活动ID',
    name VARCHAR(100) NOT NULL COMMENT '活动名称',
    type VARCHAR(20) NOT NULL COMMENT '活动类型',
    description TEXT COMMENT '活动描述',
    discount_value DECIMAL(10,2) COMMENT '折扣值/减免金额',
    min_amount DECIMAL(10,2) DEFAULT 0 COMMENT '最低消费金额',
    max_discount DECIMAL(10,2) COMMENT '最大折扣金额',
    start_time DATETIME NOT NULL COMMENT '活动开始时间',
    end_time DATETIME NOT NULL COMMENT '活动结束时间',
    status TINYINT DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_type (type),
    INDEX idx_status (status),
    INDEX idx_start_time (start_time),
    INDEX idx_end_time (end_time),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='促销活动表';

-- 商品促销关联表
CREATE TABLE IF NOT EXISTS product_promotion (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    promotion_id BIGINT NOT NULL COMMENT '促销活动ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_product_promotion (product_id, promotion_id),
    INDEX idx_product_id (product_id),
    INDEX idx_promotion_id (promotion_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品促销关联表';

-- 购物车表
CREATE TABLE IF NOT EXISTS cart_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '购物车项ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL DEFAULT 1 COMMENT '商品数量',
    selected TINYINT DEFAULT 1 COMMENT '是否选中 0-未选中 1-选中',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_product (user_id, product_id),
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    INDEX idx_selected (selected),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车表';

-- 退货申请表
CREATE TABLE IF NOT EXISTS refund_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '退货ID',
    refund_no VARCHAR(50) NOT NULL UNIQUE COMMENT '退货单号',
    order_id BIGINT NOT NULL COMMENT '订单ID',
    order_no VARCHAR(50) NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL COMMENT '退货数量',
    refund_amount DECIMAL(10,2) NOT NULL COMMENT '退款金额',
    reason VARCHAR(20) NOT NULL COMMENT '退货原因',
    description TEXT COMMENT '退货说明',
    images TEXT COMMENT '退货图片，JSON格式',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '退货状态',
    admin_remark TEXT COMMENT '管理员备注',
    process_time DATETIME COMMENT '处理时间',
    refund_time DATETIME COMMENT '退款时间',
    deleted TINYINT DEFAULT 0 COMMENT '逻辑删除 0-未删除 1-已删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_refund_no (refund_no),
    INDEX idx_order_id (order_id),
    INDEX idx_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='退货申请表';

-- 插入默认管理员用户
INSERT INTO sys_user (username, password, nickname, role, status) 
VALUES ('admin', '$2a$10$7JB720yubVSQLVM5c2uyOuWmiBZwM9L9rN4r9sK2E.k4hZ7W.W7W6', '系统管理员', 'ROLE_ADMIN', 1)
ON DUPLICATE KEY UPDATE password = '$2a$10$7JB720yubVSQLVM5c2uyOuWmiBZwM9L9rN4r9sK2E.k4hZ7W.W7W6';
-- 默认密码是: admin123

-- 插入默认分类数据
INSERT INTO product_category (name, parent_id, sort_order, description) VALUES
('运动鞋', 0, 1, '各类运动鞋'),
('运动服装', 0, 2, '运动服装系列'),
('运动器材', 0, 3, '健身运动器材'),
('户外装备', 0, 4, '户外运动装备'),
('运动配件', 0, 5, '运动配件用品'),
('跑步鞋', 1, 1, '专业跑步鞋'),
('篮球鞋', 1, 2, '专业篮球鞋'),
('足球鞋', 1, 3, '专业足球鞋'),
('运动T恤', 2, 1, '运动短袖T恤'),
('运动裤', 2, 2, '运动长裤短裤'),
('运动套装', 2, 3, '运动套装');

-- 插入示例商品数据
INSERT INTO product (code, name, brand, category_id, main_image, description, price, original_price, stock, origin, status, is_recommend, is_new) VALUES
('PRD20241001001', 'Nike Air Max 270 跑步鞋', 'Nike', 6, '/img/nike-air-max-270.jpg', '轻量化设计，提供卓越的缓震效果', 899.00, 1099.00, 50, '美国', 1, 1, 1),
('PRD20241001002', 'Adidas UltraBoost 22 跑步鞋', 'Adidas', 6, '/img/adidas-ultraboost.jpg', '采用Boost中底科技，能量回弹出色', 1299.00, 1599.00, 30, '德国', 1, 1, 0),
('PRD20241001003', 'Under Armour 运动T恤', 'Under Armour', 9, '/img/ua-tshirt.jpg', '速干透气，舒适运动体验', 199.00, 299.00, 100, '美国', 1, 0, 1),
('PRD20241001004', 'Puma 运动裤', 'Puma', 10, '/img/puma-pants.jpg', '弹性面料，自由活动不受限', 299.00, 399.00, 80, '德国', 1, 0, 0),
('PRD20241001005', '瑜伽垫专业版', '自有品牌', 3, '/img/yoga-mat.jpg', '环保材质，防滑耐用', 89.00, 129.00, 200, '中国', 1, 1, 0);

-- 创建示例优惠券
INSERT INTO coupon (name, code, type, amount, min_amount, total_count, start_time, end_time, description) VALUES
('新用户专享券', 'NEWUSER50', 'FIXED_AMOUNT', 50.00, 200.00, 1000, '2024-01-01 00:00:00', '2024-12-31 23:59:59', '新用户专享50元优惠券'),
('满500减100', 'SAVE100', 'FULL_REDUCTION', 100.00, 500.00, 500, '2024-01-01 00:00:00', '2024-12-31 23:59:59', '满500元减100元'),
('9折优惠券', 'DISCOUNT10', 'PERCENTAGE', 0.90, 100.00, 300, '2024-01-01 00:00:00', '2024-12-31 23:59:59', '全场9折优惠');

-- 创建示例促销活动
INSERT INTO promotion (name, type, description, discount_value, min_amount, start_time, end_time) VALUES
('春季大促', 'DISCOUNT', '春季运动装备大促销', 0.85, 300.00, '2024-03-01 00:00:00', '2024-05-31 23:59:59'),
('夏日清仓', 'FULL_REDUCTION', '夏日运动装备清仓活动', 200.00, 1000.00, '2024-06-01 00:00:00', '2024-08-31 23:59:59'),
('新品首发', 'FIXED_AMOUNT', '新品首发特惠', 100.00, 500.00, '2024-01-01 00:00:00', '2024-12-31 23:59:59');