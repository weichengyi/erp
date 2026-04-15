import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/install',
    name: 'Install',
    component: () => import('../views/Install.vue')
  },
  {
    path: '/',
    component: () => import('../layouts/MainLayout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { title: '仪表盘' }
      },
      {
        path: 'companies',
        name: 'Companies',
        component: () => import('../views/companies/CompanyList.vue'),
        meta: { title: '公司管理' }
      },
      {
        path: 'contacts',
        name: 'Contacts',
        component: () => import('../views/contacts/ContactList.vue'),
        meta: { title: '联系人管理' }
      },
      {
        path: 'product-sns',
        name: 'ProductSns',
        component: () => import('../views/product-sn/ProductSnList.vue'),
        meta: { title: 'SN 码管理' }
      },
      {
        path: 'assets',
        name: 'Assets',
        component: () => import('../views/assets/AssetList.vue'),
        meta: { title: '资产管理' }
      },
      {
        path: 'sales-orders',
        name: 'SalesOrders',
        component: () => import('../views/orders/SalesOrderList.vue'),
        meta: { title: '销售订单' }
      },
      {
        path: 'reports',
        name: 'Reports',
        component: () => import('../views/reports/ReportDashboard.vue'),
        meta: { title: '报表统计' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.path !== '/login' && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router
