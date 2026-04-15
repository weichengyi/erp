-- ============================================
-- 贸易行业序列号全流程追踪管理系统
-- 数据库初始化脚本
-- ============================================

CREATE DATABASE IF NOT EXISTS erp_trade 
DEFAULT CHARACTER SET utf8mb4 
DEFAULT COLLATE utf8mb4_unicode_ci;

USE erp_trade;

-- ============================================
-- 1. 系统用户与权限表
-- ============================================

-- 系统用户表
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户 ID',
    username VARCHAR(50) UNIQUE NOT NULL COMMENT '用户名',
    password VARCHAR(255) NOT NULL COMMENT '密码 (加密)',
    real_name VARCHAR(50) COMMENT '真实姓名',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '手机号',
    department VARCHAR(50) COMMENT '部门 (销售部/采购部/管理层/等)',
    role VARCHAR(50) COMMENT '角色 (ADMIN/SALES/PURCHASE/MANAGER/OTHER)',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_login_at DATETIME COMMENT '最后登录时间',
    INDEX idx_department (department),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

-- ============================================
-- 2. 公司档案表 (客户/供应商)
-- ============================================

CREATE TABLE company (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '公司 ID',
    company_name VARCHAR(200) NOT NULL COMMENT '公司全称',
    uniform_credit_code VARCHAR(50) COMMENT '统一信用代码',
    type VARCHAR(20) NOT NULL COMMENT '类型 (CUSTOMER 客户/SUPPLIER 供应商)',
    address VARCHAR(500) COMMENT '地址',
    contact_person VARCHAR(50) COMMENT '主联系人',
    contact_phone VARCHAR(20) COMMENT '联系电话',
    contact_email VARCHAR(100) COMMENT '联系邮箱',
    cooperation_status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '合作状态 (ACTIVE/INACTIVE/POTENTIAL)',
    cooperation_level VARCHAR(20) COMMENT '合作等级 (A/B/C)',
    payment_cycle INT COMMENT '回款周期 (天)',
    parent_company_id BIGINT COMMENT '母公司 ID',
    is_active BOOLEAN DEFAULT TRUE COMMENT '是否启用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建人',
    INDEX idx_type (type),
    INDEX idx_status (cooperation_status),
    INDEX idx_parent (parent_company_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公司档案表';

-- ============================================
-- 3. 公司关联关系表
-- ============================================

CREATE TABLE company_relation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联 ID',
    company_id BIGINT NOT NULL COMMENT '公司 ID',
    related_company_id BIGINT NOT NULL COMMENT '关联公司 ID',
    relation_type VARCHAR(50) COMMENT '关联类型 (PARENT_SUBSIDIARY 母子公司/SIBLING 兄弟公司/RELATED_HOLDING 关联控股/COOPERATION 合作联营/SAME_LEGAL 同一法人/FAMILY_ENTERPRISE 家族企业/GROUP_SUBSIDIARY 集团子公司)',
    relation_desc VARCHAR(500) COMMENT '关联描述',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by BIGINT COMMENT '创建人',
    UNIQUE KEY uk_relation (company_id, related_company_id),
    INDEX idx_company (company_id),
    INDEX idx_related (related_company_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='公司关联关系表';

-- ============================================
-- 4. 联系人档案表
-- ============================================

CREATE TABLE contact_person (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '联系人 ID',
    company_id BIGINT NOT NULL COMMENT '所属公司 ID',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    position VARCHAR(50) COMMENT '职位',
    phone VARCHAR(20) COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    is_primary BOOLEAN DEFAULT FALSE COMMENT '是否主要对接人',
    is_decision_maker BOOLEAN DEFAULT FALSE COMMENT '是否决策人',
    entry_date DATE COMMENT '入职时间',
   离职_date DATE COMMENT '离职时间',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态 (ACTIVE/LEFT/TRANSFERRED)',
    remark VARCHAR(500) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建人',
    INDEX idx_company (company_id),
    INDEX idx_status (status),
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='联系人档案表';

-- ============================================
-- 5. 人员流动记录表
-- ============================================

CREATE TABLE person_movement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '流动记录 ID',
    person_id BIGINT NOT NULL COMMENT '联系人 ID',
    movement_type VARCHAR(20) NOT NULL COMMENT '流动类型 (RESIGN 离职/TRANSFER 转岗/JOIN 入职)',
    from_company_id BIGINT COMMENT '原公司 ID',
    to_company_id BIGINT COMMENT '新公司 ID',
    movement_date DATE NOT NULL COMMENT '流动日期',
    new_position VARCHAR(50) COMMENT '新职位',
    remark VARCHAR(500) COMMENT '备注',
    notified BOOLEAN DEFAULT FALSE COMMENT '是否已通知对应销售/采购',
    notified_at DATETIME COMMENT '通知时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by BIGINT COMMENT '创建人',
    INDEX idx_person (person_id),
    INDEX idx_type (movement_type),
    INDEX idx_date (movement_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='人员流动记录表';

-- ============================================
-- 6. 产品 SN 码表
-- ============================================

CREATE TABLE product_sn (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT 'SN 记录 ID',
    sn_code VARCHAR(100) UNIQUE NOT NULL COMMENT 'SN 码 (一机一码)',
    product_name VARCHAR(200) COMMENT '产品名称',
    product_model VARCHAR(100) COMMENT '产品型号',
    category VARCHAR(100) COMMENT '产品类别',
    production_date DATE COMMENT '生产日期',
    status VARCHAR(20) DEFAULT 'IN_STOCK' COMMENT '状态 (IN_STOCK 在库/SOLD 已售/OUT_OF_STOCK 出库/REPAIR 维修中/SCRAP 报废)',
    current_company_id BIGINT COMMENT '当前所属公司 ID',
    current_contact_id BIGINT COMMENT '当前联系人 ID',
    warehouse_location VARCHAR(200) COMMENT '仓库位置',
    remark VARCHAR(500) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_sn (sn_code),
    INDEX idx_status (status),
    INDEX idx_company (current_company_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='产品 SN 码表';

-- ============================================
-- 7. 资产编码表
-- ============================================

CREATE TABLE asset (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '资产 ID',
    asset_code VARCHAR(20) UNIQUE NOT NULL COMMENT '资产编码 (YYYYMMDD+4 位)',
    asset_name VARCHAR(200) NOT NULL COMMENT '资产名称',
    asset_type VARCHAR(50) COMMENT '资产类型',
    sn_id BIGINT COMMENT '关联的 SN 记录 ID',
    company_id BIGINT COMMENT '所属公司 ID',
    purchase_date DATE COMMENT '购置日期',
    purchase_price DECIMAL(10,2) COMMENT '购置价格',
    status VARCHAR(20) DEFAULT 'ACTIVE' COMMENT '状态 (ACTIVE/INACTIVE/TRANSFERRED/SCRAP)',
    location VARCHAR(200) COMMENT '存放位置',
    responsible_person VARCHAR(50) COMMENT '负责人',
    remark VARCHAR(500) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建人',
    INDEX idx_asset_code (asset_code),
    INDEX idx_sn (sn_id),
    INDEX idx_company (company_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资产编码表';

-- ============================================
-- 8. SN-资产绑定关系表
-- ============================================

CREATE TABLE sn_asset_binding (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '绑定 ID',
    sn_id BIGINT NOT NULL COMMENT 'SN 记录 ID',
    asset_id BIGINT NOT NULL COMMENT '资产 ID',
    binding_date DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '绑定时间',
    binding_type VARCHAR(20) COMMENT '绑定类型 (INITIAL 初始绑定/REBIND 重新绑定)',
    remark VARCHAR(500) COMMENT '备注',
    created_by BIGINT COMMENT '操作人',
    UNIQUE KEY uk_sn_asset (sn_id, asset_id),
    INDEX idx_sn (sn_id),
    INDEX idx_asset (asset_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='SN-资产绑定关系表';

-- ============================================
-- 9. 销售订单表
-- ============================================

CREATE TABLE sales_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单 ID',
    order_no VARCHAR(50) UNIQUE NOT NULL COMMENT '订单编号',
    customer_id BIGINT NOT NULL COMMENT '客户公司 ID',
    contact_person_id BIGINT COMMENT '对接联系人 ID',
    order_date DATE NOT NULL COMMENT '订单日期',
    total_amount DECIMAL(10,2) DEFAULT 0 COMMENT '订单总额',
    paid_amount DECIMAL(10,2) DEFAULT 0 COMMENT '已付金额',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态 (PENDING/CONFIRMED/SHIPPED/COMPLETED/CANCELLED)',
    payment_status VARCHAR(20) DEFAULT 'UNPAID' COMMENT '付款状态 (UNPAID/PARTIAL/PAID)',
    shipping_address VARCHAR(500) COMMENT '收货地址',
    remark VARCHAR(1000) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建人 (销售)',
    INDEX idx_order_no (order_no),
    INDEX idx_customer (customer_id),
    INDEX idx_status (status),
    INDEX idx_date (order_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售订单表';

-- ============================================
-- 10. 销售订单明细表
-- ============================================

CREATE TABLE sales_order_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '明细 ID',
    order_id BIGINT NOT NULL COMMENT '订单 ID',
    product_sn_id BIGINT COMMENT '产品 SN 记录 ID',
    product_name VARCHAR(200) COMMENT '产品名称',
    product_model VARCHAR(100) COMMENT '产品型号',
    quantity INT DEFAULT 1 COMMENT '数量',
    unit_price DECIMAL(10,2) COMMENT '单价',
    subtotal DECIMAL(10,2) COMMENT '小计',
    sn_code VARCHAR(100) COMMENT 'SN 码 (冗余字段，方便查询)',
    asset_code VARCHAR(20) COMMENT '资产编码 (冗余字段)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_order (order_id),
    INDEX idx_sn (product_sn_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售订单明细表';

-- ============================================
-- 11. 采购订单表
-- ============================================

CREATE TABLE purchase_order (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '订单 ID',
    order_no VARCHAR(50) UNIQUE NOT NULL COMMENT '订单编号',
    supplier_id BIGINT NOT NULL COMMENT '供应商 ID',
    contact_person_id BIGINT COMMENT '对接联系人 ID',
    order_date DATE NOT NULL COMMENT '订单日期',
    total_amount DECIMAL(10,2) DEFAULT 0 COMMENT '订单总额',
    paid_amount DECIMAL(10,2) DEFAULT 0 COMMENT '已付金额',
    status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态 (PENDING/CONFIRMED/RECEIVED/COMPLETED/CANCELLED)',
    payment_status VARCHAR(20) DEFAULT 'UNPAID' COMMENT '付款状态',
    delivery_date DATE COMMENT '交货日期',
    remark VARCHAR(1000) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建人 (采购)',
    INDEX idx_order_no (order_no),
    INDEX idx_supplier (supplier_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购订单表';

-- ============================================
-- 12. 出入库记录表
-- ============================================

CREATE TABLE stock_in_out (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录 ID',
    stock_no VARCHAR(50) UNIQUE NOT NULL COMMENT '出入库单号',
    type VARCHAR(20) NOT NULL COMMENT '类型 (IN 入库/OUT 出库)',
    business_type VARCHAR(20) COMMENT '业务类型 (PURCHASE 采购入库/SALES 销售出库/RETURN 退货/TRANSFER 调拨)',
    related_order_no VARCHAR(50) COMMENT '关联订单号',
    company_id BIGINT COMMENT '关联公司 ID',
    product_sn_id BIGINT COMMENT '产品 SN 记录 ID',
    sn_code VARCHAR(100) COMMENT 'SN 码',
    quantity INT DEFAULT 1 COMMENT '数量',
    warehouse_location VARCHAR(200) COMMENT '仓库位置',
    operator VARCHAR(50) COMMENT '操作人',
    operation_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    remark VARCHAR(500) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    created_by BIGINT COMMENT '创建人',
    INDEX idx_stock_no (stock_no),
    INDEX idx_type (type),
    INDEX idx_sn (sn_code),
    INDEX idx_time (operation_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出入库记录表';

-- ============================================
-- 13. 售后服务单表
-- ============================================

CREATE TABLE after_sales (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '售后单 ID',
    service_no VARCHAR(50) UNIQUE NOT NULL COMMENT '服务单号',
    customer_id BIGINT NOT NULL COMMENT '客户公司 ID',
    contact_person_id BIGINT COMMENT '联系人 ID',
    product_sn_id BIGINT COMMENT '产品 SN 记录 ID',
    sn_code VARCHAR(100) COMMENT 'SN 码',
    service_type VARCHAR(50) COMMENT '服务类型 (INSTALL 安装/REPAIR 维修/MAINTENANCE 维护/RETURN 退货)',
    service_status VARCHAR(20) DEFAULT 'PENDING' COMMENT '状态 (PENDING/PROCESSING/COMPLETED/CLOSED)',
    problem_desc VARCHAR(1000) COMMENT '问题描述',
    solution_desc VARCHAR(1000) COMMENT '解决方案',
    service_result VARCHAR(20) COMMENT '服务结果 (RESOLVED/UNRESOLVED/PENDING_PARTS)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    completed_at DATETIME COMMENT '完成时间',
    created_by BIGINT COMMENT '创建人',
    handler BIGINT COMMENT '处理人',
    INDEX idx_service_no (service_no),
    INDEX idx_customer (customer_id),
    INDEX idx_sn (sn_code),
    INDEX idx_status (service_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='售后服务单表';

-- ============================================
-- 14. 系统日志表
-- ============================================

CREATE TABLE sys_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT COMMENT '用户 ID',
    operation VARCHAR(100) COMMENT '操作',
    module VARCHAR(50) COMMENT '模块',
    method VARCHAR(20) COMMENT '请求方法',
    url VARCHAR(500) COMMENT '请求 URL',
    params TEXT COMMENT '请求参数',
    result TEXT COMMENT '返回结果',
    ip_address VARCHAR(50) COMMENT 'IP 地址',
    time_taken BIGINT COMMENT '耗时 (ms)',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    INDEX idx_user (user_id),
    INDEX idx_time (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志表';

-- ============================================
-- 初始化数据
-- ============================================

-- 插入默认管理员账户 (密码：admin123, BCrypt 加密)
INSERT INTO sys_user (username, password, real_name, department, role, is_active) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5ULUDsGkaHjQYv3c9qDqI0K', '系统管理员', '管理层', 'ADMIN', TRUE);

-- 插入测试销售用户
INSERT INTO sys_user (username, password, real_name, department, role, is_active) 
VALUES ('sales01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5ULUDsGkaHjQYv3c9qDqI0K', '张三', '销售部', 'SALES', TRUE);

-- 插入测试采购用户
INSERT INTO sys_user (username, password, real_name, department, role, is_active) 
VALUES ('purchase01', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5ULUDsGkaHjQYv3c9qDqI0K', '李四', '采购部', 'PURCHASE', TRUE);

-- ============================================
-- 视图：关联公司视图
-- ============================================

CREATE OR REPLACE VIEW v_company_relation_view AS
SELECT 
    c.id AS company_id,
    c.company_name,
    c.type,
    c.cooperation_status,
    rc.related_company_id,
    rc2.company_name AS related_company_name,
    rc.relation_type,
    rc.relation_desc
FROM company c
LEFT JOIN company_relation rc ON c.id = rc.company_id
LEFT JOIN company rc2 ON rc.related_company_id = rc2.id;

-- ============================================
-- 视图：人员流动完整视图
-- ============================================

CREATE OR REPLACE VIEW v_person_movement_view AS
SELECT 
    cp.id AS person_id,
    cp.name,
    cp.position,
    cp.company_id,
    c.company_name AS current_company,
    cp.status AS person_status,
    pm.movement_type,
    pm.from_company_id,
    fc.company_name AS from_company_name,
    pm.to_company_id,
    tc.company_name AS to_company_name,
    pm.movement_date,
    pm.new_position
FROM contact_person cp
LEFT JOIN company c ON cp.company_id = c.id
LEFT JOIN person_movement pm ON cp.id = pm.person_id
LEFT JOIN company fc ON pm.from_company_id = fc.id
LEFT JOIN company tc ON pm.to_company_id = tc.id
ORDER BY cp.id, pm.movement_date DESC;

-- ============================================
-- 视图：SN 码全流程溯源视图
-- ============================================

CREATE OR REPLACE VIEW v_sn_trace_view AS
SELECT 
    ps.sn_code,
    ps.product_name,
    ps.product_model,
    ps.status AS sn_status,
    ps.current_company_id,
    c.company_name AS current_company,
    cp.name AS current_contact,
    a.asset_code,
    a.asset_name,
    a.status AS asset_status,
    so.order_no AS sales_order_no,
    so.order_date,
    sto.stock_no,
    sto.type AS stock_type,
    sto.operation_time
FROM product_sn ps
LEFT JOIN company c ON ps.current_company_id = c.id
LEFT JOIN contact_person cp ON ps.current_contact_id = cp.id
LEFT JOIN asset a ON ps.id = a.sn_id
LEFT JOIN sales_order_item soi ON ps.id = soi.product_sn_id
LEFT JOIN sales_order so ON soi.order_id = so.id
LEFT JOIN stock_in_out sto ON ps.sn_code = sto.sn_code
ORDER BY ps.sn_code, sto.operation_time DESC;
