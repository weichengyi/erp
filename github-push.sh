#!/bin/bash
# GitHub 推送脚本

set -e

echo "========================================"
echo "推送到 GitHub"
echo "========================================"
echo ""

cd "$(dirname "$0")"

# 检查远程仓库
REMOTE_URL=$(git config --get remote.origin.url)
if [ -z "$REMOTE_URL" ]; then
    echo "❌ 未配置远程仓库"
    echo "正在添加远程仓库..."
    git remote add origin https://github.com/weichengyi/erp.git
fi

echo "✅ 远程仓库：$REMOTE_URL"
echo ""

# 检查分支
CURRENT_BRANCH=$(git branch --show-current)
if [ "$CURRENT_BRANCH" != "main" ]; then
    echo "📌 重命名分支为 main..."
    git branch -M main
fi

echo "✅ 当前分支：main"
echo ""

# 显示提交历史
echo "📊 提交历史:"
git log --oneline -5
echo ""

# 推送选项
echo "选择推送方式:"
echo "1. 使用 Personal Access Token (推荐)"
echo "2. 使用 SSH 密钥"
echo "3. 取消"
echo ""
read -p "请选择 (1-3): " choice

case $choice in
    1)
        echo ""
        echo "========================================"
        echo "使用 Personal Access Token 推送"
        echo "========================================"
        echo ""
        echo "📝 请按以下步骤操作:"
        echo ""
        echo "1. 访问 https://github.com/settings/tokens"
        echo "2. 点击 'Generate new token (classic)'"
        echo "3. 勾选 'repo' 权限"
        echo "4. 生成并复制 Token"
        echo ""
        read -p "按回车键继续..."
        
        echo ""
        echo "🚀 开始推送..."
        echo ""
        echo "当提示输入密码时，粘贴你的 PAT Token"
        echo ""
        
        git push -u origin main
        
        echo ""
        echo "✅ 推送完成!"
        echo ""
        echo "访问仓库：https://github.com/weichengyi/erp"
        ;;
        
    2)
        echo ""
        echo "========================================"
        echo "使用 SSH 密钥推送"
        echo "========================================"
        echo ""
        
        # 检查 SSH 密钥
        if [ ! -f ~/.ssh/id_ed25519.pub ]; then
            echo "🔑 生成 SSH 密钥..."
            ssh-keygen -t ed25519 -C "your_email@example.com"
        fi
        
        echo ""
        echo "📋 复制以下公钥内容:"
        echo ""
        cat ~/.ssh/id_ed25519.pub
        echo ""
        
        echo "🌐 然后访问:"
        echo "https://github.com/settings/keys"
        echo ""
        echo "点击 'New SSH key' 并粘贴公钥"
        echo ""
        read -p "按回车键继续..."
        
        # 修改为 SSH URL
        echo ""
        echo "🔄 修改远程仓库为 SSH 地址..."
        git remote set-url origin git@github.com:weichengyi/erp.git
        
        echo "✅ 远程仓库：$(git config --get remote.origin.url)"
        echo ""
        
        echo "🚀 开始推送..."
        git push -u origin main
        
        echo ""
        echo "✅ 推送完成!"
        echo ""
        echo "访问仓库：https://github.com/weichengyi/erp"
        ;;
        
    3)
        echo "❌ 已取消"
        exit 0
        ;;
        
    *)
        echo "❌ 无效选择"
        exit 1
        ;;
esac

echo ""
echo "========================================"
echo "下一步:"
echo "1. 访问 https://github.com/weichengyi/erp"
echo "2. 检查所有文件已上传"
echo "3. 更新仓库说明"
echo "========================================"
