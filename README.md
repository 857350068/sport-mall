# 运动装备专业商城系统

## 项目简介

运动装备专业商城是一个基于Spring Boot + LayuiMini的全栈电商项目，专注于运动装备销售。项目采用前后端分离架构，支持用户购物、管理员后台管理等完整功能。

## 技术栈

### 后端技术
- **Spring Boot 2.7.14** - 主框架
- **Spring Security** - 安全框架
- **MyBatis Plus 3.5.3.1** - ORM框架
- **MySQL 8.0** - 数据库
- **Redis** - 缓存
- **JWT** - 身份验证
- **HuTool** - 工具类库
- **Maven** - 依赖管理

### 前端技术
- **LayuiMini** - UI框架
- **ECharts** - 图表库
- **jQuery** - JavaScript库
- **HTML5/CSS3** - 页面技术

## 项目结构

```
d:\Sport-Mall\
├── src\main\java\com\sportmall\          # Java源码
│   ├── common\                           # 公共类
│   │   ├── base\BaseEntity.java         # 基础实体类
│   │   ├── exception\BusinessException.java # 业务异常
│   │   └── result\                      # 统一响应结果
│   ├── config\                          # 配置类
│   │   ├── SecurityConfig.java         # 安全配置
│   │   ├── JwtProperties.java          # JWT配置
│   │   ├── MetaObjectHandlerConfig.java # 字段自动填充
│   │   └── GlobalExceptionHandler.java # 全局异常处理
│   ├── controller\                      # 控制器
│   │   ├── UserController.java         # 用户控制器
│   │   ├── ProductController.java      # 商品控制器
│   │   ├── CategoryController.java     # 分类控制器
│   │   ├── CartController.java         # 购物车控制器
│   │   ├── OrderController.java        # 订单控制器
│   │   ├── AdminController.java        # 管理员控制器
│   │   └── StatisticsController.java   # 统计控制器
│   ├── dto\                            # 数据传输对象
│   ├── entity\                         # 实体类
│   ├── mapper\                         # Mapper接口
│   ├── service\                        # 服务接口
│   ├── service\impl\                   # 服务实现
│   ├── utils\                          # 工具类
│   │   └── JwtUtil.java               # JWT工具类
│   └── vo\                            # 视图对象
├── src\main\resources\
│   ├── mapper\                        # MyBatis映射文件
│   └── application.yml               # 应用配置
├── frontend\                         # 前端代码
│   ├── admin\                       # 管理端页面
│   │   ├── login.html              # 管理员登录
│   │   ├── index.html              # 管理后台首页
│   │   ├── dashboard.html          # 数据统计
│   │   └── users.html              # 用户管理
│   ├── static\                     # 静态资源
│   │   ├── css\                   # 样式文件
│   │   ├── js\                    # JavaScript文件
│   │   ├── images\                # 图片资源
│   │   └── layui\                 # LayUI框架
│   ├── index.html                 # 用户端首页
│   ├── login.html                # 用户登录
│   ├── register.html             # 用户注册
│   └── reset-password.html       # 找回密码
├── sql\                          # 数据库脚本
│   └── database_optimize.sql     # 数据库优化脚本
└── pom.xml                       # Maven配置
```

## 核心功能

### 用户端功能
- ✅ 用户注册、登录、找回密码
- ✅ 商品浏览、搜索、分类筛选
- ✅ 购物车管理（增删改查）
- ✅ 订单管理（创建、支付、取消、确认收货）
- ✅ 个人信息管理

### 管理端功能
- ✅ 管理员登录认证
- ✅ 数据统计dashboard（用户、商品、订单、销售额）
- ✅ 用户管理（查看、编辑、禁用、删除）
- ✅ 商品管理（分类管理、商品CRUD）
- ✅ 订单管理（查看、发货、状态管理）
- ✅ 权限控制（基于角色的访问控制）

## 数据库设计

### 核心表结构
- `user` - 用户表
- `admin` - 管理员表
- `product` - 商品表
- `category` - 商品分类表
- `cart` - 购物车表
- `order_info` - 订单主表
- `order_item` - 订单详情表
- `merchant` - 商家表
- `wallet` - 用户钱包表

## 环境要求

- **JDK**: 1.8+
- **MySQL**: 8.0+
- **Redis**: 6.0+ (可选)
- **Maven**: 3.6+
- **IDE**: IntelliJ IDEA (推荐)

## 快速开始

### 1. 克隆项目
```bash
git clone <项目地址>
cd Sport-Mall
```

### 2. 数据库配置
1. 创建MySQL数据库 `sport_mall`
2. 导入数据库脚本（见文档中的SQL）
3. 执行优化脚本 `sql/database_optimize.sql`

### 3. 修改配置
编辑 `src/main/resources/application.yml`：
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sport_mall?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: 123456  # 修改为你的数据库密码
```

### 4. 启动后端
```bash
mvn spring-boot:run
```
后端服务将在 http://localhost:8080 启动

### 5. 启动前端
直接在浏览器中打开前端页面：
- 用户端：`frontend/index.html`
- 管理端：`frontend/admin/login.html`

## 默认账号

### 管理员账号
- 用户名：`admin`
- 密码：`111111`

### 测试用户账号
- 用户名：`testuser`
- 密码：`123456`

## API接口

### 用户相关
- `POST /api/user/login` - 用户登录
- `POST /api/user/register` - 用户注册
- `POST /api/user/send-reset-code` - 发送重置密码验证码
- `POST /api/user/reset-password` - 重置密码

### 商品相关
- `GET /api/product/list` - 商品列表
- `GET /api/product/detail/{id}` - 商品详情
- `GET /api/product/hot` - 热门商品
- `GET /api/category/tree` - 分类树

### 购物车相关
- `POST /api/cart/add` - 添加到购物车
- `GET /api/cart/list` - 购物车列表
- `PUT /api/cart/quantity` - 更新商品数量

### 订单相关
- `POST /api/order/create` - 创建订单
- `GET /api/order/list` - 订单列表
- `POST /api/order/pay/{id}` - 支付订单

## 企业级特性

### 安全性
- JWT令牌认证
- 密码BCrypt加密
- 防SQL注入
- CSRF保护
- 接口权限控制

### 性能优化
- Redis缓存热点数据
- 数据库索引优化
- 分页查询
- 连接池配置

### 数据一致性
- 事务管理
- 乐观锁并发控制
- 逻辑删除
- 字段自动填充

### 代码质量
- 统一异常处理
- 参数校验
- 日志记录
- 代码注释

## 部署说明

### 1. 打包项目
```bash
mvn clean package -DskipTests
```

### 2. 部署jar包
```bash
java -jar target/sport-mall-1.0.0.jar
```

### 3. nginx配置（前端）
```nginx
server {
    listen 80;
    server_name yourdomain.com;
    
    location / {
        root /path/to/frontend;
        index index.html;
        try_files $uri $uri/ /index.html;
    }
    
    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }
}
```

## 开发指南

### 1. 新增功能模块
1. 创建实体类（Entity）
2. 创建Mapper接口和XML
3. 创建服务接口和实现
4. 创建控制器
5. 添加前端页面

### 2. 代码规范
- 类名使用PascalCase
- 方法名使用camelCase
- 常量使用UPPER_CASE
- 重要业务逻辑需添加注释

### 3. 数据库规范
- 表名使用下划线命名
- 字段名使用下划线命名
- 必须包含create_time、update_time、deleted字段
- 外键字段以_id结尾

## 常见问题

### Q: 启动时提示数据库连接失败
A: 检查MySQL服务是否启动，数据库配置是否正确

### Q: 前端无法访问后端接口
A: 检查跨域配置，确认后端服务已启动

### Q: 管理员无法登录
A: 确认数据库中admin表有数据，密码为明文或BCrypt加密

## 更新日志

### v1.0.0 (2025-09-20)
- ✅ 完成基础架构搭建
- ✅ 实现用户注册登录功能
- ✅ 实现商品管理功能
- ✅ 实现购物车功能
- ✅ 实现订单管理功能
- ✅ 实现管理员后台
- ✅ 完成前端页面开发

## 贡献指南

1. Fork 项目
2. 创建功能分支
3. 提交更改
4. 推送到分支
5. 创建 Pull Request

## 许可证

本项目采用 MIT 许可证。

## 联系方式

如有问题请联系开发团队。

---

**运动装备专业商城系统 © 2025**#   s p o r t - m a l l  
 