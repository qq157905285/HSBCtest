
# 🧾 内存交易管理系统（Transaction Management System）

这是一个使用 Spring Boot 构建的简易交易管理系统，支持基本的 CRUD 操作、分页查询、类型筛选、参数校验、缓存与容器部署，适合作为练手项目或课程作业。

---

## 🚀 功能特性

- ✅ 添加、查询、更新、删除交易
- ✅ 按类型（收入/支出/转账）筛选交易记录
- ✅ 分页查询，支持 `page` 和 `size` 参数
- ✅ 参数校验与统一异常处理
- ✅ 内存存储，支持快速测试
- ✅ 查询结果缓存，提升性能
- ✅ 单元测试覆盖主要功能
- ✅ 简易压力测试支持（ab、JMeter）
- ✅ Docker 打包部署
- ✅ Kubernetes 运行支持

---

## 🚀 快速启动

### 1. 启动后端服务

```bash
# 构建项目
./mvnw clean package

# 运行 Spring Boot 应用
java -jar target/hsbc-transaction-app-0.0.1-SNAPSHOT.jar
```
### 2.启动前端页面
直接用浏览器打开 index.html 文件即可（支持静态请求）。

---

应用默认运行在 `http://localhost:8080`

---

## 📁 项目结构
```
hsbc-transaction-app/
├── src/
│   ├── main/
│   │   ├── java/com/sqzer/hcbctransaction/
│   │   │   ├── controller/
│   │   │   ├── model/
│   │   │   ├── service/
│   │   │   └── TransactionAppApplication.java
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-pro.properties
│   │       └── static/
│   │           └── index.html
│   └── test/
│       └── java/com/sqzer/hsbctransaction/TransactionServiceTest.java
├── Dockerfile
├── k8s/
│   ├── deployment.yaml
│   ├── service.yaml
│   └── hpa.yaml
├── README.md
└── pom.xml
```

---

## 🔗 API 示例

### 添加交易

```http
POST /transactions
Content-Type: application/json

{
  "description": "工资",
  "amount": 8000,
  "type": "INCOME"
}
```

### 查询交易（分页 + 类型）

```http
GET /transactions?page=0&size=5&type=EXPENSE
```

### 更新交易

```http
PUT /transactions/{id}
Content-Type: application/json

{
  "description": "购物",
  "amount": 300,
  "type": "EXPENSE"
}
```

### 删除交易

```http
DELETE /transactions/{id}
```

### 获取全部交易数据（用于图表等）

```http
GET /transactions
```

---

## 🧪 单元测试

运行测试：

```bash
./mvnw test
```

测试类位置：

```
src/test/java/com/example/transaction/TransactionServiceTest.java
```

---

## 💥 压力测试（可选）

使用 Apache Bench (`ab`) 进行简单测试：

```bash
ab -n 1000 -c 100 -p payload.json -T application/json http://localhost:8080/transactions
```

`payload.json` 内容示例：

```json
{
  "description": "测试交易",
  "amount": 50,
  "type": "EXPENSE"
}
```

---

## 🧠 缓存说明（Spring Cache）

分页接口启用了缓存：

```java
@Cacheable(value = "transactions", key = "#page + '-' + #size + '-' + #type")
public List<Transaction> findPaginated(...)
```

每次新增、修改、删除交易都会触发缓存清除：

```java
@CacheEvict(value = "transactions", allEntries = true)
```

---

## 🧾 限流说明

系统已集成 Resilience4j RateLimiter，对所有接口统一限流：

默认配置：每秒最多 5 次请求（通过 application.properties 配置）。

限流失败返回 429 状态码，提示“请求过于频繁”。

### ⚙ 配置示例（application-dev.properties）

```properties
# 应用配置
server.port=8080
# 限流配置（Resilience4j）
resilience4j.ratelimiter.instances.default.limitForPeriod=5
resilience4j.ratelimiter.instances.default.limitRefreshPeriod=1s
resilience4j.ratelimiter.instances.default.timeoutDuration=0
```
---


## 🐳 Docker 部署

### 构建镜像

```bash
./mvnw clean package -DskipTests
docker build -t hsbc-transaction-app .
```

### 启动容器

```bash
docker run -p 8080:8080 hsbc-transaction-app
```

---

## ☸ Kubernetes 支持（YAML 模板）
项目提供了 k8s/deployment.yaml 和 k8s/service.yaml 和 k8s/hpa.yaml 文件，支持部署到 K8s 集群。

---

## 📌 技术栈

- 后端：Spring Boot 3.x、Spring Web、Spring Validation、Spring Cache、Caffeine、Resilience4j、Lombok
- 前端：原生 HTML + JS、Chart.js
- 测试：JUnit 5、Mockito、JMH
- 构建工具：Maven
- 部署：Docker、Kubernetes（YAML 模板支持）

---
