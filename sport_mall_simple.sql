-- Sport Mall Database Initialization
DROP DATABASE IF EXISTS sport_mall;
CREATE DATABASE sport_mall CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE sport_mall;

-- User table
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    nickname VARCHAR(50),
    real_name VARCHAR(50),
    email VARCHAR(100),
    phone VARCHAR(20),
    avatar VARCHAR(255),
    gender TINYINT DEFAULT 0,
    birthday DATE,
    address VARCHAR(500),
    status TINYINT NOT NULL DEFAULT 1,
    role VARCHAR(20) NOT NULL DEFAULT 'ROLE_USER',
    last_login_time DATETIME,
    last_login_ip VARCHAR(50),
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email),
    INDEX idx_phone (phone),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
);

-- Product category table
CREATE TABLE product_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL,
    description VARCHAR(200),
    icon VARCHAR(100),
    parent_id BIGINT DEFAULT 0,
    sort_order INT DEFAULT 0,
    status TINYINT NOT NULL DEFAULT 1,
    show_in_home BOOLEAN DEFAULT FALSE,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status),
    INDEX idx_deleted (deleted)
);

-- Product table
CREATE TABLE product (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(200) NOT NULL,
    brand VARCHAR(100),
    model VARCHAR(100),
    category_id BIGINT,
    main_image VARCHAR(255),
    images TEXT,
    description TEXT,
    detail_description LONGTEXT,
    original_price DECIMAL(10,2),
    price DECIMAL(10,2) NOT NULL,
    cost_price DECIMAL(10,2),
    stock INT NOT NULL DEFAULT 0,
    sold_count INT NOT NULL DEFAULT 0,
    view_count BIGINT NOT NULL DEFAULT 0,
    origin VARCHAR(100),
    weight DECIMAL(8,2),
    size VARCHAR(100),
    color VARCHAR(50),
    material VARCHAR(100),
    status TINYINT NOT NULL DEFAULT 1,
    is_recommend TINYINT NOT NULL DEFAULT 0,
    is_new TINYINT NOT NULL DEFAULT 0,
    is_hot TINYINT NOT NULL DEFAULT 0,
    meta_title VARCHAR(255),
    meta_keywords VARCHAR(255),
    meta_description TEXT,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_code (code),
    INDEX idx_category_id (category_id),
    INDEX idx_brand (brand),
    INDEX idx_status (status),
    INDEX idx_price (price),
    INDEX idx_is_recommend (is_recommend),
    INDEX idx_is_new (is_new),
    INDEX idx_is_hot (is_hot),
    INDEX idx_deleted (deleted)
);

-- Cart item table
CREATE TABLE cart_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    selected TINYINT DEFAULT 1,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_user_product (user_id, product_id),
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    INDEX idx_deleted (deleted)
);

-- Orders table
CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_no VARCHAR(50) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    product_id BIGINT NOT NULL,
    product_name VARCHAR(200) NOT NULL,
    product_image VARCHAR(255),
    product_price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    discount_amount DECIMAL(10,2) DEFAULT 0.00,
    actual_amount DECIMAL(10,2) NOT NULL,
    coupon_id BIGINT,
    promotion_id BIGINT,
    receiver_name VARCHAR(50) NOT NULL,
    receiver_phone VARCHAR(20) NOT NULL,
    receiver_address VARCHAR(500) NOT NULL,
    remark TEXT,
    status VARCHAR(20) DEFAULT 'PENDING',
    payment_status VARCHAR(20) DEFAULT 'PENDING',
    payment_time DATETIME,
    shipment_time DATETIME,
    delivery_time DATETIME,
    completion_time DATETIME,
    auto_cancel_time DATETIME,
    deleted TINYINT NOT NULL DEFAULT 0,
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_order_no (order_no),
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    INDEX idx_status (status),
    INDEX idx_payment_status (payment_status),
    INDEX idx_create_time (create_time),
    INDEX idx_deleted (deleted)
);

-- Insert admin user (password: admin123)
INSERT INTO sys_user (username, password, nickname, role, status) VALUES 
('admin', '$2a$10$7JB720yubVSQLVM5c2uyOuWmiBZwM9L9rN4r9sK2E.k4hZ7W.W7W6', 'Administrator', 'ROLE_ADMIN', 1),
('test', '$2a$10$7JB720yubVSQLVM5c2uyOuWmiBZwM9L9rN4r9sK2E.k4hZ7W.W7W6', 'Test User', 'ROLE_USER', 1);

-- Insert categories
INSERT INTO product_category (name, parent_id, sort_order, description, show_in_home) VALUES
('Running Equipment', 0, 1, 'Professional running gear', true),
('Fitness Equipment', 0, 2, 'Gym and home fitness equipment', true),
('Outdoor Sports', 0, 3, 'Outdoor adventure equipment', true),
('Ball Sports', 0, 4, 'Various ball sports equipment', true),
('Swimming', 0, 5, 'Swimming and water sports', true),
('Sports Clothing', 0, 6, 'Professional sports apparel', true);

-- Insert sample products
INSERT INTO product (code, name, brand, category_id, main_image, description, price, original_price, stock, origin, status, is_recommend, is_new, is_hot) VALUES
('SPT2024001', 'Nike Air Zoom Pegasus 40', 'Nike', 1, '/img/nike-pegasus-40.jpg', 'Classic running shoes with excellent cushioning', 899.00, 1199.00, 50, 'USA', 1, 1, 1, 1),
('SPT2024002', 'Adidas Ultraboost 23', 'Adidas', 1, '/img/adidas-ultraboost-23.jpg', 'Boost midsole technology for energy return', 1299.00, 1699.00, 35, 'Germany', 1, 1, 1, 0),
('SPT2024003', 'Under Armour HeatGear T-Shirt', 'Under Armour', 6, '/img/ua-heatgear-tshirt.jpg', 'Moisture-wicking fabric keeps you dry', 199.00, 299.00, 100, 'USA', 1, 0, 1, 0),
('SPT2024004', 'TRX Suspension Trainer', 'TRX', 2, '/img/trx-suspension-trainer.jpg', 'Full-body functional training anywhere', 1299.00, 1599.00, 30, 'USA', 1, 1, 0, 0),
('SPT2024005', 'Manduka PRO Yoga Mat', 'Manduka', 2, '/img/manduka-pro-mat.jpg', 'Professional yoga mat with lifetime guarantee', 799.00, 999.00, 80, 'USA', 1, 0, 0, 1);

SELECT 'Database initialization completed successfully!' AS message;