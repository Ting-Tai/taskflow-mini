# TaskFlow Mini

极简任务管理 H5 应用 —— 个人接单作品集项目。

## 技术栈

- 后端：Spring Boot 3.5 + MyBatis-Plus + MySQL
- 前端：uniapp（H5）
- 构建：Maven + JDK 21

## 目录结构

    taskflow-mini/
    ├── backend/    # Spring Boot 后端
    └── frontend/   # uniapp 前端（H5）

## 功能范围（P0）

- 任务列表、筛选、搜索
- 新建 / 编辑 / 删除任务
- 一键完成
- 统计（总数 / 待办 / 已完成）

## 进度

- [x] 项目初始化
- [x] 数据库建表
- [ ] 后端 CRUD 接口
- [ ] 前端页面
- [ ] 完整 MVP 演示

## 说明

单用户演示模式，未做登录与权限控制，可按需扩展 JWT。