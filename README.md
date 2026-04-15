# 贸易行业序列号全流程追踪管理系统

> 企业级 ERP 解决方案 - **支持 Docker 一键部署**

[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3-green.svg)](https://vuejs.org/)
[![Docker](https://img.shields.io/badge/Docker-Ready-blue.svg)](https://www.docker.com/)

---

## 🚀 一键部署（推荐）⭐

### 方式一：宝塔面板部署（最简单！不懂技术也能用）⭐⭐⭐

**完全图形界面操作，5 分钟完成！**

**前置要求：**
- 有一台服务器（阿里云/腾讯云/华为云等）
- 已安装宝塔面板（https://www.bt.cn/download/）

**部署步骤：**

1. **安装 Docker 管理器**
   - 登录宝塔面板
   - 软件商店 → 搜索 "Docker 管理器" → 安装

2. **下载项目代码**
   - 宝塔面板 → 文件 → /www/wwwroot/
   - 创建文件夹 `erp-trade`
   - 使用 Git 拉取或手动上传代码

3. **导入 Docker Compose**
   - Docker 管理器 → Compose → 添加
   - 名称：`erp-trade`
   - 路径：`/www/wwwroot/erp-trade`
   - 文件：`docker-compose-baota.yml`
   - 点击 "创建并启动"

4. **等待部署完成**
   - 等待 3-5 分钟（首次构建镜像）
   - 查看容器状态（应显示 3 个运行中）

5. **访问系统**
   - 浏览器打开：`http://你的服务器 IP`
   - 登录：`admin` / `admin123`

**详细图文教程**: [BAOTA_DOCKER.md](BAOTA_DOCKER.md)

> ⚠️ 首次登录后请立即修改密码！

---

### 方式二：Docker 命令行部署

**5 分钟完成部署！**

```bash
# 1. 克隆仓库
git clone https://github.com/weichengyi/erp.git
cd erp-trade

# 2. 一键部署
bash docker-deploy.sh
```

**部署完成后访问：**
- **前端**: http://localhost
- **后端**: http://localhost:8080
- **数据库**: localhost:3306

**默认账户：**
```
用户名：admin
密码：admin123
```

---

### 方式三：传统部署

详见：[QUICKSTART.md](QUICKSTART.md)

---

## 📋 项目简介

本系统专为贸易行业设计，核心解决两大痛点：
1. **关联公司管理** - 老板控股多家公司的关联关系图谱
2. **人员流动追踪** - 客户方人员跳槽/转岗的全流程追踪

### 核心功能

| 模块 | 功能描述 |
|------|----------|
| 关联公司管理 | 客户/供应商档案、关联关系图谱、关联标签 |
| 人员流动追踪 | 人员档案、流动轨迹、离职提醒、公海流转 |
| SN 码管理 | 一机一码、SN 码溯源、状态跟踪 |
| 资产编码 | 自动生成 (YYYYMMDD+4 位)、SN-资产绑定 |
| 业务单据 | 销售订单、采购订单、出入库、售后服务 |
| 权限管控 | 部门隔离、数据权限、角色管理 |
| **报表统计** | **数据导出 (Excel)、统计图表** |

---

## 📦 Docker 部署架构

```
┌─────────────────────────────────────┐
│        Nginx (Port 80)              │
│  ┌─────────────────────────────┐    │
│  │ Vue 3 前端 (静态文件)        │    │
│  └─────────────────────────────┘    │
│            │                        │
│            │ /api 代理               │
│            ▼                        │
│  ┌─────────────────────────────┐    │
│  │ Spring Boot 后端 (Port 8080) │    │
│  └─────────────────────────────┘    │
│            │                        │
│            │ JDBC                   │
│            ▼                        │
│  ┌─────────────────────────────┐    │
│  │ MySQL 8.0 (Port 3306)       │    │
│  │ 数据卷：mysql_data          │    │
│  └─────────────────────────────┘    │
└─────────────────────────────────────┘
```

---

## 🎯 快速开始

### 前置要求

**Docker 部署：**
- Docker 20.10+
- Docker Compose 2.0+

**传统部署：**
- JDK 17+
- MySQL 8.0+
- Maven 3.6+
- Node.js 18+ (前端开发)

### Docker 部署步骤

#### 1. 检查 Docker 环境
```bash
docker --version
docker-compose --version
```

#### 2. 一键部署
```bash
git clone https://github.com/weichengyi/erp.git
cd erp-trade
bash docker-deploy.sh
```

#### 3. 验证部署
```bash
docker-compose ps
```

应显示：
- erp-mysql (healthy)
- erp-backend (healthy)
- erp-frontend

#### 4. 访问系统
- 浏览器打开：http://localhost
- 登录：admin / admin123

### 传统部署步骤

#### 1. 数据库初始化
```bash
# 登录 MySQL
mysql -u root -p

# 执行初始化脚本
source database/init.sql
```

#### 2. 配置数据库连接
编辑 `src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/erp_trade?useUnicode=true&characterEncoding=utf8
    username: root
    password: your_password
```

#### 3. 编译运行
```bash
# 编译项目
mvn clean package -DskipTests

# 运行应用
mvn spring-boot:run
```

#### 4. 访问系统
```
后端 API: http://localhost:8080
```

---

## 📡 API 接口

### 公司管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/companies` | 获取公司列表 |
| GET | `/api/companies/{id}` | 获取公司详情 |
| POST | `/api/companies` | 创建公司 |
| PUT | `/api/companies/{id}` | 更新公司 |
| DELETE | `/api/companies/{id}` | 删除公司 |
| GET | `/api/companies/search?keyword=xxx` | 搜索公司 |

### SN 码管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/product-sns` | 获取所有 SN 码 |
| GET | `/api/product-sns/code/{snCode}` | 根据 SN 码查询 |
| GET | `/api/product-sns/trace/{snCode}` | 溯源查询 |
| POST | `/api/product-sns` | 创建 SN 码记录 |
| PUT | `/api/product-sns/{id}` | 更新 SN 码 |
| DELETE | `/api/product-sns/{id}` | 删除 SN 码 |

### 资产管理

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/assets` | 获取所有资产 |
| GET | `/api/assets/generate-code` | 生成新资产编码 |
| GET | `/api/assets/code/{code}` | 根据编码查询 |
| POST | `/api/assets` | 创建资产 |
| PUT | `/api/assets/{id}` | 更新资产 |
| DELETE | `/api/assets/{id}` | 删除资产 |

### 报表统计

| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/reports/dashboard-stats` | 仪表盘统计 |
| GET | `/api/reports/company-stats` | 公司统计 |
| GET | `/api/reports/export/companies` | 导出公司数据 (Excel) |
| GET | `/api/reports/export/assets` | 导出资产数据 (Excel) |
| GET | `/api/reports/export/product-sns` | 导出 SN 码数据 (Excel) |

---

## 📐 资产编码规则

```
格式：YYYYMMDD + 4 位序列号

示例:
202604150001 - 2026 年 04 月 15 日第 1 个资产
202604150002 - 2026 年 04 月 15 日第 2 个资产
...
202604159999 - 2026 年 04 月 15 日第 9999 个资产 (当日上限)
```

### 生成逻辑

1. 获取当前日期 (8 位 YYYYMMDD)
2. 查询当日已生成的最大序列号
3. 序列号 +1，不足 4 位补零
4. 当日上限 9999 个

---

## 🗄️ 数据库表结构

### 核心表

| 表名 | 描述 |
|------|------|
| `sys_user` | 系统用户 |
| `company` | 公司档案 (客户/供应商) |
| `company_relation` | 公司关联关系 |
| `contact_person` | 联系人档案 |
| `person_movement` | 人员流动记录 |
| `product_sn` | 产品 SN 码 |
| `asset` | 资产编码 |
| `sn_asset_binding` | SN-资产绑定 |
| `sales_order` | 销售订单 |
| `sales_order_item` | 销售订单明细 |
| `purchase_order` | 采购订单 |
| `stock_in_out` | 出入库记录 |
| `after_sales` | 售后服务单 |
| `sys_log` | 系统日志 |

---

## 🔐 默认账户

| 用户名 | 密码 | 角色 | 部门 |
|--------|------|------|------|
| admin | admin123 | ADMIN | 管理层 |
| sales01 | admin123 | SALES | 销售部 |
| purchase01 | admin123 | PURCHASE | 采购部 |

> ⚠️ 首次登录后请立即修改密码！

---

## 📂 项目结构

```
erp-trade/
├── 📂 database/              # 数据库脚本
│   └── init.sql
├── 📂 src/main/java/         # Java 后端代码
│   └── com/erp/tracksn/
│       ├── entity/           # 实体类
│       ├── repository/       # 数据访问层
│       ├── service/          # 业务逻辑层
│       └── controller/       # 控制器层
├── 📂 frontend/              # Vue 3 前端
│   ├── src/
│   ├── package.json
│   └── Dockerfile
├── 📂 Docker 部署 ⭐
│   ├── docker-compose.yml    # Docker 编排
│   ├── Dockerfile            # 后端容器
│   ├── docker-deploy.sh      # 一键部署
│   ├── docker-backup.sh      # 数据备份
│   └── docker-restore.sh     # 数据恢复
├── 📂 文档
│   ├── DOCKER.md             # Docker 部署文档
│   ├── API_GUIDE.md          # API 接口文档
│   ├── QUICKSTART.md         # 快速开始
│   └── DELIVERY.md           # 项目交付文档
├── pom.xml                   # Maven 配置
└── README.md                 # 本文档
```

---

## 🛠️ 常用命令

### Docker 部署

```bash
# 一键部署
bash docker-deploy.sh

# 查看状态
docker-compose ps

# 查看日志
docker-compose logs -f

# 停止服务
bash docker-stop.sh

# 备份数据
bash docker-backup.sh

# 恢复数据
bash docker-restore.sh ./backups/erp_trade_*.sql.gz
```

### 传统部署

```bash
# 编译
mvn clean package -DskipTests

# 运行
mvn spring-boot:run

# 打包
mvn clean package
```

---

## 📚 文档

| 文档 | 说明 |
|------|------|
| [DOCKER.md](DOCKER.md) | ⭐ Docker 部署完整文档 |
| [README_DOCKER_SUMMARY.md](README_DOCKER_SUMMARY.md) | ⭐ 快速开始 |
| [DEPLOYMENT_OPTIONS.md](DEPLOYMENT_OPTIONS.md) | 部署方式对比 |
| [API_GUIDE.md](API_GUIDE.md) | API 接口文档 |
| [QUICKSTART.md](QUICKSTART.md) | 传统部署指南 |
| [DELIVERY.md](DELIVERY.md) | 项目交付文档 |

---

## ⚠️ 生产环境配置

部署到生产环境前，请修改：

### 1. 数据库密码
编辑 `docker-compose.yml`:
```yaml
environment:
  MYSQL_ROOT_PASSWORD: 你的强密码
  MYSQL_PASSWORD: 你的强密码
```

### 2. JWT 密钥
编辑 `docker-compose.yml`:
```yaml
environment:
  JWT_SECRET: 你的随机生成密钥
```

### 3. 启用 HTTPS
使用 Nginx Proxy Manager 或 Let's Encrypt

### 4. 配置防火墙
```bash
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw deny 3306/tcp
```

---

## 🎯 开发计划

详见：[PROJECT_PLAN.md](PROJECT_PLAN.md)

- [x] Phase 1: 基础架构 ✅
- [x] Phase 2: 关联公司管理 ✅
- [x] Phase 3: 人员流动追踪 ✅
- [x] Phase 4: SN 码与资产管理 ✅
- [x] Phase 5: 业务单据 ✅
- [x] Phase 6: 权限管控 ✅
- [x] Phase 7: 报表统计 ✅
- [x] Docker 部署 ✅

---

## 📝 许可证

MIT License

---

## 📞 技术支持

**仓库地址**: https://github.com/weichengyi/erp

**文档**:
- [Docker 部署指南](DOCKER.md)
- [API 接口文档](API_GUIDE.md)
- [快速开始](README_DOCKER_SUMMARY.md)

---

## 🎉 快速开始

```bash
# 克隆仓库
git clone https://github.com/weichengyi/erp.git
cd erp-trade

# 一键部署
bash docker-deploy.sh

# 访问系统
# http://localhost
# 登录：admin / admin123
```

**5 分钟完成部署！** 🚀
