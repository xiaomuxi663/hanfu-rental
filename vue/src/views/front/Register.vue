<template>
  <div class="register-page">
    <div class="register-card">
      <h2>用户注册</h2>
      <el-form ref="formRef" :model="form" :rules="rules" label-width="0">
        <el-form-item prop="username"><el-input v-model="form.username" placeholder="请输入账号" prefix-icon="User" size="large" /></el-form-item>
        <el-form-item prop="password"><el-input v-model="form.password" type="password" placeholder="请输入密码" prefix-icon="Lock" size="large" show-password /></el-form-item>
        <el-form-item prop="confirmPassword"><el-input v-model="form.confirmPassword" type="password" placeholder="请确认密码" prefix-icon="Lock" size="large" show-password /></el-form-item>
        <el-form-item prop="nickname"><el-input v-model="form.nickname" placeholder="请输入昵称" prefix-icon="UserFilled" size="large" /></el-form-item>
        <el-form-item><el-button type="primary" size="large" :loading="loading" @click="handleRegister" style="width: 100%">注 册</el-button></el-form-item>
      </el-form>
      <div class="footer-links"><router-link to="/login">已有账号？去登录</router-link></div>
    </div>
  </div>
</template>
<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { register } from '@/api/user'
import { ElMessage } from 'element-plus'
const router = useRouter()
const formRef = ref()
const loading = ref(false)
const form = reactive({ username: '', password: '', confirmPassword: '', nickname: '' })
const validateConfirmPassword = (rule, value, callback) => { value !== form.password ? callback(new Error('两次密码输入不一致')) : callback() }
const rules = {
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }, { min: 3, max: 20, message: '账号长度3-20个字符', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }, { min: 6, max: 20, message: '密码长度6-20个字符', trigger: 'blur' }],
  confirmPassword: [{ required: true, message: '请确认密码', trigger: 'blur' }, { validator: validateConfirmPassword, trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }]
}
const handleRegister = async () => {
  await formRef.value.validate()
  loading.value = true
  try { await register({ username: form.username, password: form.password, nickname: form.nickname }); ElMessage.success('注册成功，请登录'); router.push('/login') }
  catch (error) { console.error(error) } finally { loading.value = false }
}
</script>
<style scoped>
.register-page { min-height: calc(100vh - 140px); display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #f5f7fa 0%, #e4e8eb 100%); }
.register-card { width: 400px; padding: 40px; background: #fff; border-radius: 8px; box-shadow: 0 4px 20px rgba(0,0,0,0.1); }
.register-card h2 { text-align: center; margin-bottom: 30px; color: #333; }
.footer-links { text-align: center; margin-top: 20px; }
.footer-links a { color: #c41d7f; font-size: 14px; }
</style>
