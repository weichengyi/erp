#!/bin/bash
# Docker 一键部署脚本

set -e

echo "========================================"
echo "贸易行业序列号追踪管理系统"
echo "Docker 一键部署"
echo "========================================"
echo ""

# 检查 Docker
if ! command -v docker &> /dev/null; then
    echo "❌ Docker 未安装，请先安装 Docker"
    exit 1
fi

# 检查 Docker Compose
if ! command -v docker-compose &> /dev/null; then
    echo "❌ Docker Compose 未安装，请先安装"
    exit 1
fi

echo "✅ Docker 版本：$(docker --version)"
echo "✅ Docker Compose 版本：$(docker-compose --version)"
echo ""

# 进入项目目录
cd "$(dirname "$0")"

# 停止旧容器
echo "🔄 停止旧容器..."
docker-compose down 2>/dev/null || true
echo ""

# 构建镜像
echo "🔨 构建 Docker 镜像..."
echo "   这可能需要 5-10 分钟，请耐心等待..."
echo ""
docker-compose build
echo ""

# 启动服务
echo "🚀 启动服务..."
docker-compose up -d
echo ""

# 等待服务启动
echo "⏳ 等待服务启动..."
sleep 15

# 检查服务状态
echo "📊 检查服务状态..."
docker-compose ps
echo ""

# 显示访问信息
echo "========================================"
echo "✅ 部署完成!"
echo "========================================"
echo ""
echo "访问地址:"
echo "  前端：http://localhost"
echo "  后端：http://localhost:8080"
echo "  数据库：localhost:3306"
echo ""
echo "默认账户:"
echo "  用户名：admin"
echo "  密码：admin123"
echo ""
echo "数据库连接信息:"
echo "  主机：localhost"
echo "  端口：3306"
echo "  数据库：erp_trade"
echo "  用户名：erp_user"
echo "  密码：erp_password_2026"
echo ""
echo "容器管理命令:"
echo "  查看日志：docker-compose logs -f"
echo "  停止服务：docker-compose down"
echo "  重启服务：docker-compose restart"
echo "  查看状态：docker-compose ps"
echo ""
echo "========================================"
