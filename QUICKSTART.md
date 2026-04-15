# 快速开始指南

> 5 分钟完成系统部署

---

## ✅ 已完成

- [x] MySQL 8.0 安装
- [x] 数据库初始化
- [x] 后端 Java 项目创建
- [x] 前端 Vue 3 项目创建
- [x] 报表统计功能
- [x] 数据导出功能

---

## 🚀 启动步骤

### 方式一：一键启动脚本（推荐）

```bash
cd /home/admin/openclaw/workspace/erp-trade
bash start.sh
```

### 方式二：手动启动

#### 1. 启动后端

```bash
cd /home/admin/openclaw/workspace/erp-trade

# 编译项目
mvn clean package -DskipTests

# 启动应用
mvn spring-boot:run
```

访问：http://localhost:8080

#### 2. 启动前端（新终端）

```bash
cd /home/admin/openclaw/workspace/erp-trade/frontend

# 安装依赖
npm install

# 启动开发服务器
npm run dev
```

访问：http://localhost:3000

---

## 🔐 默认账户

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 管理员 |
| sales01 | admin123 | 销售 |
| purchase01 | admin123 | 采购 |

---

## 📡 API 接口

后端服务：http://localhost:8080/api

### 测试接口

```bash
# 获取公司列表
curl http://localhost:8080/api/companies

# 获取所有 SN 码
curl http://localhost:8080/api/product-sns

# 获取所有资产
curl http://localhost:8080/api/assets

# 导出公司数据 (Excel)
curl -o companies.xlsx http://localhost:8080/api/reports/export/companies
```

---

## 📊 报表功能

访问：http://localhost:3000/reports

### 功能列表

1. **数据导出**
   - 导出公司数据 (Excel)
   - 导出资产数据 (Excel)
   - 导出 SN 码数据 (Excel)

2. **统计图表**
   - 月度销售趋势
   - 公司关联关系分析
   - 人员流动统计
   - 产品状态分布

3. **详细统计表格**
   - 各类别总数
   - 活跃/停用数量
   - 趋势百分比

---

## 🛠️ 常见问题

### MySQL 连接失败

```bash
# 检查 MySQL 状态
systemctl status mysql

# 重启 MySQL
sudo systemctl restart mysql
```

### 端口被占用

```bash
# 查看端口占用
sudo lsof -i :8080
sudo lsof -i :3000

# 修改端口
# 后端：编辑 application.yml 的 server.port
# 前端：编辑 vite.config.js 的 server.port
```

### 前端依赖安装失败

```bash
# 使用淘宝镜像
npm config set registry https://registry.npmmirror.com
npm install
```

---

## 📝 下一步

1. **修改默认密码** - 首次登录后立即修改
2. **配置生产环境** - 修改数据库密码、JWT 密钥
3. **添加业务数据** - 录入公司、产品、客户信息
4. **培训用户** - 销售部、采购部使用培训

---

## 📞 技术支持

如有问题，请查看：
- README.md - 项目说明
- API_GUIDE.md - API 接口文档
- PROJECT_PLAN.md - 开发计划

---

## 🎯 系统架构图

```
┌─────────────┐         ┌──────────────┐
│  用户浏览器  │ ──────▶ │  Vue 3 前端   │
│  (Chrome)   │         │ (Element Plus)│
└─────────────┘         └──────────────┘
                               │
                               ▼
                        ┌──────────────┐
                        │ Spring Boot  │
                        │   后端 API    │
                        └──────────────┘
                               │
                               ▼
                        ┌──────────────┐
                        │   MySQL 8.0  │
                        │  数据库      │
                        └──────────────┘
```

---

祝使用愉快！🎉
