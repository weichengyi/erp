<template>
  <div class="reports">
    <h2 class="page-title">报表统计</h2>
    
    <!-- 数据导出 -->
    <el-card class="export-card">
      <template #header>
        <div class="card-header">
          <span><el-icon><Download /></el-icon> 数据导出</span>
        </div>
      </template>
      
      <el-row :gutter="20">
        <el-col :span="8">
          <el-button type="primary" @click="exportCompanies">
            <el-icon><Document /></el-icon>
            导出公司数据
          </el-button>
          <p class="export-desc">导出所有客户/供应商信息 (Excel)</p>
        </el-col>
        
        <el-col :span="8">
          <el-button type="success" @click="exportAssets">
            <el-icon><Document /></el-icon>
            导出资产数据
          </el-button>
          <p class="export-desc">导出所有资产编码信息 (Excel)</p>
        </el-col>
        
        <el-col :span="8">
          <el-button type="warning" @click="exportProductSns">
            <el-icon><Document /></el-icon>
            导出 SN 码数据
          </el-button>
          <p class="export-desc">导出所有产品 SN 码记录 (Excel)</p>
        </el-col>
      </el-row>
    </el-card>
    
    <!-- 统计图表 -->
    <el-row :gutter="20" class="charts-row">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>月度销售趋势</span>
          </template>
          <div ref="salesTrendRef" class="chart"></div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>公司关联关系分析</span>
          </template>
          <div ref="relationRef" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>人员流动统计</span>
          </template>
          <div ref="movementRef" class="chart"></div>
        </el-card>
      </el-col>
      
      <el-col :span="12">
        <el-card>
          <template #header>
            <span>产品状态分布</span>
          </template>
          <div ref="productStatusRef" class="chart"></div>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 详细统计表格 -->
    <el-card class="stats-table">
      <template #header>
        <span>详细统计数据</span>
      </template>
      
      <el-table :data="statsData" style="width: 100%" border>
        <el-table-column prop="category" label="统计类别" width="200" />
        <el-table-column prop="total" label="总数" width="120" />
        <el-table-column prop="active" label="活跃/在库" width="120" />
        <el-table-column prop="inactive" label="停用/出库" width="120" />
        <el-table-column prop="trend" label="趋势" width="150">
          <template #default="{ row }">
            <el-tag :type="row.trend > 0 ? 'success' : 'danger'">
              {{ row.trend > 0 ? '+' : '' }}{{ row.trend }}%
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" />
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'
import * as XLSX from 'xlsx'
import { companyApi, assetApi, productSnApi } from '../../api'

const salesTrendRef = ref(null)
const relationRef = ref(null)
const movementRef = ref(null)
const productStatusRef = ref(null)

const statsData = ref([
  { category: '公司总数', total: 156, active: 142, inactive: 14, trend: 12.5, remark: '客户 98 家，供应商 58 家' },
  { category: '产品 SN 码', total: 1280, active: 890, inactive: 390, trend: 8.3, remark: '在库 890，已售 390' },
  { category: '资产编码', total: 890, active: 856, inactive: 34, trend: 15.2, remark: '正常使用 856 个' },
  { category: '联系人', total: 456, active: 398, inactive: 58, trend: -5.6, remark: '本月离职 12 人' },
  { category: '销售订单', total: 423, active: 389, inactive: 34, trend: 22.1, remark: '本月新增 45 单' }
])

// 导出公司数据
const exportCompanies = async () => {
  try {
    const companies = await companyApi.getAll()
    const data = companies.map(c => ({
      '公司 ID': c.id,
      '公司名称': c.companyName,
      '类型': c.type === 'CUSTOMER' ? '客户' : '供应商',
      '统一信用代码': c.uniformCreditCode || '',
      '联系人': c.contactPerson || '',
      '电话': c.contactPhone || '',
      '邮箱': c.contactEmail || '',
      '合作状态': c.cooperationStatus || '',
      '合作等级': c.cooperationLevel || '',
      '回款周期 (天)': c.paymentCycle || '',
      '创建时间': c.createdAt
    }))
    
    const ws = XLSX.utils.json_to_sheet(data)
    const wb = XLSX.utils.book_new()
    XLSX.utils.book_append_sheet(wb, ws, '公司数据')
    XLSX.writeFile(wb, `公司数据_${new Date().toISOString().split('T')[0]}.xlsx`)
    
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

// 导出资产数据
const exportAssets = async () => {
  try {
    const assets = await assetApi.getAll()
    const data = assets.map(a => ({
      '资产编码': a.assetCode,
      '资产名称': a.assetName,
      '资产类型': a.assetType || '',
      '购置日期': a.purchaseDate || '',
      '购置价格': a.purchasePrice || '',
      '状态': a.status,
      '存放位置': a.location || '',
      '负责人': a.responsiblePerson || '',
      '备注': a.remark || '',
      '创建时间': a.createdAt
    }))
    
    const ws = XLSX.utils.json_to_sheet(data)
    const wb = XLSX.utils.book_new()
    XLSX.utils.book_append_sheet(wb, ws, '资产数据')
    XLSX.writeFile(wb, `资产数据_${new Date().toISOString().split('T')[0]}.xlsx`)
    
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

// 导出 SN 码数据
const exportProductSns = async () => {
  try {
    const sns = await productSnApi.getAll()
    const data = sns.map(s => ({
      'SN 码': s.snCode,
      '产品名称': s.productName || '',
      '产品型号': s.productModel || '',
      '类别': s.category || '',
      '生产日期': s.productionDate || '',
      '状态': s.status,
      '仓库位置': s.warehouseLocation || '',
      '备注': s.remark || '',
      '创建时间': s.createdAt
    }))
    
    const ws = XLSX.utils.json_to_sheet(data)
    const wb = XLSX.utils.book_new()
    XLSX.utils.book_append_sheet(wb, ws, 'SN 码数据')
    XLSX.writeFile(wb, `SN 码数据_${new Date().toISOString().split('T')[0]}.xlsx`)
    
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

onMounted(() => {
  // 月度销售趋势图
  const salesChart = echarts.init(salesTrendRef.value)
  salesChart.setOption({
    tooltip: { trigger: 'axis' },
    xAxis: {
      type: 'category',
      data: ['1 月', '2 月', '3 月', '4 月', '5 月', '6 月']
    },
    yAxis: { type: 'value' },
    series: [{
      data: [82, 93, 105, 125, 145, 167],
      type: 'line',
      smooth: true,
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(52, 152, 219, 0.3)' },
          { offset: 1, color: 'rgba(52, 152, 219, 0.01)' }
        ])
      },
      itemStyle: { color: '#3498db' }
    }]
  })
  
  // 公司关联关系图
  const relationChart = echarts.init(relationRef.value)
  relationChart.setOption({
    tooltip: { trigger: 'item' },
    legend: { orient: 'vertical', left: 'left' },
    series: [{
      name: '关联类型',
      type: 'pie',
      radius: '50%',
      data: [
        { value: 45, name: '母子公司' },
        { value: 32, name: '兄弟公司' },
        { value: 28, name: '关联控股' },
        { value: 25, name: '同一法人' },
        { value: 18, name: '家族企业' },
        { value: 8, name: '集团子公司' }
      ],
      emphasis: {
        itemStyle: {
          shadowBlur: 10,
          shadowOffsetX: 0,
          shadowColor: 'rgba(0, 0, 0, 0.5)'
        }
      }
    }]
  })
  
  // 人员流动统计
  const movementChart = echarts.init(movementRef.value)
  movementChart.setOption({
    tooltip: { trigger: 'axis' },
    legend: { data: ['入职', '离职', '转岗'] },
    xAxis: {
      type: 'category',
      data: ['1 月', '2 月', '3 月', '4 月', '5 月', '6 月']
    },
    yAxis: { type: 'value' },
    series: [
      { name: '入职', type: 'bar', data: [12, 8, 15, 10, 14, 11], itemStyle: { color: '#2ecc71' } },
      { name: '离职', type: 'bar', data: [5, 3, 8, 4, 6, 5], itemStyle: { color: '#e74c3c' } },
      { name: '转岗', type: 'bar', data: [3, 2, 4, 3, 5, 2], itemStyle: { color: '#f39c12' } }
    ]
  })
  
  // 产品状态分布
  const productStatusChart = echarts.init(productStatusRef.value)
  productStatusChart.setOption({
    tooltip: { trigger: 'item' },
    series: [{
      name: '产品状态',
      type: 'pie',
      radius: ['40%', '70%'],
      avoidLabelOverlap: false,
      itemStyle: {
        borderRadius: 10,
        borderColor: '#fff',
        borderWidth: 2
      },
      label: { show: false, position: 'center' },
      emphasis: {
        label: { show: true, fontSize: 18, fontWeight: 'bold' }
      },
      labelLine: { show: false },
      data: [
        { value: 680, name: '在库' },
        { value: 390, name: '已售' },
        { value: 85, name: '出库' },
        { value: 20, name: '维修中' },
        { value: 5, name: '报废' }
      ]
    }]
  })
  
  window.addEventListener('resize', () => {
    salesChart.resize()
    relationChart.resize()
    movementChart.resize()
    productStatusChart.resize()
  })
})
</script>

<style scoped>
.reports {
  padding: 20px;
}

.page-title {
  margin-bottom: 20px;
  color: #2c3e50;
}

.export-card {
  margin-bottom: 20px;
}

.export-desc {
  margin-top: 10px;
  color: #7f8c8d;
  font-size: 13px;
}

.charts-row {
  margin-bottom: 20px;
}

.chart {
  height: 300px;
}

.stats-table {
  margin-top: 20px;
}

.card-header span {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
