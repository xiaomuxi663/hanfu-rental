import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

// 前台路由
const frontRoutes = [
  {
    path: '/',
    component: () => import('@/views/front/Layout.vue'),
    children: [
      {
        path: '',
        name: 'Home',
        component: () => import('@/views/front/Home.vue'),
        meta: { title: '首页' }
      },
      {
        path: 'login',
        name: 'Login',
        component: () => import('@/views/front/Login.vue'),
        meta: { title: '登录' }
      },
      {
        path: 'register',
        name: 'Register',
        component: () => import('@/views/front/Register.vue'),
        meta: { title: '注册' }
      },
      {
        path: 'notice',
        name: 'NoticeList',
        component: () => import('@/views/front/NoticeList.vue'),
        meta: { title: '公告' }
      }
    ]
  }
]

// 后台管理路由
const adminRoutes = [
  {
    path: '/admin',
    component: () => import('@/views/admin/Layout.vue'),
    meta: { requiresAuth: true, roles: ['admin', 'staff'] },
    children: [
      {
        path: '',
        redirect: '/admin/dashboard'
      },
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '控制台' }
      },
      {
        path: 'user',
        name: 'AdminUser',
        component: () => import('@/views/admin/UserManage.vue'),
        meta: { title: '用户管理', roles: ['admin'] }
      },
      {
        path: 'config',
        name: 'AdminConfig',
        component: () => import('@/views/admin/ConfigManage.vue'),
        meta: { title: '系统配置', roles: ['admin'] }
      },
      {
        path: 'notice',
        name: 'AdminNotice',
        component: () => import('@/views/admin/NoticeManage.vue'),
        meta: { title: '公告管理', roles: ['admin'] }
      },
      {
        path: 'login',
        name: 'AdminLogin',
        component: () => import('@/views/admin/Login.vue'),
        meta: { title: '管理员登录', requiresAuth: false }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes: [...frontRoutes, ...adminRoutes]
})

// 路由守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 汉服租赁系统` : '汉服租赁系统'

  const userStore = useUserStore()
  
  // 检查是否需要认证
  if (to.meta.requiresAuth) {
    if (!userStore.token) {
      // 未登录，跳转到对应的登录页
      if (to.path.startsWith('/admin')) {
        next({ name: 'AdminLogin', query: { redirect: to.fullPath } })
      } else {
        next({ name: 'Login', query: { redirect: to.fullPath } })
      }
      return
    }

    // 检查角色权限
    if (to.meta.roles && !to.meta.roles.includes(userStore.userInfo?.role)) {
      next({ path: '/403' })
      return
    }
  }

  next()
})

export default router
