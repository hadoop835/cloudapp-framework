# Design and Usage Guide

#### [中文版](README_zh.md)

# Design Goals

The goal of the CloudApp framework is to design a unified  SDK  that encapsulates cloud vendor services, decoupling the programming differences among various cloud vendors' PaaS services, thereby achieving flexible delivery across different vendors with a single codebase.

![alt text](README/assets/CloudApp.jpg)

From an application perspective, the current package structure includes both general  starter  dependencies and implementation dependencies corresponding to specific cloud products during the application building process, as illustrated below:

![alt text](README/assets/cloudapp-framework-uml.png)

In the unified SDK implementation of the framework, there are platform services such as distributed object storage, distributed messaging, distributed caching, and distributed configuration. It also includes common services like global sequences and data sources, as well as microservice governance, observability, application server aliee, and AI platforms. In addition to unifying the SDK, the framework provides dynamic refresh and monitoring capabilities for each service. Moreover, the framework supports multiple JDK versions and Spring Boot.
