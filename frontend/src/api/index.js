import axios from 'axios'
import { ElMessage } from 'element-plus'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          ElMessage.error('未授权，请登录')
          localStorage.removeItem('token')
          window.location.href = '/login'
          break
        case 403:
          ElMessage.error('拒绝访问')
          break
        case 404:
          ElMessage.error('请求错误，未找到该资源')
          break
        case 500:
          ElMessage.error('服务器错误')
          break
        default:
          ElMessage.error(error.response.data.message || '请求失败')
      }
    } else {
      ElMessage.error('网络错误')
    }
    return Promise.reject(error)
  }
)

// API 接口
export const companyApi = {
  getAll: (params) => api.get('/companies', { params }),
  getById: (id) => api.get(`/companies/${id}`),
  create: (data) => api.post('/companies', data),
  update: (id, data) => api.put(`/companies/${id}`, data),
  delete: (id) => api.delete(`/companies/${id}`),
  search: (keyword) => api.get('/companies/search', { params: { keyword } })
}

export const productSnApi = {
  getAll: () => api.get('/product-sns'),
  getByCode: (snCode) => api.get(`/product-sns/code/${snCode}`),
  trace: (snCode) => api.get(`/product-sns/trace/${snCode}`),
  create: (data) => api.post('/product-sns', data),
  update: (id, data) => api.put(`/product-sns/${id}`, data),
  delete: (id) => api.delete(`/product-sns/${id}`),
  getByStatus: (status) => api.get(`/product-sns/status/${status}`)
}

export const assetApi = {
  getAll: () => api.get('/assets'),
  generateCode: () => api.get('/assets/generate-code'),
  getByCode: (code) => api.get(`/assets/code/${code}`),
  create: (data) => api.post('/assets', data),
  update: (id, data) => api.put(`/assets/${id}`, data),
  delete: (id) => api.delete(`/assets/${id}`)
}

export const authApi = {
  login: (username, password) => api.post('/auth/login', { username, password })
}

export const reportApi = {
  dashboardStats: () => api.get('/reports/dashboard-stats'),
  companyStats: () => api.get('/reports/company-stats'),
  salesStats: (params) => api.get('/reports/sales-stats', { params }),
  exportCompanies: () => api.get('/reports/export/companies', { responseType: 'blob' }),
  exportAssets: () => api.get('/reports/export/assets', { responseType: 'blob' })
}

export default api
