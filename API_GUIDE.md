# API 接口使用指南

## 基础 URL

```
http://localhost:8080/api
```

---

## 1. 公司管理 API

### 1.1 获取公司列表

```http
GET /api/companies
```

**查询参数:**
- `type` (可选): `CUSTOMER` 或 `SUPPLIER`
- `status` (可选): `ACTIVE`, `INACTIVE`, `POTENTIAL`

**示例:**
```bash
curl http://localhost:8080/api/companies?type=CUSTOMER
```

**响应:**
```json
[
  {
    "id": 1,
    "companyName": "北京科技有限公司",
    "type": "CUSTOMER",
    "cooperationStatus": "ACTIVE",
    "contactPerson": "张三",
    "contactPhone": "010-12345678",
    ...
  }
]
```

### 1.2 创建公司

```http
POST /api/companies
Content-Type: application/json
```

**请求体:**
```json
{
  "companyName": "上海贸易公司",
  "uniformCreditCode": "91310000MA1234567X",
  "type": "CUSTOMER",
  "address": "上海市浦东新区",
  "contactPerson": "李四",
  "contactPhone": "021-87654321",
  "contactEmail": "lisi@shtrade.com",
  "cooperationStatus": "ACTIVE",
  "cooperationLevel": "A",
  "paymentCycle": 30
}
```

### 1.3 搜索公司

```http
GET /api/companies/search?keyword=科技
```

---

## 2. SN 码管理 API

### 2.1 创建 SN 码记录

```http
POST /api/product-sns
Content-Type: application/json
```

**请求体:**
```json
{
  "snCode": "SN20260415001",
  "productName": "企业级服务器",
  "productModel": "SRV-2024-PRO",
  "category": "硬件设备",
  "productionDate": "2026-04-10",
  "status": "IN_STOCK",
  "warehouseLocation": "北京仓-A 区 -001"
}
```

### 2.2 溯源查询

```http
GET /api/product-sns/trace/{snCode}
```

**示例:**
```bash
curl http://localhost:8080/api/product-sns/trace/SN20260415001
```

---

## 3. 资产管理 API

### 3.1 生成资产编码

```http
GET /api/assets/generate-code
```

**响应:**
```
202604150001
```

### 3.2 创建资产

```http
POST /api/assets
Content-Type: application/json
```

**请求体:**
```json
{
  "assetName": "办公电脑 -001",
  "assetType": "电子设备",
  "snId": 1,
  "purchaseDate": "2026-04-15",
  "purchasePrice": 8500.00,
  "location": "北京办公室 -3 楼",
  "responsiblePerson": "王五"
}
```

> 如果不传 `assetCode`，系统会自动生成 (YYYYMMDD+4 位)

### 3.3 根据编码查询

```http
GET /api/assets/code/202604150001
```

---

## 4. 错误响应

**404 Not Found:**
```json
{
  "status": 404,
  "message": "未找到资源"
}
```

**500 Internal Server Error:**
```json
{
  "status": 500,
  "message": "服务器内部错误",
  "error": "详细错误信息"
}
```

---

## 5. 完整测试示例

### 场景：新增客户公司并添加联系人

```bash
# 1. 创建客户公司
curl -X POST http://localhost:8080/api/companies \
  -H "Content-Type: application/json" \
  -d '{
    "companyName": "深圳电子科技有限公司",
    "type": "CUSTOMER",
    "contactPerson": "赵六",
    "contactPhone": "0755-11111111",
    "cooperationStatus": "ACTIVE"
  }'

# 返回：{"id":1,"companyName":"深圳电子科技有限公司",...}

# 2. 创建产品 SN 码
curl -X POST http://localhost:8080/api/product-sns \
  -H "Content-Type: application/json" \
  -d '{
    "snCode": "SN20260415001",
    "productName": "智能控制器",
    "productModel": "CTRL-2024",
    "status": "IN_STOCK"
  }'

# 3. 创建资产 (自动编码)
curl -X POST http://localhost:8080/api/assets \
  -H "Content-Type: application/json" \
  -d '{
    "assetName": "智能控制器 -001",
    "assetType": "产品设备",
    "snId": 1
  }'

# 返回：{"id":1,"assetCode":"202604150001",...}
```

---

## 6. 使用 Postman 测试

1. 导入集合：创建新的 Collection
2. 设置 Base URL: `http://localhost:8080/api`
3. 添加环境变量便于切换测试/生产环境

---

## 7. 注意事项

1. **日期格式**: `yyyy-MM-dd` (如：`2026-04-15`)
2. **金额精度**: 小数点后 2 位
3. **枚举值**: 严格按照文档中的枚举值传递
4. **跨域**: 已配置允许所有来源跨域访问
