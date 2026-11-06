# TaskFlow Mini 后端

极简任务管理的后端 API。单用户演示模式，不含登录鉴权。

技术栈：Spring Boot 3 + MyBatis-Plus + MySQL，提供 7 个接口，覆盖新建、列表、筛选、搜索、编辑、状态切换、删除、统计。

---

## 1. 环境要求

| 依赖 | 版本 | 说明 |
|---|---|---|
| JDK | 21（17 及以上均可） | Spring Boot 3 最低要求 JDK 17 |
| Maven | 3.6+ | |
| MySQL | 8.x | |

> 不要用 JDK 8 跑本项目，会报 `UnsupportedClassVersionError`。

## 2. 准备数据库

```sql
CREATE DATABASE IF NOT EXISTS taskflow_mini DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE taskflow_mini;

CREATE TABLE task (
    id          BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键',
    title       VARCHAR(100) NOT NULL                COMMENT '标题',
    description VARCHAR(500)     NULL                COMMENT '描述',
    priority    VARCHAR(10)  NOT NULL DEFAULT 'MEDIUM' COMMENT '优先级：LOW / MEDIUM / HIGH',
    status      VARCHAR(10)  NOT NULL DEFAULT 'TODO'   COMMENT '状态：TODO / DONE',
    due_date    DATE             NULL                COMMENT '截止日期，可为空',
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    KEY idx_status (status)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COMMENT ='任务表';
```

## 3. 修改数据库连接

打开 `src/main/resources/application.yml`，把 `username` / `password` 改成你自己的 MySQL 账号：

```yaml
spring:
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/taskflow_mini?useUnicode=true&characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false&allowPublicKeyRetrieval=true
    username: root
    password: root
```

## 4. 启动

三种方式任选一种。

**方式一：IDEA**

1. 用 IDEA 打开 `backend` 目录（识别到 `pom.xml` 后右键 → Maven → Reload Project，等依赖下载完）
2. 打开 `src/main/java/com/example/taskflowmini/TaskflowMiniApplication.java`
3. 点 `main` 方法左边的绿色三角运行

**方式二：命令行**

```bash
cd backend
mvn spring-boot:run
```

**方式三：打成 jar 运行**

```bash
cd backend
mvn clean package -DskipTests
java -jar target/taskflow-mini-backend-0.0.1-SNAPSHOT.jar
```

启动成功后访问 <http://localhost:8080/api/health>，返回下面内容即正常：

```json
{"status":"ok","time":"2026-11-06 21:45:00"}
```

## 5. 接口列表

所有接口前缀 `/api`，统一返回 `{code, message, data}`。

> **Windows PowerShell 用户注意**：下面 curl 示例里单引号包裹的 JSON，在 PowerShell 中双引号会被吞掉，服务端收到非法 JSON 返回 400（`请求体格式错误`）。这不是接口问题。解决办法：用 `cmd.exe` 执行、用 Apifox，或改用 `Invoke-RestMethod -Method Post -Uri ... -ContentType 'application/json' -Body '{"title":"x"}'`。

| # | 方法 | 路径 | 说明 |
|---|---|---|---|
| 1 | GET | `/api/health` | 健康检查 |
| 2 | GET | `/api/tasks` | 任务列表，支持按状态筛选和关键字搜索 |
| 3 | POST | `/api/tasks` | 新建任务 |
| 4 | PUT | `/api/tasks/{id}` | 编辑任务（部分更新） |
| 5 | PATCH | `/api/tasks/{id}/status` | 切换状态 TODO / DONE |
| 6 | DELETE | `/api/tasks/{id}` | 删除任务 |
| 7 | GET | `/api/tasks/stats` | 统计：总数 / 待办数 / 已完成数 |

### 5.1 列表

```bash
# 全部
curl http://localhost:8080/api/tasks

# 只看待办
curl "http://localhost:8080/api/tasks?status=TODO"

# 按关键字搜标题和描述
curl "http://localhost:8080/api/tasks?keyword=接口"
```

响应（按 id 倒序，即最新的在前）：

```json
{
  "code": 0,
  "message": "ok",
  "data": [
    {
      "id": 3,
      "title": "搭建 JDK 21 + Maven + MySQL 开发环境",
      "description": "springboot 3.5.0 + mybatis-plus 3.5.12",
      "priority": "LOW",
      "status": "TODO",
      "dueDate": "2026-11-04",
      "createdAt": "2026-11-04 20:12:33",
      "updatedAt": "2026-11-04 20:12:33"
    }
  ]
}
```

### 5.2 新建

```bash
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{"title":"写周报","description":"总结本周进度","priority":"HIGH","dueDate":"2026-11-08"}'
```

- `title` 必填，最长 100 字
- `priority` 只能是 `LOW` / `MEDIUM` / `HIGH`，不传默认 `MEDIUM`
- `status` 不用传，默认 `TODO`
- `dueDate` 可为空，格式 `yyyy-MM-dd`

### 5.3 编辑（部分更新）

```bash
curl -X PUT http://localhost:8080/api/tasks/1 \
  -H "Content-Type: application/json" \
  -d '{"title":"写周报（改）","priority":"MEDIUM"}'
```

只改请求体里出现的字段，没传的保持原样。状态不在这个接口里改。

### 5.4 切换状态

```bash
curl -X PATCH http://localhost:8080/api/tasks/1/status \
  -H "Content-Type: application/json" \
  -d '{"status":"DONE"}'
```

`status` 只能是 `TODO` 或 `DONE`。

### 5.5 删除

```bash
curl -X DELETE http://localhost:8080/api/tasks/1
```

成功返回 `{"code":0,"message":"ok","data":null}`；id 不存在返回 404。

### 5.6 统计

```bash
curl http://localhost:8080/api/tasks/stats
```

```json
{"code":0,"message":"ok","data":{"total":3,"todo":2,"done":1}}
```

## 6. 统一响应与错误码

成功：

```json
{"code": 0, "message": "ok", "data": { ... }}
```

失败：

```json
{"code": 400, "message": "标题不能为空", "data": null}
```

| HTTP 状态 | code | 场景 |
|---|---|---|
| 200 | 0 | 成功 |
| 400 | 400 | 参数校验失败、JSON 格式错误、id 不是数字、状态值非法、没有要更新的字段 |
| 404 | 404 | 任务 id 不存在、接口路径不存在 |
| 405 | 405 | 请求方法用错（例如对 `/api/tasks` 发 PUT） |
| 500 | 500 | 服务器内部错误（不返回堆栈） |

## 7. 目录结构

```
backend/
├── pom.xml
└── src/main/
    ├── java/com/example/taskflowmini/
    │   ├── TaskflowMiniApplication.java     启动类
    │   ├── common/Result.java               统一响应体
    │   ├── controller/TaskController.java   7 个接口
    │   ├── dto/
    │   │   ├── TaskUpdateRequest.java       PUT 的请求体（不含 status）
    │   │   ├── StatusUpdateRequest.java     PATCH 状态请求体
    │   │   └── TaskStats.java               统计结果
    │   ├── entity/Task.java                 表映射 + 字段校验
    │   ├── exception/
    │   │   ├── GlobalExceptionHandler.java  全局异常 → 统一错误码
    │   │   └── NotFoundException.java       404 业务异常
    │   ├── mapper/TaskMapper.java           BaseMapper + 统计 SQL
    │   └── service/
    │       ├── TaskService.java
    │       └── impl/TaskServiceImpl.java    业务逻辑
    └── resources/application.yml            端口、数据源、MyBatis-Plus 配置
```

## 8. 已知限制

- **单用户模式**，没有登录和鉴权；需要多人用时在 `TaskController` 前面加一层 JWT 过滤器即可
- **PUT 无法把 `due_date` 清空成 null**：null 表示「这个字段不改」，要清空必须另外实现
- **没有分页**：数据量大时在列表接口加 `LIMIT` 和 `COUNT` 即可
- 没有写单元测试，接口靠 Apifox 手工验证
