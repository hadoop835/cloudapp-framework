# CloudApp Framework

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Java](https://img.shields.io/badge/Java-8+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7.18-green.svg)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.x-red.svg)](https://maven.apache.org/)

#### [中文版](README-zh.md)

A unified multi-cloud abstraction framework designed for seamless application deployment across different cloud providers. The CloudApp Framework encapsulates cloud vendor services through a unified SDK, eliminating vendor lock-in and enabling flexible multi-cloud deployments with a single codebase.

![CloudApp Framework](docs/assets/CloudApp.jpg)

## 🎯 Project Overview

The CloudApp Framework addresses the critical challenge of vendor lock-in in cloud computing by providing:

- **Unified SDK**: Single API interface for multiple cloud providers
- **Zero Vendor Lock-in**: Switch between cloud providers without code changes
- **Multi-Cloud Support**: Deploy the same application across different cloud platforms
- **Spring Boot Integration**: Native Spring Boot auto-configuration and starter support
- **Comprehensive Services**: Complete PaaS service abstractions including storage, messaging, caching, and more

![Framework Architecture](docs/assets/cloudapp-framework-uml.png)

## ✨ Key Features

### 🔧 Core Capabilities
- **Automatic Configuration**: Zero-configuration Spring Boot integration
- **Dynamic Refresh**: Runtime configuration updates without restart
- **Observability**: Built-in metrics, logging, and tracing with OpenTelemetry
- **Multi-Version Support**: Compatible with Java 8+ and multiple Spring Boot versions

### 🌐 Distributed Services
- **Object Storage**: Unified file storage across cloud providers
- **Messaging**: Abstract messaging with Kafka and RocketMQ support
- **Caching**: Distributed Redis caching abstraction
- **Configuration**: Centralized configuration management
- **Search**: Elasticsearch integration for full-text search
- **API Gateway**: Service gateway abstraction

### 🚀 Enterprise Features
- **Microservice Governance**: Service discovery, load balancing, and traffic management
- **Transaction Management**: Distributed transaction support with Seata
- **Security**: OAuth2 authentication and authorization
- **Data Management**: Connection pooling with Druid
- **Global Sequences**: Distributed ID generation
- **Email Services**: Template-based email with Freemarker and Thymeleaf
- **Task Scheduling**: Distributed task scheduling capabilities

## ☁️ Supported Cloud Providers

| Service | Alibaba Cloud | Huawei Cloud | MinIO | Kafka | RocketMQ | Redis | Elasticsearch |
|---------|---------------|--------------|-------|-------|----------|-------|--------------|
| **Object Storage** | ✅ OSS | ✅ OBS | ✅ | - | - | - | - |
| **Messaging** | ✅ RocketMQ | 🔧 | - | ✅ | ✅ | - | - |
| **Caching** | ✅ ApsaraDB for Redis | 🔧 | - | - | - | ✅ | - |
| **Configuration** | ✅ ACM/Nacos | 🔧 | - | - | - | - | - |
| **Search** | ✅ Elasticsearch | 🔧 | - | - | - | - | ✅ |
| **API Gateway** | ✅ API Gateway | 🔧 | - | - | - | - | - |
| **Microservices** | ✅ EDAS/Nacos | 🔧 | - | - | - | - | - |
| **Scheduling** | ✅ SchedulerX | 🔧 | - | - | - | - | - |

*Legend: ✅ Fully Supported | 🔧 In Development | - Not Applicable*

## 🛠️ Supported Middleware & Technologies

### **Messaging**
- Apache Kafka
- Apache RocketMQ (including Alibaba Cloud RocketMQ)
- Spring Cloud Stream integration

### **Storage & Databases**
- Redis (distributed caching)
- Elasticsearch (search and analytics)
- Druid (connection pooling)
- Multiple database drivers support

### **Microservices & Governance**
- Spring Cloud Alibaba (Nacos, Dubbo)
- Spring Cloud (Service Discovery, Config)
- OpenTelemetry (observability)
- Seata (distributed transactions)

### **Security & Authentication**
- OAuth2 / JWT
- Spring Security integration
- RBAC support

### **Development & Monitoring**
- Spring Boot 2.7.x
- Maven build system
- OpenTelemetry metrics and tracing
- Prometheus integration
- Log4j2 with trace correlation

## 🚀 Quick Start

### Prerequisites
- Java 8 or higher
- Maven 3.x
- Spring Boot 2.7.x

### 1. Add Dependencies

```xml
<dependency>
    <groupId>com.alibaba.cloudapp</groupId>
    <artifactId>spring-boot-starter-cloudapp</artifactId>
    <version>1.0.3</version>
</dependency>

<!-- Add specific cloud provider implementations -->
<dependency>
    <groupId>com.alibaba.cloudapp</groupId>
    <artifactId>cloudapp-spring-filestore-aliyun</artifactId>
    <version>1.0.3</version>
</dependency>
```

### 2. Configuration

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

### 3. Usage

```java
@Autowired
private ObjectStorageService objectStorageService;

public void uploadFile(String key, InputStream inputStream) {
    objectStorageService.putObject(key, inputStream);
}
```

## 📋 Module Structure

### **Core Modules**
- `cloudapp-framework-core` - Core abstractions and utilities
  - `cloudapp-base-api` - Unified service interfaces
  - `cloudapp-base-utils` - Common utilities
  - `spring-boot-starter-cloudapp` - Spring Boot auto-configuration

### **PaaS Services**
- `cloudapp-framework-paas` - Platform service implementations
  - `cloudapp-spring-config` - Configuration management
  - `cloudapp-spring-filestore` - Object storage (OSS, OBS, MinIO)
  - `cloudapp-spring-messaging` - Messaging (Kafka, RocketMQ)
  - `cloudapp-spring-redis` - Distributed caching
  - `cloudapp-spring-search` - Search services (Elasticsearch)
  - `cloudapp-spring-apigateway` - API gateway integration
  - `cloudapp-spring-scheduler` - Task scheduling

### **Microservices**
- `cloudapp-framework-microservices` - Microservice governance
  - `cloudapp-microservice-aliyun` - Alibaba Cloud microservice support
  - `cloudapp-microservice-huawei` - Huawei Cloud microservice support

### **Observability**
- `cloudapp-framework-observabilities` - Monitoring and observability
  - `cloudapp-observabilities-opentelemetry` - OpenTelemetry integration

### **Tools & Extensions**
- `cloudapp-framework-tools` - Additional tools and integrations
  - `cloudapp-datasource-druid` - Database connection pooling
  - `cloudapp-spring-mail` - Email services
  - `cloudapp-spring-oauth2` - OAuth2 security
  - `cloudapp-spring-seata` - Distributed transactions
  - `cloudapp-spring-sequence` - Global sequence generation

### **Demos & Examples**
- `cloudapp-framework-demos` - Complete demo applications for each service

## 🔧 Build & Development

### Build from Source
```bash
git clone https://github.com/alibaba/cloudapp-framework.git
cd cloudapp-framework
mvn clean install
```

### Run Tests
```bash
mvn test
```

### Run Specific Demo
```bash
cd cloudapp-framework-demos/cloudapp-filestore-demo/cloudapp-filestore-demo-aliyun
mvn spring-boot:run
```

## 📖 Documentation

Comprehensive documentation for each service:

- [Object Storage Design & Usage](docs/分布式对象存储设计与使用.md)
- [Messaging Design & Usage](docs/分布式消息设计与使用.md)
- [Caching Design & Usage](docs/分布式缓存设计与使用.md)
- [Configuration Design & Usage](docs/分布式配置设计与使用.md)
- [Search Design & Usage](docs/分布式搜索设计与使用.md)
- [API Gateway Design & Usage](docs/服务网关设计与使用.md)
- [Microservice Governance](docs/微服务治理设计与使用.md)
- [Observability Design & Usage](docs/可观测设计与使用.md)
- [More Documentation...](README-zh.md)

## 🤝 Contributing

We welcome contributions! Please read our [Contributing Guide](CONTRIBUTING.md) for details on our code of conduct and the process for submitting pull requests.

## 📄 License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## 🔗 Related Projects

- [Spring Cloud Alibaba](https://github.com/alibaba/spring-cloud-alibaba)
- [Spring Boot](https://github.com/spring-projects/spring-boot)
- [OpenTelemetry](https://github.com/open-telemetry/opentelemetry-java)

## 📞 Support

- 🐛 [Issues](https://github.com/alibaba/cloudapp-framework/issues)
- 💬 [Discussions](https://github.com/alibaba/cloudapp-framework/discussions)
- 📧 [Email Support](mailto:support@example.com)

---

**Made with ❤️ by the CloudApp Framework Team**