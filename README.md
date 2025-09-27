# 运动装备商城系统 (SportMall)

## 项目概述

运动装备商城系统是一个基于Spring Boot 3.2.0的企业级电子商务平台，专注于运动装备销售。系统采用现代化的技术架构和运动风格的UI设计，提供完整的电商功能。

## 技术栈

### 后端技术
- **Java 17** - 主要开发语言
- **Spring Boot 3.2.0** - 核心框架
- **Spring Security 6.x** - 安全认证框架
- **Spring Data JPA** - 数据访问层
- **Hibernate** - ORM框架
- **MySQL 8.0** - 主数据库
- **Maven** - 项目构建工具

### 前端技术
- **Thymeleaf** - 模板引擎
- **Bootstrap 5** - CSS框架
- **LayUI** - 管理后台UI框架
- **jQuery 3.5.1** - JavaScript库
- **运动风格CSS** - 自定义样式系统

## 项目结构

```
SportMalltest/
├── src/
│   ├── main/
│   │   ├── java/com/sportmall/
│   │   │   ├── common/           # 通用工具类
│   │   │   ├── config/           # 配置类
│   │   │   │   ├── SecurityConfig.java      # Spring Security配置
│   │   │   │   ├── WebConfig.java          # Web配置
│   │   │   │   ├── WebMvcConfig.java       # MVC配置
│   │   │   │   └── CorsConfig.java         # 跨域配置
│   │   │   ├── controller/       # 控制器层
│   │   │   │   ├── AuthController.java     # 认证控制器
│   │   │   │   ├── ProductController.java  # 商品控制器
│   │   │   │   ├── OrderController.java    # 订单控制器
│   │   │   │   └── AdminController.java    # 管理员控制器
│   │   │   ├── dto/              # 数据传输对象
│   │   │   ├── entity/           # 实体类
│   │   │   │   ├── User.java               # 用户实体
│   │   │   │   ├── Product.java            # 商品实体
│   │   │   │   ├── Order.java              # 订单实体
│   │   │   │   ├── Category.java           # 分类实体
│   │   │   │   ├── Brand.java              # 品牌实体
│   │   │   │   ├── CartItem.java           # 购物车项实体
│   │   │   │   ├── Coupon.java             # 优惠券实体
│   │   │   │   ├── Promotion.java          # 促销活动实体
│   │   │   │   └── ...                     # 其他实体
│   │   │   ├── enums/            # 枚举类
│   │   │   ├── repository/       # 数据访问层
│   │   │   ├── security/         # 安全相关类
│   │   │   ├── service/          # 业务逻辑层
│   │   │   └── SportMallApplication.java   # 启动类
│   │   └── resources/
│   │       ├── application.yml    # 配置文件
│   │       ├── static/           # 静态资源
│   │       │   ├── css/          # 样式文件
│   │       │   │   └── sport-style.css     # 运动风格样式
│   │       │   ├── js/           # JavaScript文件
│   │       │   ├── images/       # 图片资源
│   │       │   └── ...
│   │       └── templates/        # Thymeleaf模板
│   │           ├── login.html               # 登录页面
│   │           ├── register.html            # 注册页面
│   │           ├── index.html               # 首页
│   │           ├── admin/                   # 管理后台页面
│   │           ├── product/                 # 商品相关页面
│   │           ├── order/                   # 订单相关页面
│   │           └── ...
│   └── test/                     # 测试代码
├── database/                     # 数据库相关文件
├── logs/                        # 日志文件
├── pom.xml                      # Maven配置文件
└── README.md                    # 项目说明文档
```

## 核心功能模块

### 1. 用户管理模块
- 用户注册/登录
- 用户信息管理
- 密码重置
- 权限控制

### 2. 商品管理模块
- 商品分类管理
- 品牌管理
- 商品信息管理
- 商品搜索

### 3. 订单管理模块
- 购物车功能
- 订单创建
- 订单状态管理
- 订单查询

### 4. 营销模块
- 优惠券系统
- 促销活动
- 会员等级

### 5. 管理后台模块
- 系统管理
- 数据统计
- 内容管理

## 数据库配置

### 连接信息
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/sportmall?useUnicode=true&characterEncoding=UTF-8&serverTimezone=Asia/Shanghai
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver
```

### 主要数据表
- `users` - 用户表
- `products` - 商品表
- `categories` - 分类表
- `brands` - 品牌表
- `orders` - 订单表
- `order_items` - 订单明细表
- `cart_items` - 购物车表
- `coupons` - 优惠券表
- `promotions` - 促销活动表

## 启动指南

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- IDE (推荐IntelliJ IDEA)

### 启动步骤

1. **克隆项目**
   ```bash
   git clone [项目地址]
   cd SportMalltest
   ```

2. **配置数据库**
   - 创建数据库：`CREATE DATABASE sportmall`
   - 导入数据库脚本（如有）
   - 修改`application.yml`中的数据库连接信息

3. **安装依赖**
   ```bash
   mvn clean install
   ```

4. **启动应用**
   ```bash
   mvn spring-boot:run
   ```
   或者在IDE中运行`SportMallApplication.java`

5. **访问应用**
   - 前台地址：http://localhost:8080
   - 管理后台：http://localhost:8080/admin

### 默认账户
- 管理员：admin / 123456
- 测试用户：test / 123456

## 开发规范

### 代码规范
- 使用Java标准命名规范
- 类名使用PascalCase
- 方法名和变量名使用camelCase
- 常量使用UPPER_CASE
- 包名使用小写字母

### 项目规范
- Controller层负责请求处理
- Service层负责业务逻辑
- Repository层负责数据访问
- 统一异常处理
- 统一返回结果格式

### 前端规范
- 使用运动风格色彩体系
- 响应式设计
- 语义化HTML标签
- 模块化CSS
- 合理的用户体验设计

## 样式系统

### 色彩体系
```css
:root {
  --primary-color: #1a73e8;        /* 活力蓝 */
  --secondary-color: #34a853;      /* 运动绿 */
  --accent-color: #ff6b35;         /* 能量橙 */
  --gradient-hero: linear-gradient(135deg, #1a73e8 0%, #34a853 100%);
}
```

### 组件库
- 表单组件
- 按钮组件
- 卡片组件
- 导航组件
- 模态框组件

## API接口

### 认证接口
- `POST /api/auth/login` - 用户登录
- `POST /api/auth/register` - 用户注册
- `POST /api/auth/logout` - 用户登出

### 商品接口
- `GET /api/products` - 获取商品列表
- `GET /api/products/{id}` - 获取商品详情
- `POST /api/products` - 创建商品
- `PUT /api/products/{id}` - 更新商品

### 订单接口
- `GET /api/orders` - 获取订单列表
- `POST /api/orders` - 创建订单
- `GET /api/orders/{id}` - 获取订单详情

## 部署说明

### 开发环境
```bash
mvn spring-boot:run
```

### 生产环境
```bash
mvn clean package
java -jar target/sportmall-1.0.0.jar
```

### Docker部署
```dockerfile
FROM openjdk:17-jre-slim
COPY target/sportmall-1.0.0.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 常见问题

### 1. 启动失败
- 检查JDK版本是否为17+
- 检查数据库连接是否正常
- 检查端口8080是否被占用

### 2. 静态资源404
- 确认文件放在`src/main/resources/static/`目录
- 检查Spring Security配置是否正确
- 清理浏览器缓存

### 3. 登录重定向循环
- 检查SecurityConfig配置
- 确认登录页面路径正确
- 检查用户认证逻辑

## 更新日志

### v1.0.0 (2024-09-26)
- 初始版本发布
- 完成基础功能模块
- 实现运动风格UI设计
- 集成Spring Security认证

## 贡献指南

1. Fork项目
2. 创建特性分支
3. 提交改动
4. 推送到分支
5. 创建Pull Request

## 许可证

本项目采用MIT许可证，详见LICENSE文件。

## 联系方式

- 项目维护者：[维护者姓名]
- 邮箱：[联系邮箱]
- 项目地址：[GitHub地址]

---

**注意事项**：
1. 本项目仅供学习和开发使用
2. 生产环境使用前请进行充分测试
3. 定期更新依赖版本以保证安全性
4. 遵循相关法律法规和隐私保护要求