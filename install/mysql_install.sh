#!/bin/bash
# MySQL 8.0 安装脚本 (Ubuntu/Debian)

set -e

echo "========================================"
echo "MySQL 8.0 安装脚本"
echo "========================================"

# 检查是否 root
if [ "$EUID" -ne 0 ]; then 
  echo "请使用 sudo 运行此脚本"
  echo "用法：sudo bash mysql_install.sh"
  exit 1
fi

echo "正在更新软件包列表..."
apt-get update

echo "正在安装 MySQL 8.0..."
DEBIAN_FRONTEND=noninteractive apt-get install -y mysql-server

echo "正在启动 MySQL 服务..."
systemctl start mysql
systemctl enable mysql

echo "正在配置 MySQL..."
# 设置 root 密码为 root123 (生产环境请修改)
mysql -u root <<EOF
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'root123';
FLUSH PRIVILEGES;
EOF

echo "正在创建数据库..."
mysql -u root -proot123 <<EOF
CREATE DATABASE IF NOT EXISTS erp_trade 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

-- 创建应用用户 (可选)
CREATE USER IF NOT EXISTS 'erp_user'@'localhost' IDENTIFIED BY 'erp_password123';
GRANT ALL PRIVILEGES ON erp_trade.* TO 'erp_user'@'localhost';
FLUSH PRIVILEGES;
EOF

echo "正在导入表结构..."
mysql -u root -proot123 erp_trade < /home/admin/openclaw/workspace/erp-trade/database/init.sql

echo ""
echo "========================================"
echo "✅ MySQL 8.0 安装完成!"
echo "========================================"
echo ""
echo "连接信息:"
echo "  主机：localhost"
echo "  端口：3306"
echo "  用户名：root"
echo "  密码：root123"
echo ""
echo "数据库：erp_trade"
echo ""
echo "⚠️ 生产环境请修改密码!"
echo "========================================"
