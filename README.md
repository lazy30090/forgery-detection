# README

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot) [![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/) [![License](https://img.shields.io/badge/License-MIT-blue.svg)](https://claude.ai/chat/LICENSE) [![Docker](https://img.shields.io/badge/Docker-Supported-2496ED.svg)](https://www.docker.com/)

## 简介

2025服创赛题A21伪造新闻检测平台后端练习，AI辅助完成

所有功能都通过测试，过了一遍整体开发流程，保证正式开发效率

采用 Spring Boot 3 微服务架构

## 文档 docs/

开发文档  服创A21后端项目开发日志.md

API文档

ml接口文档

##  快速开始

### 前置要求

- JDK 17+
- Maven 3.8+
- Docker & Docker Compose
- MySQL 8.0（本地开发可选）

#### 克隆项目

```bash
git clone https://github.com/lazy30090/forgery-detection.git
cd forgery-detection
```

### Docker 一键部署

#### 1. 构建并启动所有服务

```bash
docker compose up -d --build
```

该命令将自动启动：

- MySQL 数据库（端口 3307）
- Redis 缓存（端口 6380）
- Python 算法服务（端口 5000）
- Java 后端应用（端口 8080）
- Nginx 网关（端口 80）

#### 2. 停止服务

```bash
docker compose down
```

------

## 🛠️ 技术栈

### 后端技术

| 技术              | 版本   | 说明           |
| ----------------- | ------ | -------------- |
| Spring Boot       | 3.5.5  | 核心框架       |
| MyBatis-Plus      | 3.5.6  | ORM 增强框架   |
| MySQL             | 8.0    | 关系型数据库   |
| Redis             | 7.0    | 缓存中间件     |
| Sa-Token          | 1.37.0 | 轻量级权限认证 |
| Knife4j           | 4.5.0  | API 文档生成   |
| Lombok            | -      | 代码简化工具   |
| Spring Validation | -      | 参数校验框架   |

### 算法服务

| 技术    | 版本   | 说明            |
| ------- | ------ | --------------- |
| Python  | 3.9    | 算法运行环境    |
| FastAPI | Latest | 高性能 API 框架 |
| Uvicorn | Latest | ASGI 服务器     |

### 基础设施

| 技术           | 版本   | 说明              |
| -------------- | ------ | ----------------- |
| Docker         | Latest | 容器化平台        |
| Docker Compose | 3.8    | 容器编排工具      |
| Nginx          | Stable | 反向代理/静态服务 |
| Maven          | 3.8+   | 项目构建工具      |

------

## 📦 项目结构

```
forgery-detection/
├── src/main/java/com/juntong/forgerydetection/
│   ├── config/              # 配置类
│   │   ├── MybatisPlusConfig.java      # 分页插件配置
│   │   ├── RedisConfig.java            # Redis 序列化配置
│   │   ├── RestTemplateConfig.java     # HTTP 客户端配置
│   │   ├── SwaggerConfig.java          # API 文档配置
│   │   └── WebMvcConfig.java           # CORS、拦截器、静态资源
│   ├── controller/          # 控制层
│   │   ├── AuthController.java         # 用户认证接口
│   │   ├── NewsController.java         # 新闻 CRUD 接口
│   │   ├── DetectionController.java    # 检测任务接口
│   │   ├── StatisticsController.java   # 数据统计接口
│   │   └── CommonController.java       # 文件上传接口
│   ├── service/             # 业务层
│   │   ├── impl/
│   │   │   ├── NewsServiceImpl.java           # 新闻业务实现
│   │   │   ├── DetectionTaskServiceImpl.java  # 检测业务实现
│   │   │   └── UserServiceImpl.java           # 用户业务实现
│   │   └── ...
│   ├── mapper/              # 持久层
│   ├── entity/              # 实体类
│   │   ├── News.java                   # 新闻实体
│   │   ├── DetectionTask.java          # 检测任务实体
│   │   └── User.java                   # 用户实体
│   ├── common/              # 通用组件
│   │   ├── ApiResponse.java            # 统一响应体
│   │   └── ResultCode.java             # 状态码枚举
│   ├── enums/               # 业务枚举
│   ├── aspect/              # AOP 切面
│   │   └── WebLogAspect.java           # 请求日志切面
│   └── exception/           # 异常处理
│       └── GlobalExceptionHandler.java  # 全局异常拦截
│
├── src/main/resources/
│   ├── application.properties          # 通用配置
│   ├── application-dev.properties      # 开发环境配置
│   ├── application-prod.properties     # 生产环境配置
│   └── static/                         # 静态资源（含前端页面）
│
├── ml_service/              # Python 算法服务
│   ├── mock_ml_server.py               # 模拟检测服务
│   ├── Dockerfile                      # Python 镜像构建
│   └── requirements.txt                # Python 依赖
│
├── nginx/                   # Nginx 配置
│   └── nginx.conf                      # 反向代理配置
│
├── sql/                     # 数据库脚本
│   └── init.sql                        # 初始化 SQL（含 2 万条数据）
│
├── docker-compose.yml       # 容器编排配置
├── Dockerfile               # Java 后端镜像构建
└── pom.xml                  # Maven 依赖配置
```

------

## 📖 核心功能模块

### 1. 用户认证模块 (AuthController)

- **登录接口** `POST /api/auth/login`
  - 基于 Sa-Token 的无状态认证
  - 返回 JWT 格式 Token
  - 支持角色区分（admin/user）
- **注销接口** `POST /api/auth/logout`
  - 清除服务端会话
  - Token 失效处理
- **状态查询** `GET /api/auth/isLogin`
  - 验证当前登录状态

### 2. 新闻管理模块 (NewsController)

- **分页查询** `GET /api/news/list`
  - 支持标题模糊搜索
  - 标签筛选（谣言/事实）
  - 按创建时间倒序排列
  - MyBatis-Plus 分页插件
- **详情查询** `GET /api/news/{id}`
  - 根据主键获取新闻全文
- **新增新闻** `POST /api/news/add`
  - 参数校验（`@Valid` + Validation）
  - 自动标记为"待检测"
  - 来源标记为"用户上传"

### 3. 伪造检测模块 (DetectionController)

- **提交检测任务** `POST /api/detect/submit`
  - 接收新闻 ID
  - 异步调用 Python 算法服务
  - 返回任务 ID 供轮询查询
- **查询检测结果** `GET /api/detect/result/{taskId}`
  - 轮询式结果获取
  - 状态码：0-待检测, 1-检测中, 2-完成
- **前端直连检测** `POST /api/detect`
  - 文件上传 + 同步返回结果
  - 适配前端演示需求

### 4. 数据统计模块 (StatisticsController)

- 仪表盘数据

  ```
  GET /api/stats/dashboard
  ```

  - 新闻总数统计
  - 谣言/事实分布
  - 谣言占比计算
  - Redis 缓存优化（10分钟过期）

### 5. 文件上传模块 (CommonController)

- 图片上传

  ```
  POST /api/common/upload
  ```

  - UUID 文件名防冲突
  - 自动创建目录
  - 返回可访问 URL

------

## 🗄️ 数据库设计

### sys_news（新闻表）

| 字段        | 类型         | 说明                         |
| ----------- | ------------ | ---------------------------- |
| id          | BIGINT       | 主键                         |
| news_id     | VARCHAR(50)  | 原始数据集 ID                |
| title       | VARCHAR(200) | 新闻标题                     |
| content     | TEXT         | 正文内容                     |
| pic_url     | VARCHAR(500) | 图片 URL                     |
| label       | VARCHAR(20)  | 真伪标签（谣言/事实/待检测） |
| data_source | TINYINT      | 来源（1-数据集, 2-用户上传） |
| create_time | DATETIME     | 创建时间                     |

**索引设计**：

- PRIMARY KEY (id)
- INDEX idx_label (label)
- INDEX idx_create_time (create_time)

### biz_detection_task（检测任务表）

| 字段         | 类型         | 说明                               |
| ------------ | ------------ | ---------------------------------- |
| id           | BIGINT       | 任务 ID                            |
| news_id      | BIGINT       | 关联新闻 ID                        |
| status       | TINYINT      | 状态（0-待检测, 1-检测中, 2-完成） |
| result_label | VARCHAR(20)  | 检测结论                           |
| confidence   | DECIMAL(5,4) | 置信度（0~1）                      |
| explanation  | TEXT         | AI 解释报告                        |
| create_time  | DATETIME     | 创建时间                           |
| update_time  | DATETIME     | 更新时间                           |

### sys_user（用户表）

| 字段        | 类型         | 说明               |
| ----------- | ------------ | ------------------ |
| id          | BIGINT       | 用户 ID            |
| username    | VARCHAR(50)  | 登录账号           |
| password    | VARCHAR(100) | 密码               |
| role        | VARCHAR(20)  | 角色（admin/user） |
| create_time | DATETIME     | 创建时间           |