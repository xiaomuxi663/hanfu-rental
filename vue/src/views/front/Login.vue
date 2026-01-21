<template>
  <div class="login-page">
    <div class="login-card">
      <h2>用户登录</h2>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0">
        <el-form-item prop="username">
          <el-input v-model="form.username" placeholder="请输入账号" prefix-icon="User" size="large" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock" size="large" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" :loading="loading" @click="handleLogin" style="width: 100%">登 录</el-button>
        </el-form-item>
      </el-form>
      <div class="footer-links">
        <router-link to="/register">没有账号？去注册</router-link>
        <router-link to="/admin/login">管理员入口</router-link>
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '@/store/user'
import { login } from '@/api/user'
import { ElMessage } from 'element-plus'
const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const formRef = ref()
const loading = ref(false)
const form = reactive({ username: '', password: '' })
const rules = { username: [{ required: true, message: '请输入账号', trigger: 'blur' }], password: [{ required: true, message: '请输入密码', trigger: 'blur' }] }
const handleLogin = async () => {
  await formRef.value.validate()
  loading.value = true
  try {
    const res = await login(form)
    userStore.login(res.data.token, res.data.user)
    ElMessage.success('登录成功')
    router.push(route.query.redirect || '/')
  } catch (error) { console.error(error) } finally { loading.value = false }
}
</script>
<style scoped>
.login-page { min-height: calc(100vh - 140px); display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #f5f7fa 0%, #e4e8eb 100%); }
.login-card { width: 400px; padding: 40px; background: #fff; border-radius: 8px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
.login-card h2 { text-align: center; margin-bottom: 30px; color: #333; }
.footer-links { display: flex; justify-content: space-between; margin-top: 20px; }
.footer-links a { color: #c41d7f; font-size: 14px; }
.footer-links a:hover { text-decoration: underline; }
</style>
