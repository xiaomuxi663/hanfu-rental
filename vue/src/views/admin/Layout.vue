<template>
  <div class="admin-layout">
    <el-container>
      <el-aside :width="isCollapse ? '64px' : '200px'">
        <div class="logo"><span v-if="!isCollapse">汉服管理系统</span><span v-else>HF</span></div>
        <el-menu :default-active="activeMenu" :collapse="isCollapse" background-color="#304156" text-color="#bfcbd9" active-text-color="#409EFF" router>
          <el-menu-item index="/admin/dashboard"><el-icon><DataAnalysis /></el-icon><span>控制台</span></el-menu-item>
          <el-menu-item v-if="userStore.isAdmin" index="/admin/user"><el-icon><User /></el-icon><span>用户管理</span></el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header>
          <div class="header-left">
            <el-icon class="collapse-btn" @click="isCollapse = !isCollapse"><Fold v-if="!isCollapse" /><Expand v-else /></el-icon>
            <el-breadcrumb separator="/"><el-breadcrumb-item :to="{ path: '/admin' }">首页</el-breadcrumb-item><el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item></el-breadcrumb>
          </div>
          <div class="header-right">
            <el-dropdown>
              <span class="user-info"><el-avatar :size="32">{{ userStore.userInfo?.nickname?.charAt(0) || 'A' }}</el-avatar><span class="username">{{ userStore.userInfo?.nickname || '管理员' }}</span></span>
              <template #dropdown><el-dropdown-menu><el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item></el-dropdown-menu></template>
            </el-dropdown>
          </div>
        </el-header>
        <el-main><router-view /></el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script setup>
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import { ElMessage } from 'element-plus'
const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const isCollapse = ref(false)
const activeMenu = computed(() => route.path)
const currentTitle = computed(() => route.meta.title || '控制台')
const handleLogout = () => { userStore.logout(); ElMessage.success('退出成功'); router.push('/admin/login') }
</script>
<style scoped>
.admin-layout { height: 100vh; }
.admin-layout .el-container { height: 100%; }
.el-aside { background-color: #304156; transition: width 0.3s; }
.logo { height: 60px; display: flex; align-items: center; justify-content: center; color: #fff; font-size: 18px; font-weight: bold; background-color: #263445; }
.el-menu { border-right: none; }
.el-header { display: flex; align-items: center; justify-content: space-between; background: #fff; box-shadow: 0 1px 4px rgba(0,0,0,0.08); }
.header-left { display: flex; align-items: center; gap: 16px; }
.collapse-btn { font-size: 20px; cursor: pointer; }
.header-right { display: flex; align-items: center; }
.user-info { display: flex; align-items: center; gap: 8px; cursor: pointer; }
.username { color: #333; }
.el-main { background: #f0f2f5; padding: 20px; }
</style>
