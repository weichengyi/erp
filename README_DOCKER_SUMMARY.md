# 🐳 Docker 一键部署 - 快速总结

> 5 分钟完成容器化部署

---

## ⚡ 30 秒快速开始

```bash
# 1. 进入项目目录
cd /home/admin/openclaw/workspace/erp-trade

# 2. 一键部署
bash docker-deploy.sh
```

**完成！** 访问 http://localhost 即可使用系统。

---

## 📦 包含的组件

| 组件 | 技术 | 端口 | 说明 |
|------|------|------|------|
| **前端** | Vue 3 + Nginx | 80 | 静态文件服务 |
| **后端** | Spring Boot | 8080 | RESTful API |
| **数据库** | MySQL 8.0 | 3306 | 数据存储 |

---

## 🔐 默认账户

```
用户名：admin
密码：admin123
```

---

## 🛠️ 常用命令

### 查看状态
```bash
docker-compose ps
```

### 查看日志
```bash
docker-compose logs -f
```

### 停止服务
```bash
bash docker-stop.sh
```

### 备份数据
```bash
bash docker-backup.sh
```

### 恢复数据
```bash
bash docker-restore.sh ./backups/erp_trade_20260415_*.sql.gz
```

---

## 📊 系统架构

```
用户浏览器
    ↓
┌─────────────────┐
│   Nginx (80)    │ ← 前端 + API 代理
└─────────────────┘
    ↓ /api
┌─────────────────┐
│ Spring Boot     │ ← 后端 API
│    (8080)       │
└─────────────────┘
    ↓ JDBC
┌─────────────────┐
│   MySQL 8.0     │ ← 数据库
│    (3306)       │
└─────────────────┘
    ↓
  数据卷持久化
```

---

## 📝 完整文档

| 文档 | 说明 |
|------|------|
| [DOCKER.md](DOCKER.md) | Docker 部署完整文档 |
| [DEPLOYMENT_OPTIONS.md](DEPLOYMENT_OPTIONS.md) | 部署方式对比 |
| [QUICKSTART.md](QUICKSTART.md) | 传统部署指南 |
| [API_GUIDE.md](API_GUIDE.md) | API 接口文档 |
| [DELIVERY.md](DELIVERY.md) | 项目交付文档 |

---

## 🎯 下一步

1. ✅ 访问 http://localhost
2. ✅ 登录系统 (admin/admin123)
3. ✅ 修改默认密码
4. ✅ 录入业务数据
5. ✅ 配置备份计划

---

## ⚠️ 生产环境配置

部署到生产环境前，请修改：

1. **数据库密码** (docker-compose.yml)
```yaml
MYSQL_ROOT_PASSWORD: 你的强密码
MYSQL_PASSWORD: 你的强密码
```

2. **JWT 密钥** (docker-compose.yml)
```yaml
JWT_SECRET: 你的随机生成密钥
```

3. **启用 HTTPS**
```bash
# 使用 Nginx Proxy Manager 或 Let's Encrypt
```

4. **配置防火墙**
```bash
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw deny 3306/tcp
```

---

## 📞 获取帮助

### 查看日志
```bash
# 后端日志
docker-compose logs backend

# 前端日志
docker-compose logs frontend

# 数据库日志
docker-compose logs mysql
```

### 故障排查
详见：[DOCKER.md - 故障排查章节](DOCKER.md#故障排查)

### 重置系统
```bash
# 删除所有容器和数据卷
docker-compose down -v

# 重新部署
bash docker-deploy.sh
```

---

## 🎉 部署完成！

系统已在 Docker 容器中运行，数据持久化存储。

**访问地址**: http://localhost  
**默认账户**: admin / admin123

祝使用愉快！🚀
