# CustMax Official Backend

## 项目概述

CustMax Official Backend 是一个基于 Spring Boot 3.4.7 的企业级后端应用，提供用户管理、域名管理、支付处理、订阅服务等核心功能。该项目使用现代化的技术栈，包括 Spring AI、MyBatis Plus、Spring Security 等。

## 技术栈

### 核心框架
- **Spring Boot 3.4.7** - 主要应用框架
- **Java 21** - 编程语言
- **Maven** - 项目构建工具

### 数据层
- **MySQL** - 主数据库
- **MyBatis Plus 3.5.11** - ORM 框架
- **MySQL Connector** - 数据库连接器

### 安全认证
- **Spring Security** - 安全框架
- **JWT (jjwt 0.11.5)** - 令牌认证
- **Spring Security Crypto** - 密码加密

### 第三方集成
- **Spring AI 1.0.0** - AI 集成 (OpenAI GPT-3.5-turbo)
- **Stripe 29.2.0** - 支付处理
- **AWS S3 SDK 2.27.13** - 文件存储
- **JSch 0.1.55** - SSH 连接

### 工具库
- **Lombok** - 代码简化
- **Hutool 5.8.38** - 工具库
- **Spring Boot Mail** - 邮件服务
- **SpringDoc OpenAPI 2.5.0** - API 文档

## 项目结构

```
custmax-official-backend/
├── src/main/java/com/custmax/officialsite/
│   ├── OfficialsiteApplication.java          # 应用启动类
│   ├── config/                               # 配置类
│   │   ├── AppConfig.class
│   │   ├── CorsConfig.class
│   │   ├── SecurityConfig.class
│   │   └── SwaggerConfig.class
│   ├── controller/                           # 控制器层
│   │   ├── DomainController.java             # 域名管理
│   │   ├── HelloController.java              # 测试控制器
│   │   ├── InviteCodeController.java         # 邀请码管理
│   │   ├── PaymentController.java            # 支付处理
│   │   ├── SubscriptionController.java       # 订阅管理
│   │   ├── UserController.java               # 用户管理
│   │   ├── UserPaymentController.java        # 用户支付
│   │   └── WebSiteController.java            # 网站管理
│   ├── dto/                                  # 数据传输对象
│   │   ├── domain/                           # 域名相关 DTO
│   │   ├── payment/                          # 支付相关 DTO
│   │   ├── subscription/                     # 订阅相关 DTO
│   │   ├── user/                             # 用户相关 DTO
│   │   └── website/                          # 网站相关 DTO
│   ├── entity/                               # 实体类
│   │   ├── domain/                           # 域名实体
│   │   ├── invite/                           # 邀请码实体
│   │   ├── payment/                          # 支付实体
│   │   ├── SshServer/                        # SSH 服务器实体
│   │   ├── subscription/                     # 订阅实体
│   │   ├── user/                             # 用户实体
│   │   └── website/                          # 网站实体
│   ├── filter/                               # 过滤器
│   ├── mapper/                               # MyBatis 映射器
│   ├── service/                              # 服务层
│   │   ├── domain/                           # 域名服务
│   │   ├── email/                            # 邮件服务
│   │   ├── invite/                           # 邀请码服务
│   │   ├── payment/                          # 支付服务
│   │   ├── subscription/                     # 订阅��务
│   │   ├── user/                             # 用户服务
│   │   └── website/                          # 网站服务
│   └── util/                                 # 工具类
├── src/main/resources/
│   ├── application.yml                       # 主配置文件
│   ├── application-prod.yml                  # 生产环境配置
│   ├── domain_reg.html                       # 域名注册页面模板
│   ├── reset_password.html                   # 密码重置页面模板
│   ├── prompt.txt                            # AI 提示模板
│   └── mapper/
│       └── UserMapper.xml                    # MyBatis 映射文件
└── src/test/java/                            # 测试代码
```

## 核心功能模块

### 1. 用户管理 (User Management)
- 用户注册与��录
- JWT 令牌认证
- 用户信息管理
- 密码重置功能

### 2. 域名管理 (Domain Management)
- 域名注册与管理
- DNS 配置
- 域名状态监控

### 3. 订阅服务 (Subscription Service)
- 订阅计划管理
- 订阅状态跟踪
- 自动续费处理

### 4. 支付处理 (Payment Processing)
- Stripe 支付集成
- 支付历史记录
- 退款处理

### 5. 邀请码系统 (Invite Code System)
- 邀请码生成与管理
- 邀请关系跟踪

### 6. 网站管理 (Website Management)
- 网站创建与配置
- 网站状态监控
- SSH 服务器管理

### 7. AI 集成 (AI Integration)
- OpenAI GPT-3.5-turbo 集成
- AI 辅助功能

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/custmax_official
    username: root
    password: rootpassword
```

### 邮件服务配置
```yaml
spring:
  mail:
    host: smtp.larksuite.com
    port: 465
    username: hello@custmax.com
    password: [已配置]
```

### AI 服务配置
```yaml
spring:
  ai:
    openai:
      model: gpt-3.5-turbo
      api-key: [需要配置]
```

### CORS 配置
```yaml
cors:
  allowed-origins: http://localhost:3000
```

## 环境要求

- **Java**: JDK 21+
- **数据库**: MySQL 8.0+
- **构建工具**: Maven 3.6+
- **操作系统**: Windows/Linux/macOS

## 快速开始

### 1. 环境准备
```bash
# 确保安装了 Java 21
java -version

# 确保安装了 Maven
mvn -version

# 确保 MySQL 服务运行
```

### 2. 数据库初始化
```sql
CREATE DATABASE custmax_official;
-- 运行相关的数据库初始化脚本
```

### 3. 配置文件
复制 `application.yml` 并根据环境调整配置：
- 数据库连接信息
- OpenAI API Key
- Stripe API Key
- 邮件服务配置

### 4. 构建和运行
```bash
# 编译项目
mvn clean compile

# 运行测试
mvn test

# 启动应用
mvn spring-boot:run
```

### 5. 访问应用
- **应用地址**: http://localhost:9001
- **API 文档**: http://localhost:9001/swagger-ui.html

## API 文档

项目集成了 SpringDoc OpenAPI，启动应用后可以通过以下地址访问 API 文档：
- Swagger UI: http://localhost:9001/swagger-ui.html
- OpenAPI JSON: http://localhost:9001/v3/api-docs

## 主要 API 端点

### 用户管理
- `POST /api/users/register` - 用户注册
- `POST /api/users/login` - 用户登录
- `GET /api/users/profile` - 获取用户信息

### 域名管理
- `GET /api/domains` - 获取域名列表
- `POST /api/domains` - 注册新域名
- `PUT /api/domains/{id}` - 更新域名信息

### 订阅服务
- `GET /api/subscriptions` - 获取订阅列表
- `POST /api/subscriptions` - 创建订阅
- `PUT /api/subscriptions/{id}` - 更新订阅

### 支付处理
- `POST /api/payments` - 创建支付
- `GET /api/payments/history` - 支付历史
- `POST /api/payments/webhook` - Stripe 回调

## 安全机制

### 认证方式
- JWT Token 认证
- 基于角色的访问控制 (RBAC)

### 安全配置
- 密码加密存储
- CORS 跨域配置
- SQL 注入防护
- XSS 攻击防护

## 部署说明

### 开发环境
使用 `application.yml` 配置文件，连接本地数据库。

### 生产环境
使用 `application-prod.yml` 配置文件，包含生产环境的安全配置。

### Docker 部署 (推荐)
```dockerfile
# Dockerfile 示例
FROM openjdk:21-jre-slim
COPY target/custmax-official-backend-1.0-SNAPSHOT.jar app.jar
EXPOSE 9001
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

## 监控和日志

### 应用监控
- Spring Boot Actuator (可选配置)
- 应用健康检查端点

### 日志配置
- 使用 Logback 作为日志框架
- 支持不同环境的日志级别配置

## 开发规范

### 代码规范
- 使用 Lombok 减少样板代码
- 统一的异常处理
- RESTful API 设计规范

### 测试规范
- 单元测试覆盖率目标: 80%+
- 集成测试重要业务逻辑

## 常见问题

### Q: 如何配置 OpenAI API Key?
A: 在 `application.yml` 中设置 `spring.ai.openai.api-key` 属性。

### Q: 如何修改数据库连接?
A: 修改 `application.yml` 中的 `spring.datasource` 配置。

### Q: 如何启用 HTTPS?
A: 在 `application.yml` 中配置 SSL 证书路径和端口。

## 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 开启 Pull Request

## 许可证

本项目采用 [MIT License](LICENSE) 许可证。

## 联系方式

- 邮箱: hello@custmax.com
- 项目地址: [GitHub Repository]

---

**注意**: 请确保在生产环境中替换所有默认密码和 API 密钥，并根据实际需求调整配置参数。
