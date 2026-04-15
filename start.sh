#!/bin/bash
# 贸易行业序列号追踪管理系统 - 一键启动脚本

set -e

echo "========================================"
echo "贸易行业序列号追踪管理系统"
echo "========================================"
echo ""

# 检查 MySQL 状态
echo "📊 检查 MySQL 状态..."
if systemctl is-active --quiet mysql; then
    echo "✅ MySQL 运行正常"
else
    echo "⚠️  MySQL 未运行，正在启动..."
    sudo systemctl start mysql
    echo "✅ MySQL 已启动"
fi
echo ""

# 检查数据库
echo "📊 检查数据库..."
if mysql -u root -proot123 -e "USE erp_trade;" 2>/dev/null; then
    echo "✅ 数据库 erp_trade 存在"
else
    echo "❌ 数据库不存在，正在创建..."
    mysql -u root -proot123 < database/init.sql
    echo "✅ 数据库已创建"
fi
echo ""

# 启动后端
echo "🚀 启动后端服务..."
cd /home/admin/openclaw/workspace/erp-trade

# 检查是否已编译
if [ ! -f "target/*.jar" ]; then
    echo "📦 正在编译项目..."
    mvn clean package -DskipTests
fi

echo "🔹 后端将在 http://localhost:8080 启动"
echo "🔹 API 文档：http://localhost:8080/api"
echo ""

# 后台启动后端
nohup mvn spring-boot:run > backend.log 2>&1 &
BACKEND_PID=$!
echo "✅ 后端服务已启动 (PID: $BACKEND_PID)"

# 等待后端启动
echo "⏳ 等待后端启动..."
sleep 10

# 启动前端
echo "🚀 启动前端服务..."
cd /home/admin/openclaw/workspace/erp-trade/frontend

# 检查 node_modules
if [ ! -d "node_modules" ]; then
    echo "📦 安装前端依赖..."
    npm install
fi

echo "🔹 前端将在 http://localhost:3000 启动"
echo ""

# 后台启动前端
nohup npm run dev > frontend.log 2>&1 &
FRONTEND_PID=$!
echo "✅ 前端服务已启动 (PID: $FRONTEND_PID)"

echo ""
echo "========================================"
echo "✅ 系统启动完成!"
echo "========================================"
echo ""
echo "访问地址:"
echo "  前端：http://localhost:3000"
echo "  后端：http://localhost:8080"
echo "  API:  http://localhost:8080/api"
echo ""
echo "默认账户:"
echo "  用户名：admin"
echo "  密码：admin123"
echo ""
echo "停止服务:"
echo "  kill $BACKEND_PID  # 停止后端"
echo "  kill $FRONTEND_PID # 停止前端"
echo ""
echo "查看日志:"
echo "  tail -f backend.log"
echo "  tail -f frontend.log"
echo "========================================"
