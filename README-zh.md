# CloudApp Framework

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java](https://img.shields.io/badge/Java-8+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-green.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.x-red.svg)](https://maven.apache.org/)

#### [English Version](README.md)

一个统一的多云抽象框架，旨在实现跨不同云服务提供商的无缝应用部署。CloudApp Framework通过统一的SDK封装云厂商服务，消除厂商锁定，支持单一代码库在多云环境中灵活部署。

![CloudApp Framework](docs/assets/CloudApp.jpg)

## 🎯 项目概述

CloudApp Framework解决了云计算中厂商锁定的关键挑战，提供：

- **统一SDK**：为多个云服务提供商提供单一API接口
- **零厂商锁定**：无需代码更改即可在云服务提供商之间切换
- **多云支持**：在不同云平台上部署相同应用程序
- **Spring Boot集成**：原生Spring Boot自动配置和启动器支持
- **全面服务**：完整的PaaS服务抽象，包括存储、消息传递、缓存等

![Framework Architecture](docs/assets/cloudapp-framework-uml.png)

## ✨ 核心特性

### 🔧 核心能力
- **自动配置**：零配置Spring Boot集成
- **动态刷新**：运行时配置更新，无需重启
- **可观测性**：基于OpenTelemetry的内置指标、日志和链路追踪
- **多版本支持**：兼容Java 8+和多个Spring Boot版本

### 🌐 分布式服务
- **对象存储**：跨云服务提供商的统一文件存储
- **消息传递**：支持Kafka和RocketMQ的抽象消息传递
- **缓存**：分布式Redis缓存抽象
- **配置管理**：集中式配置管理
- **搜索**：Elasticsearch全文搜索集成
- **API网关**：服务网关抽象

### 🚀 企业特性
- **微服务治理**：服务发现、负载均衡和流量管理
- **事务管理**：基于Seata的分布式事务支持
- **安全性**：OAuth2认证和授权
- **数据管理**：基于Druid的连接池
- **全局序列**：分布式ID生成
- **邮件服务**：基于Freemarker和Thymeleaf的模板邮件
- **任务调度**：分布式任务调度能力

## 🛠️ 支持的中间件和技术

### **消息传递**
- Apache Kafka
- Apache RocketMQ（包括阿里云RocketMQ）
- Spring Cloud Stream集成

### **存储和数据库**
- Redis（分布式缓存）
- Elasticsearch（搜索和分析）
- Druid（连接池）
- 多种数据库驱动支持

### **微服务和治理**
- Spring Cloud Alibaba（Nacos、Dubbo）
- Spring Cloud（服务发现、配置）
- OpenTelemetry（可观测性）
- Seata（分布式事务）

### **安全和认证**
- OAuth2 / JWT
- Spring Security集成
- RBAC支持

### **开发和监控**
- Spring Boot 2.7.x
- Maven构建系统
- OpenTelemetry指标和链路追踪
- Prometheus集成
- 支持链路关联的Log4j2

## 🚀 快速开始

### 前置条件
- Java 8或更高版本
- Maven 3.x
- Spring Boot 2.7.x

### 1. 添加依赖

```xml
<dependency>
    <groupId>com.alibaba.cloudapp</groupId>
    <artifactId>spring-boot-starter-cloudapp</artifactId>
    <version>1.0.3</version>
</dependency>

<!-- 添加特定云服务提供商实现 -->
<dependency>
    <groupId>com.alibaba.cloudapp</groupId>
    <artifactId>cloudapp-spring-filestore-aliyun</artifactId>
    <version>1.0.3</version>
</dependency>
```

### 2. 配置

```yaml
io:
  cloudapp:
    filestore:
      aliyun:
        endpoint: your-oss-endpoint
        access-key-id: your-access-key
        access-key-secret: your-secret-key
        bucket-name: your-bucket
```

### 3. 使用

```java
@Autowired
private ObjectStorageService objectStorageService;

public void uploadFile(String key, InputStream inputStream) {
    objectStorageService.putObject(key, inputStream);
}
```

## 📋 模块结构

### **核心模块**
- `cloudapp-framework-core` - 核心抽象和工具
  - `cloudapp-base-api` - 统一服务接口
  - `cloudapp-base-utils` - 通用工具
  - `spring-boot-starter-cloudapp` - Spring Boot自动配置

### **PaaS服务**
- `cloudapp-framework-paas` - 平台服务实现
  - `cloudapp-spring-config` - 配置管理
  - `cloudapp-spring-filestore` - 对象存储（OSS、OBS、MinIO）
  - `cloudapp-spring-messaging` - 消息传递（Kafka、RocketMQ）
  - `cloudapp-spring-redis` - 分布式缓存
  - `cloudapp-spring-search` - 搜索服务（Elasticsearch）
  - `cloudapp-spring-apigateway` - API网关集成
  - `cloudapp-spring-scheduler` - 任务调度

### **微服务**
- `cloudapp-framework-microservices` - 微服务治理
  - `cloudapp-microservice-aliyun` - 阿里云微服务支持
  - `cloudapp-microservice-huawei` - 华为云微服务支持

### **可观测性**
- `cloudapp-framework-observabilities` - 监控和可观测性
  - `cloudapp-observabilities-opentelemetry` - OpenTelemetry集成

### **工具和扩展**
- `cloudapp-framework-tools` - 附加工具和集成
  - `cloudapp-datasource-druid` - 数据库连接池
  - `cloudapp-spring-mail` - 邮件服务
  - `cloudapp-spring-oauth2` - OAuth2安全
  - `cloudapp-spring-seata` - 分布式事务
  - `cloudapp-spring-sequence` - 全局序列生成

### **演示和示例**
- `cloudapp-framework-demos` - 每个服务的完整演示应用程序

## 🔧 构建和开发

### 从源代码构建
```bash
git clone https://github.com/alibaba/cloudapp-framework.git
cd cloudapp-framework
mvn clean install
```

### 运行测试
```bash
mvn test
```

### 运行特定演示
```bash
cd cloudapp-framework-demos/cloudapp-filestore-demo/cloudapp-filestore-demo-aliyun
mvn spring-boot:run
```

## 📖 文档

每个服务的详细文档：

- [分布式对象存储设计与使用](docs/分布式对象存储设计与使用.md)
- [分布式消息设计与使用](docs/分布式消息设计与使用.md)
- [分布式缓存设计与使用](docs/分布式缓存设计与使用.md)
- [分布式配置设计与使用](docs/分布式配置设计与使用.md)
- [分布式搜索设计与使用](docs/分布式搜索设计与使用.md)
- [服务网关设计与使用](docs/服务网关设计与使用.md)
- [微服务治理设计与使用](docs/微服务治理设计与使用.md)
- [可观测设计与使用](docs/可观测设计与使用.md)
- [数据源设计与使用](docs/数据源设计与使用.md)
- [线程池设计与使用](docs/线程池设计与使用.md)
- [分布式任务设计与使用](docs/分布式任务设计与使用.md)
- [OAuth2应用设计与使用](docs/OAuth2应用设计与使用.md)
- [事务管理设计与使用](docs/事务管理设计与使用.md)
- [邮件设计与使用](docs/邮件设计与使用.md)
- [全局序列设计与使用](docs/全局序列设计与使用.md)
- [工具类设计与使用](docs/工具类设计与使用.md)
- [异常设计与使用](docs/异常设计与使用.md)


## 📄 许可证

本项目采用Apache License 2.0许可证 - 详情请参阅[LICENSE](LICENSE)文件。
