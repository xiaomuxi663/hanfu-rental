<template>
  <div class="front-layout">
    <header class="header">
      <div class="header-content">
        <div class="logo"><router-link to="/">汉服租赁</router-link></div>
        <nav class="nav"><router-link to="/">首页</router-link></nav>
        <div class="user-area">
          <template v-if="userStore.isLogin">
            <el-dropdown>
              <span class="user-info">
                <el-avatar :size="32">{{ userStore.userInfo?.nickname?.charAt(0) || 'U' }}</el-avatar>
                <span class="username">{{ userStore.userInfo?.nickname || '用户' }}</span>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="router.push('/user/profile')">个人中心</el-dropdown-item>
                  <el-dropdown-item @click="router.push('/user/orders')">我的订单</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <router-link to="/login" class="btn-link">登录</router-link>
            <router-link to="/register" class="btn-link btn-primary">注册</router-link>
          </template>
        </div>
      </div>
    </header>
    <main class="main"><router-view /></main>
    <footer class="footer"><p>汉服租赁系统 &copy; 2024</p></footer>
  </div>
</template>
<script setup>
import { useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'
const router = useRouter()
const userStore = useUserStore()
const handleLogout = () => { userStore.logout(); ElMessage.success('退出成功'); router.push('/') }
</script>
<style scoped>
.front-layout { min-height: 100vh; display: flex; flex-direction: column; }
.header { background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.08); position: sticky; top: 0; z-index: 100; }
.header-content { max-width: 1200px; margin: 0 auto; padding: 0 20px; height: 60px; display: flex; align-items: center; justify-content: space-between; }
.logo a { font-size: 20px; font-weight: bold; color: #c41d7f; }
.nav { display: flex; gap: 30px; }
.nav a { color: #333; font-size: 15px; }
.nav a:hover, .nav a.router-link-active { color: #c41d7f; }
.user-area { display: flex; align-items: center; gap: 15px; }
.user-info { display: flex; align-items: center; gap: 8px; cursor: pointer; }
.username { color: #333; }
.btn-link { padding: 8px 16px; border-radius: 4px; font-size: 14px; }
.btn-link:hover { color: #c41d7f; }
.btn-primary { background: #c41d7f; color: #fff !important; }
.btn-primary:hover { background: #a8186b; }
.main { flex: 1; background: #f5f5f5; }
.footer { background: #333; color: #999; text-align: center; padding: 20px; }
</style>
