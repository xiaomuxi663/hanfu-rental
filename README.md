# 汉服租赁系统

基于 Vue 3 + SpringBoot 3 + MyBatis-Plus 的汉服租赁平台

## 技术栈

**前端:** Vue 3 + Vite + Element Plus + Axios + Vue Router + Pinia

**后端:** SpringBoot 3.x + MyBatis-Plus + JWT + JDK 17+

**数据库:** MySQL 8.0

## 项目结构

```
├── hangfuzhulingxit/   # 后端SpringBoot项目
├── vue/                # 前端Vue项目
└── sql/                # 数据库脚本
```

## 快速开始

1. 执行 `sql/hanfu_final_db.sql` 创建数据库
2. 启动后端: `cd hangfuzhulingxit && mvn spring-boot:run`
3. 启动前端: `cd vue && npm install && npm run dev`

## 默认账号

- 管理员: admin / 123456
- 库管员: staff / 123456
