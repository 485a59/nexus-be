# Nexus 课程资源管理系统

Nexus是一个专门用于管理课程资源的系统，支持多种类型的课程资源上传、管理和分享。

## 功能特点

### 文件管理
- 支持大文件分片上传
- 支持断点续传
- 支持秒传（基于文件指纹）
- 支持多种存储方式（本地存储、云存储等）

### 资源类型
- 教材（Textbook）
  - 支持ISBN、出版社、作者等元数据
  - 支持多版本管理
- 视频（Video）
  - 支持在线预览
  - 支持讲师信息、封面图等元数据
- 幻灯片（Slide）
  - 支持作者信息
  - 支持在线预览
- 软件（Software）
  - 支持版本管理
  - 包含系统要求、许可证等信息

### 权限控制
- 基于Token的认证机制
- 细粒度的资源访问控制
- 支持资源分享和提取码

## 技术栈

### 后端
- Spring Boot
- MyBatis-Plus
- MySQL
- Redis (缓存)
- ElasticSearch (全文检索)

### 存储
- 支持多种存储方式
  - 本地文件系统
  - 对象存储（如阿里云OSS）
- 文件分片上传
- 文件指纹识别

## 项目结构
```
├── nexus-admin/ # 管理后台
├── nexus-common/ # 公共模块
├── nexus-domain/ # 领域模块
│ ├── component/ # 业务组件
│ └── service/ # 业务服务
├── nexus-infrastructure/ # 基础设施
│ ├── mapper/ # 数据访问
│ └── model/ # 数据模型
└── nexus-security/ # 安全模块
```


## 快速开始

### 环境要求
- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 配置说明
1. 数据库配置
```yaml
spring:
    datasource:
        url: jdbc:mysql://localhost:3306/nexus
        username: your_username
        password: your_password
```     

2. Redis配置
```yaml
spring:
    redis:
        host: localhost
        port: 6379
```
## 贡献指南

欢迎提交Issue和Pull Request。在提交PR之前，请确保：
1. 代码符合项目规范
2. 添加必要的测试
3. 更新相关文档

## 许可证

[MIT License](LICENSE)

