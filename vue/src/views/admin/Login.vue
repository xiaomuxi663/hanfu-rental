<template>
  <div class="admin-login">
    <div class="login-card">
      <h2>管理员登录</h2>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0">
        <el-form-item prop="username"><el-input v-model="form.username" placeholder="请输入账号" prefix-icon="User" size="large" /></el-form-item>
        <el-form-item prop="password"><el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock" size="large" show-password /></el-form-item>
        <el-form-item><el-button type="primary" size="large" :loading="loading" @click="handleLogin" style="width: 100%">登 录</el-button></el-form-item>
      </el-form>
      <div class="footer-links"><router-link to="/login">返回前台</router-link></div>
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
    const user = res.data.user
    if (user.role !== 'admin' && user.role !== 'staff') { ElMessage.error('您不是管理员，无权访问后台'); return }
    userStore.login(res.data.token, user)
    ElMessage.success('登录成功')
    router.push(route.query.redirect || '/admin/dashboard')
  } catch (error) { console.error(error) } finally { loading.value = false }
}
</script>
<style scoped>
.admin-login { height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #304156 0%, #1a252f 100%); }
.login-card { width: 400px; padding: 40px; background: #fff; border-radius: 8px; box-shadow: 0 4px 20px rgba(0,0,0,0.3); }
.login-card h2 { text-align: center; margin-bottom: 30px; color: #333; }
.footer-links { text-align: center; margin-top: 20px; }
.footer-links a { color: #409EFF; font-size: 14px; }
</style>
