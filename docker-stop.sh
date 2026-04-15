#!/bin/bash
# Docker 停止服务脚本

echo "========================================"
echo "停止 Docker 服务"
echo "========================================"

cd "$(dirname "$0")"

# 停止并删除容器
docker-compose down

echo ""
echo "✅ 服务已停止"
echo ""
echo "数据保留在数据卷中，不会丢失"
echo "如需删除数据卷，执行：docker-compose down -v"
