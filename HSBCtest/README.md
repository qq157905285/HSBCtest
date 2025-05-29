
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
- ✅ Docker 化部署支持
- ✅ 单元测试覆盖主要功能
- ✅ 简易压力测试支持（ab、JMeter）

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
│   │       └── application.properties
│   └── test/
│       └── java/com/sqzer/hsbctransaction/TransactionServiceTest.java
├── Dockerfile
├── README.md
└── pom.xml
```

---

## 📦 构建与运行

### 本地运行

```bash
# 使用 Maven 构建
./mvnw clean package

# 运行应用
java -jar target/hsbc-transaction-app.jar
```

应用默认运行在 `http://localhost:8080`

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

## 📌 技术栈

- Spring Boot 3.x
- Maven
- Jakarta Bean Validation
- Spring Cache（基于 ConcurrentMap 缓存）
- JUnit 5
- Docker

---
