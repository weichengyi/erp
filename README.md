# 贸易行业序列号全流程追踪管理系统

> 企业级 Java Spring Boot + MySQL 解决方案

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

---

## 🚀 快速开始

### 环境要求

- JDK 17+
- MySQL 8.0+
- Maven 3.6+

### 1. 数据库初始化

```bash
# 登录 MySQL
mysql -u root -p

# 执行初始化脚本
source database/init.sql
```

### 2. 配置数据库连接

编辑 `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/erp_trade?useUnicode=true&characterEncoding=utf8
    username: root
    password: your_password
```

### 3. 编译运行

```bash
# 编译项目
mvn clean package -DskipTests

# 运行应用
mvn spring-boot:run

# 或直接运行 jar
java -jar target/trade-sn-tracking-1.0.0-SNAPSHOT.jar
```

### 4. 访问系统

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
├── database/
│   └── init.sql              # 数据库初始化脚本
├── src/main/java/com/erp/tracksn/
│   ├── TradeSnTrackingApplication.java  # 启动类
│   ├── entity/               # 实体类
│   │   ├── Company.java
│   │   ├── CompanyRelation.java
│   │   ├── ContactPerson.java
│   │   ├── PersonMovement.java
│   │   ├── ProductSn.java
│   │   ├── Asset.java
│   │   ├── SalesOrder.java
│   │   └── SysUser.java
│   ├── repository/           # 数据访问层
│   │   ├── CompanyRepository.java
│   │   ├── CompanyRelationRepository.java
│   │   ├── ProductSnRepository.java
│   │   └── AssetRepository.java
│   ├── service/              # 业务逻辑层
│   │   └── AssetService.java
│   └── controller/           # 控制器层
│       ├── CompanyController.java
│       ├── ProductSnController.java
│       └── AssetController.java
├── src/main/resources/
│   └── application.yml       # 配置文件
├── pom.xml                   # Maven 配置
├── README.md                 # 本文档
└── PROJECT_PLAN.md           # 开发计划
```

---

## 🔧 开发计划

详见 `PROJECT_PLAN.md`

- [x] Phase 1: 基础架构
- [ ] Phase 2: 关联公司管理
- [ ] Phase 3: 人员流动追踪
- [ ] Phase 4: SN 码与资产管理
- [ ] Phase 5: 业务单据
- [ ] Phase 6: 权限管控
- [ ] Phase 7: 报表统计

---

## 📝 许可证

MIT License

---

## 📞 技术支持

如有问题，请联系开发团队。
