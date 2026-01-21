import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/store/user'

const frontRoutes = [
  { path: '/', component: () => import('@/views/front/Layout.vue'), children: [
    { path: '', name: 'Home', component: () => import('@/views/front/Home.vue'), meta: { title: '首页' } },
    { path: 'login', name: 'Login', component: () => import('@/views/front/Login.vue'), meta: { title: '登录' } },
    { path: 'register', name: 'Register', component: () => import('@/views/front/Register.vue'), meta: { title: '注册' } }
  ] }
]

const adminRoutes = [
  { path: '/admin', component: () => import('@/views/admin/Layout.vue'), meta: { requiresAuth: true, roles: ['admin', 'staff'] }, children: [
    { path: '', redirect: '/admin/dashboard' },
    { path: 'dashboard', name: 'AdminDashboard', component: () => import('@/views/admin/Dashboard.vue'), meta: { title: '控制台' } },
    { path: 'user', name: 'AdminUser', component: () => import('@/views/admin/UserManage.vue'), meta: { title: '用户管理', roles: ['admin'] } },
    { path: 'login', name: 'AdminLogin', component: () => import('@/views/admin/Login.vue'), meta: { title: '管理员登录', requiresAuth: false } }
  ] }
]

const router = createRouter({ history: createWebHistory(), routes: [...frontRoutes, ...adminRoutes] })

router.beforeEach((to, from, next) => {
  document.title = to.meta.title ? `${to.meta.title} - 汉服租赁系统` : '汉服租赁系统'
  const userStore = useUserStore()
  if (to.meta.requiresAuth) {
    if (!userStore.token) { next({ name: to.path.startsWith('/admin') ? 'AdminLogin' : 'Login', query: { redirect: to.fullPath } }); return }
    if (to.meta.roles && !to.meta.roles.includes(userStore.userInfo?.role)) { next({ path: '/403' }); return }
  }
  next()
})

export default router
