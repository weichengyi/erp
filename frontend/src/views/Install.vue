<template>
  <div class="install-container">
    <div class="install-box">
      <!-- 步骤指示器 -->
      <div class="install-steps">
        <div class="step" :class="{ active: currentStep === 1, completed: currentStep > 1 }">
          <div class="step-number">1</div>
          <div class="step-text">数据库配置</div>
        </div>
        <div class="step-line" :class="{ active: currentStep > 1 }"></div>
        <div class="step" :class="{ active: currentStep === 2, completed: currentStep > 2 }">
          <div class="step-number">2</div>
          <div class="step-text">管理员账户</div>
        </div>
        <div class="step-line" :class="{ active: currentStep > 2 }"></div>
        <div class="step" :class="{ active: currentStep === 3 }">
          <div class="step-number">3</div>
          <div class="step-text">完成安装</div>
        </div>
      </div>

      <!-- 步骤 1：数据库配置 -->
      <div v-if="currentStep === 1" class="step-content">
        <h2>📦 数据库配置</h2>
        <p class="step-desc">配置 MySQL 数据库连接信息</p>

        <el-form ref="dbFormRef" :model="dbConfig" :rules="dbRules" class="install-form">
          <el-form-item label="数据库主机" prop="host">
            <el-input v-model="dbConfig.host" placeholder="例如：mysql 或 127.0.0.1">
              <template #prefix><el-icon><Connection /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item label="端口" prop="port">
            <el-input-number v-model="dbConfig.port" :min="1" :max="65535" style="width: 100%" />
          </el-form-item>

          <el-form-item label="数据库名" prop="database">
            <el-input v-model="dbConfig.database" placeholder="例如：erp_trade">
              <template #prefix><el-icon><Folder /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item label="用户名" prop="username">
            <el-input v-model="dbConfig.username" placeholder="数据库用户名">
              <template #prefix><el-icon><User /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item label="密码" prop="password">
            <el-input v-model="dbConfig.password" type="password" show-password placeholder="数据库密码">
              <template #prefix><el-icon><Lock /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item>
            <el-button type="primary" :loading="testingConnection" @click="testConnection">
              测试连接
            </el-button>
            <el-tag v-if="connectionStatus === 'success'" type="success">连接成功</el-tag>
            <el-tag v-if="connectionStatus === 'error'" type="danger">连接失败</el-tag>
          </el-form-item>
        </el-form>

        <div class="step-actions">
          <el-button type="primary" size="large" :disabled="connectionStatus !== 'success'" @click="currentStep = 2">
            下一步
          </el-button>
        </div>
      </div>

      <!-- 步骤 2：管理员账户 -->
      <div v-if="currentStep === 2" class="step-content">
        <h2>👤 创建管理员账户</h2>
        <p class="step-desc">设置系统管理员账号和密码</p>

        <el-form ref="adminFormRef" :model="adminConfig" :rules="adminRules" class="install-form">
          <el-form-item label="用户名" prop="username">
            <el-input v-model="adminConfig.username" placeholder="设置管理员用户名">
              <template #prefix><el-icon><User /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item label="邮箱" prop="email">
            <el-input v-model="adminConfig.email" placeholder="管理员邮箱">
              <template #prefix><el-icon><Message /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item label="密码" prop="password">
            <el-input v-model="adminConfig.password" type="password" show-password placeholder="设置管理员密码">
              <template #prefix><el-icon><Lock /></el-icon></template>
            </el-input>
          </el-form-item>

          <el-form-item label="确认密码" prop="confirmPassword">
            <el-input v-model="adminConfig.confirmPassword" type="password" show-password placeholder="确认密码">
              <template #prefix><el-icon><Lock /></el-icon></template>
            </el-input>
          </el-form-item>
        </el-form>

        <div class="step-actions">
          <el-button @click="currentStep = 1">上一步</el-button>
          <el-button type="primary" size="large" :loading="installing" @click="handleInstall">
            开始安装
          </el-button>
        </div>
      </div>

      <!-- 步骤 3：完成安装 -->
      <div v-if="currentStep === 3" class="step-content">
        <div class="success-box">
          <el-icon :size="80" color="#67C23A"><CircleCheckFilled /></el-icon>
          <h2>🎉 安装成功！</h2>
          <p class="success-text">系统已完成安装，即将跳转到登录页面</p>

          <el-alert
            title="请牢记以下信息"
            type="success"
            :closable="false"
            show-icon
          >
            <template #default>
              <p><strong>管理员用户名：</strong>{{ adminConfig.username }}</p>
              <p><strong>密码：</strong>******（您设置的密码）</p>
            </template>
          </el-alert>

          <el-button type="primary" size="large" @click="goToLogin">
            前往登录
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import api from '../api'

const router = useRouter()
const currentStep = ref(1)
const testingConnection = ref(false)
const connectionStatus = ref('')
const installing = ref(false)

// 数据库配置
const dbConfig = reactive({
  host: 'mysql',
  port: 3306,
  database: 'erp_trade',
  username: 'erp_user',
  password: ''
})

// 管理员配置
const adminConfig = reactive({
  username: 'admin',
  email: 'admin@example.com',
  password: '',
  confirmPassword: ''
})

// 数据库表单验证
const dbFormRef = ref(null)
const dbRules = {
  host: [{ required: true, message: '请输入数据库主机', trigger: 'blur' }],
  port: [{ required: true, message: '请输入端口', trigger: 'blur' }],
  database: [{ required: true, message: '请输入数据库名', trigger: 'blur' }],
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

// 管理员表单验证
const adminFormRef = ref(null)
const adminRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度至少 6 位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== adminConfig.password) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 测试数据库连接
const testConnection = async () => {
  try {
    await dbFormRef.value.validate()
    testingConnection.value = true
    connectionStatus.value = ''

    // TODO: 调用后端测试连接 API
    // const response = await api.post('/install/test-db', dbConfig)
    
    // 模拟测试（实际使用时删除）
    await new Promise(resolve => setTimeout(resolve, 1000))
    
    connectionStatus.value = 'success'
    ElMessage.success('数据库连接成功')
  } catch (error) {
    connectionStatus.value = 'error'
    ElMessage.error('数据库连接失败，请检查配置')
  } finally {
    testingConnection.value = false
  }
}

// 执行安装
const handleInstall = async () => {
  try {
    await adminFormRef.value.validate()
    
    installing.value = true

    // 准备安装数据
    const installData = {
      database: dbConfig,
      admin: {
        username: adminConfig.username,
        email: adminConfig.email,
        password: adminConfig.password
      }
    }

    // TODO: 调用后端安装 API
    // const response = await api.post('/install', installData)
    
    // 模拟安装（实际使用时删除）
    await new Promise(resolve => setTimeout(resolve, 2000))
    
    // 保存安装标记
    localStorage.setItem('erp_installed', 'true')
    
    currentStep.value = 3
    ElMessage.success('安装成功')
  } catch (error) {
    ElMessage.error('安装失败，请检查配置')
  } finally {
    installing.value = false
  }
}

// 跳转到登录页面
const goToLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.install-container {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.install-box {
  width: 100%;
  max-width: 600px;
  padding: 40px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.2);
}

.install-steps {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 40px;
  padding: 0 20px;
}

.step {
  display: flex;
  flex-direction: column;
  align-items: center;
  position: relative;
}

.step-number {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  background: #e0e0e0;
  color: #999;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  margin-bottom: 10px;
  transition: all 0.3s;
}

.step.active .step-number {
  background: #667eea;
  color: #fff;
}

.step.completed .step-number {
  background: #67C23A;
  color: #fff;
}

.step-text {
  font-size: 14px;
  color: #666;
}

.step.active .step-text {
  color: #667eea;
  font-weight: bold;
}

.step-line {
  flex: 1;
  height: 2px;
  background: #e0e0e0;
  margin: 0 10px;
  margin-bottom: 30px;
}

.step-line.active {
  background: #67C23A;
}

.step-content {
  text-align: center;
}

.step-content h2 {
  font-size: 24px;
  color: #2c3e50;
  margin-bottom: 10px;
}

.step-desc {
  color: #7f8c8d;
  margin-bottom: 30px;
}

.install-form {
  text-align: left;
  margin-bottom: 30px;
}

.step-actions {
  display: flex;
  justify-content: space-between;
  gap: 20px;
}

.success-box {
  text-align: center;
  padding: 20px;
}

.success-box h2 {
  margin: 20px 0 10px;
  color: #67C23A;
}

.success-text {
  color: #7f8c8d;
  margin-bottom: 30px;
}

.success-box .el-alert {
  margin: 20px 0;
  text-align: left;
}

.success-box p {
  margin: 10px 0;
}
</style>
