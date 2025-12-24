# Handwritten Series（手写系列）

一个用于学习和理解 Java 核心框架原理的手写实现项目。通过从零实现常用框架的核心功能，深入理解其底层原理。

## 技术栈

- Java 21
- Maven 多模块项目
- Lombok / Guava / Commons-Lang3

## 项目结构

```
handwritten-series/
├── collection/     # 集合框架手写实现
├── thread/         # 多线程与并发手写实现
├── spring/         # Spring IOC 容器手写实现
├── spring-mvc/     # Spring MVC 手写实现
└── rpc/            # RPC 框架手写实现
```

## 模块说明

### collection - 集合框架

手写实现 Java 集合框架的核心数据结构：

- `list/` - List 相关实现（ArrayList、LinkedList 等）
- `map/` - Map 相关实现（HashMap、TreeMap 等）

### thread - 多线程与并发

手写实现 Java 并发编程的核心组件：

- `aqs/` - AbstractQueuedSynchronizer 同步器实现
- `thread/` - 线程池相关实现
- `scheduled/` - 定时任务调度实现

### spring - Spring IOC 容器

手写实现 Spring 核心功能：

- `ioc/` - IOC 容器与依赖注入
- `annotation/` - 自定义注解支持
- `test/` - 测试用例

### spring-mvc - Spring MVC 框架

基于手写的 Spring 模块，实现 Spring MVC 功能：

- `mvc/` - MVC 核心组件（DispatcherServlet、HandlerMapping 等）
- `annotation/` - MVC 相关注解（@Controller、@RequestMapping 等）
- 内嵌 Tomcat 10.1 作为 Web 容器

### rpc - RPC 远程调用框架

手写实现 RPC 框架的核心功能：

- `bio/` - BIO 阻塞式网络通信
- `netty/` - 基于 Netty 的 NIO 网络通信
- `serialization/` - 多种序列化方式（JSON、Protobuf、Protostuff、Hessian）
- `distributed/` - 分布式服务注册与发现（基于 Curator/ZooKeeper）

## 快速开始

### 环境要求

- JDK 21+
- Maven 3.6+

### 构建项目

```bash
mvn clean install
```

### 运行示例

各模块下的 `test/` 目录包含对应的测试用例和使用示例。

## License

MIT
