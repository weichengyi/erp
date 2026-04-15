#!/bin/bash
# Docker 数据恢复脚本

set -e

echo "========================================"
echo "数据恢复"
echo "========================================"
echo ""

# 检查参数
if [ $# -eq 0 ]; then
    echo "用法：$0 <备份文件.sql.gz>"
    echo ""
    echo "可用的备份文件:"
    ls -lh ./backups/*.sql.gz 2>/dev/null || echo "  未找到备份文件"
    exit 1
fi

BACKUP_FILE="$1"

# 检查文件是否存在
if [ ! -f "$BACKUP_FILE" ]; then
    echo "❌ 备份文件不存在：$BACKUP_FILE"
    exit 1
fi

echo "📦 准备恢复：$BACKUP_FILE"
echo ""

# 解压并恢复
if [[ "$BACKUP_FILE" == *.gz ]]; then
    echo "🗜️  解压并导入数据库..."
    gunzip -c "$BACKUP_FILE" | docker-compose exec -T mysql mysql -u root -perp_root_2026
else
    echo "📥 导入数据库..."
    docker-compose exec -T mysql mysql -u root -perp_root_2026 < "$BACKUP_FILE"
fi

echo ""
echo "✅ 数据恢复完成!"
echo ""
echo "请重启服务以确保数据生效:"
echo "  docker-compose restart"
