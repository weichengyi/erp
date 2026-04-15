# 部署方式对比

> 选择适合你的部署方式

---

## 📊 三种部署方式对比

| 特性 | Docker 部署 ⭐ | 传统部署 | 开发环境 |
|------|-------------|----------|----------|
| **推荐度** | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ |
| **适用场景** | 生产环境 | 传统服务器 | 本地开发 |
| **部署时间** | 5 分钟 | 30 分钟 | 10 分钟 |
| **环境一致性** | ✅ 完全一致 | ❌ 可能有差异 | ✅ 本地一致 |
| **资源占用** | 中等 | 低 | 低 |
| **维护成本** | 低 | 中等 | 低 |
| **扩展性** | ✅ 容易 | ❌ 困难 | ❌ 困难 |
| **数据备份** | ✅ 自动化 | 手动 | 手动 |
| **隔离性** | ✅ 完全隔离 | ✅ 系统隔离 | ❌ 无隔离 |

---

## 🐳 方案一：Docker 部署（推荐）⭐

### 适用场景
- ✅ 生产环境部署
- ✅ 需要快速部署
- ✅ 多环境一致性要求高
- ✅ 需要容器化隔离

### 优点
- 一键部署，5 分钟完成
- 环境完全一致，避免"在我机器上能跑"问题
- 自动备份和恢复
- 易于扩展和迁移
- 服务隔离，互不影响

### 缺点
- 需要学习 Docker 基础
- 占用额外资源（约 500MB）

### 快速开始
```bash
cd /home/admin/openclaw/workspace/erp-trade
bash docker-deploy.sh
```

### 访问地址
- 前端：http://localhost
- 后端：http://localhost:8080
- 数据库：localhost:3306

### 文档
详见：[DOCKER.md](DOCKER.md)

---

## 🖥️ 方案二：传统部署

### 适用场景
- ✅ 已有 MySQL 环境
- ✅ 不允许使用 Docker
- ✅ 需要完全控制每个组件

### 优点
- 资源占用最低
- 完全控制每个服务
- 无需学习 Docker

### 缺点
- 部署时间长
- 环境配置复杂
- 容易出现环境差异问题

### 部署步骤

#### 1. 安装 MySQL
```bash
sudo bash install/mysql_install.sh
```

#### 2. 启动后端
```bash
cd /home/admin/openclaw/workspace/erp-trade
mvn spring-boot:run
```

#### 3. 启动前端
```bash
cd /home/admin/openclaw/workspace/erp-trade/frontend
npm install
npm run dev
```

### 访问地址
- 前端：http://localhost:3000
- 后端：http://localhost:8080
- 数据库：localhost:3306

### 文档
详见：[QUICKSTART.md](QUICKSTART.md)

---

## 💻 方案三：开发环境部署

### 适用场景
- ✅ 本地开发调试
- ✅ 功能测试
- ✅ 快速原型验证

### 优点
- 热重载，实时预览
- 调试方便
- 无需编译打包

### 缺点
- 不适合生产环境
- 性能未优化

### 快速开始
```bash
# 确保 MySQL 已运行
systemctl status mysql

# 启动后端（终端 1）
cd /home/admin/openclaw/workspace/erp-trade
mvn spring-boot:run

# 启动前端（终端 2）
cd /home/admin/openclaw/workspace/erp-trade/frontend
npm install
npm run dev
```

### 访问地址
- 前端：http://localhost:3000
- 后端：http://localhost:8080

---

## 🎯 选择建议

### 选择 Docker 部署，如果：
- [ ] 你需要快速部署到生产环境
- [ ] 你希望环境完全一致
- [ ] 你需要自动备份功能
- [ ] 你计划使用容器编排（K8s）
- [ ] 你需要快速扩展

### 选择传统部署，如果：
- [ ] 你的服务器不允许 Docker
- [ ] 你已有完善的 MySQL 环境
- [ ] 你需要完全控制每个组件
- [ ] 你的资源非常有限

### 选择开发环境，如果：
- [ ] 你在本地开发调试
- [ ] 你需要热重载功能
- [ ] 你只是测试功能

---

## 📝 迁移指南

### 开发环境 → Docker 部署

```bash
# 1. 导出开发环境数据
mysqldump -u root -p erp_trade > backup.sql

# 2. Docker 部署
bash docker-deploy.sh

# 3. 恢复数据
bash docker-restore.sh backup.sql
```

### Docker 部署 → 传统部署

```bash
# 1. 从 Docker 导出数据
docker-compose exec -T mysql mysqldump \
  -u root -perp_root_2026 erp_trade > backup.sql

# 2. 安装传统环境
sudo bash install/mysql_install.sh

# 3. 导入数据
mysql -u root -p erp_trade < backup.sql

# 4. 启动后端和前端
mvn spring-boot:run
npm run dev
```

---

## 🔧 混合部署

也可以混合使用：

- **开发环境**: 本地运行前后端 + Docker MySQL
- **测试环境**: Docker 完整部署
- **生产环境**: Docker 完整部署 + 负载均衡

示例：开发环境使用 Docker MySQL
```bash
# 只启动 MySQL
docker-compose up -d mysql

# 本地运行前后端
mvn spring-boot:run
npm run dev
```

---

## 📊 资源需求对比

| 部署方式 | CPU | 内存 | 磁盘 |
|---------|-----|------|------|
| Docker | 2 核 | 2GB | 5GB |
| 传统 | 2 核 | 1.5GB | 3GB |
| 开发 | 2 核 | 1GB | 2GB |

---

## ✅ 推荐配置

### 小型部署 (< 50 用户)
- Docker 部署
- 2 核 CPU
- 2GB 内存
- 20GB 磁盘

### 中型部署 (50-200 用户)
- Docker 部署 + Nginx 反向代理
- 4 核 CPU
- 4GB 内存
- 50GB 磁盘

### 大型部署 (> 200 用户)
- Docker Swarm / Kubernetes
- 8 核 CPU
- 8GB 内存
- 100GB SSD 磁盘
- 读写分离

---

## 🎓 学习资源

### Docker 入门
- [Docker 官方文档](https://docs.docker.com/)
- [Docker Compose 文档](https://docs.docker.com/compose/)

### 故障排查
- [DOCKER.md - 故障排查章节](DOCKER.md#故障排查)
- [常见问题 FAQ](FAQ.md)

---

## 📞 技术支持

如有部署问题，请查阅对应文档：
- Docker 部署：[DOCKER.md](DOCKER.md)
- 传统部署：[QUICKSTART.md](QUICKSTART.md)
- API 接口：[API_GUIDE.md](API_GUIDE.md)

---

**推荐选择 Docker 部署，享受容器化带来的便利！** 🐳
