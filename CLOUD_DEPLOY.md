# 云平台一键部署配置指南

> Git + Docker Compose 方式 - 适合云平台集成

---

## 🎯 适用场景

- ✅ 云平台想要集成"一键部署"功能
- ✅ 用户不懂技术，需要最简单的方式
- ✅ 需要从 Git 仓库自动拉取代码
- ✅ 需要自动构建和部署

---

## 📦 部署原理

```
用户点击"一键部署"
    ↓
云平台自动执行：
1. git clone https://github.com/weichengyi/erp.git
2. cd erp-trade
3. docker-compose -f docker-compose-cloud.yml up -d
4. 等待构建完成（3-5 分钟）
5. 显示访问地址
    ↓
完成！
```

---

## 🔧 云平台集成步骤

### 步骤 1：配置 Git 仓库

在云平台配置：

| 配置项 | 值 |
|--------|-----|
| **Git 仓库地址** | https://github.com/weichengyi/erp.git |
| **分支** | main |
| **部署目录** | erp-trade |

### 步骤 2：配置 Docker Compose

在云平台配置：

| 配置项 | 值 |
|--------|-----|
| **Compose 文件** | docker-compose-cloud.yml |
| **自动构建** | 是 |
| **构建上下文** | 当前目录 |

### 步骤 3：配置环境变量（可选）

云平台可以让用户自定义：

| 变量名 | 说明 | 默认值 |
|--------|------|--------|
| `MYSQL_ROOT_PASSWORD` | MySQL root 密码 | erp_root_2026 |
| `MYSQL_PASSWORD` | MySQL 用户密码 | erp_password_2026 |
| `JWT_SECRET` | JWT 密钥 | 自动生成 |

### 步骤 4：配置端口映射

| 容器端口 | 主机端口 | 说明 |
|----------|----------|------|
| 80 | 80 | 前端访问 |
| 8080 | 8080 | 后端 API |
| 3306 | 3306 | 数据库（建议不开放） |

### 步骤 5：配置数据卷

持久化存储：

| 数据卷 | 挂载路径 | 说明 |
|--------|----------|------|
| mysql_data | /var/lib/mysql | MySQL 数据 |
| logs | /app/logs | 应用日志 |

---

## 🚀 一键部署命令

### 标准部署命令

```bash
# 1. 拉取代码
git clone https://github.com/weichengyi/erp.git
cd erp-trade

# 2. 一键部署
docker-compose -f docker-compose-cloud.yml up -d

# 3. 查看状态
docker-compose ps

# 4. 查看日志
docker-compose logs -f
```

### 停止部署

```bash
docker-compose -f docker-compose-cloud.yml down
```

### 重新部署

```bash
# 停止并删除
docker-compose -f docker-compose-cloud.yml down

# 重新构建并启动
docker-compose -f docker-compose-cloud.yml up -d --build
```

---

## 📊 部署流程详解

### 阶段 1：拉取代码（10 秒）

```bash
git clone https://github.com/weichengyi/erp.git
```

### 阶段 2：构建后端镜像（3-5 分钟）

```bash
docker build -t erp-backend .
```

**构建过程：**
1. 拉取 Maven 基础镜像
2. 下载依赖（首次较慢）
3. 编译 Java 代码
4. 打包成 JAR
5. 创建运行镜像

### 阶段 3：构建前端镜像（2-3 分钟）

```bash
docker build -t erp-frontend ./frontend
```

**构建过程：**
1. 拉取 Node.js 基础镜像
2. 安装依赖
3. 编译 Vue 代码
4. 使用 Nginx 提供静态文件

### 阶段 4：启动容器（30 秒）

```bash
docker-compose up -d
```

**启动顺序：**
1. MySQL 容器
2. 等待 MySQL 就绪（健康检查）
3. 后端容器
4. 等待后端就绪（健康检查）
5. 前端容器

---

## ✅ 部署验证

### 检查容器状态

```bash
docker-compose ps
```

应显示：
```
NAME           STATUS         PORTS
erp-mysql      Up (healthy)   3306/tcp
erp-backend    Up (healthy)   0.0.0.0:8080->8080/tcp
erp-frontend   Up             0.0.0.0:80->80/tcp
```

### 检查服务健康状态

```bash
# 检查后端
curl http://localhost:8080/api/companies

# 检查前端
curl http://localhost
```

### 访问系统

浏览器打开：`http://服务器 IP`

登录：`admin` / `admin123`

---

## 🛠️ 云平台集成示例

### 示例 1：阿里云容器服务

```yaml
# aliyun-compose.yml
version: '3.8'
services:
  app:
    image: registry.cn-hangzhou.aliyuncs.com/your-namespace/erp:latest
    ports:
      - "80:80"
    environment:
      - MYSQL_HOST=mysql-service
      - MYSQL_PASSWORD=${MYSQL_PASSWORD}
```

### 示例 2：腾讯云容器服务

```yaml
# tencent-compose.yml
version: '3.8'
services:
  app:
    image: ccr.ccs.tencentyun.com/your-namespace/erp:latest
    ports:
      - "80:80"
```

### 示例 3：华为云容器引擎

```yaml
# huawei-compose.yml
version: '3.8'
services:
  app:
    image: swr.cn-east-2.myhuaweicloud.com/your-namespace/erp:latest
    ports:
      - "80:80"
```

---

## 📝 云平台部署模板

### 应用描述文件（app.yaml）

```yaml
apiVersion: v1
kind: Application
metadata:
  name: erp-trade
  description: 贸易行业序列号追踪管理系统
  version: 1.0.0
  icon: https://github.com/weichengyi/erp/raw/main/logo.png
spec:
  source:
    type: git
    url: https://github.com/weichengyi/erp.git
    branch: main
    subPath: erp-trade
  
  deploy:
    type: docker-compose
    file: docker-compose-cloud.yml
  
  ports:
    - port: 80
      protocol: HTTP
      name: web
  
  env:
    - name: MYSQL_ROOT_PASSWORD
      description: MySQL root 密码
      required: false
      default: erp_root_2026
    
    - name: MYSQL_PASSWORD
      description: MySQL 用户密码
      required: false
      default: erp_password_2026
    
    - name: JWT_SECRET
      description: JWT 密钥
      required: false
      generate: random
  
  volumes:
    - name: mysql-data
      mountPath: /var/lib/mysql
      persistent: true
  
  healthCheck:
    httpGet:
      path: /api/companies
      port: 8080
    initialDelaySeconds: 60
    periodSeconds: 10
  
  postDeploy:
    - message: |
        ✅ 部署成功！
        
        访问地址：http://${SERVER_IP}
        登录账户：admin / admin123
        
        ⚠️ 首次登录后请立即修改密码！
```

---

## 🔐 安全配置

### 1. 使用环境变量存储敏感信息

```yaml
environment:
  MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
  MYSQL_PASSWORD: ${MYSQL_PASSWORD}
  JWT_SECRET: ${JWT_SECRET}
```

### 2. 限制数据库访问

```yaml
ports:
  # 不开放 3306 到外网
  - "127.0.0.1:3306:3306"
```

### 3. 启用 HTTPS

在云平台配置 SSL 证书：

```yaml
tls:
  enabled: true
  certificate: /etc/ssl/certs/erp.crt
  key: /etc/ssl/private/erp.key
```

---

## 📊 资源需求

| 组件 | CPU | 内存 | 磁盘 |
|------|-----|------|------|
| MySQL | 0.5 核 | 512MB | 2GB |
| Backend | 0.5 核 | 512MB | 500MB |
| Frontend | 0.25 核 | 128MB | 200MB |
| **总计** | **1.25 核** | **1.1GB** | **2.7GB** |

### 推荐配置

| 配置 | 最低要求 | 推荐配置 |
|------|----------|----------|
| CPU | 1 核 | 2 核 |
| 内存 | 1GB | 2GB |
| 磁盘 | 5GB | 10GB |

---

## ❓ 常见问题

### 问题 1: 构建失败

**解决**:
```bash
# 查看构建日志
docker-compose build --progress=plain

# 清理缓存重新构建
docker-compose build --no-cache
```

### 问题 2: 容器启动失败

**解决**:
```bash
# 查看容器日志
docker-compose logs mysql
docker-compose logs backend
docker-compose logs frontend

# 检查端口占用
netstat -tulpn | grep -E '80|8080|3306'
```

### 问题 3: 数据库连接失败

**解决**:
```bash
# 等待 MySQL 初始化完成
sleep 30

# 重启后端
docker-compose restart backend
```

### 问题 4: 前端无法访问

**解决**:
```bash
# 检查前端容器状态
docker-compose ps frontend

# 查看前端日志
docker-compose logs frontend

# 重启前端
docker-compose restart frontend
```

---

## 🎯 云平台集成检查清单

- [ ] Git 仓库地址已配置
- [ ] Docker Compose 文件已选择
- [ ] 环境变量已配置
- [ ] 端口映射已设置
- [ ] 数据卷已配置
- [ ] 健康检查已启用
- [ ] 自动重启已启用
- [ ] 资源限制已设置
- [ ] 日志收集已启用
- [ ] 监控告警已配置

---

## 📞 技术支持

### 项目仓库
https://github.com/weichengyi/erp

### 文档
- [Docker 部署文档](DOCKER.md)
- [宝塔面板部署](BAOTA_DOCKER.md)
- [快速开始](README_DOCKER_SUMMARY.md)

### 联系方式
如有问题，请提交 GitHub Issue 或联系开发团队。

---

## 🎉 完成！

按照以上配置，云平台可以实现：

1. ✅ 从 Git 仓库自动拉取代码
2. ✅ 自动构建 Docker 镜像
3. ✅ 自动启动容器
4. ✅ 健康检查
5. ✅ 自动重启
6. ✅ 数据持久化

**用户只需要点击"一键部署"，3-5 分钟即可完成！** 🚀
