#!/bin/bash
# 最终推送脚本 - HTTPS 方式

set -e

echo "========================================"
echo "GitHub 推送脚本"
echo "========================================"
echo ""

cd /home/admin/openclaw/workspace/erp-trade

# 修改为 HTTPS URL
echo "🔄 配置远程仓库..."
git remote set-url origin https://github.com/weichengyi/erp.git
echo "✅ 远程仓库：$(git remote -v | grep origin | head -1)"
echo ""

# 显示提交历史
echo "📊 待推送的提交:"
git log --oneline -5
echo ""

# 提示输入
echo "========================================"
echo "请输入 GitHub 认证信息"
echo "========================================"
echo ""
echo "Username: weichengyi"
echo "Password: [粘贴你的 PAT Token]"
echo ""
echo "⚠️ 注意：输入密码时不会显示任何字符，这是正常的"
echo ""

# 执行推送
echo "🚀 开始推送..."
git push -u origin main

echo ""
echo "========================================"
echo "✅ 推送完成!"
echo "========================================"
echo ""
echo "访问仓库：https://github.com/weichengyi/erp"
echo ""
