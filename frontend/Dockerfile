# 前端 Dockerfile
FROM node:20-alpine AS builder

WORKDIR /app

# 复制 package.json
COPY package.json .

# 安装依赖 (使用淘宝镜像加速)
RUN npm config set registry https://registry.npmmirror.com && \
    npm install

# 复制源代码
COPY . .

# 编译生产版本
RUN npm run build

# Nginx 运行阶段
FROM nginx:alpine

# 复制自定义 nginx 配置
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 复制编译后的静态文件
COPY --from=builder /app/dist /usr/share/nginx/html

# 暴露端口
EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
