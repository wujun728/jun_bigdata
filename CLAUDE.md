# CLAUDE.md

本文件为 Claude Code (claude.ai/code) 在此代码仓库中工作时提供指导。

## 项目概述

这是一个全面的大数据学习和参考仓库，包含 Hadoop 生态系统及其他主流大数据技术的实现、文档和示例。项目采用多模块 Maven 结构组织，每个技术栈独立为一个模块。

## 仓库结构

仓库遵循模块化结构，每个大数据技术作为独立的 Maven 模块：

**核心计算框架：**
- `bigdata-hadoop` - Hadoop、HDFS、MapReduce、YARN
- `spark-core` - Spark Core 基础
- `bigdata-spark-sql` - Spark SQL 模块
- `bigdata-spark-streaming` - Spark Streaming 模块
- `spark-graphx` - Spark GraphX 图计算
- `spark-mllib` - Spark MLlib 机器学习
- `bigdata-flink` - Apache Flink 流式框架

**存储系统：**
- `bigdata-hbase` - HBase NoSQL 数据库
- `bigdata-hive` - Hive 数据仓库
- `bigdata-doris` - Apache Doris OLAP 数据库
- `bigdata-druid` - Apache Druid 实时分析

**消息与流处理：**
- `bigdata-kafka` - Apache Kafka 消息队列

**项目示例：**
- `bigdata-project` - 实际项目实现，包括：
  - 用户画像
  - ID mapping
  - 数据仓库
  - 实时仓库
  - 特征工程

**其他：**
- `bigdata-info` - 附加技术（Elasticsearch、Oozie、Griffin、Pegasus、Talos）
- `bigdata-demo` - 旧版演示代码（HBase、Hadoop、Hive、Java、Redis、SpringBoot）

## 模块结构模式

大多数模块遵循以下内部结构：
- `src/main/java/com/libin` 或 `src/main/java/com/shujia` - Java 源代码
- `src/main/scala/com/libin` - Scala 源代码（Spark/Flink 模块）
- `src/main/docs` 或 `src/main/doc` - 技术文档（架构、概念、算法）
- `README.md` - 模块专用文档，包含代码和文档链接

## 构建命令

**构建整个项目：**
```bash
mvn clean install
```

**构建特定模块：**
```bash
cd <模块名>
mvn clean install
```

**编译但不运行测试：**
```bash
mvn clean compile -DskipTests
```

**打包模块：**
```bash
cd <模块名>
mvn clean package
```

## 技术版本

各模块使用的主要依赖版本：
- Hadoop: 2.6.0
- Scala: 2.11
- Java: 1.6 target（遗留兼容性）

注意：各个模块可能有不同的版本要求。请始终检查模块的 `pom.xml` 以了解具体依赖。

## 模块使用说明

**Spark 模块**（spark-core、bigdata-spark-sql、bigdata-spark-streaming、spark-graphx、spark-mllib）：
- 主要使用 Scala 编写
- 代码示例位于 `src/main/scala/com/libin/`
- 基础包中包含工具类和公共代码
- 专用子目录中包含作业/模板

**Flink 模块**（bigdata-flink）：
- 混合 Java/Scala 代码库
- 文档位于 `src/main/docs/`
- 涵盖检查点、状态管理、时间操作和窗口

**Hadoop 模块**（bigdata-hadoop）：
- 包含 HDFS、MapReduce 和 YARN 示例
- API 文档位于 `src/main/java/com/libin/api/`
- 概念文档位于 `src/main/java/com/libin/doc/`

**HBase 模块**（bigdata-hbase）：
- 包含详细文档：
  - HBase 架构和数据模型
  - 内部组件（RegionServer、BlockCache、HFile、HLog、MemStore）
  - 算法（LSM 树、布隆过滤器、跳跃表）
  - Compaction 策略
- 客户端实现示例

**Hive 模块**（bigdata-hive）：
- UDF/UDTF 自定义函数
- JDBC 操作
- 架构和查询解析文档

## 通用模式

1. **文档结构**：大多数模块都有详尽的 Markdown 文档说明：
   - 架构和核心概念
   - 内部机制和算法
   - 面试题和常见问题

2. **代码组织**：
   - API 示例展示技术用法
   - 文档解释"为什么"和"怎么做"
   - 公共工具类可在示例中重用

3. **双语言支持**：面向 Spark/Flink 生态系统的模块同时支持 Java 和 Scala

## 重要说明

- 这主要是一个**学习和参考仓库**，而非生产代码库
- 代码示例旨在演示概念和 API 用法
- 文档使用中文编写
- 许多模块包含面试题和理论解释
- `bigdata-demo` 目录包含旧版演示代码，使用不同的包结构（`com.shujia`）

## 参考资源

文档中提到的外部参考：
- 各技术的 Apache 官方网站
- DataFunTalk（知乎）
- 美团技术博客
- InfoQ 大数据主题
