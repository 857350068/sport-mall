# 运动装备商城项目总结

## 项目概述

运动装备商城是一个基于Spring Boot + Thymeleaf + MySQL技术栈的企业级电商平台，专注于为运动爱好者提供专业的运动装备。项目采用前后端不分离的架构，实现了完整的电商功能模块。

## 技术栈

- **后端**: Spring Boot 3.2.0, Spring Security 6.x, Spring Data JPA, Hibernate
- **前端**: Thymeleaf模板引擎, Bootstrap 5, jQuery
- **数据库**: MySQL 8.0
- **构建工具**: Maven
- **运行环境**: JDK 17

## 功能模块

### 1. 用户系统
- 用户注册、登录、找回密码、修改密码
- 双角色系统：管理员和普通用户
- 用户信息管理（CRUD）
- 逻辑删除（非物理删除）
- 管理员不能删除自己

### 2. 商品管理
- 商品增删改查
- 商品上架/下架
- 批量操作（批量上架/下架）
- 模糊查询和条件查询（商品名称+分类）
- 商品分类管理
- 品牌管理

### 3. 订单系统
- 订单增删改查
- 订单批量操作
- 用户下单流程
- 订单一小时未支付自动取消
- 订单状态管理

### 4. 支付系统
- 模拟支付流程
- 支付状态更新
- 订单支付完成处理

### 5. 营销系统
- 优惠券管理（增删改查、发放、抵扣）
- 促销活动管理（折扣、满减等规则）
- 退货功能

## 页面结构

### 前台页面

#### 首页
- `src/main/resources/templates/index.html`
- 展示热门分类和推荐商品
- 搜索功能
- 导航栏和页脚

#### 用户中心
- `src/main/resources/templates/user/profile.html` - 个人信息
- `src/main/resources/templates/user/orders.html` - 我的订单
- `src/main/resources/templates/user/coupons.html` - 我的优惠券

#### 商品中心
- `src/main/resources/templates/product/list.html` - 商品列表
- `src/main/resources/templates/product/detail.html` - 商品详情

#### 购物车
- `src/main/resources/templates/cart/list.html` - 购物车列表

#### 分类浏览
- `src/main/resources/templates/category/list.html` - 分类浏览

#### 结账流程
- `src/main/resources/templates/checkout/index.html` - 订单确认
- `src/main/resources/templates/payment/index.html` - 支付页面

### 后台管理页面

#### 首页
- `src/main/resources/templates/admin/index.html` - 管理后台首页

#### 用户管理
- `src/main/resources/templates/admin/user/list.html` - 用户列表
- `src/main/resources/templates/admin/user/detail.html` - 用户详情
- `src/main/resources/templates/admin/user/form.html` - 用户编辑

#### 商品管理
- `src/main/resources/templates/admin/product/list.html` - 商品列表
- `src/main/resources/templates/admin/product/detail.html` - 商品详情
- `src/main/resources/templates/admin/product/form.html` - 商品编辑

#### 订单管理
- `src/main/resources/templates/admin/order/list.html` - 订单列表
- `src/main/resources/templates/admin/order/detail.html` - 订单详情

#### 分类管理
- `src/main/resources/templates/admin/category/list.html` - 分类列表
- `src/main/resources/templates/admin/category/detail.html` - 分类详情
- `src/main/resources/templates/admin/category/form.html` - 分类编辑

#### 品牌管理
- `src/main/resources/templates/admin/brand/list.html` - 品牌列表
- `src/main/resources/templates/admin/brand/detail.html` - 品牌详情
- `src/main/resources/templates/admin/brand/form.html` - 品牌编辑

#### 促销活动
- `src/main/resources/templates/admin/promotion/list.html` - 活动列表
- `src/main/resources/templates/admin/promotion/detail.html` - 活动详情
- `src/main/resources/templates/admin/promotion/form.html` - 活动编辑

#### 优惠券
- `src/main/resources/templates/admin/coupon/list.html` - 优惠券列表
- `src/main/resources/templates/admin/coupon/detail.html` - 优惠券详情
- `src/main/resources/templates/admin/coupon/form.html` - 优惠券编辑

## 数据库设计

### 核心实体类
1. `User` - 用户实体
2. `Category` - 商品分类
3. `Brand` - 品牌
4. `Product` - 商品
5. `Order` - 订单
6. `OrderItem` - 订单项
7. `Coupon` - 优惠券
8. `Promotion` - 促销活动
9. `CartItem` - 购物车项

### 枚举类型
1. `UserRole` - 用户角色
2. `UserStatus` - 用户状态
3. `ProductStatus` - 商品状态
4. `OrderStatus` - 订单状态
5. `CouponType` - 优惠券类型
6. `CouponStatus` - 优惠券状态
7. `PromotionType` - 促销类型
8. `PromotionStatus` - 促销状态

## 后端架构

### 控制器层 (Controller)
- `AuthController` - 认证相关接口
- `UserController` - 用户相关接口
- `CategoryController` - 分类相关接口
- `BrandController` - 品牌相关接口
- `ProductController` - 商品相关接口
- `OrderController` - 订单相关接口
- `CartController` - 购物车相关接口
- `CouponController` - 优惠券相关接口
- `PromotionController` - 促销活动相关接口
- `AdminController` - 管理后台相关接口

### 服务层 (Service)
- `UserService` - 用户服务
- `CategoryService` - 分类服务
- `BrandService` - 品牌服务
- `ProductService` - 商品服务
- `OrderService` - 订单服务
- `CartService` - 购物车服务
- `CouponService` - 优惠券服务
- `PromotionService` - 促销服务

### 数据访问层 (Repository)
- `UserRepository` - 用户数据访问
- `CategoryRepository` - 分类数据访问
- `BrandRepository` - 品牌数据访问
- `ProductRepository` - 商品数据访问
- `OrderRepository` - 订单数据访问
- `CartItemRepository` - 购物车数据访问
- `CouponRepository` - 优惠券数据访问
- `PromotionRepository` - 促销数据访问

## 安全配置

- 基于Spring Security的认证授权
- 用户密码加密存储
- 角色权限控制
- 登录状态管理

## 部署说明

1. 确保已安装JDK 17和MySQL 8.0
2. 创建数据库并执行初始化脚本
3. 修改`application.yml`配置文件中的数据库连接信息
4. 使用Maven构建项目：`mvn clean package`
5. 运行项目：`java -jar target/sport-mall-0.0.1-SNAPSHOT.jar`

## 项目特点

1. **完整功能**: 涵盖电商核心功能模块
2. **响应式设计**: 基于Bootstrap 5的响应式页面
3. **权限控制**: 完善的用户角色权限管理
4. **数据安全**: 逻辑删除、数据验证
5. **易于扩展**: 模块化设计，便于功能扩展

## 后续优化建议

1. 集成Redis缓存提升性能
2. 添加文件上传功能支持商品图片管理
3. 集成消息队列处理异步任务
4. 添加日志系统便于问题排查
5. 集成Elasticsearch实现商品搜索功能