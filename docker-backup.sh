#!/bin/bash
# Docker 数据备份脚本

set -e

BACKUP_DIR="./backups"
TIMESTAMP=$(date +%Y%m%d_%H%M%S)

echo "========================================"
echo "数据备份"
echo "========================================"

# 创建备份目录
mkdir -p "$BACKUP_DIR"

# 备份 MySQL 数据库
echo "📦 备份 MySQL 数据库..."
docker-compose exec -T mysql mysqldump \
    -u root \
    -perp_root_2026 \
    --databases erp_trade \
    --single-transaction \
    --quick \
    --lock-tables=false \
    > "$BACKUP_DIR/erp_trade_$TIMESTAMP.sql"

echo "✅ 备份完成：$BACKUP_DIR/erp_trade_$TIMESTAMP.sql"
echo ""

# 压缩备份
echo "🗜️  压缩备份文件..."
gzip "$BACKUP_DIR/erp_trade_$TIMESTAMP.sql"
echo "✅ 压缩完成：$BACKUP_DIR/erp_trade_$TIMESTAMP.sql.gz"
echo ""

# 显示备份大小
BACKUP_SIZE=$(du -h "$BACKUP_DIR/erp_trade_$TIMESTAMP.sql.gz" | cut -f1)
echo "📊 备份大小：$BACKUP_SIZE"
echo ""

# 保留最近 7 天的备份
echo "🧹 清理 7 天前的旧备份..."
find "$BACKUP_DIR" -name "erp_trade_*.sql.gz" -mtime +7 -delete
echo "✅ 清理完成"
echo ""

echo "========================================"
echo "备份完成!"
echo "========================================"
