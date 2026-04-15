# Docker 一键部署指南

> 5 分钟完成容器化部署

---

## 📦 前置要求

### 安装 Docker

**Ubuntu/Debian:**
```bash
# 安装 Docker
curl -fsSL https://get.docker.com | bash -s docker
sudo systemctl enable docker
sudo systemctl start docker

# 安装 Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

# 验证安装
docker --version
docker-compose --version
```

**CentOS/RHEL:**
```bash
sudo yum install -y docker
sudo systemctl enable docker
sudo systemctl start docker

# 安装 Docker Compose
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
```

---

## 🚀 一键部署

### 方式一：使用部署脚本（推荐）

```bash
cd /home/admin/openclaw/workspace/erp-trade
bash docker-deploy.sh
```

### 方式二：手动部署

```bash
cd /home/admin/openclaw/workspace/erp-trade

# 1. 构建并启动
docker-compose up -d --build

# 2. 查看日志
docker-compose logs -f

# 3. 检查状态
docker-compose ps
```

---

## 📡 访问地址

| 服务 | 地址 | 说明 |
|------|------|------|
| **前端** | http://localhost | Nginx 服务 |
| **后端** | http://localhost:8080 | Spring Boot API |
| **数据库** | localhost:3306 | MySQL 8.0 |

---

## 🔐 默认账户

| 用户名 | 密码 | 角色 |
|--------|------|------|
| admin | admin123 | 管理员 |

---

## 🗄️ 数据库信息

```yaml
主机：localhost
端口：3306
数据库：erp_trade
用户名：erp_user
密码：erp_password_2026
root 密码：erp_root_2026
```

---

## 🔧 常用命令

### 查看服务状态
```bash
docker-compose ps
```

### 查看日志
```bash
# 所有服务
docker-compose logs -f

# 单独服务
docker-compose logs -f backend
docker-compose logs -f mysql
docker-compose logs -f frontend
```

### 重启服务
```bash
# 重启所有
docker-compose restart

# 重启单个
docker-compose restart backend
```

### 停止服务
```bash
bash docker-stop.sh
# 或
docker-compose down
```

### 删除所有（包括数据）
```bash
docker-compose down -v
```

---

## 💾 数据备份

### 自动备份
```bash
bash docker-backup.sh
```

备份文件保存在 `./backups/` 目录，自动保留最近 7 天的备份。

### 手动备份
```bash
docker-compose exec -T mysql mysqldump \
  -u root \
  -perp_root_2026 \
  --databases erp_trade \
  > backup_$(date +%Y%m%d).sql
```

### 恢复数据
```bash
# 从备份文件恢复
bash docker-restore.sh ./backups/erp_trade_20260415_120000.sql.gz

# 或手动恢复
docker-compose exec -T mysql mysql \
  -u root \
  -perp_root_2026 \
  erp_trade < backup.sql
```

---

## 🛠️ 故障排查

### 后端无法启动
```bash
# 查看后端日志
docker-compose logs backend

# 常见问题：
# 1. 数据库未就绪 - 等待 MySQL 健康检查通过
# 2. 端口被占用 - 修改 docker-compose.yml 的端口映射
```

### 前端无法访问
```bash
# 查看前端日志
docker-compose logs frontend

# 检查 Nginx 配置
docker-compose exec frontend nginx -t
```

### 数据库连接失败
```bash
# 测试数据库连接
docker-compose exec mysql mysql -u erp_user -perp_password_2026 erp_trade

# 重置数据库
docker-compose down -v
docker-compose up -d --build
```

### 容器无法启动
```bash
# 查看系统资源
docker stats

# 清理无用镜像
docker image prune -a

# 重新构建
docker-compose build --no-cache
```

---

## 📊 容器架构

```
┌─────────────────────────────────────────┐
│           Nginx (Port 80)               │
│  ┌─────────────────────────────────┐    │
│  │  Vue 3 前端 (静态文件)           │    │
│  └─────────────────────────────────┘    │
│              │                          │
│              │ /api 代理                 │
│              ▼                          │
│  ┌─────────────────────────────────┐    │
│  │  Spring Boot 后端 (Port 8080)    │    │
│  └─────────────────────────────────┘    │
│              │                          │
│              │ JDBC                     │
│              ▼                          │
│  ┌─────────────────────────────────┐    │
│  │  MySQL 8.0 (Port 3306)          │    │
│  │  数据卷：mysql_data             │    │
│  └─────────────────────────────────┘    │
└─────────────────────────────────────────┘
```

---

## 🔒 安全配置

### 生产环境修改

1. **修改默认密码**
```yaml
# docker-compose.yml
environment:
  MYSQL_ROOT_PASSWORD: 你的强密码
  MYSQL_PASSWORD: 你的强密码
  JWT_SECRET: 你的 JWT 密钥
```

2. **限制端口访问**
```yaml
# 仅允许内网访问
ports:
  - "127.0.0.1:8080:8080"
```

3. **启用 HTTPS**
```bash
# 使用 Nginx Proxy Manager 或 Let's Encrypt
```

4. **配置防火墙**
```bash
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw deny 3306/tcp  # 禁止外网访问数据库
```

---

## 📈 性能优化

### 增加 JVM 内存
```yaml
# docker-compose.yml
backend:
  environment:
    JAVA_OPTS: "-Xms512m -Xmx1024m"
```

### MySQL 优化
```yaml
# docker-compose.yml
mysql:
  command:
    - --innodb-buffer-pool-size=1G
    - --max-connections=200
```

### Nginx 缓存
```nginx
# frontend/nginx.conf
location ~* \.(css|js)$ {
    expires 7d;
    add_header Cache-Control "public";
}
```

---

## 🔄 更新部署

### 更新后端代码
```bash
# 1. 拉取最新代码
git pull

# 2. 重新构建并启动
docker-compose up -d --build backend

# 3. 查看日志
docker-compose logs -f backend
```

### 更新前端代码
```bash
# 1. 拉取最新代码
git pull

# 2. 重新构建并启动
docker-compose up -d --build frontend
```

### 更新数据库结构
```bash
# 1. 备份数据
bash docker-backup.sh

# 2. 执行新的 SQL 脚本
docker-compose exec -T mysql mysql \
  -u root \
  -perp_root_2026 \
  erp_trade < database/update.sql
```

---

## 📝 环境变量配置

### 后端环境变量
```yaml
SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/erp_trade?...
SPRING_DATASOURCE_USERNAME: erp_user
SPRING_DATASOURCE_PASSWORD: erp_password_2026
JWT_SECRET: your-secret-key
TZ: Asia/Shanghai
```

### 数据库环境变量
```yaml
MYSQL_ROOT_PASSWORD: erp_root_2026
MYSQL_DATABASE: erp_trade
MYSQL_USER: erp_user
MYSQL_PASSWORD: erp_password_2026
TZ: Asia/Shanghai
```

---

## 🎯 完整部署流程

```bash
# 1. 安装 Docker (如果未安装)
curl -fsSL https://get.docker.com | bash -s docker

# 2. 克隆项目
cd /home/admin/openclaw/workspace/erp-trade

# 3. 一键部署
bash docker-deploy.sh

# 4. 访问系统
# 浏览器打开 http://localhost

# 5. 登录
# 用户名：admin
# 密码：admin123
```

---

## ✅ 部署检查清单

- [ ] Docker 已安装
- [ ] Docker Compose 已安装
- [ ] 80 端口未被占用
- [ ] 8080 端口未被占用
- [ ] 3306 端口未被占用
- [ ] 磁盘空间充足 (至少 5GB)
- [ ] 内存充足 (至少 2GB)

---

部署完成后，系统将在 Docker 容器中运行，数据持久化在数据卷中，即使删除容器数据也不会丢失。

祝部署顺利！🎉
